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
	ok = do_recv(Sock, LSock, Port, []).
%	ok = gen_tcp:close(Sock),
%	ok = gen_tcp:close(LSock)


do_recv(Sock, LSock, Port, TupleList) ->

	case gen_tcp:recv(Sock, 0) of
		{ok, B} ->
			util:logging("abc", util:to_String(B)),
      [D, Obj, Name, Host, Po] = string:tokens(string:trim(B),"{};"),
			case D of
				"rebind" ->
          util:logging("abc", util:to_String(erlang:timestamp()) ++ " " ++ string:trim(Obj) ++ ">--<" ++ string:trim(Name) ++ "\n"),
          gen_tcp:send(Sock, "ok"),
					do_recv(Sock, LSock, Port, append(TupleList, {Name, Obj ++ "," ++ Host ++ "," ++ Po}));
				"resolve" ->
					case keyfind(Name, TupleList) of
						{_, Object} ->
							gen_tcp:send(Sock, util:to_String(Object));
						false ->
							gen_tcp:send(Sock, "null")
					end,
          %{_, Object} = keyfind(Name, TupleList),
          %gen_tcp:send(Sock, util:to_String(Object)),
          util:logging("abc", util:to_String(erlang:timestamp()) ++ string:trim(Name) ++ "\n"),
					do_recv(Sock, LSock, Port, TupleList);
				"shutdown" ->
					ok = gen_tcp:close(Sock),
					ok = gen_tcp:close(LSock),
					ok
			end;
		{error, _Closed} ->
			ok = gen_tcp:close(Sock),
			ok = gen_tcp:close(LSock),
			{ok, NLSock} = gen_tcp:listen(Port, [list, {packet, 0},
				{active, false}]),
			{ok, NSock} = gen_tcp:accept(NLSock),
			do_recv(NSock,NLSock,Port,TupleList)
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