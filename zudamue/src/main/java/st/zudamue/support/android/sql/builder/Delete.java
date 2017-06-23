package st.zudamue.support.android.sql.builder;

import java.util.LinkedList;
import java.util.List;

import st.zudamue.support.android.sql.AbstractSQL;

/**
 *
 * Created by dchost on 04/02/17.
 */

public class Delete extends AbstractSQL {

    private String sql;
    private List<Object> listArguments;

    public Delete () {
        this.sql = "";
        this.listArguments = new LinkedList<>();
    }

    @Override
    public String sql() {
        return this.sql;
    }

    @Override
    public List<Object> arguments() {
        return this.listArguments;
    }

    @Override
    public int length() {
        return this.sql.length();
    }

    @Override
    public char charAt(int index) {
        return this.sql.charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return this.sql.subSequence(beginIndex, endIndex);
    }



}
