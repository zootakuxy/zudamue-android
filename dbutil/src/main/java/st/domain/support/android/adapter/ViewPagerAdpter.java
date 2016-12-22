package st.domain.support.android.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import st.domain.support.android.model.ItemFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewPagerAdpter extends FragmentStatePagerAdapter implements Serializable
{
	private final Rect defaultBounds;
	private Rect custonBounds;
	private HashMap<CharSequence, ItemFragment> mapFragments;
	private ArrayList<ItemFragment> listFragments;
	private Context context;
	
	public ViewPagerAdpter(FragmentManager fm, Context context)
	{
		super(fm);
		this.context = context;
		this.mapFragments = new HashMap<>();
		this.listFragments = new ArrayList<>();
		this.defaultBounds = new Rect(0, 0, 40, 40);
	}

	public static SpannableString createSpannableString(String text, Drawable icon, Rect bounds)
	{
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
			if(item.getProtocolKey().equals(key))
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
	public CharSequence getPageTitle(int position) 
	{
		CharSequence title = this.listFragments.get(position).getTitle();
		return  title;
	}

	public CharSequence getSimpleTitle(int position)
	{
		return this.listFragments.get(position).getTitle();
	}

	
	/**
	 * Adicionar um fragmento novo
	 */
	public void addFragment(ItemFragment item)
	{
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> Adding fragement item "+item);
		if(mapFragments.containsKey(item.getProtocolKey()))
		{
			Log.e("DBA:APP.TEST", "Duplicated protocol key in  view pager");
			throw new Error("Duplicated protocol key in  view pager");
		}
		this.mapFragments.put(item.getProtocolKey(), item);
		this.listFragments.add(item);
	}
	
	/**
	 * Proucurar o fragmento na lista
	 * @param key
	 * @return
	 */
	public Fragment find(String key)
	{
		return this.mapFragments.get(key).getFragment();
	}


	/**
	 * Limpar por completo o adapter 
	 * remover todos dos fragmetos do adpter
	 */
	public void clear ()
	{
		this.listFragments.clear();
		this.mapFragments.clear();
	}

	/**
	 * Obter a lista do  mapa do fragmento
	 * @return
	 */
	public Set<Map.Entry<CharSequence, ItemFragment>> getMap()
	{
		return this.mapFragments.entrySet();
	}

}