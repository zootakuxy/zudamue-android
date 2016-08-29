package com.st.dbutil.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;

import java.util.List;

/**
 * Created by xdata on 8/9/16.
 */
public class SupportRecyclerAdapter extends BaseReclyclerAdapter
{
    private  int idDefaultLayoutItem;

    private OnBindViewHolder onBindViewHolder;
    private OnRequireTypeView onRequireTypeView;
    private OnCreateView onCreateView;
    private OnCreateViewHolder onCreateViewHolder;
    private OnItemClickListener onItemClickListener;

    private int onRecyclerViewId;
    private boolean useTypeViewAsReferenceLayout;
    private boolean hasDefaultTypeView;
    private OnPosViewCreated onPosViewCreated;

    public SupportRecyclerAdapter(Context context, List listDataSet, int idDefaultLayoutItem)
    {
        super(context, listDataSet);
        this.idDefaultLayoutItem = idDefaultLayoutItem;
        this.hasDefaultTypeView = true;
    }

    public SupportRecyclerAdapter(Context context, List listDataSet, OnCreateView onCreateView)
    {
        super(context, listDataSet);
        this.onCreateView = onCreateView;
        this.idDefaultLayoutItem = 0;
        this.hasDefaultTypeView = false;
    }

    public SupportRecyclerAdapter(Context context, int idLayoutItem)
    {
        super(context, null);
        this.idDefaultLayoutItem = idLayoutItem;
        this.hasDefaultTypeView = true;
    }

    public SupportRecyclerAdapter(Context context, OnCreateView onCreateView) {
        super(context, null);
        this.onCreateView = onCreateView;
        this.idDefaultLayoutItem = 0;
        this.hasDefaultTypeView = false;
    }

    public SupportRecyclerAdapter(Context context)
    {
        super(context, null);
        this.onCreateView = null;
        this.idDefaultLayoutItem = 0;
        this.hasDefaultTypeView = false;
    }


    @Override
    protected ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType, LayoutInflater inflater)
    {
        String layoutName = "";
        View view;
        if(useTypeViewAsReferenceLayout)
        {
            view = inflater.inflate(viewType, parent, false);
            layoutName = ", layount:\""+getContext().getResources().getResourceName(viewType)+"\"";
        }
        else if(onCreateView == null && this.idDefaultLayoutItem != 0)
        {
            view = inflater.inflate(idDefaultLayoutItem, parent, false);
            layoutName = ", layount:\""+getContext().getResources().getResourceName(idDefaultLayoutItem)+"\"";
        }
        else if(onCreateView != null)
        {
            view = onCreateView.onCreateView(inflater, parent, viewType, this.onRecyclerViewId);
            layoutName = ", view{id:\""+getContext().getResources().getResourceName(view.getId())
                    +", type:\""+getContext().getResources().getResourceTypeName(view.getId())+"}";

        }
        else throw new RuntimeException("can not create view for adapter of recycler view "+this.onRecyclerViewId);

        if(this.onPosViewCreated != null)
            this.onPosViewCreated.onViewCreated(view, parent, viewType);

        ItemViewHolder viewHolder = null;
        if(this.onCreateViewHolder != null)
            viewHolder = onCreateViewHolder.onCreateViewHolder(view, viewType, this.onRecyclerViewId);

        if(viewHolder == null)
        {
            viewHolder = new ItemViewHolder(view);
        }
        Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> viewHolder{"
                +"class:\""+viewHolder.getClass().getSimpleName()
                +layoutName
                +"}");
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(final ItemViewHolder holder, final ItemDataSet data, int position)
    {
        if(this.onBindViewHolder != null)
            this.onBindViewHolder.onBindViewHolder(holder, data, position, this.onRecyclerViewId);
        if(this.onItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    onItemClickListener.onItemClick(view, data, holder.getAdapterPosition(), holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(this.onRequireTypeView != null)
        {
            return this.onRequireTypeView.onRequireTypeView(position, this.onRecyclerViewId);
        }
        else if(this.hasDefaultTypeView)
        {
            return this.idDefaultLayoutItem;
        }
        else
        {
            return super.getItemViewType(position);
        }
    }

    public boolean isUseTypeViewAsReferenceLayout() {
        return useTypeViewAsReferenceLayout;
    }

    public void useTypeViewAsReferenceLayout(boolean useTypeViewAsReferenceLayout) {
        this.useTypeViewAsReferenceLayout = useTypeViewAsReferenceLayout;
    }

    public void setOuSupportRecyclerAdapter(OnSupportRecyclerAdapter onSupportRecyclerAdapter)
    {
        this.onCreateViewHolder = onSupportRecyclerAdapter;
        this.onCreateView = onSupportRecyclerAdapter;
        this.onBindViewHolder = onSupportRecyclerAdapter;
        this.onRequireTypeView  = onSupportRecyclerAdapter;
        this.onPosViewCreated = onSupportRecyclerAdapter;
    }

    public void setOnBindViewHolder(OnBindViewHolder onBindViewHolder) {
        this.onBindViewHolder = onBindViewHolder;
    }

    public void setOnRequireTypeView(OnRequireTypeView onRequireTypeView) {
        this.onRequireTypeView = onRequireTypeView;
    }

    public void setOnCreateView(OnCreateView onCreateView) {
        this.onCreateView = onCreateView;
    }

    public void setOnCreateViewHolder(OnCreateViewHolder onCreateViewHolder) {
        this.onCreateViewHolder = onCreateViewHolder;
    }

    public void setDefaultTypeView(int idDefaultLayoutItem) {
        this.idDefaultLayoutItem = idDefaultLayoutItem;
        this.hasDefaultTypeView = true;
    }

    public void setOnRecyclerViewId(int onRecyclerViewId)
    {
        this.onRecyclerViewId = onRecyclerViewId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnPosViewCreated(OnPosViewCreated onPosViewCreated) {
        this.onPosViewCreated = onPosViewCreated;
    }

    public interface OnBindViewHolder
    {
        void onBindViewHolder(ItemViewHolder viewHolder, ItemDataSet dataSet, int position, int onRecyclerViewId);
    }

    public interface OnRequireTypeView
    {
        int onRequireTypeView(int position, int onRecyclerViewId);
    }

    public interface OnCreateView
    {
        /**
         * On create view
         * @param inflater
         * @param group
         * @param viewType
         * @param onRecyclerViewId
         * @return
         */
        View onCreateView(LayoutInflater inflater, ViewGroup group, int viewType, int onRecyclerViewId);
    }

    public interface OnCreateViewHolder
    {
        ItemViewHolder onCreateViewHolder(View view, int viewType, int onRecyclerViewId);
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, ItemDataSet dataSet, int adapterPosition, int viewPosition);
    }

    interface OnSupportRecyclerAdapter extends OnBindViewHolder, OnRequireTypeView, OnCreateView, OnCreateViewHolder, OnPosViewCreated
    {

    }

    public interface OnPosViewCreated
    {
        public void onViewCreated(View view, ViewGroup parent, int viewType);
    }

    public static class SupportAnimatorListener implements Animator.AnimatorListener
    {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
