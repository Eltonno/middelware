package bancomat;

import mware_lib.ObjectBroker;

import java.io.IOException;

public  class _BankmanagerHandler extends _BankmanagerImplBase {
	private String name;
	private String host;
	private int port;
	ObjectBroker ob;

	_BankmanagerHandler(Object rawObjectRef) throws IOException {
		System.out.println(rawObjectRef.toString());
		name = rawObjectRef.toString().split(",")[0];
		host = rawObjectRef.toString().split(",")[1];
		port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
		ob = ObjectBroker.init(host,port,false);
	}

	public static _BankmanagerHandler narrowCast(Object rawObjectRef) throws IOException {
		return new _BankmanagerHandler(rawObjectRef);
	}

	public  String getAccountID(int key) throws Exception {
		Object result = this.ob.remoteCall(name,host,port,"getAccountID", key);
		if (result instanceof RuntimeException) throw (RuntimeException) result;
		return String.valueOf(result);
	}
}