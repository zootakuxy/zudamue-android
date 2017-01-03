package st.domain.support.android.sql;

import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * Created by xdata on 12/25/16.
 */

public interface SQLRow {

    String NULL = "null";
    String INTEGER = "integer";
    String FLOAT = "float";
    String STRING = "string";
    String TIME = "time";
    String DATE = "date";
    String TIMESTAMP = "timestamp";
    String BLOB = "blob";

    /**
     * Get the get the value inSelect row
     * @param columnName the name of column
     * @return Object
     */
    Object value(String columnName);

    /**
     * Get th column integer
     * @param columnName the name of column
     * @return Integer
     */
    Integer integer(String columnName);

    /**
     * Get the float value
     * @param columnName the name of column
     * @return Float
     */
    Float real(String columnName);

    /**
     * Get de double value
     * @param columnName the name of column
     * @return Double
     */
    Double realDouble(String columnName);

    /**
     * get the String value
     * @param columnName the name of column
     * @return String
     */
    String string(String columnName);

    /**
     * Get the blob|bye[] value
     * @param columnName the name of columnName
     * @return byte[]
     */
    byte[] blob(String columnName);

    /**
     * Get the date value
     * @param columnName the name of column
     * @return Date
     */
    Date date(String columnName);


    /**
     * Get the timestamp value
     * @param columnName the name of column
     * @return Timestamp
     */
    Date timestamp(String columnName);

    /**
     * Get the time value
     * @param columnName the name of columnName
     * @return Time
     */
    Date time (String columnName);

    /**
     * Get the type of one cell in row
     * @param columnName the name of column
     * @return {@value NULL} {@value INTEGER} {@value } {@value FLOAT} {@value TIME} {@value DATE} {@value TIMESTAMP} {@value BLOB}
     */
    String type(String columnName);
}
