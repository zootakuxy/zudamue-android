package st.domain.support.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by xdata on 12/25/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    protected final Context context;
    LayoutInflater inflater;
    private Map<Integer, ViewHolderFactory> factoryMap;
    protected List<ItemDataSet> listItem;

    public  RecyclerViewAdapter (Context context) {
        this.context = context;
        this.factoryMap = new LinkedHashMap<>();
        this.inflater = LayoutInflater.from(context);
        this.listItem = new LinkedList<>();
    }


    public void add(ItemDataSet itemDataSet) {
        addItem(listItem.size(), itemDataSet);
    }

    private void addItem(int index, ItemDataSet itemDataSet){
        this.listItem.add(index, itemDataSet);
        this.notifyItemInserted(index);
    }

    public void addItemFactory(int viewType, ViewHolderFactory viewHolderFactory){
        this.factoryMap.put(viewType, viewHolderFactory);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int layoytViewId) {
        ViewHolderFactory factory = this.factoryMap.get(layoytViewId);
        View view = inflater.inflate(layoytViewId, parent, false);
        if(factory != null)
            return factory.factory(view);
        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {

        //if(holder instanceof ItemViewHolder)
            ((ItemViewHolder) holder).bind(this.listItem.get(index), index, this.listItem.size());

    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }


    @Override
    public int getItemViewType(int index) {

        return this.listItem.get(index).getLayoutId();

    }

    public void clear(){
        int count = this.getItemCount();
        this.listItem.clear();
        super.notifyItemRangeRemoved(0, count);
    }

    public void add(List<ItemDataSet> itemDataSetList) {
        for (ItemDataSet itemDataSet: itemDataSetList) {
            this.add(itemDataSet);
        }
    }

    public void add(int index, ItemDataSet itemDataSet) {
        this.listItem.add(index, itemDataSet);
        this.notifyItemInserted(index);
    }

    public interface ViewHolderFactory {

        ItemViewHolder factory(View view);

    }

    public List<ItemDataSet> getListItem() {
        return this.listItem;
    }
}
