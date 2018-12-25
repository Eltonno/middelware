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
	%% TODO: Nameservice auf Port einstellen
	loop([]).

loop(TupleList) ->
	receive
		{PID, {rebind, Name, Node}} ->
			%% TODO
			loop(append(TupleList, {Name, Node})),
			PID ! ok;
		{PID, {unbind, Name}} ->
			Member = member(Name, TupleList),
			if
				Member ->
					%%TODO: Name aus TupleList entfernen.
					NewTupleList = delete(Name,TupleList),
					PID ! ok;
				true ->
					PID ! "Object not found"
			end,
			loop(NewTupleList);
		{PID, {lookup, Name}} ->
			Member = member(Name, TupleList),
			if
				Member ->
					PID ! keyfind(Name, TupleList);%%TODO: Referenz senden
				true ->
					PID ! "Object not found"
			end,
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

member(_,[]) ->
  false;
member(Elem, [H|T]) ->
  if
    H == Elem -> true;
    true -> member(Elem, T)
  end.

delete(_, []) -> 
	[];
delete(Elem, [{Name,Node}|T]) ->
	if
		Name == Elem -> T;
		true -> delete(Elem, append([],{Name,Node}), T)
	end.

delete(_,L,[]) ->
	L;
delete(Elem, L, [{Name,Node}|T]) ->
	if
		Name == Elem -> append(L,T);
		true -> delete(Elem, append(L,{Name,Node}), T)
	end.

keyfind(_,[]) ->
	false;
keyfind(Key, Tuplelist) ->
	[Head|Rest] = Tuplelist,
	{K,_} = Head,
	if K == Key -> Head;
		true -> keyfind(Key,Rest)
	end.