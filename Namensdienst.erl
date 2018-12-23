%%%-------------------------------------------------------------------
%%% @author Florian Weiß, Ditmar Lange
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 22. Dez 2018 00:38
%%%-------------------------------------------------------------------
-module(Nameservice).
-author("Ditmar").

-export([init/1]).


init(Port) ->
	%% TODO: Nameservice auf Port einstellen
	loop(TupleList).

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
					PID ! ok,
				true ->
					PID ! "Object not found"
			end
			loop(TupleList);
		{PID, {lookup, Name} ->
			Member = member(Name, TupleList),
			if
				Member ->
					PID ! %%TODO: Referenz senden,
				true ->
					PID ! "Object not found"
			end
			loop(TupleList);
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