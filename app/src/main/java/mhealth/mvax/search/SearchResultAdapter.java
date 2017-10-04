package mhealth.mvax.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.patient.Patient;

/**
 * @author Robert Steilberg
 */

public class SearchResultAdapter extends BaseAdapter {

    private Context _Context;
    private LayoutInflater _Inflater;
    private ArrayList<Patient> _DataSource;

    public SearchResultAdapter(Context context, ArrayList<Patient> items) {
        _Context = context;
        _DataSource = items;
        _Inflater = (LayoutInflater) _Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = _Inflater.inflate(R.layout.list_item_search_result, parent, false);

        TextView titleTextView = rowView.findViewById(R.id.search_result_title);
        TextView subtitleTextView = rowView.findViewById(R.id.search_result_subtitle);
        TextView rightTextView = rowView.findViewById(R.id.search_result_right);

        Patient result = (Patient) getItem(position);
        titleTextView.setText(result.getFullName());

        String DOBprompt = _Context.getResources().getString(R.string.DOB_prompt);
        SimpleDateFormat sdf = new SimpleDateFormat(_Context.getResources().getString(R.string.date_format));
        String DOBstr = DOBprompt + " " + sdf.format(result.getDOB());

        subtitleTextView.setText(DOBstr);

        rightTextView.setText(result.getCommunity());

        return rowView;
    }

}
