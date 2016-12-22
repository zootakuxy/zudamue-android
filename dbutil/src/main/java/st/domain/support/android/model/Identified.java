package st.domain.support.android.model;

import java.io.Serializable;

/**
 * Created by xdata on 7/26/16.
 */
public interface Identified extends Serializable
{
    /**
     * Obter o do cliente na net
     * @return
     */
    public CharSequence getProtocolKey();
}
