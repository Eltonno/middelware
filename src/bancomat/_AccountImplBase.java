package bancomat;

import mware_lib.CommunicationModule;
public abstract class _AccountImplBase {

	public static _AccountImplBase narrowCast(Object rawObjectRef){
		return new _AccountHandler(rawObjectRef);
	}
	public abstract  double deposit(double param0);
	public abstract  double withdraw(double param0);
}