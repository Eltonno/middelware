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

-export([init/0]).


init() ->
	loop([]).

loop(TupleList) ->
	receive
		{rebind, RH_PID, {Name, Host, Po}} ->
			util:logging("nameservice.log", vsutil:now2string(erlang:timestamp()) ++ " rebind request from: "++util:to_String(RH_PID)++" with: "++Name++"|"++Host++"|"++Po++"\n"),
			New_TupleList = append(TupleList, {Name, util:to_String(Host) ++ "," ++util:to_String(Po)}),
			RH_PID ! ok,
			loop(New_TupleList);
		{resolve, RH_PID, Name} ->
			util:logging("nameservice.log", vsutil:now2string(erlang:timestamp()) ++ " resolve request from: "++util:to_String(RH_PID)++" with: "++Name++"\n"),
			Ref = keyfind(Name, TupleList),
			RH_PID ! Ref,
			loop(TupleList)
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