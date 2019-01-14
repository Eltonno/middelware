package bancomat;

import mware_lib.CommunicationModule;
import mware_lib.ObjectBroker;

import java.io.IOException;


public abstract class _AccountImplBase {
	public abstract double deposit(double amount) throws IOException;

	public abstract double withdraw(double amount) throws IOException;

	public static _AccountImplBase narrowCast(Object rawObjectRef) throws IOException {

	    return new _AccountImplBase() {

			 //String name = rawObjectRef.toString().split(",")[0];
			 //String host = rawObjectRef.toString().split(",")[1];
			// int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
            String ref = (String) rawObjectRef;

			@Override
			public double deposit(double amount) throws IOException {
                ref = ref.replace("\"", "");
			 String name = ref.split(",")[0];
			 String host = ref.split(",")[1];
			int port = Integer.parseInt(ref.split(",")[2]);
                System.out.println("_AccountImplBase ruft deposit auf bei Name <<" + name + ">>, Host <<" + host + ">> und port <<" + port + ">>");
                Object result = null;
                result = CommunicationModule.invoke(name, host, port,/* "_BankImplBase,"*/ "deposit", amount);
               if (result instanceof RuntimeException) throw (RuntimeException) result;
				return (double) result;

			}

			@Override
			public double withdraw(double amount) throws IOException {
                ref = ref.replace("\"", "");
                String name = ref.split(",")[0];
                String host = ref.split(",")[1];
                int port = Integer.parseInt(ref.split(",")[2]);
                System.out.println("_AccountImplBase ruft withdraw auf bei Name <<" + name + ">>, Host <<" + host + ">> und port <<" + port + ">>");
                Object result = null;


                    result = CommunicationModule.invoke(name, host, port/*, "_BankImplBase"*/, "withdraw", amount);
                if (result instanceof RuntimeException) throw (RuntimeException) result;
				return (double) result;

			}

		};
	}
}