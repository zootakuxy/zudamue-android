package st.zudamue.support.android.sql;

/**
 * Created by xdaniel on 12/31/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
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
