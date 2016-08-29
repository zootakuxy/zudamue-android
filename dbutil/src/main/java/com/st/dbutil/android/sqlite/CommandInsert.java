package com.st.dbutil.android.sqlite;

/**
 * Created by xdata on 7/23/16.
 */
public interface CommandInsert
{
    /**
     * public <T extends From & Where> T insert(String ... columns);
     **/

    public interface ComplectInsert extends Insert, Values, Columns, As, Returning
    {

    }

    public interface Insert
    {
        /**
         * Identificar as colunas que serao selecionadas da tabela
         * @param <T>
         * @param tObject
         *@param tableName  @return
         */
        public <T extends Values & Columns & As> T insertInto(String tObject, String tableName) throws DMLException;
    }

    public interface Values
    {
        /**
         * Os valores que serao inserido
         * @param values
         * @param <T>
         * @return
         */
        public <T extends Returning & Values> T values(Object... values) throws DMLException;
    }

    public interface  Columns
    {
        /**
         * As colunas que irao receber os dados
         * @param columnsName
         * @param <T>
         * @return
         */
        public <T extends As & Values> T columns(String... columnsName) throws DMLException;
    }

    public  interface  As
    {
        /**
         * O quere em que o insert ira receber
         * @param query
         * @param <T>
         * @return
         */
        public <T extends Returning>  T as (LiteSelect query) throws DMLException;
    }

    public interface Returning
    {
        /**
         * O que se pretende recuperar depois que a coluna for inserida
         * @param catchResult
         */
        public <T extends DML> T returning(DML.CatchResult catchResult) throws DMLException;
    }
}
