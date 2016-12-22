package st.domain.support.android.sqlite;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import st.domain.support.android.model.Money;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by xdata on 7/23/16.
 */
public abstract class DMLite implements DML.Debugable, DML
{
    private final String prefixe;
    private boolean debugable;
    private String tagDebug;
    private int debugableType;
    private DML.Status currentStatus;
    private DML.Status oldStatus;

    public DMLite()
    {
        this.setTagDebug("DBA:APP.TEST");
        this.setDebugable(false, Log.INFO);
        this.prefixe = this.getClass().getSimpleName()+"-> ";
        this.currentStatus = BaseStatus.INIT;
    }

    protected void mapParam(ContentValues containerValues, LinkedHashMap<CharSequence, Object> mapValues)
    {
        Object obj;
        String name;
        String type;
        int index = 0;
        for(Map.Entry<CharSequence, Object> item: mapValues.entrySet())
        {
            index ++;
            type = "UNKNOW";
            obj = item.getValue();
            name = item.getKey().toString();

            if(obj instanceof byte[])
            {
                containerValues.put(name, (byte[]) obj);
                type = "byte[]";
            }
            else if(obj instanceof Boolean)
            {
                containerValues.put(name, (Boolean) obj);
                type = "Boolean";
            }
            else if(obj instanceof Byte)
            {
                containerValues.put(name, (Byte) obj);
                type = "Byte";
            }
            else if(obj instanceof Double)
            {
                containerValues.put(name, (Double) obj);
                type = "Double";
            }
            else if(obj instanceof Float)
            {
                containerValues.put(name, (Float) obj);
                type = "Float";
            }
            else if(obj instanceof Integer)
            {
                containerValues.put(name, (Integer) obj);
                type = "Integer";
            }
            else if(obj instanceof Long)
            {
                containerValues.put(name, (Long) obj);
                type = "Long";
            }
            else if(obj instanceof Short)
            {
                containerValues.put(name, (Short) obj);
                type = "Short";
            }
            else if(obj instanceof String)
            {
                containerValues.put(name, (String) obj);
                type = "String";
            }
            else if(obj instanceof Money)
            {
                containerValues.put(name, ((Money) obj).doubleValue());
                type = "Money";
            }
            else
            {
                containerValues.put(name, (obj==null)? null: obj.toString());
            }
            Log.i("DBA:APP.TEST", "MAPING param{index:\""+index+"\", name:\""+name+"\", value:\""+obj+"\", type:\""+type+"\"}");
        }
    }

    /**
     * Crear as interogacoes necessarias para um comando de DML
     * @param numInterogacion
     * @return
     */
    public static String createInterogacion(int numInterogacion)
    {
        if(numInterogacion<1) return "";
        String interogacion = "";
        for(int i=0; i<numInterogacion; i++)
            interogacion = interogacion+"?"+((i+1 <numInterogacion)?", " :"");
        return interogacion;
    }

    public static String toText(Object [] array)
    {
        return Arrays.toString(array).replace("[", "{").replace("]", "}");
    }

    @Override
    public boolean isDebugable()
    {
        return  this.debugable;
    }


    @Override
    public int getDebugableType() {
        return debugableType;
    }

    /**
     * Depurar o que estara acontecedo com o dml
     * @param debugable
     * @param typeDebug
     */
    public void setDebugable(boolean debugable, int typeDebug)
    {
        this.debugable = debugable;
        this.debugableType = typeDebug;
    }

    /**
     * Depurar o que estara acontecedo com o dml
     * @param debugable
     */
    public void setDebugable(DML.Debugable debugable)
    {
        this.debugable = debugable.isDebugable();
        this.debugableType = debugable.getDebugableType();
    }


    public String getTagDebug()
    {
        return tagDebug;
    }

    public void setTagDebug(String tagDebug) {
        this.tagDebug = tagDebug;
    }

    /**
     * Depurar um texto
     * @param  text
     */
    public void debug(String text)
    {
        switch (this.debugableType)
        {
//            case Log.INFO: Log.i(this.tagDebug, this.prefixe + text); break;
//            case Log.DEBUG: Log.d(this.tagDebug, this.prefixe + text); break;
//            case Log.ERROR: Log.e(this.tagDebug, this.prefixe + text); break;
//            case Log.VERBOSE: Log.v(this.tagDebug, this.prefixe + text); break;
//            case Log.WARN: Log.w(this.tagDebug, this.prefixe + text); break;
        }
    }

    @Override
    public void begin() throws DMLException {

    }

    @Override
    public void end() throws DMLException
    {
        this.currentStatus = BaseStatus.INIT;
    }

    public Status getStatus()
    {
        return this.currentStatus;
    }

    @Override
    public synchronized void execute() throws DMLException {}

    @Override
    public Object getResult() throws DMLException {
        return null;
    }

    @Override
    public String getSql() throws DMLException {
        return null;
    }

    public void checkStatus(DML.Status newStatus)
    {
        debug("Accept STATUS \""+newStatus.statusName()+"\" RELATIVE \""+currentStatus.statusName()+"\"");
        if(!currentStatus.accept(newStatus))
            throw new DMLException.DMLStatusException("The new action is invalid new action="+newStatus+" " +
                    "| old action = "+currentStatus.statusName()+" | require = "+currentStatus.possiblit());
        this.oldStatus = this.currentStatus;
        this.currentStatus = newStatus;
        debug("Valid Accept");

    }




    public class WhereClausa
    {
        protected final WhereConcat concat;
        public boolean result;
        protected  Condicion condicion;

        public WhereClausa(WhereConcat or, Condicion condicion) {
            this.concat = or;
            this.condicion = condicion;
        }
    }


    static  class Column implements  CharSequence, Serializable, Comparable<String>
    {
        protected String realName;
        protected String alias;
        protected boolean use;
        protected ColumnType type;

        /**
         * Proucura na lista a coluna que possui o nome real fornecido do parametro
         * @param columnName O nome da coluna real
         * @return
         */
        protected static Column find(ArrayList<Column> listColumns, String columnName)
        {
            for(Column coll: listColumns)
                if(coll.equals(columnName))
                    return coll;
            return null;
        }




        /**
         * Proucura na lista a coluna que possui o nome real fornecido do parametro
         * @param columnName O nome da coluna real
         * @return
         */
        protected static Column findReal(ArrayList<Column> listColumns, String columnName)
        {
            columnName = columnName.toUpperCase();
            for(Column coll: listColumns)
                if(coll.realName.equals(columnName))
                    return coll;
            return null;
        }

        public Column(String realName, String alias)
        {
            this.realName = realName.toUpperCase();
            this.alias = alias;
            this.type = ColumnType.SIMPLE;
        }

        public Column(String realName, String alias, ColumnType type)
        {
            this.realName = realName.toUpperCase();
            this.alias = alias;
            this.type = ColumnType.SIMPLE;
            this.type = type;
        }


        /**
         * Coparar as colunas
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj)
        {
            if(obj == null) return false;
            if(obj == this) return true;
            else if(obj instanceof Column)
            {
                Column compare = (Column) obj;
                return (compare.alias.equals(this.alias));
            }
            else if(obj instanceof String)
                return obj.toString().equals(this.alias);
            return false;
        }

        @Override
        public int length()
        {
            return this.alias.length();
        }

        @Override
        public char charAt(int index)
        {
            return this.alias.charAt(index);
        }

        @Override
        public CharSequence subSequence(int beginIndex, int endIndex)
        {
            return this.alias.subSequence(beginIndex, endIndex);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public IntStream chars()
        {
            return this.alias.chars();
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public IntStream codePoints()
        {
            return this.alias.codePoints();
        }

        @Override
        public String toString()
        {
            return this.alias.toString();
        }

        public String toText()
        {
            return "column{dbName:\""+this.realName+"\", asName:\""+alias+"\", use:\""+use+"\", type:\""+type+"\"}";
        }

        @Override
        public int compareTo(String s)
        {
            return alias.compareTo(s);
        }

        /**
         * Proucurar na lista a coluna que seja a chave primaria
         * @param listColumns
         * @return
         */
        public static Column findPrimaryKey(ArrayList<Column> listColumns)
        {
            for(Column coll: listColumns)
                if(coll.type == ColumnType.RRIMARY_KEY)
                    return coll;
            return null;
        }

        public static ArrayList<Column> useds(ArrayList<Column> listColumns)
        {
            ArrayList<Column> columns = new ArrayList<>();
            for (Column coll: listColumns)
                if(coll.use)
                    columns.add(coll);
            return columns;
        }

        /**
         * Tipo de colunas existente
         */
        public enum ColumnType
        {
            /**
             * Coluna simple
             */
            SIMPLE,

            /**
             * Coluna chave da entidade
             */
            RRIMARY_KEY
        }
    }



    public enum BaseStatus implements DML.Status
    {
        INIT(2),
        BEGIN(-1),
        EXECUTE(4),
        FINISHED(1);

        private int[] gerralPossiblit;

        /**
         *
         * @param requiredeIndex Index do auxStatus em que o currente auxStatus podera passar para ele
         */
        BaseStatus(int ... requiredeIndex)
        {
            this.gerralPossiblit = requiredeIndex;
        }

        public String possiblit()
        {
            DML.Status[] required = getPossiblite();
            return DMLite.toText(required);
        }

        @NonNull
        @Override
        public DML.Status[] getPossiblite()
        {
            DML.Status required [] = new DML.Status[this.gerralPossiblit.length];
            for(int i = 0; i< gerralPossiblit.length; i++)
            {
                DML.Status req = values()[gerralPossiblit[i]-1];
                required[i] = req;
            }
            return required;
        }

        @Override
        public boolean accept(DML.Status newPossiblit)
        {
            if(this.equals(BEGIN))
            {
                for(DML.Status status: values())
                    if(status.equalsName(newPossiblit))
                        return false;
                return true;
            }
            for(DML.Status possiblit: this.getPossiblite())
                if(newPossiblit.equalsName(newPossiblit)) return true;
            return false;
        }


        public static BaseStatus findKnowStatus(Status other)
        {
            for(BaseStatus status: values())
                if(other.statusName().equals(status.name()))
                    return status;
            return null;
        }

        @Override
        public boolean equalsName(Status other)
        {
            return this.name().equals(other.statusName());
        }

        @Override
        public Status[] statusValues() {
            return values();
        }

        @Override
        public String statusName() {
            return name();
        }
    }
}