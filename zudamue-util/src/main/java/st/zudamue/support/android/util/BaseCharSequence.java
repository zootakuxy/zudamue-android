package st.zudamue.support.android.util;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by xdaniel on 8/9/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public abstract class BaseCharSequence implements CharSequence, Serializable
{

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int index) {
        return this.toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {

        return this.toString().subSequence(beginIndex, endIndex);
    }

    @NonNull
    public abstract String toString();

}
