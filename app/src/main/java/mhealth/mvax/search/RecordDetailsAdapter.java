package mhealth.mvax.search;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing details about a Record
 */

abstract class RecordDetailsAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    static final int TYPE_SECTION = 0;
    static final int TYPE_FIELD = 1;

    private LayoutInflater mInflater;

    List<Pair<String, String>> mDataSource;

    /**
     * Contains position of headers and their titles
     */
    Map<Integer, String> mHeaders;


    //================================================================================
    // Constructors
    //================================================================================

    RecordDetailsAdapter(Context context, LinkedHashMap<String, ArrayList<Pair<String, String>>> sectionedData) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        populateListView(sectionedData);
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Refresh the ListView with new data
     *
     * @param sectionedData is the new data with which to populate the ListView
     */
    void refresh(LinkedHashMap<String, ArrayList<Pair<String, String>>> sectionedData) {
        populateListView(sectionedData);
        notifyDataSetChanged();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mHeaders.keySet().contains((position)) ? TYPE_SECTION : TYPE_FIELD;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    abstract public View getView(int position, View rowView, ViewGroup viewGroup);

//    @Override
//    public View getView(int position, View rowView, ViewGroup viewGroup) {
//        ViewHolder holder;
//        int rowType = getItemViewType(position);
//        if (rowView == null) {
//            holder = new ViewHolder();
//            switch (rowType) {
//                case TYPE_SECTION:
//                    rowView = mInflater.inflate(R.layout.list_item_record_detail_section, null);
//                    holder.fieldView = rowView.findViewById(R.id.record_detail_separator);
//                    break;
//                case TYPE_FIELD:
//                    rowView = mInflater.inflate(R.layout.list_item_record_detail, null);
//                    holder.fieldView = rowView.findViewById(R.id.textview_field);
//                    holder.valueView = rowView.findViewById(R.id.edittext_value);
//                    break;
//            }
//            assert rowView != null;
//            rowView.setTag(holder);
//        } else {
//            holder = (ViewHolder) rowView.getTag();
//        }
//        // populate row with data
//        if (rowType == TYPE_SECTION){
//            holder.fieldView.setText(mHeaders.get(position));
//        } else if(rowType == TYPE_FIELD) {
//            holder.fieldView.setText(mDataSource.get(position).first);
//            holder.valueView.setText(mDataSource.get(position).second);
////            holder.valueView.setFocusable(false);
//        }
//
//        return rowView;
//    }

    static class ViewHolder {
        TextView fieldView;
        TextView valueView;
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void populateListView(LinkedHashMap<String, ArrayList<Pair<String, String>>> sectionedData) {
        mDataSource = new ArrayList<>();
        mHeaders = new HashMap<>();
        for (String key : sectionedData.keySet()) {
            ArrayList<Pair<String, String>> values = sectionedData.get(key);
            // add a null to the data array so that separator doesn't mess up position ordering
            mDataSource.add(null);
            mHeaders.put(mDataSource.size() - 1, key);
            mDataSource.addAll(values);
        }
    }

}
