package st.domain.support.android.sql.builder;

import java.util.LinkedList;
import java.util.List;

import st.domain.support.android.sql.AbstractSQL;
import st.domain.support.android.sql.Select;

/**
 *
 * Created by xdata on 1/7/17.
 */

public class Insert  extends AbstractSQL implements st.domain.support.android.sql.Insert, st.domain.support.android.sql.Insert.ResultInsertInto, st.domain.support.android.sql.Insert.ResultColumn, st.domain.support.android.sql.Insert.ResultColumns
{

    private Select select;
    private CharSequence table;
    private List<PairColumn> list;

    public Insert() {
        this.select = new st.domain.support.android.sql.builder.Select();
        this.list = new LinkedList<>();
    }

    @Override
    public ResultInsertInto insertInto(CharSequence table) {
        this.table = table;
        return this;
    }

    @Override
    public Select as() {
        return select;
    }

    @Override
    public ResultColumn column(CharSequence column, CharSequence value) {
        this.list.add(new PairColumn(column, value, true));
        return this;
    }

    @Override
    public ResultColumns columns(CharSequence ... columns) {
        for(CharSequence column: columns) {
            this.list.add(new PairColumn(column));
        }
        return this;
    }

    @Override
    public void values (Object ... values) {

        int iCount = 0;
        for(PairColumn pairColumn: this.list) {
            if(!pairColumn.isSet())
                pairColumn.value(values[iCount++]);
        }
    }

    @Override
    public String sql() {
        return null;
    }

    @Override
    public List<Object> arguments() {
        return null;
    }

    private class PairColumn {

        private boolean set;
        private CharSequence column;
        private Object value;

        public PairColumn(CharSequence column, CharSequence value, boolean set) {
            this.column = column;
            this.value = value;
            this.set = set;
        }

        public PairColumn(CharSequence column) {
            this.column = column;
            this.set = false;
        }

        public boolean isSet() {
            return set;
        }

        public void value(Object value) {
            this.value = value;
            this.set = true;
        }
    }
}
