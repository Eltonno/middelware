package bancomat;


import mware_lib.CommunicationModule;
import java.io.IOException;

public abstract class _AccountImplBase {
	public abstract  double deposit(double param0) throws Exception;

	public abstract  double withdraw(double param0) throws Exception;

		public static _AccountImplBase narrowCast(Object rawObjectRef) throws IOException{
	    return new _AccountImplBase() {

            String ref = (String) rawObjectRef;

			@Override
			public  double deposit(double param0) throws Exception {
				ref = ref.replace("\"", "");
			String name = ref.split(",")[0];
			String host = ref.split(",")[1];
			int port = Integer.parseInt(ref.split(",")[2]);
                Object result = null;
                result = CommunicationModule.invoke(name, host, port, "deposit", param0);
                if (result instanceof Exception) {
                   throw new RuntimeException(result.toString());
               }
				return (double) result;

			}

			@Override
			public  double withdraw(double param0) throws Exception {
				ref = ref.replace("\"", "");
			String name = ref.split(",")[0];
			String host = ref.split(",")[1];
			int port = Integer.parseInt(ref.split(",")[2]);
                Object result = null;
                result = CommunicationModule.invoke(name, host, port, "withdraw", param0);
                if (result instanceof Exception) {
                   throw new RuntimeException(result.toString());
               }
				return (double) result;

			}

};
}
}