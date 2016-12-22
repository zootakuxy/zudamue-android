package st.domain.support.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import st.domain.support.android.R;
import st.domain.support.android.adapter.ViewPagerAdpter;
import st.domain.support.android.model.ItemFragment;
import st.domain.support.android.model.ListNavegation;
import st.domain.support.android.view.SlidingTabLayout;


public class GeralFragmentPager extends Fragment implements ListNavegation
{
	private ViewPagerAdpter adapter;
	private ViewPager pager;
	private SlidingTabLayout tabLayout;
	private int defaultTabsLayoutId;
	private int defaultAbaTilteId;
	private SlidingTabLayout.TabColorizer customTabColorize;
	private boolean distributeEvenly;
	private Context context;
	private SlidingTabLayout.TypeAba typeAba;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.adapter = new ViewPagerAdpter(this.getChildFragmentManager(), this.context);
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
	}

	/**
	 * Constroi the scrooll view pager
	 */
	public void setUp()
	{

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
			else this.tabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));

			this.tabLayout.setViewPager(this.pager);
		}
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
	public void naveTo(String keyLoacal) 
	{
		Fragment frag = this.adapter.getItem(keyLoacal);
		if(frag != null)
		{
			int position = this.adapter.getItemPosition(frag);
			this.naveTo(position);
		}
			
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

	public ViewPagerAdpter getAdapter() {
		return adapter;
	}
}
