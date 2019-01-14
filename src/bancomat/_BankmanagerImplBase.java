package bancomat;

import mware_lib.CommunicationModule;

import java.io.IOException;

public abstract class _BankmanagerImplBase {

	public abstract String getAccountID(int key) throws Exception;


	public static _BankmanagerImplBase narrowCast(Object rawObjectRef) throws IOException {

		return new _BankmanagerImplBase() {
			 //String name = rawObjectRef.toString().split(",")[0];
			 //String host = rawObjectRef.toString().split(",")[1];
			 //int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
                String ref = (String) rawObjectRef;

			@Override
			public String getAccountID(int key) throws RuntimeException, IOException {
                ref = ref.replace("\"", "");
                String name = ref.split(",")[0];
                String host = ref.split(",")[1];
                int port = Integer.parseInt(ref.split(",")[2]);
			    System.out.println("_BankmanagerImplBase ruft getAccountID auf bei Name <<" + name + ">>, Host <<" + host + ">> und port <<" + port + ">>");
				Object result = CommunicationModule.invoke(name, host, port, /*"_BankImplBase", */"getAccountID", key);
				if (result instanceof RuntimeException) throw (RuntimeException) result;
				return (String) result;
			}
		};
	}
}