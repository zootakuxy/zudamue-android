package st.zudamue.support.android.sql.type;

import st.zudamue.support.android.util.BaseCharSequence;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class BaseTypeCharSequence<E> extends BaseCharSequence implements TypeCharSequence<E> {

    private E value;

    public BaseTypeCharSequence(E value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if( value == null ) return null;
        return String.valueOf(value);
    }

    @Override
    public E value() {
        return this.value;
    }

    protected void value(E value) {
        this.value = value;
    }
}
