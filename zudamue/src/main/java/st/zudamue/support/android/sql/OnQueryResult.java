package st.zudamue.support.android.sql;

/**
 * Reciver the result from any query
 * Created by xdata on 12/30/16.
 */
public interface OnQueryResult {

    /**
     * Reciver row per row from result of query
     * @param row the current row of result
     * @return  true to continue | false from break next results
     */
    boolean accept( SQLRow row );
}
