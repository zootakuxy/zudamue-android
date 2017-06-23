package st.zudamue.support.android.sql;

import java.util.List;

/**
 * Created by xdata on 12/31/16.
 */

public interface SQL extends CharSequence  {

    String sql ();

    List<Object> arguments();

    SQL addArgument( Object value );
}
