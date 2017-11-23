package mhealth.mvax.records.details.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.records.views.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing details about a Record
 */

public abstract class RecordDetailsAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    protected static final int TYPE_SECTION = 0;
    protected static final int TYPE_FIELD = 1;

    private LayoutInflater mInflater;

    protected List<Detail> mDataSource;

    /**
     * Contains position of headers and their titles
     */
    protected Map<Integer, String> mHeaders;


    //================================================================================
    // Constructors
    //================================================================================

    protected RecordDetailsAdapter(Context context, LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
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
    public void refresh(LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
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


    //================================================================================
    // Private methods
    //================================================================================

    private void populateListView(LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
        mDataSource = new ArrayList<>();
        mHeaders = new HashMap<>();
        for (String key : sectionedData.keySet()) {
            ArrayList<Detail> values = sectionedData.get(key);
            // add a null to the data array so that separator doesn't mess up position ordering
            mDataSource.add(null);
            mHeaders.put(mDataSource.size() - 1, key);
            mDataSource.addAll(values);
        }
    }


    public static class ViewHolder {
        public TextView fieldView;
        public EditText valueView;
    }

}
