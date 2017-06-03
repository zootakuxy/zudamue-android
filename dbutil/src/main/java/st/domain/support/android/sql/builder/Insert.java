package st.domain.support.android.sql.builder;

import android.support.v4.util.Pair;

import java.util.LinkedList;
import java.util.List;

import st.domain.support.android.sql.AbstractSQL;
import st.domain.support.android.sql.Select;
import st.domain.support.android.sql.object.Identifier;

/**
 *
 * Created by xdata on 1/7/17.
 */

public class Insert  extends AbstractSQL implements st.domain.support.android.sql.Insert, st.domain.support.android.sql.Insert.ResultInsertInto, st.domain.support.android.sql.Insert.ResultColumn, st.domain.support.android.sql.Insert.ResultColumns
{

    private Select select;
    private List<Object> list;
    private List<String> listColumns;
    private String sql;
    private String table;

    public Insert() {
        this.select = new st.domain.support.android.sql.builder.Select();
        this.list = new LinkedList<>();
        this.listColumns = new LinkedList<>();
    }

    @Override
    public ResultInsertInto insertInto( CharSequence table ) {
        this.listColumns.clear();
        this.list.clear();
        if( table instanceof Identifier )
            this.table = ((Identifier) table).name();
        else table = String.valueOf( table );

        this.sql = "INSERT INTO "+table;
        return this;
    }

    @Override
    public Select as() {
        return select;
    }

    @Override
    public ResultColumns columns( CharSequence ... columns) {
        this.sql += "( ";
        int count = 0;
        for( CharSequence col: columns) {
            String column = ( col instanceof  Identifier )? ((Identifier) col ).name() : String.valueOf( col );
            this.listColumns.add(column);
            this.sql += column;
            if(++count < columns.length)
                this.sql += ", ";
        }
        this.sql += " )";

        return this;
    }

    @Override
    public Insert values (Object ... values) {
        this.sql += " VALUES( ";
        int iCount = 0;
        for(Object value: values) {
            this.sql += "?";
            this.list.add(value);

            if(++iCount < values.length)
                this.sql += ", ";
        }
        this.sql+= ") ";
        return this;
    }

    @Override
    public String sql() {
        return this.sql;
    }

    @Override
    public List<Object> arguments() {
        return this.list;
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
