package bancomat;

import mware_lib.CommunicationModule;

import java.io.IOException;

public abstract class _BankmanagerImplBase {

	public abstract String getAccountID(int key) throws Exception;

	public static _BankmanagerImplBase narrowCast(Object rawObjectRef) throws IOException {
		return new _BankmanagerImplBase() {
			private String name = rawObjectRef.toString().split(",")[0];
			private String host = rawObjectRef.toString().split(",")[1];
			private int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);

			@Override
			public String getAccountID(int key) throws RuntimeException, IOException {
				Object result = CommunicationModule.invoke(name, host, port, "_BankImplBase", "getAccountID", key);
				if (result instanceof RuntimeException) throw (RuntimeException) result;
				return (String) result;
			}
		};
	}
}