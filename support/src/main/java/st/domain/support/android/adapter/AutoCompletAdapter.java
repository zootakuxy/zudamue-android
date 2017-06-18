package st.domain.support.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import st.domain.support.android.R;
import st.domain.support.android.model.ItemView;

import java.util.ArrayList;

/**
 * Created by xdata on 7/22/16.
 */
public class AutoCompletAdapter extends ListAdapter implements Filterable
{
    private ArrayList<ItemView> listFilter;
    private ItemFilter filter;
    private Chose choser;

    private int idLayout;
    private int idText;
    private FilterModo filterModo;

    public AutoCompletAdapter(Context context)
    {
        super(context);
        this.initFilter();
        this.idLayout = R.layout.lib_item_text;
        this.idText = R.id.text_desc;
    }

    public AutoCompletAdapter(Context context, int idLayout, int idText)
    {
        super(context);
        this.initFilter();
        this.idLayout = idLayout;
        this.idText = idText;
    }

    public void setCustomText(int idLayout, int idText)
    {
        this.idLayout = idLayout;
        this.idText = idText;
    }

    /**
     *
     */
    private void initFilter()
    {
        this.filterModo = FilterModo.INITIAL;

        this.choser = new Chose()
        {
            @Override
            public boolean accepet(String query, ItemView item)
            {

                Object ob = item.getObject();
                String itemText;
                if(ob == null)
                    ob = "";
                itemText = ob.toString();

                if(AutoCompletAdapter.this.filterModo == FilterModo.INITIAL)
                    return query != null
                        && itemText.length() >= query.length()
                        && itemText.substring(0, query.length()).equalsIgnoreCase(query);
                else return query != null
                    && query.toUpperCase().contains(itemText.toUpperCase());
            }
        };

        this.filter = new ItemFilter(new QueryFilter()
        {
            ArrayList<ItemView> result = new ArrayList<>();
            @Override
            public ArrayList<ItemView> query(String text)
            {
                result.clear();
                for(ItemView item: getList())
                    if(choser.accepet(text, item))
                        result.add(item);
                return result;
            }

            @Override
            public ArrayList<ItemView> getQueryResult()
            {
                return this.result;
            }

            @Override
            public void changeCursor(ArrayList<ItemView> values)
            {
                AutoCompletAdapter.this.listFilter = values;
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getCount()
    {
        return this.listFilter == null? 0 : this.listFilter.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.listFilter.get(position).getObject();
    }

    @Override
    public ItemView getItemView(int position) {
        return this.listFilter.get(position);
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    /**
     * Modo de filter
     * @param modo
     */
    public void setContentModo(FilterModo modo)
    {
        this.filterModo = modo;
    }

    public void addItem(String text)
    {
        super.addItem(new DefaultItemText(text));
    }

    private class ItemFilter extends Filter
    {

        QueryFilter mClient;

        public ItemFilter(QueryFilter client) 
        {
            mClient = client;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            ArrayList<ItemView> cursor = mClient.query((constraint == null)? "": constraint.toString());
            FilterResults results = new FilterResults();

            if (cursor != null)
            {
                results.count = cursor.size();
                results.values = cursor;
            } else {
                results.count = 0;
                results.values = null;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) 
        {
            mClient.changeCursor((ArrayList<ItemView>) results.values);
        }
    }


    public class DefaultItemText implements ItemView
    {
        private int index;
        private View rootView;
        private TextView text;
        private String textDescrision;

        public DefaultItemText (String text)
        {
            this.textDescrision = text;
        }

        @Override
        public View createView(int position, LayoutInflater inflater, View view, ViewGroup viewGroup)
        {
            this.rootView =inflater.inflate(idLayout, null);
            this.text  =(TextView) rootView.findViewById(idText);
            this.text.setText(this.textDescrision);
            return this.rootView;
        }


        @Override
        public Object getObject()
        {
            return this.textDescrision;
        }
    }


    public static interface QueryFilter
    {
        public ArrayList<ItemView> query(String text);

        ArrayList<ItemView> getQueryResult();

        public void changeCursor(ArrayList<ItemView> values);
    }

    public static interface Chose
    {
        public boolean accepet(String query, ItemView item);
    }

    public static enum FilterModo
    {
        INITIAL,
        filterModo, CONTAINS
    }
}
