package bancomat;

public abstract class _BankmanagerImplBase {

	public abstract  String getAccountID(int key);
		public static _BankmanagerImplBase narrowCast(Object rawObjectRef){
return new _BankmanagerHandler(rawObjectRef);
}