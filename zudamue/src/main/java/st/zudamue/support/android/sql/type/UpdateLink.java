package st.zudamue.support.android.sql.type;

import st.zudamue.support.android.sql.SQL;

/**
 *
 * Created by daniel on 3/14/17.
 */

public interface UpdateLink {


    interface Update extends SQL{
        UpdateSet update( CharSequence tableName );
    }


    interface UpdateSet extends SQL {
        UpdateLinkSet set ( CharSequence columnName, CharSequence argument );
    }

    interface UpdateLinkSet extends UpdateSet, UpdateLink, Where, SQL {}

    interface Where extends SQL {
        WhereOperator where( CharSequence column );
    }



    interface JoinOperatorWhere extends SQL {

        /**
         * and column
         * in default de value wait is one column name for value this value require instance of { BaseTypeCharSequence }
         * @param column
         * @return
         */
        WhereOperator and(CharSequence ... column);
        WhereOperator or(CharSequence ... column);

    }

    interface WhereOperatorResult extends JoinOperatorWhere, SQL {}

    interface WhereOperator extends SQL {

        WhereOperatorResult equal(CharSequence argument);
        WhereOperatorResult notEqual(CharSequence argument);
        WhereOperatorResult like (CharSequence argument);
        WhereOperatorResult in (CharSequence ... argument);
        WhereOperatorResult notIn (CharSequence ... argument);
        WhereOperatorResult notLike (CharSequence argument);
        WhereOperatorResult between (CharSequence argumentStart, CharSequence argumentEnd);
        WhereOperatorResult isNull ();
        WhereOperatorResult isNotNull ();

    }

}
