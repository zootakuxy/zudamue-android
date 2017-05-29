package st.domain.support.android.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 *
 * Created by xdata on 12/25/16.
 */

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {


    private Context context;
    private ItemCallback callBack;
    private ItemDataSet dataSet;

    public ItemViewHolder(View itemView) {
        super(itemView);
        this.context = this.itemView.getContext();
    }

    public Context getContext() {
        return context;
    }

    /**
     * Join the data setItem with view older
     * @param dataSet
     * @param currentAdapterPosition
     * @param totalDataSet
     */
    public abstract void onBind(ItemDataSet dataSet, int currentAdapterPosition, int totalDataSet );

    public void dataSet(ItemDataSet dataSet) {
        this.dataSet = dataSet;
    }


    /**
     * Execute any ItemCallback
     * @param callBack
     */
    protected void callback( ItemCallback callBack ){
        if( callBack != null )
            callBack.onCallback( this, this.itemView, this.dataSet, this.getAdapterPosition() );
    }

    protected void onPreBind() {}

    protected void onPosBind() {}

    /**
     * Look for a child view with the given id.  If this view has the given
     * id, return this view.
     *
     * @param id The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    @Nullable
    public final View findViewById( @IdRes int id ) {
        if (this.itemView == null) return null;
        return this.itemView.findViewById(id);
    }

    /**
     *
     * @param index
     * @param oldItemDataSet
     * @param newItemDataSet
     */
    protected void onDataSetReplaced(int index, ItemDataSet oldItemDataSet, ItemDataSet newItemDataSet, int totalDataSet) {}

    /**
     *
     * @param index
     * @param oldItemDataSet
     */
    protected void onDataSetRemoved(int index, ItemDataSet oldItemDataSet) {}

    /**
     *
     * @param index
     * @param newItemDataSet
     * @param oldDataSet
     */
    protected void onNewDataSetAddInCurrentPosition(int index, ItemDataSet newItemDataSet, ItemDataSet oldDataSet, int totalDataSet) {}

    /**
     *
     * @param indexFrom
     * @param indexTo
     * @param itemDataSetFrom
     * @param itemDataSetTo
     */
    protected void onDataSetMovedFrom(int indexFrom, int indexTo, ItemDataSet itemDataSetFrom, ItemDataSet itemDataSetTo, int totalDataSet) {}

    /**
     *
     * @param indexFrom
     * @param indexTo
     * @param itemDataSetFrom
     * @param itemDataSetTo
     */
    protected void onDataSetMovedTo(int indexFrom, int indexTo, ItemDataSet itemDataSetFrom, ItemDataSet itemDataSetTo, int totalDataSet) {}


    protected void onViewDetachedFromWindow(){}

    protected void onViewAttachedToWindow(){}

    protected boolean onFailedToRecycleView(){ return false; }

    protected void onViewRecycled(){ }

    public void onSaveInstanceState(Bundle outState){}

    public interface ItemCallback {
        void onCallback(ItemViewHolder itemViewHolder, View view , ItemDataSet itemDataSet, int adapterPosition );
    }

    public abstract static class DataSetCallback implements ItemCallback {

        @Override
        public void onCallback( ItemViewHolder itemViewHolder, View view, ItemDataSet itemDataSet, int adapterPosition ) {
            onDataSetCallback( itemDataSet, adapterPosition );
        }

        protected abstract void onDataSetCallback(ItemDataSet itemDataSet, int adapterPosition);
    }

    public abstract static class ViewHolderCallback implements  ItemCallback {

        @Override
        public void onCallback( ItemViewHolder itemViewHolder, View view, ItemDataSet itemDataSet, int adapterPosition ) {
            onViewHolderCallback( itemViewHolder, view, adapterPosition );
        }

        protected abstract void onViewHolderCallback(ItemViewHolder itemViewHolder, View view, int adapterPosition);
    }
}
