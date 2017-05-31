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

public abstract class ItemViewHolder< I extends ItemDataSet > extends RecyclerView.ViewHolder {


    private Context context;
    private ItemCallback callBack;
    private I dataSet;

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
    public abstract void onBind( I dataSet, int currentAdapterPosition, int totalDataSet );

    protected void dataSet(I dataSet) {
        this.dataSet = dataSet;
    }

    public I getDataSet() {
        return dataSet;
    }

    /**
     * Execute any ItemCallback
     * @param callBack
     */
    protected boolean callback( ItemCallback< I > callBack ){
        if( callBack != null ){
            callBack.onCallback( this, this.itemView, this.dataSet, this.getAdapterPosition() );
            return true;
        }
        return false;
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
    protected void onDataSetReplaced(int index, I oldItemDataSet, I newItemDataSet, int totalDataSet) {}

    /**
     *
     * @param index
     * @param oldItemDataSet
     */
    protected void onDataSetRemoved(int index, I oldItemDataSet) {}

    /**
     *
     * @param index
     * @param newItemDataSet
     * @param oldDataSet
     */
    protected void onNewDataSetAddInCurrentPosition(int index, I newItemDataSet, I oldDataSet, int totalDataSet) {}

    /**
     *
     * @param indexFrom
     * @param indexTo
     * @param itemDataSetFrom
     * @param itemDataSetTo
     */
    protected void onDataSetMovedFrom(int indexFrom, int indexTo, ItemDataSet itemDataSetFrom, I itemDataSetTo, int totalDataSet) {}

    /**
     *
     * @param indexFrom
     * @param indexTo
     * @param itemDataSetFrom
     * @param itemDataSetTo
     */
    protected void onDataSetMovedTo(int indexFrom, int indexTo, ItemDataSet itemDataSetFrom, I itemDataSetTo, int totalDataSet) {}


    protected void onViewDetachedFromWindow(){}

    protected void onViewAttachedToWindow(){}

    protected boolean onFailedToRecycleView(){ return false; }

    protected void onViewRecycled(){ }

    public void onSaveInstanceState(Bundle outState){}

    public interface ItemCallback < I extends  ItemDataSet > {
        void onCallback(ItemViewHolder itemViewHolder, View view , I itemDataSet, int adapterPosition );
    }

    public abstract static class DataSetCallback < I extends ItemDataSet > implements ItemCallback< I > {

        @Override
        public void onCallback( ItemViewHolder itemViewHolder, View view, I itemDataSet, int adapterPosition ) {
            onDataSetCallback( itemDataSet, adapterPosition );
        }

        protected abstract void onDataSetCallback( I itemDataSet, int adapterPosition);
    }

    public abstract static class ViewHolderCallback < I extends ItemDataSet > implements ItemCallback< I > {

        @Override
        public void onCallback( ItemViewHolder itemViewHolder, View view, I itemDataSet, int adapterPosition ) {
            onViewHolderCallback( itemViewHolder, view, adapterPosition );
        }

        protected abstract void onViewHolderCallback(ItemViewHolder itemViewHolder, View view, int adapterPosition);
    }
}
