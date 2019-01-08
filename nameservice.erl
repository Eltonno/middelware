%%%-------------------------------------------------------------------
%%% @author Florian Weiß, Ditmar Lange
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 22. Dez 2018 00:38
%%%-------------------------------------------------------------------
-module(nameservice).
-author("Ditmar").

-export([init/1]).


init(Port) ->
	{ok, LSock} = gen_tcp:listen(Port, [list, {packet, 0},
	{active, false}]),
	{ok, Sock} = gen_tcp:accept(LSock),
	{ok, _Bin} = do_recv(Sock, []),
	ok = gen_tcp:close(Sock),
	ok = gen_tcp:close(LSock).

do_recv(Sock, TupleList) ->
	case gen_tcp:recv(Sock, 0) of
		{ok, B} ->
      [D, Obj, Name] = string:tokens(string:trim(B),"{};"),
			case D of
				"rebind" ->
          util:logging("abc", util:to_String(erlang:timestamp()) ++ " " ++ string:trim(Obj) ++ ">--<" ++ string:trim(Name) ++ "\n"),
          gen_tcp:send(Sock, <<"ok">>),
					do_recv(Sock, append(TupleList, {Name, string:trim(Obj)}));
				"resolve" ->
          {_, Object} = keyfind(Name, TupleList),
          gen_tcp:send(Sock, util:to_String(Object)),
          util:logging("abc", util:to_String(erlang:timestamp()) ++ string:trim(Name) ++ "\n"),
					do_recv(Sock, TupleList);
				"shutdown" ->
					ok
			end;
		{error, _Closed} ->
			{ok, TupleList}
	end.

append([], []) ->
  [];
append([],[H|T]) ->
[H|T];
append([],Elem) ->
[Elem];
append([H|T], []) ->
[H|T];
append(Elem, []) ->
[Elem];
append([H1|T1],[H2|T2]) ->
append([H1|T1] ++ [H2], T2);
append([H|T],Elem) ->
[H|T] ++ [Elem];
append(L,[H|T]) ->
append([L] ++ [H], T);
append(E1,E2) ->
[E1] ++ [E2].

keyfind(_,[]) ->
false;
keyfind(Key, Tuplelist) ->
[Head|Rest] = Tuplelist,
{K,_} = Head,
if K == Key -> Head;
	true -> keyfind(Key,Rest)
end.