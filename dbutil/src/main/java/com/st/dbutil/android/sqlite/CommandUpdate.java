package com.st.dbutil.android.sqlite;

/**
 * Created by xdata on 7/23/16.
 */
public class CommandUpdate
{
    public interface  CompletUpdate extends Update, Set, SetColumns, Values, Where, WhereROWID
    {
    }

    public interface Update
    {
        /**
         * Definir o nome da tabela que ira ser atualizada
         * @param tableName O nome da tabela a ser atulizada
         * @param <T>
         * @return
         * @throws DMLException
         */
        public <T extends Set & SetColumns> T update(String tableName, String primaryKey) throws DMLException;
    }

    public interface Set
    {
        /**
         * Definir a coluna que ira receber a atulizacao juntament com o seu valor
         * @param columnName O nome da coluna
         * @param value O valor que essa recebera
         * @param <T>
         * @return
         * @throws DMLException
         */
        public <T extends Set & SetColumns & Where & WhereROWID> T set(String columnName, Object value) throws DMLException;
    }

    public interface SetColumns
    {
        /**
         * Definir os colunas que irao sofrer as alteracao
         * @param columnsName Os nomes das colunas
         * @param <T>
         * @return
         * @throws DMLException
         */
        public <T extends Values> T setColumns(String... columnsName) throws DMLException;
    }

    public interface Values
    {
        /**
         * Definir os valores para as colunas previamentes definidas
         * @param values Os valores para as colunas definidas previmente
         * @param <T>
         * @return
         * @throws DMLException
         */
        public <T extends Set & Where & SetColumns> T values(Object... values)throws DMLException;
    }

    public interface Where
    {
        /**
         * Definir as condicoes de atualizacoes
         * @param condicion A condicao para atuliza
         * @param <T>
         * @return
         * @throws DMLException
         */
        public <T extends DML & WhereROWID> T where(DML.Condicion condicion) throws DMLException;
    }


    public interface WhereROWID
    {
        public <T extends DML> T rowId(String... id) throws DMLException;
    }
}
