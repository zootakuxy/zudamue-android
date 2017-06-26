package st.zudamue.support.android.model;


import android.support.v4.app.Fragment;

/**
 * Created by xdaniel
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public interface ItemFragment extends Identified
{
	/**
	 * 	Obter o tiitulo para o fragmento
	 * @return
	 */
	CharSequence getTitle();
	
	/**
	 * Obter o fragmento
	 * @return
	 */
	Fragment getFragment();
}
