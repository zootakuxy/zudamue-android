package com.st.dbutil.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.st.dbutil.android.R;
import com.st.dbutil.android.adapter.ViewPagerAdpter;
import com.st.dbutil.android.model.ItemFragment;
import com.st.dbutil.android.model.ListNavegation;
import com.st.dbutil.android.view.SlidingTabLayout;


public class GeralActivityPager extends AppCompatActivity implements ListNavegation
{
	private ViewPagerAdpter adapter;
	private ViewPager pager;
	private SlidingTabLayout tabLayout;
	private int defaultTabsLayoutId;
	private int defaultAbaTilteId;
	private SlidingTabLayout.TabColorizer customTabColorize;
	private boolean distributeEvenly;
	private SlidingTabLayout.TypeAba typeAba;
	private int[] selectedIndicatorColor;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.adapter = new ViewPagerAdpter(this.getSupportFragmentManager(), this);
		this.distributeEvenly = true;
	}

	/**
	 * Detribução iguas a todos
	 * @param distributeEvenly
     */
	public void setDistributeEvenly(boolean distributeEvenly)
	{
		this.distributeEvenly = distributeEvenly;
	}

	/**
	 * Adicionar um novo fragemnto na activite
	 * @param item
	 */
	public void addFragment(ItemFragment item)
	{
		this.adapter.addFragment(item);
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"."+new Throwable().getLocalizedMessage()+"-> TOTAL ZISE "+adapter.getCount());
	}

	/**
	 * Constroi the scrooll view pager
	 */
	public void setUp()
	{
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> .setUp\\pagers: "+adapter.getCount());
		if(tabLayout != null && this.pager != null)
		{
			this.pager.setAdapter(this.adapter);

			if(this.typeAba != null)
				this.tabLayout.setTypeAba(this.typeAba);
			else if(defaultTabsLayoutId == 0 || defaultAbaTilteId == 0)
				this.tabLayout.setCustomTabView(this.defaultTabsLayoutId, this.defaultAbaTilteId);

			this.tabLayout.setDistributeEvenly(this.distributeEvenly);

			if (this.customTabColorize != null)
				this.tabLayout.setCustomTabColorizer(this.customTabColorize);
			else if(this.selectedIndicatorColor != null)
				this.tabLayout.setSelectedIndicatorColors(this.selectedIndicatorColor);
			else this.tabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));

			Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> total pager found: "+this.adapter.getCount());
			this.tabLayout.setViewPager(this.pager);
		}
	}

	public void setCustomTabColorize(SlidingTabLayout.TabColorizer customTabColorize) {
		this.customTabColorize = customTabColorize;
	}

	public void setSelectedIndicatorColor(int ...selectedIndicatorColor) {
		this.selectedIndicatorColor = selectedIndicatorColor;
	}

	public void setViewPager(ViewPager pager)
	{
		this.pager = pager;
	}

	@Override
	public void naveTo(Navegation navegation)
	{
		if(pager == null 
				|| navegation == null
				|| this.adapter.getCount() == 0)
			return;
		
		int position = this.pager.getCurrentItem();
		int size = this.adapter.getCount();
		
		switch (navegation)
		{
			case GO_FRONT: case GO_RITHG:
				this.naveTo(++position);
				break;
			case GO_BACK: case GO_LEFT:
				this.naveTo(--position);
				break;
			case GO_FRIST:
				this.naveTo(0);
				break;
			case GO_LAST:
				this.naveTo(size-1);
				break;
		}
			
	}

	@Override
	public void naveTo(String keyFragment)
	{
		int index = this.adapter.getItemPosition(keyFragment);
		if(index != -1)
			this.naveTo(index);
		else Log.e("DBA:APP.TEST", getClass().getSimpleName()+"-> Não consegue navegara para  "+keyFragment);
	}

	@Override
	public void naveTo(int index) 
	{
		if(index <0 
				|| index>this.adapter.getCount()
				|| pager == null
				|| adapter.getCount() == 0)
			return;
			this.pager.setCurrentItem(index);
		
	}

	public void setTabLayout(SlidingTabLayout tabLayout)
	{
		this.tabLayout = tabLayout;
	}

	public void setCustonView(Integer layoutId, Integer layoutTilteId)
	{
		this.defaultTabsLayoutId = layoutId;
		this.defaultAbaTilteId = layoutTilteId;
	}

	public ItemFragment getItem(int position)
	{
		return this.adapter.getItemFragment(position);
	}

	public ViewPagerAdpter getAdapter()
	{
		return adapter;
	}

	public void setTypeAba(SlidingTabLayout.TypeAba typeAba) {
		this.typeAba = typeAba;
	}
}
