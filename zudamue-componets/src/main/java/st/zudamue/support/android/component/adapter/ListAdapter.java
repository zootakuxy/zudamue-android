package st.zudamue.support.android.component.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import st.zudamue.support.android.component.model.ItemView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xdaniel
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class ListAdapter extends BaseAdapter
{

	List<ItemView> listItems;
	private Context context;

	public ListAdapter(Context context)
	{
		this.listItems =  new LinkedList<>();
		this.context = context;
	}
	
	public ListAdapter()
	{
		this.listItems = new LinkedList<>();
	}
	
	public void setContext(Context context)
	{
		this.context = context;
	}
	
	@Override
	public int getCount() 
	{
		return listItems.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return listItems.get(position).getObject();
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ItemView item = this.getItemView(position);
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView  = item.createView(position, inflater, view, viewGroup);
		return itemView;
	}

	public ItemView getItemView(int position)
	{
		return this.listItems.get(position);
	}


	public void addItem(ItemView item)
	{
		this.listItems.add(item);
	}
	
	public void removeItem(int index)
	{
		this.listItems.remove(index);
	}

	protected List<ItemView> getList()
	{

		return this.listItems;
	}

	public void addItems( List<? extends ItemView> metreages)
	{
		for(ItemView item: metreages)
			this.addItem(item);
	}

	public void clear()
	{
		this.listItems.clear();
	}

	public void replace(List <? extends ItemView> listData)
	{
		this.clear();
		this.addItems(listData);
	}

	public void moveItemTo(int indexItem, int newPosition)
	{
		ItemView itemMove = this.listItems.get(indexItem);
		this.listItems.remove(indexItem);
		this.listItems.add(newPosition, itemMove);
	}

	public void moveItemTo(ItemView item, int newPosition)
	{
		this.listItems.remove(item);
		this.listItems.add(newPosition, item);
	}

	public void addItem(int position, ItemView defaultClientView)
	{
		this.listItems.add(position, defaultClientView);
	}
}
