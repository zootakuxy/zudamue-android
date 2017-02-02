package st.domain.support.android.model;


import android.support.v4.app.Fragment;

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
