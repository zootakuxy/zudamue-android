package st.domain.support.android.sql;

import java.util.List;

/**
 *
 * Created by xdata on 12/30/16.
 */

public class Column extends Identifier {

    public Column(String name) {
        super(name);
    }

    @Override
    public List<Object> arguments() {
        return null;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    public static Column column(String name){
        return new Column(name);
    }
}
