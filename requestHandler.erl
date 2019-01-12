%%%-------------------------------------------------------------------
%%% @author Elton
%%% @copyright (C) 2019, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 11. Jan 2019 09:29
%%%-------------------------------------------------------------------
-module(requestHandler).
-author("Elton").

%% API
-export([init/1,loop/2,handleRequest/2]).


init(Port) ->
  NS_PID = spawn(nameservice,init,[]),
  {ok, LSock} = gen_tcp:listen(Port, [list, {packet, 0},{active, false}]),
  spawn(?MODULE, loop, [LSock, NS_PID]).

loop(LSock, NS_PID) ->
  util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " ListenSocket initiated and in loop function\n"),
  case gen_tcp:accept(LSock) of
    {ok, Sock} ->
      spawn(?MODULE, handleRequest, [Sock,NS_PID]),
      loop(LSock, NS_PID);
    {error, closed} ->
      util:logging("err.log", vsutil:now2string(erlang:timestamp()) ++ " Socket connection is already closed\n");
    {error, Any} ->
      util:logging("err.log", vsutil:now2string(erlang:timestamp()) ++ " " ++ util:to_String(Any) ++ "\n")
  end.

handleRequest(Sock, NS_PID) ->
  util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " Socket connection accepted, wait to handle recv\n"),
  case gen_tcp:recv(Sock, 0) of
    {ok, List} ->
      [D, Name, Host, Po] = string:tokens(string:trim(List),"{};\""),
      util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " recieved List is composed of: "++D++"|"++Name++"|"++Host++"|"++Po++"\n"),
      case D of
        "rebind" ->
          NS_PID ! {rebind, self(), {Name, Host, Po}},
          receive
            ok ->
              util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " rebind of " ++Name++" with Host: " ++Host++ " and Port: " ++Po++" was successful\n"),
              gen_tcp:send(Sock, "ok"),
              handleRequest(Sock, NS_PID)
          end;
        "resolve" ->
          util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " requestHandler is requested to handle resolve\n"),
          NS_PID ! {resolve, self(), Name},
          receive
            {Name,Ref} ->
              util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " resolve has found Refrence: "++Ref++"\n"),
              gen_tcp:send(Sock, Ref),
              handleRequest(Sock, NS_PID);
            false ->
              util:logging("requestHandler.log", vsutil:now2string(erlang:timestamp()) ++ " resolve has found nothing and sends null as a string back\n"),
              gen_tcp:send(Sock, "null"),
              handleRequest(Sock, NS_PID)
          end
      end;
    {error, closed} ->
      util:logging("err.log", vsutil:now2string(erlang:timestamp()) ++ " Socket connection is already closed\n");
    {error, Any} ->
      util:logging("err.log", vsutil:now2string(erlang:timestamp()) ++ " " ++ util:to_String(Any) ++ "\n")
  end.