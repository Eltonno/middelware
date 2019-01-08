package bancomat;

public abstract class _AccountImplBase {

	public abstract  double deposit(double param0);
	public abstract  double withdraw(double param0);
		public static _AccountImplBase narrowCast(Object rawObjectRef){
return new _AccountHandler(rawObjectRef);
}}