%%%-------------------------------------------------------------------
%%% @author Elton
%%% @copyright (C) 2019, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 10. Jan 2019 20:48
%%%-------------------------------------------------------------------
-module(requests).
-author("Elton").

%% API
-export([something/6]).


something(TupleList,Port,Sock,LSock,PID,B) ->
  [D, Obj, Name, Host, Po] = string:tokens(string:trim(B),"{};\""),
  case D of
    "rebind" ->
      util:logging("abc", util:to_String(erlang:timestamp()) ++ " " ++ string:trim(Obj) ++ ">--<" ++ string:trim(Name) ++ "\n"),
      gen_tcp:send(Sock, "ok"),
      ok = gen_tcp:close(Sock),
      ok = gen_tcp:close(LSock),
      {ok, NLSock} = gen_tcp:listen(Port, [list, {packet, 0},
        {active, false}]),
      {ok, NSock} = gen_tcp:accept(NLSock),
      PID ! {TupleList, NSock, NLSock};
    "resolve" ->
      case keyfind(Name, TupleList) of
        {_, Object} ->
          util:logging("abc", Name ++ "::" ++ util:to_String(TupleList) ++ "::" ++ util:to_String(Object)),
          gen_tcp:send(Sock, util:to_String(Object));
        false ->
          util:logging("abc", Name ++ "::" ++ util:to_String(TupleList)),
          gen_tcp:send(Sock, "null,null,null")
      end,
      %{_, Object} = keyfind(Name, TupleList),
      %gen_tcp:send(Sock, util:to_String(Object)),
      util:logging("abc", util:to_String(erlang:timestamp()) ++ string:trim(Name) ++ "\n"),
      ok = gen_tcp:close(Sock),
      ok = gen_tcp:close(LSock),
      {ok, NLSock} = gen_tcp:listen(Port, [list, {packet, 0},
        {active, false}]),
      {ok, NSock} = gen_tcp:accept(NLSock),
      PID ! {TupleList, NSock, NLSock};
    "shutdown" ->
      ok = gen_tcp:close(Sock),
      ok = gen_tcp:close(LSock),
      ok
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