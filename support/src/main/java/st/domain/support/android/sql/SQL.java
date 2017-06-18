package st.domain.support.android.sql;

import java.util.List;
import java.util.Objects;

/**
 * Created by xdata on 12/31/16.
 */

public interface SQL extends CharSequence  {

    String sql ();

    List<Object> arguments();

    SQL addArgument( Object value );
}
