package bancomat;


import mware_lib.CommunicationModule;
import java.io.IOException;

public abstract class _BankmanagerImplBase {
	public abstract  String getAccountID(int key) throws Exception;

		public static _BankmanagerImplBase narrowCast(Object rawObjectRef) throws IOException{
	    return new _BankmanagerImplBase() {

            String ref = (String) rawObjectRef;

			@Override
			public  String getAccountID(int key) throws Exception {
				ref = ref.replace("\"", "");
			String name = ref.split(",")[0];
			String host = ref.split(",")[1];
			int port = Integer.parseInt(ref.split(",")[2]);
                Object result = null;
                result = CommunicationModule.invoke(name, host, port, "getAccountID", key);
                if (result instanceof Exception) {
                   throw new RuntimeException(result.toString());
               }
				return String.valueOf(result);

			}

};
}
}