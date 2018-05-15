package st.zudamue.support.android.sql;

/**
 * Reciver the result from any query
 *
 * Created by xdaniel on 12/30/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public interface OnQueryResult {

    /**
     * Reciver row per row from result of query
     * @param row the current row of result
     * @return  true to continue | false from break next results
     */
    boolean accept( SQLRow row );
}
