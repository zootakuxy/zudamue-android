package com.st.dbutil.android.model;


import android.support.v4.app.Fragment;

public interface ItemFragment extends Identified
{
	/**
	 * 	Obter o tiitulo para o fragmento
	 * @return
	 */
	public CharSequence getTitle();
	
	/**
	 * Obter o fragmento
	 * @return
	 */
	public Fragment getFragment();
}
