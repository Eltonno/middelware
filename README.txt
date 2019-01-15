Prerequisits:
-------------

bancomat.idl   (interface client/server defined in IDL)
client/        (client app)
server/        (server app)
mware_lib/     (binaries middleware lib)

bancomat/      (to be generated from bancomat.idl by IDL compiler + javac)

All these in one directory.



Launch commands:
---------------

Launch your name server first.
erl sname <name>
c(nameservice).
c(requestHandler).
requestHandler:init(<port>).

java server.Server <ns-host> <ns-port>

java client.Client <ns-host> <ns-port>

