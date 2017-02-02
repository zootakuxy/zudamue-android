package st.domain.support.android.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import st.domain.support.android.model.ItemFragment;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter implements Serializable, BasePagerAdapter
{
	private final Rect defaultBounds;
	private Rect custonBounds;
	private HashMap<CharSequence, ItemFragment> mapFragments;
	private ArrayList<ItemFragment> listFragments;
	private Context context;
	private String tag;

	public BaseFragmentPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
		this.mapFragments = new HashMap<>();
		this.listFragments = new ArrayList<>();
		this.defaultBounds = new Rect(0, 0, 40, 40);
		this.tag = this.getClass().getSimpleName();
	}

	public static SpannableString createSpannableString(String text, Drawable icon, Rect bounds) {
		icon.setBounds(bounds);
		ImageSpan imageSpan = new ImageSpan(icon);
		SpannableString spannableString = new SpannableString(text);
		spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}

	@Override
	public Fragment getItem(int position)
	{
		return this.listFragments.get(position).getFragment();
	}
	
	
	public Fragment getItem(String key)
	{
		return this.mapFragments.get(key).getFragment();
	}

	public int getItemPosition(String key)
	{
		int index = 0;
		for(ItemFragment item: this.listFragments)
		{
			if(item.getIdentifier().equals(key))
				return index;
			index++;
		}
		return -1;
	}

	public ItemFragment getItemFragment(int position)
	{
		return  this.listFragments.get(position);
	}

	@Override
	public int getCount() 
	{
		return mapFragments.size();
	}
	
	
	
	@Override
	public CharSequence getPageTitle(int position) {
		CharSequence title = this.listFragments.get(position).getTitle();
		return  title;
	}

	
	/**
	 * Adicionar um fragmento novo
	 */
	public void addFragment(ItemFragment item)
	{
		Log.i(getTag(), getClass().getSimpleName()+"-> Adding fragement item "+item);
		if(mapFragments.containsKey(item.getIdentifier()))
		{
			Log.e(getTag(), "Duplicated protocol key inSelect  view pager");
			throw new Error("Duplicated protocol key inSelect  view pager");
		}
		this.mapFragments.put(item.getIdentifier(), item);
		this.listFragments.add(item);
	}


	/**
	 * Limpar por completo o adapter 
	 * remover todos dos fragmetos do adpter
	 */
	public void clear () {
		this.listFragments.clear();
		this.mapFragments.clear();
	}

	public ArrayList<ItemFragment> getListFragments() {
		return listFragments;
	}

	/**
	 * Obter a lista do  mapa do fragmento
	 * @return
	 */
	public Map<CharSequence, ItemFragment> getMap()
	{
		return this.mapFragments;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}