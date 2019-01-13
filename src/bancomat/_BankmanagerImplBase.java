package bancomat;

import mware_lib.CommunicationModule;
public abstract class _BankmanagerImplBase {

		public static _BankmanagerImplBase narrowCast(Object rawObjectRef){
return new _BankmanagerHandler(rawObjectRef);
}	public abstract  String getAccountID(int key);
}