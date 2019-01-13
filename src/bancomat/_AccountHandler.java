package bancomat;

import mware_lib.CommunicationModule;
public  class _AccountHandler {

private static String name;
private static String host;
private static int port;
_AccountHandler(Object rawObjectRef){
	name = rawObjectRef.toString().split(",")[0];
	host = rawObjectRef.toString().split(",")[1];
	port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
}

public static _AccountHandler narrowCast(Object rawObjectRef){
	return new _AccountHandler(rawObjectRef);
}

public  double deposit(double param0) throws RuntimeException {
	Object result = CommunicationModule.invoke(name, host, port, "_AccountImplBase", "balanceInquiry");
	if (result instanceof RuntimeException) throw (RuntimeException) result;
	return (double) result;
}

public  double withdraw(double param0) throws RuntimeException {
	Object result = CommunicationModule.invoke(name, host, port, "_AccountImplBase", "balanceInquiry");
	if (result instanceof RuntimeException) throw (RuntimeException) result;
	return (double) result;
}
}