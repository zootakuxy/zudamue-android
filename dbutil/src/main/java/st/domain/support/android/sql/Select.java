package st.domain.support.android.sql;

/**
 *
 * Created by xdata on 12/25/16.
 */

public interface Select  extends SQL {

    interface SelectResult  extends From, SQL, SelectColumn {}

    String sql();

    SelectResult select(CharSequence ... columns);

    interface SelectColumn {

        SelectResult column (CharSequence columnName);

        SelectResult column (CharSequence columnName, String asAlias);
    }


    interface FromResult extends Select, Where, InnerJoin, LeftJoin, RigthJoin,
            FullJoin, GroupBy, OrderBy, Limit {}
    interface From extends SQL {

        FromResult from(CharSequence query);

        FromResult from(CharSequence queryTable, String aliasTable);

    }



    interface Join extends SQL {

        JoinOperator on(CharSequence column);
    }

    interface JoinOperatorResult extends InnerJoin,
            LeftJoin,
            FullJoin,
            RigthJoin,
            Where,
            GroupBy,
            OrderBy,
            Limit {}

    interface JoinOperator extends SQL {

        JoinOperatorResult jEqual(CharSequence column);
        JoinOperatorResult jNotEqual(CharSequence column);
        JoinOperatorResult jLike(CharSequence column);
        JoinOperatorResult jNotLike(CharSequence column);
        JoinOperatorResult jIsNull();
        JoinOperatorResult jIsNotNUll();

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

    interface WhereOperatorResult extends JoinOperatorWhere, GroupBy, OrderBy, Limit {}
    interface WhereOperator extends SQL {

        WhereOperatorResult equal(CharSequence argument);
        WhereOperatorResult notEqual(CharSequence argument);
        WhereOperatorResult like (CharSequence argument);
        WhereOperatorResult in (CharSequence ... argument);
        WhereOperatorResult notIn (CharSequence ... argument);
        WhereOperatorResult notLike (CharSequence argument);
        WhereOperatorResult between (CharSequence argumentStart, CharSequence argumetEnd);
        WhereOperatorResult isNull ();
        WhereOperatorResult isNotNull ();

    }


    interface InnerJoin extends SQL {

        Join innerJoin(String tableName);
    }

    interface  LeftJoin extends SQL {

        Join leftJoin (String tableName);
    }

    interface  RigthJoin extends SQL {

        Join rightJoin(String tableName);
    }


    interface FullJoin extends SQL {

        Join fullJoin (String tableName);
    }

    interface WhereResult extends  GroupBy, OrderBy, Limit {}
    interface  Where extends SQL {

        WhereResult where (boolean result);

        WhereOperator where (CharSequence ... columns);

    }


    interface GroupByResult extends OrderBy, Limit {}
    interface  GroupBy extends SQL {

        GroupByResult groupBy (String ... columns);

    }


    interface OrderBy extends SQL {

        OrderByMode orderBy ();
    }

    interface OrderByModeResult extends OrderByMode, Limit {}
    interface OrderByMode extends SQL {

        OrderByModeResult asc(String column);

        OrderByModeResult desc(String column);

    }

    interface Limit extends SQL{

        SQL limit (int limit);

        SQL limit (CharSequence limit);
    }
}
