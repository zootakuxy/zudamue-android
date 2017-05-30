package st.domain.support.android.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by xdata on 12/25/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter implements Iterable<ItemDataSet> {

    protected final Context context;
    private boolean autoNotify;
    LayoutInflater inflater;
    private Map<Integer, ViewHolderFactory> factoryMap;
    private Map<Integer, ItemViewHolder> viewHolderMap;
    protected List<ItemDataSet> listItem;

    public  RecyclerViewAdapter (Context context) {
        this.context = context;
        this.factoryMap = new LinkedHashMap<>();
        this.inflater = LayoutInflater.from(context);
        this.listItem = new LinkedList<>();
        this.viewHolderMap = new LinkedHashMap<>();
        this.autoNotify = true;
    }


    public void addItemFactory(int viewType, ViewHolderFactory viewHolderFactory){
        this.factoryMap.put(viewType, viewHolderFactory);
    }

    public void onSaveInstanceState(Bundle outState ){
        for( Map.Entry<Integer, ItemViewHolder > entry: this.viewHolderMap.entrySet()){
            entry.getValue().onSaveInstanceState( outState );
        }
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
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if( holder instanceof  ItemViewHolder ){
            ((ItemViewHolder) holder).onViewDetachedFromWindow();
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if( holder instanceof ItemViewHolder ){
            ((ItemViewHolder) holder).onViewAttachedToWindow();
        }
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if( holder instanceof ItemViewHolder ){
            return ((ItemViewHolder) holder).onFailedToRecycleView();
        }
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if( holder instanceof  ItemViewHolder ){
            ((ItemViewHolder) holder).onViewRecycled();
        }
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {

        if( holder instanceof ItemViewHolder ){

            ((ItemViewHolder) holder).onPreBind();
            ((ItemViewHolder) holder).onBind( this.listItem.get( index ), index, this.listItem.size());
            ((ItemViewHolder) holder).dataSet( this.listItem.get( index ) );
            ((ItemViewHolder) holder).onPosBind();
            this.viewHolderMap.remove( holder.getOldPosition() );
            this.viewHolderMap.put( index, (ItemViewHolder) holder);
        }

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
        int length = this.getItemCount();
        this.listItem.clear();
        if( this.autoNotify)
            super.notifyDataSetChanged();
    }

    public boolean addItem(ItemDataSet itemDataSet) {
        return addItem(listItem.size(), itemDataSet);
    }

    public boolean addItem(int index, ItemDataSet newItemDataSet){
        if( index <0 || index > this.listItem.size() ) return false;
        ItemDataSet oldDataSet = index < this.listItem.size() ? this.listItem.get(index) : null;
        this.listItem.add(index, newItemDataSet);

        if( oldDataSet != null && hasViewHolder(index) ) {
            ItemViewHolder viewHolder = this.viewHolderMap.get(index);
            viewHolder.onNewDataSetAddInCurrentPosition( index, newItemDataSet, oldDataSet, this.listItem.size() );
        }
        if( this.autoNotify)
            this.notifyItemInserted( index );
        return true;
    }

    private boolean hasViewHolder(int index) {
        return this.viewHolderMap.containsKey( index )
                && this.viewHolderMap.get( index ) != null;
    }


    public boolean addItem( List<ItemDataSet> itemDataSetList ) {
        boolean result = true;
        for (ItemDataSet itemDataSet: itemDataSetList) {
            if( !this.addItem(itemDataSet) )
                result = false;
        }
        return result;
    }

    public boolean setItem( int index, ItemDataSet newItemDataSet) {

        if( index < 0 && index >= this.listItem.size() || newItemDataSet == null ) return false;
        ItemDataSet oldItemDataSet = this.listItem.set(index, newItemDataSet);
        ItemViewHolder viewHolder = this.viewHolderMap.get(index);
        viewHolder.onDataSetReplaced( index, oldItemDataSet, newItemDataSet, this.listItem.size() );

        if( this.autoNotify)
            notifyItemChanged( index  );
        return true;
    }

    public boolean moveDataSet(int indexFrom, int indexTo ){

        if( indexFrom <0
                || indexFrom >= this.listItem.size()
                || indexTo <0
                || indexTo >= this.listItem.size() )
            return false;
        ItemDataSet itemDataSetFrom = this.listItem.get( indexFrom );
        ItemDataSet itemDataSetTo = this.listItem.get( indexTo );


        int realFrom = indexFrom;
        int realTo = indexTo;

        if( indexTo > indexFrom )
            indexTo --;
        this.listItem.remove( indexFrom );
        this.listItem.add( indexTo, itemDataSetFrom );

        if( hasViewHolder( realFrom ) )
            this.viewHolderMap.get( realFrom ).onDataSetMovedFrom( realFrom, realTo, itemDataSetFrom, itemDataSetTo, this.listItem.size() );

        if( hasViewHolder( realTo ) )
            this.viewHolderMap.get( realTo ).onDataSetMovedTo( realFrom, realTo, itemDataSetFrom, itemDataSetTo, this.listItem.size() );

        if( this.autoNotify)
            this.notifyItemMoved( realFrom, realTo );
        return true;
    }


    public ItemDataSet removeItem( int index ) {
        if( index <0 || index >= this.listItem.size() ) return null;
        ItemDataSet oldItemDataSet = this.listItem.remove(index);
        ItemViewHolder viewHolder = this.viewHolderMap.get(index);
        if( viewHolder != null )
            viewHolder.onDataSetRemoved( index, oldItemDataSet );

        if( this.autoNotify)
            this.notifyItemRemoved( index );
        return oldItemDataSet;
    }

    @Override
    public Iterator<ItemDataSet> iterator() {

        return new Iterator<ItemDataSet>(){
            private int current;

            @Override
            public boolean hasNext() {
                return current < listItem.size() -1;
            }

            @Override
            public ItemDataSet next() {
                return listItem.get( this.current++ );
            }
        };
    }

    /**
     * Get item dataSet at position
     * @param i
     * @return
     */
    public ItemDataSet getItemAt(int i) {
        return this.listItem.get( i );
    }

    public ItemDataSet getLastItem() {
        return this.listItem.size() > 0 ? this.listItem.get(this.listItem.size() - 1) : null;
    }

    public void setAoutoNotify(boolean aoutoNotify) {
        this.autoNotify = aoutoNotify;
    }


    public interface ViewHolderFactory {
        ItemViewHolder factory(View view);
    }

    public List<ItemDataSet> getListItem() {
        return this.listItem;
    }
}
