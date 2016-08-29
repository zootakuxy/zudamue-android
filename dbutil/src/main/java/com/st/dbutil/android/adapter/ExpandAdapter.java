package com.st.dbutil.android.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.st.dbutil.android.model.ItemView;
import com.st.dbutil.android.model.ItemViewExpand;

import java.util.ArrayList;

/**
 * Created by xdata on 8/2/16.
 */
public class ExpandAdapter extends BaseExpandableListAdapter
{
    private final Context context;
    private ArrayList<Pair<ItemView, ArrayList<ItemViewExpand>>> listGroup;
    private boolean hasStableIds;

    public ExpandAdapter(Context context, boolean hasStableIds)
    {
        this.context = context;
        this.listGroup = new ArrayList<>();
        this.hasStableIds = hasStableIds;
    }


    public void createGroup(ItemView group)
    {
        ArrayList<ItemViewExpand> listItems = new ArrayList<>();
        Pair<ItemView, ArrayList<ItemViewExpand>> pair = new Pair<>(group, listItems);
        this.listGroup.add(pair);
    }

    public void setHasStableIds(boolean hasStableIds)
    {
        this.hasStableIds = hasStableIds;
    }

    public void addItemGroup(int group, ItemViewExpand item)
    {
        this.listGroup.get(group).second.add(item);
    }

    @Override
    public int getGroupCount()
    {
        return this.listGroup.size();
    }

    @Override
    public int getChildrenCount(int positionGroup)
    {
        return this.listGroup.get(positionGroup).second.size();
    }

    @Override
    public Object getGroup(int positionGrpup)
    {
        return this.listGroup.get(positionGrpup).first.getObject();
    }

    @Override
    public Object getChild(int positionGroup, int positionItem)
    {
        return this.listGroup.get(positionGroup).second.get(positionItem).getObject();
    }

    @Override
    public long getGroupId(int position)
    {
        return position+1;
    }

    @Override
    public long getChildId(int positionGroup, int positionItem)
    {
        return positionGroup+1+positionItem+1;
    }

    @Override
    public boolean hasStableIds()
    {
        return this.hasStableIds;
    }

    @Override
    public View getGroupView(int positionGroup, boolean b, View view, ViewGroup viewGroup)
    {
        ItemView group = this.listGroup.get(positionGroup).first;
        View rootView = group.createView(positionGroup, LayoutInflater.from(this.context), view, viewGroup);
        return rootView;
    }

    @Override
    public View getChildView(int positionGroup, int positionItem, boolean b, View view, ViewGroup viewGroup)
    {
        ItemView group = this.listGroup.get(positionGroup).second.get(positionItem);
        View rootView = group.createView(positionGroup, LayoutInflater.from(this.context), view, viewGroup);
        return rootView;
    }

    @Override
    public boolean isChildSelectable(int positionGroup, int positionItem)
    {
        return this.listGroup.get(positionGroup).second.get(positionItem).isSelectable();
    }
}
