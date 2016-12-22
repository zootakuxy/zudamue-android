package st.domain.support.android.sqlite;

/**
 * Created by xdata on 7/23/16.
 */
public interface CommandDelect
{
    public interface CompletDelete extends  DeleteFrom, Where, WhereAdd
    {

    }

    public interface DeleteFrom
    {
        public <T extends Where> T deleteFrom(String tableName) throws DMLException;
    }

    public interface Where
    {
        public <T extends CommandSelect.WhereAdd> T where(DML.Condicion condicion) throws DMLException;
    }

    public interface WhereAdd
    {
        public <T> T and(DML.Condicion condicion) throws DMLException;

        public <T> T or(DML.Condicion condicion) throws DMLException;
    }

}
