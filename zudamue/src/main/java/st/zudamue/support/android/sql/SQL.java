package st.zudamue.support.android.sql;

import java.util.List;

/**
 * Created by xdaniel on 12/31/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public interface SQL extends CharSequence  {

    String sql ();

    List<Object> arguments();

    SQL addArgument( Object value );
}
