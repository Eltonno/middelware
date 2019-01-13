package bancomat;

import mware_lib.CommunicationModule;
import mware_lib.ObjectBroker;

import java.io.IOException;

public  class _AccountHandler extends _AccountImplBase {

private static String name;
private static String host;
private static int port;
ObjectBroker ob;

_AccountHandler(Object rawObjectRef) throws IOException {
name = rawObjectRef.toString().split(",")[0];
host = rawObjectRef.toString().split(",")[1];
port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
ob = ObjectBroker.init("", 0, false);
}

public static _AccountHandler narrowCast(Object rawObjectRef) throws IOException {
	return new _AccountHandler(rawObjectRef);
}

public  double deposit(double param0) throws Exception {
	Object result = this.ob.remoteCall(name,host,port,"deposit", param0);
	if (result instanceof RuntimeException) throw (RuntimeException) result;
	return (double) result;
}

public  double withdraw(double param0) throws Exception {
	Object result = this.ob.remoteCall(name,host,port,"withdraw",param0);
	if (result instanceof RuntimeException) throw (RuntimeException) result;
	return (double) result;
}
}