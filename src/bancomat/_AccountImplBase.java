package bancomat;

import mware_lib.CommunicationModule;
import mware_lib.ObjectBroker;

import java.io.IOException;


public abstract class _AccountImplBase {
	public abstract double deposit(double amount);

	public abstract double withdraw(double amount);

	public static _AccountImplBase narrowCast(Object rawObjectRef) {
		return new _AccountImplBase() {
			private String name = rawObjectRef.toString().split(",")[0];
			private String host = rawObjectRef.toString().split(",")[1];
			private int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);

			@Override
			public double deposit(double amount) throws RuntimeException {
				Object result = null;
				try {
					result = CommunicationModule.invoke(name, host, port,/* "_BankImplBase,"*/ "deposit", amount);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (result instanceof RuntimeException) throw (RuntimeException) result;
				return (double) result;
			}

			@Override
			public double withdraw(double amount) throws RuntimeException {
				Object result = null;
				try {
					result = CommunicationModule.invoke(name, host, port/*, "_BankImplBase"*/, "withdraw", amount);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (result instanceof RuntimeException) throw (RuntimeException) result;
				return (double) result;
			}

		};
	}
}