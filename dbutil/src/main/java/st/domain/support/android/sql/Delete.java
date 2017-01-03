package st.domain.support.android.sql;

/**
 *
 * Created by xdata on 12/31/16.
 */

public interface Delete {

    Where deleteFrom (String tableName);

    interface Where {

        WhereOperator where(CharSequence columnName);
    }

    interface WhereOperator {

        JoinWhereOperator equal(CharSequence column);
        JoinWhereOperator notEqual(CharSequence column);
        JoinWhereOperator like (CharSequence column);
        JoinWhereOperator notLike (CharSequence column);
        JoinWhereOperator between (CharSequence columnStart, CharSequence columnEnd);
        JoinWhereOperator isNull ();
        JoinWhereOperator isNotNull ();

    }

    interface JoinWhereOperator {

        WhereOperator and (CharSequence column);
        WhereOperator or (CharSequence column);

    }
}
