package st.zudamue.support.android.sql.sqlite;

import st.zudamue.support.android.sql.Delete;

/**
 * Created by xdaniel on 12/31/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class SQLiteDelete implements Delete, Delete.Where, Delete.WhereOperator, Delete.JoinWhereOperator {


    @Override
    public Where deleteFrom(String tableName) {
        return this;
    }


    @Override
    public WhereOperator where(CharSequence columnName) {
        return this;
    }

    @Override
    public JoinWhereOperator equal(CharSequence column) {
        return this;
    }

    @Override
    public JoinWhereOperator notEqual(CharSequence column) {
        return this;
    }

    @Override
    public JoinWhereOperator like(CharSequence column) {
        return this;
    }

    @Override
    public JoinWhereOperator notLike(CharSequence column) {
        return this;
    }

    @Override
    public JoinWhereOperator between(CharSequence columnStart, CharSequence columnEnd) {
        return this;
    }

    @Override
    public JoinWhereOperator isNull() {
        return this;
    }

    @Override
    public JoinWhereOperator isNotNull() {
        return this;
    }

    @Override
    public WhereOperator and(CharSequence column) {
        return this;
    }

    @Override
    public WhereOperator or(CharSequence column) {
        return this;
    }
}
