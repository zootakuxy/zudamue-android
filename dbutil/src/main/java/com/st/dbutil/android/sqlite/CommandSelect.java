package com.st.dbutil.android.sqlite;

/**
 * Created by xdata on 7/23/16.
 */
public interface CommandSelect
{
    /**
     * public <T extends From & Where> T insert(String ... columns);
     **/

    public interface ComplectSelect extends Select, SelectAs, From, Join, JoinON, Where, WhereAdd, Order, Group, Limit, WhereROWID {
    }
    public interface Select
    {
        /**
         * Identificar as colunas que serao selecionadas da tabela
         * @param columns
         * @param <T>
         * @return
         */
        public <T extends From & SelectAs> T select(String... columns) throws DMLException;

    }

    public interface SelectAs
    {
        /**
         * Renomear as colunas
         * @param aliasName
         * @param <T>
         * @return
         */
        public <T extends From & Select> T as(String... aliasName) throws DMLException;
    }

    public interface From
    {
        public <T extends Join & Where & Limit & Group & WhereROWID & DML> T from(String tableName) throws DMLException;

        public <T extends Join & Where & Limit & Group & WhereROWID & DML> T fromKey(String tableName, String primaryKey) throws DMLException;
    }

    public interface Join
    {
        public JoinON  innerJoin(String tableName) throws DMLException;

        public JoinON rightJoin(String tableName) throws DMLException;

        public JoinON leftJoin(String tableName) throws DMLException;

        public JoinON fullJoin(String tableName) throws DMLException;
    }

    public interface  JoinON
    {
        public <T extends Join & Where & Limit & Group>T on(DML.Condicion condicion, String... columns) throws DMLException;
    }

    public  interface Where
    {
        public <T extends WhereAdd & Limit & Group & Order & DML> T where(DML.Condicion condicion) throws DMLException;
    }

    public  interface WhereROWID
    {
        public  <T extends WhereAdd & Limit & Group  & Order> T whereRowId(String... id) throws DMLException;
    }

    public interface  WhereAdd
    {
        public <T extends WhereAdd & Limit & Group & Order> T and(DML.Condicion condicion) throws DMLException;

        public  <T extends WhereAdd & Limit & Group  & Order> T or(DML.Condicion condicion) throws DMLException;
    }

    public interface Group
    {
        public <T extends Limit & Order> T groupBy(String... group) throws DMLException;
    }

    public interface Order
    {
        public <T extends Limit & Order> T orderByAsc(String... column) throws DMLException;

        public <T extends Limit & Order> T orderByDesc(String... column) throws DMLException;
    }

    public interface Limit
    {
        public DML limit(int num) throws DMLException;
    }
}
