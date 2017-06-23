package st.zudamue.support.android.sql.type;

/**
 * Created by dchost on 03/02/17.
 */

public class DoubleCharSequence extends BaseTypeCharSequence<Double> {

    public DoubleCharSequence(Double value) {
        super(value);
    }

    public DoubleCharSequence(CharSequence charSequence) {
        super(0.0);
        if(charSequence == null) return;
        if(charSequence instanceof DoubleCharSequence)
            value(((DoubleCharSequence) charSequence).value());
        else try {
            value(Double.valueOf(String.valueOf(charSequence)));
        }catch (Exception ignore){

        }
    }
}
