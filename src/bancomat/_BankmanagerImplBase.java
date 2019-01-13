package bancomat;

import mware_lib.CommunicationModule;

import java.io.IOException;

public abstract class _BankmanagerImplBase {

		public static _BankmanagerImplBase narrowCast(Object rawObjectRef) throws IOException {
return new _BankmanagerHandler(rawObjectRef);
}	public abstract  String getAccountID(int key) throws Exception;
}