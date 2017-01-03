package st.domain.support.android.old_sql.sqlite;

import st.domain.support.android.sql.SQLRow;

/**
 * Created by xdata on 7/23/16.
 */
public interface   DML
{
    String ROW_ID = "primaryKey";

    /**
     * Abrir o prepare stantement
     */
    public void begin() throws DMLException;

    /**
     * Limpar e canselar o stantment corrente
     */
    public void end() throws DMLException;

    /**
     * Executar o stantement preparado
     */
    public void execute() throws DMLException;

    /**
     * Obter o resultado do stantment
     * @return
     */
    public Object getResult() throws DMLException;

    /**
     * Obter o AbstractSQL do stamnetnt
     * @return
     */
    public String getSql() throws DMLException;


    /**
     * Obter o estado da operacao
     * @return
     */
    public Status getStatus();

    public interface CatchResult
    {
        void catchInto(SQLRow rowResult);
    }

    /**
     * Codicao dos AbstractSQL
     */
    public abstract class Condicion
    {
        /**
         * Validar se a codicao sera aceite ou nao
         * @param wherePosition
         * @param row
         * @return
         */
        public abstract boolean accept(int wherePosition, SQLRow row);

        public String getSql()
        {
            return "get sql not @Overrider";
        }
    }

    public interface Debugable
    {
        boolean isDebugable();

        int getDebugableType();
    }

    /**
     * Os topos de concatenacao da condicoes existentes
     */
    public enum WhereConcat
    {
        AND,
        OR
    }

    public interface Status
    {
        /**
         * Verificar a o estado aceita a possiblidade indicada
         * @param newPossiblit
         * @return
         */
        public boolean accept(Status newPossiblit);

        /**
         * Obter as possiblidades em forma de um string
         * @return
         */
        public String possiblit();

        /**
         * Obter as possiveis possiblidades
         * @return
         */
        public Status[] getPossiblite();

        /**
         * Obter o valor total dos estados
         * @return
         */
        public Status[] statusValues();

        public String statusName();


        boolean equalsName(Status other);
    }
}
