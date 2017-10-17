package mhealth.mvax.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.patient.Patient;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing patient record search results
 */

public class SearchResultAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private Context _Context;

    private LayoutInflater _Inflater;

    private Map<Integer, Patient> _DataSource;


    //================================================================================
    // Constructors
    //================================================================================

    public SearchResultAdapter(Context context, Map<Integer, Patient> items) {
        _Context = context;
        _DataSource = items;
        _Inflater = (LayoutInflater) _Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Public methods
    //================================================================================

    @Override
    public int getCount() {
        return _DataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return _DataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        ViewHolder holder;
        if (rowView == null) {
            rowView = _Inflater.inflate(R.layout.list_item_search_result, parent, false);
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

        Patient result = (Patient) getItem(position);

        titleTextView.setText(result.getFullName());

        String DOBprompt = _Context.getResources().getString(R.string.DOB_prompt);
        SimpleDateFormat sdf = new SimpleDateFormat(_Context.getResources().getString(R.string.date_format), Locale.getDefault());
        String DOBstr = DOBprompt + " " + sdf.format(result.getDOB());

        subtitleTextView.setText(DOBstr);

        rightTextView.setText(result.getCommunity());

        return rowView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView rightTextView;
    }
}
