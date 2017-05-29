package st.domain.support.android.sql.builder;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import st.domain.support.android.sql.AbstractSQL;
import st.domain.support.android.sql.SQL;

/**
 *
 * Created by xdata on 12/25/16.
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
    public st.domain.support.android.sql.Select.SelectResult select(CharSequence ... columns) {

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
            this.query = "SELECT";
    }

    @Override
    public SelectResult column(CharSequence columnName, String asAlias) {
        column(columnName);
        this.query+= " AS "+asAlias;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.FromResult from(CharSequence tableQuery) {

        String query = this.processIdentifier(tableQuery);
        this.query = this.query + "\n  FROM "+query;
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
    public st.domain.support.android.sql.Select.Join innerJoin(String tableName) {
        this.query = this.query + "\n    INNER JOIN "+tableName;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.Join leftJoin(String tableName) {
        this.query = this.query + "\n    LEFT JOIN "+tableName;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.Join rightJoin(String tableName) {
        this.query = this.query + "\n    RIGHT JOIN "+tableName;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.Join fullJoin(String tableName) {
        this.query = query +"\n    FULL JOIN "+tableName;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereResult where(boolean result) {
        this.query = this.query +"\n  WHERE "+result;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperator where(CharSequence ... columns) {
        String identifier = processIdentifierConjunct(columns);
        this.query = this.query + "\n  WHERE "+ identifier +"";
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.GroupByResult groupBy(String... columns) {

        for (int i = 0; i < columns.length; i++) {
            this.query = query + columns[i];
            if(i+1 < columns.length)
                this.query = this.query + ", ";
        }

        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.OrderByMode orderBy() {

        this.query = this.query +"\n  ORDER BY";
        return this;
    }

    @Override
    public SQL limit(int limit) {
        this.query = query + "\n  LIMIT "+limit;
        return this;
    }

    @Override
    public SQL limit(CharSequence limit) {
        String argument = this.processIdentifier(limit);
        this.query = query + "\n  LIMIT "+argument;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperator on (CharSequence column) {
        this.query = this.query +" "+column+"";
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperatorResult jEqual(CharSequence column) {
        this.equal(column);
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperatorResult jNotEqual(CharSequence column) {
        this.notEqual(column);
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperatorResult jLike(CharSequence column) {
        this.like(column);
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperatorResult jNotLike(CharSequence column) {
        this.like(column);
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperatorResult jIsNull() {
        this.isNull();
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.JoinOperatorResult jIsNotNUll() {
        this.isNotNull();
        return this;
    }

    private  st.domain.support.android.sql.Select.WhereOperatorResult whereSignal( CharSequence argument, String signal ){
        Log.i(getTag(), String.valueOf(argument));
        String value = this.processIdentifier(argument);
        this.query = this.query + signal +value;
        return this;
    }


    @Override
    public st.domain.support.android.sql.Select.WhereOperatorResult equal(CharSequence argument) {
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
    public st.domain.support.android.sql.Select.WhereOperatorResult notEqual(CharSequence argument) {
        this.query = this.query + " <> "+ processIdentifier(argument)+"";
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperatorResult like(CharSequence argument) {
        this.query = this.query + " LIKE "+ processIdentifier(argument)+"";
        return this;
    }

    @Override
    public WhereOperatorResult in(CharSequence... argument) {
        String argumentsConjunct = this.processArgumentsConjunct(argument);
        this.query += " IN "+argumentsConjunct;
        return this;
    }

    @Override
    public WhereOperatorResult notIn(CharSequence... argument) {
        String argumentsConjunct = this.processArgumentsConjunct(argument);
        this.query += " NOT IN "+argumentsConjunct;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperatorResult notLike(CharSequence argument) {
        this.query = this.query + " NOT LIKE "+ processIdentifier(argument)+"";
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperatorResult between(CharSequence argumentStart, CharSequence argumetEnd) {
        this.query = this.query + " BETWEEN "+ processIdentifier(argumentStart)+" and "+ processIdentifier(argumetEnd);
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperatorResult isNull() {
        this.query = this.query + " IS NULL";
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperator and(CharSequence ... column) {
        String identifier = processIdentifierConjunct(column);
        this.query = this.query +" AND "+identifier;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.WhereOperator or(CharSequence ... columns) {

        String identifier = processIdentifierConjunct(columns);

        this.query = this.query +" OR "+identifier;
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.OrderByModeResult asc(String column) {
        this.query = this.query + " "+column+" ASC";
        return this;
    }

    @Override
    public st.domain.support.android.sql.Select.OrderByModeResult desc(String column) {
        this.query = query + " "+column+" DESC";
        return this;
    }


    @Override
    public st.domain.support.android.sql.Select.WhereOperatorResult isNotNull() {
        this.query = this.query + " IS NOT NULL ";
        return this;
    }

}

abstract class SelectInterfaces extends AbstractSQL implements st.domain.support.android.sql.Select, st.domain.support.android.sql.Select.FromResult, st.domain.support.android.sql.Select.SelectResult,
        st.domain.support.android.sql.Select.Join, st.domain.support.android.sql.Select.GroupByResult, st.domain.support.android.sql.Select.OrderByMode, st.domain.support.android.sql.Select.WhereResult,
        st.domain.support.android.sql.Select.WhereOperator, st.domain.support.android.sql.Select.JoinOperator, st.domain.support.android.sql.Select.OrderByModeResult,
        st.domain.support.android.sql.Select.WhereOperatorResult, st.domain.support.android.sql.Select.JoinOperatorResult
{


}
