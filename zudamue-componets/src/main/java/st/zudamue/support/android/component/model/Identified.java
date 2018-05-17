package st.zudamue.support.android.component.model;

import java.io.Serializable;

/**
 * Created by xdaniel on 7/26/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public interface Identified extends Serializable
{
    /**
     * Obter o do cliente na net
     * @return
     */
    public CharSequence getIdentifier();
}
