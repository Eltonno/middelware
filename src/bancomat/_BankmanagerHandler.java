package bancomat;

import java.util.ArrayList;
import java.util.Arrays;

public class _BankmanagerHandler extends _BankmanagerImplBase {
    public _BankmanagerHandler(Object rawObjectRef) {
        String nsAnswerSplited[] = ((String) rawObjectRef).split("\\|");
        //rawObject = new ComHandler(nsAnswerSplited[0], new Integer(nsAnswerSplited[1]), false);
    }

    public double add(double a, double b) {
        ArrayList<Double> params = new ArrayList<Double>(Arrays.asList(a,b));
        String answerString = params.toString();
        answerString = answerString.substring(1, answerString.length()-1);
        return 0;

    }

    @Override
    public String getAccountID(int key) {
        return null;
    }
}
