package st.domain.support.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xdata on 8/9/16.
 */
public abstract class BaseRecyclerAdapter<E extends BaseRecyclerAdapter.ItemDataSet> extends RecyclerView.Adapter<BaseRecyclerAdapter.ItemViewHolder>
{
    private final Context context;
    private final LayoutInflater inflater;

    private List<? extends E> listDataSet;
    private HashMap<Integer, ItemViewHolder> bindMap;

    public BaseRecyclerAdapter(Context context, List<? extends E> listDataSet)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.bindMap = new HashMap<>();
        if(listDataSet == null)
            this.listDataSet = new ArrayList<>();
        else this.listDataSet = listDataSet;
    }


    public List<? extends ItemDataSet> getListDataSet()
    {
        return listDataSet;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ItemViewHolder itemViewHolder = this.onCreateViewHolder(parent, viewType, inflater);
        if(itemViewHolder.group == null)
            itemViewHolder.group = parent;
        itemViewHolder.context = this.context;
        itemViewHolder.baseRecyclerAdapter = this;
        return itemViewHolder;
    }

    @NonNull
    protected abstract ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType, LayoutInflater inflater);

    @Override
    public final void onBindViewHolder(final ItemViewHolder holder, final int position)
    {
        ItemDataSet data = this.listDataSet.get(position);
                if(holder.isClickable(position))
        {
            holder.getClickableView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.onClink(position);
                }
            });
        }

        boolean bind = holder.bind(data, position);
        holder.currentDataSet = data;
        Log.i("DBA:APP.TEST", " bind | "+holder.getClass().getSimpleName());
        if(!bind)
            this.onBindViewHolder(holder, data, position);
        this.bindMap.put(position, holder);
    }


    @Override
    public void onViewRecycled(ItemViewHolder holder)
    {
        super.onViewRecycled(holder);
        //Quando for reciclado o holder remover a sua referencia da view
        for(Map.Entry<Integer, ItemViewHolder> item: bindMap.entrySet())
            if(item.getValue() == holder)
            {
                bindMap.remove(item.getKey());
                break;
            }
    }

    protected abstract void onBindViewHolder(ItemViewHolder holder, ItemDataSet data, int position);

    @Override
    public int getItemCount()
    {
        return this.listDataSet.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return this.listDataSet.get(position).getTypeView();
    }

    public void addAll(List<? extends E> itemDatas)
    {
        for(E itemData: itemDatas)
            this.addDataSet(itemData);
    }

    public void addDataSet(E itemData)
    {
        List<E> aEList = (List<E>) this.listDataSet;
        aEList.add(itemData);
    }

    public E getItemSet(int position) {
        return this.listDataSet.get(position);
    }

    public Context getContext()
    {
        return context;
    }


    /**
     * Get view older of item im pososision if is available
     * @param itemPositionInAdapter the position of item required
     * @return
     */
    public ItemViewHolder getViewHolderIfAvailable(int itemPositionInAdapter)
    {
        if(this.bindMap.containsKey(itemPositionInAdapter))
            return this.bindMap.get(itemPositionInAdapter);
        return null;
    }

    /**
     * Get view older of item im pososision if is available
     * @param itemDataSet the item of required
     * @return
     */
    public ItemViewHolder getViewHolderIfAvailable(ItemDataSet itemDataSet)
    {
        int position = this.listDataSet.indexOf(itemDataSet);
        return this.getViewHolderIfAvailable(position);
    }

    public void removeDataSet(int index) {
        if(exist(index)) {
            listDataSet.remove(index);
        }
    }


    public void replace(int index, ItemDataSet itemDataSet) {
        if(this.exist(index))
        {
            List<ItemDataSet> list = (List<ItemDataSet>) this.listDataSet;
            list.set(index, itemDataSet);
        }
    }

    private boolean exist(int index) {
        return index>=0 && index <= this.listDataSet.size();
    }

    public void clear() {
        this.listDataSet.clear();
    }

    public int getLastIndex() {
        return this.listDataSet.size() - 1;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        protected ViewGroup group;
        private Context context;
        private BaseRecyclerAdapter<? extends ItemDataSet> baseRecyclerAdapter;
        private ItemDataSet currentDataSet;


        public ItemViewHolder(View itemView)
        {
            super(itemView);
            context = itemView.getContext();
        }

        public boolean bind(ItemDataSet dataSet, int position)
        {
            return false;
        }

        public BaseRecyclerAdapter.ItemDataSet getCurrentDataSet(){
            return this.currentDataSet;
        }

        public ViewGroup getGroup() {
            return group;
        }

        public Context getContext() {
            return context;
        }

        public BaseRecyclerAdapter<? extends ItemDataSet> getBaseRecyclerAdapter() {
            return baseRecyclerAdapter;
        }

        public ItemViewHolder newInstace (@NonNull View view)
        {
            return new ItemViewHolder(view);
        }

        public void onClink(int position) {
        }

        public View getClickableView() {
            return this.itemView;
        }

        public boolean isClickable(int position) {
            return false;
        }

        public View findViewById(int refeId) {
            return this.itemView != null?
                    this.itemView.findViewById(refeId)
                    :null;
        }

    }

    public interface ItemDataSet extends Serializable
    {
        int getTypeView();
    }
}
