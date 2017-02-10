package st.domain.support.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by xdata on 12/25/16.
 */

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {


    private Context context;

    public ItemViewHolder(View itemView) {
        super(itemView);
        this.context = this.itemView.getContext();
    }

    public Context getContext() {
        return context;
    }

    public abstract void bind( ItemDataSet dataSet, int currentAdapterPosition, int totalAdapterItem );
}
