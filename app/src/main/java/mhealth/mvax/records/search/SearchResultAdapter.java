package mhealth.mvax.records.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.patient.RecordDateFormat;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing patient record search results
 */

public class SearchResultAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Record> mDataSource;


    //================================================================================
    // Constructors
    //================================================================================

    SearchResultAdapter(Context context, Collection<Record> records) {
        mContext = context;
        mDataSource = new ArrayList<>(records);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Public methods
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
    public View getView(int position, View rowView, ViewGroup parent) {

        ViewHolder holder;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_search_result, parent, false);
            holder = new ViewHolder();

            holder.titleTextView = rowView.findViewById(R.id.search_result_title);
            holder.subtitleTextView = rowView.findViewById(R.id.search_result_subtitle);
            holder.rightTextView = rowView.findViewById(R.id.search_result_right);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        TextView rightTextView = holder.rightTextView;

        Record result = (Record) getItem(position);

        titleTextView.setText(result.getFullName());

        String DOBprompt = mContext.getResources().getString(R.string.DOB_prompt);
        RecordDateFormat dateFormat = new RecordDateFormat(mContext.getResources().getString(R.string.date_format));
        String DOBstr = DOBprompt + " " + dateFormat.getString(result.getDOB());

        subtitleTextView.setText(DOBstr);

        rightTextView.setText(result.getCommunity());

        return rowView;
    }

    void refresh(Collection<Record> values) {
        mDataSource = new ArrayList<>(values);
        notifyDataSetChanged();
    }

    String getPatientIdFromDataSource(int position) {
        return mDataSource.get(position).getDatabaseId();
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView rightTextView;
    }
}
