package bancomat;

import mware_lib.CommunicationModule;

import java.io.IOException;

public abstract class _AccountImplBase {

		public static _AccountImplBase narrowCast(Object rawObjectRef) throws IOException {
return new _AccountHandler(rawObjectRef);
}	public abstract  double deposit(double param0) throws Exception;
	public abstract  double withdraw(double param0) throws Exception;
}