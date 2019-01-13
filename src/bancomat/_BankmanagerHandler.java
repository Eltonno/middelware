package bancomat;

import mware_lib.CommunicationModule;
public  class _BankmanagerHandler {

private static String name;
private static String host;
private static int port;
_BankmanagerHandler(Object rawObjectRef){
name = rawObjectRef.toString().split(",")[0];
host = rawObjectRef.toString().split(",")[1];
port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
}
		public static _BankmanagerHandler narrowCast(Object rawObjectRef){
return new _BankmanagerHandler(rawObjectRef)}
	public  String getAccountID(int key) throws RuntimeException {
Object result = CommunicationModule.invoke(name, host, port, "_BankmanagerImplBase", "balanceInquiry");
if (result instanceof RuntimeException) throw (RuntimeException) result;
return String.valueOf(result);
}
}