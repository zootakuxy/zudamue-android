package st.zudamue.support.android.sql.builder;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import st.zudamue.support.android.sql.AbstractSQL;
import st.zudamue.support.android.sql.SQL;
import st.zudamue.support.android.sql.object.Identifier;

/**
 * Created by xdaniel on 12/25/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */


public class Select extends SelectInterfaces
{

    String query;
    private List<Object> arguments;
    private String tag;
    String level;
    String variable;
    public String name;

    public Select() {
        this.query = "";
        this.arguments = new LinkedList<>();
        this.tag = this.getClass().getSimpleName();
        this.level = "";
        this.variable ="?";
    }

    public Select as(String name) {
        this.name = name;
        return this;
    }

    public Select(CharSequence ... columns) {
        this();
        this.select(columns);
    }

    void setLevel(String level) {
        if(level == null) level = "";
        this.level = level;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public String sql() {
        String campos [] = this.query.split("\n");
        String sql = "";
        int iCount = 0;
        for (String line: campos){
            if(iCount == 0) sql += line;
            else sql += level+line;
            iCount++;
            if(iCount < campos.length) sql +="\n";
        }
        return sql;
    }

    @Override
    public List<Object> arguments() {
        return this.arguments;
    }


    @Override
    public st.zudamue.support.android.sql.Select.SelectResult select(CharSequence ... columns) {

        initQuery();
        for (int i = 0; i < columns.length; i++) {
            column(columns[i]);
            if(i+1 < columns.length)
                this.query = this.query + ",";
        }

        return this;
    }

    @Override
    public SelectResult column(CharSequence columnName) {
        initQuery();
        String column = processIdentifier(columnName);
        this.query += " "+column;
        return this;
    }

    private void initQuery() {
        if(this.query == null || query.isEmpty())
            this.query = "select";
    }

    @Override
    public SelectResult column(CharSequence columnName, String asAlias) {
        column(columnName);
        this.query+= " as "+asAlias;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.FromResult from(CharSequence tableQuery) {

        String query = this.processIdentifier( tableQuery );
        this.query = this.query + "\n  from "+query;
        if(name == null && tableQuery instanceof Select)
            this.name = ((Select) tableQuery).name;
        else if(name == null)
            this.name = query;
        return this;
    }

    @Override
    public FromResult from(CharSequence queryTable, String aliasTable) {
        from(queryTable);
        this.query+= " AS "+aliasTable;
        this.name = aliasTable;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.Join innerJoin(String tableName) {
        this.query = this.query + "\n    inner join "+tableName;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.Join leftJoin(String tableName) {
        this.query = this.query + "\n    left join "+tableName;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.Join rightJoin(String tableName) {
        this.query = this.query + "\n    right join "+tableName;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.Join fullJoin(String tableName) {
        this.query = query +"\n    full join "+tableName;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereResult where(boolean result) {
        this.query = this.query +"\n  where "+result;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperator where(CharSequence ... columns) {
        String identifier = processIdentifierConjunct(columns);
        this.query = this.query + "\n  where "+ identifier +"";
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.GroupByResult groupBy(String... columns) {

        for (int i = 0; i < columns.length; i++) {
            this.query = query + columns[i];
            if(i+1 < columns.length)
                this.query = this.query + ", ";
        }

        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.OrderByMode orderBy() {

        this.query = this.query +"\n  order by ";
        return this;
    }

    @Override
    public SQL limit(int limit) {
        this.query = query + "\n  limit "+limit;
        return this;
    }

    @Override
    public SQL limit(CharSequence limit) {
        String argument = this.processIdentifier(limit);
        this.query = query + "\n  limit "+argument;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperator on (CharSequence column) {
        this.query = this.query +" "+column+"";
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperatorResult jEqual(CharSequence column) {
        this.equal(column);
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperatorResult jNotEqual(CharSequence column) {
        this.notEqual(column);
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperatorResult jLike(CharSequence column) {
        this.like(column);
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperatorResult jNotLike(CharSequence column) {
        this.like(column);
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperatorResult jIsNull() {
        this.isNull();
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.JoinOperatorResult jIsNotNUll() {
        this.isNotNull();
        return this;
    }

    private  st.zudamue.support.android.sql.Select.WhereOperatorResult whereSignal(CharSequence argument, String signal ){
        Log.i(getTag(), String.valueOf(argument));
        String value = this.processArgument( argument );
        this.query = this.query + signal +value;
        return this;
    }


    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult equal(CharSequence argument) {
        return whereSignal( argument, " = ");
    }

    @Override
    public WhereOperatorResult less(CharSequence argument) {
        return whereSignal( argument, " < ");
    }

    @Override
    public WhereOperatorResult lessEqual(CharSequence argument) {
        return whereSignal( argument, " <= ");
    }

    @Override
    public WhereOperatorResult maior(CharSequence argument) {
        return whereSignal( argument, " > ");
    }

    @Override
    public WhereOperatorResult maiorEqual(CharSequence argument) {
        return whereSignal( argument, " >= ");
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult notEqual(CharSequence argument) {
        this.query = this.query + " <> "+ processIdentifier(argument)+"";
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult like(CharSequence argument) {
        this.query = this.query + " like "+ processIdentifier(argument)+"";
        return this;
    }

    @Override
    public WhereOperatorResult in(CharSequence... argument) {
        String argumentsConjunct = this.processArgumentsConjunct(argument);
        this.query += " in "+argumentsConjunct;
        return this;
    }

    @Override
    public WhereOperatorResult notIn(CharSequence... argument) {
        String argumentsConjunct = this.processArgumentsConjunct(argument);
        this.query += " not in "+argumentsConjunct;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult notLike(CharSequence argument) {
        this.query = this.query + " not like "+ processIdentifier(argument)+"";
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult between(CharSequence argumentStart, CharSequence argumetEnd) {
        this.query = this.query + " between "+ processIdentifier(argumentStart)+" and "+ processIdentifier(argumetEnd);
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult isNull() {
        this.query = this.query + " is null ";
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperator and(CharSequence ... column) {
        String identifier = processIdentifierConjunct(column);
        this.query = this.query +" and "+identifier;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.WhereOperator or(CharSequence ... columns) {

        String identifier = processIdentifierConjunct(columns);

        this.query = this.query +" or "+identifier;
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.OrderByModeResult asc(CharSequence col) {

        String column = col instanceof Identifier? ((Identifier) col).name() : String.valueOf( col );
        this.query = this.query + " "+column+" asc";
        return this;
    }

    @Override
    public st.zudamue.support.android.sql.Select.OrderByModeResult desc(CharSequence col ) {
        String column = col instanceof Identifier? ((Identifier) col).name() : String.valueOf( col );
        this.query = query + " "+column+" desc ";
        return this;
    }


    @Override
    public st.zudamue.support.android.sql.Select.WhereOperatorResult isNotNull() {
        this.query = this.query + " is not null  ";
        return this;
    }

}

/**
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
abstract class SelectInterfaces extends AbstractSQL implements st.zudamue.support.android.sql.Select, st.zudamue.support.android.sql.Select.FromResult, st.zudamue.support.android.sql.Select.SelectResult,
        st.zudamue.support.android.sql.Select.Join, st.zudamue.support.android.sql.Select.GroupByResult, st.zudamue.support.android.sql.Select.OrderByMode, st.zudamue.support.android.sql.Select.WhereResult,
        st.zudamue.support.android.sql.Select.WhereOperator, st.zudamue.support.android.sql.Select.JoinOperator, st.zudamue.support.android.sql.Select.OrderByModeResult,
        st.zudamue.support.android.sql.Select.WhereOperatorResult, st.zudamue.support.android.sql.Select.JoinOperatorResult
{


}
