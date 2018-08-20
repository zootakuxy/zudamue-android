package st.zudamue.support.android.sql;

import java.util.Date;

/**
 * Created by xdaniel on 12/25/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public interface SQLRow {

    String NULL = "null";
    String INTEGER = "i";
    String FLOAT = "f";
    String STRING = "t";
    String TIME = "time";
    String DATE = "value";
    String TIMESTAMP = "timestamp";
    String BLOB = "byte";

    /**
     * Get the get the value inSelect row
     * @param columnName the name of column
     * @return Object
     */
    Object value( CharSequence columnName);

    /**
     * Get the column as long
     * @param columnName the name of column
     * @return Long
     */
    Long longer( CharSequence columnName );

    /**
     * Get the value as integer
     * @param columnName the name of column
     * @return Integer
     */
    Integer integer( CharSequence columnName );

    /**
     * Get the float value
     * @param columnName the name of column
     * @return Float
     */
    Float real( CharSequence columnName);

    /**
     * Get de double value
     * @param columnName the name of column
     * @return Double
     */
    Double realDouble( CharSequence columnName);

    /**
     * get the String value
     * @param columnName the name of column
     * @return String
     */
    String string( CharSequence columnName);

    /**
     * Get the blob|bye[] value
     * @param columnName the name of columnNameOf
     * @return byte[]
     */
    byte[] blob( CharSequence columnName);

    /**
     * Get the value value
     * @param columnName the name of column
     * @return DateCharSequence
     */
    Date date( CharSequence columnName);


    /**
     * Get the timestamp value
     * @param columnName the name of column
     * @return TimestampCharSequence
     */
    Date timestamp( CharSequence columnName);

    /**
     * Get the time value
     * @param columnName the name of columnNameOf
     * @return TimeCharSequence
     */
    Date time ( CharSequence columnName);

    /**
     * Get the type of one cell in row
     * @param columnName the name of column
     * @return {@value NULL} {@value INTEGER} {@value } {@value FLOAT} {@value TIME} {@value DATE} {@value TIMESTAMP} {@value BLOB}
     */
    String typeOf( CharSequence columnName);

    /**
     * Get class of value of column
     * @param columnName the name of column
     * @return the classe of column
     */
    Class<?> classOf( CharSequence columnName );

    /**
     * Get name of column in index
     * @param index the index of column
     * @return the name of column
     */
    String columnNameOf(int index );

    /**
     * Get count of columns in the row
     * @return the number count of row
     */
    int columnsCount();

    /**
     * Verify if hasPath one column witch named
     * @param columnName the name of column find
     * @return name of column
     */
    boolean hasColumn( CharSequence columnName );

    <T> T get( CharSequence columnName, Class< T > type);


}
