package mhealth.mvax.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import mhealth.mvax.R;
import mhealth.mvax.model.Record;
import mhealth.mvax.model.Vaccine;
import mhealth.mvax.search.SearchResultAdapter;

/**
 * Created by AlisonHuang on 11/1/17.
 */

public class VaccineCardAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private Context mContext;

    private LayoutInflater mInflater;

    private List<Vaccine> mDataSource;


    //================================================================================
    // Constructors
    //================================================================================

    VaccineCardAdapter(Context context, Collection<Vaccine> vaccines) {
        mContext = context;
        mDataSource = new ArrayList<>(vaccines);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void refresh(Collection<Vaccine> values) {
        mDataSource = new ArrayList<>(values);
        notifyDataSetChanged();
    }

    String getVaccineNameFromDataSource(int position) {
        return mDataSource.get(position).getName();
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        CardHolder holder;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_vaccine_card, parent, false);
            holder = new CardHolder();

            holder.vaccineNameTV = rowView.findViewById(R.id.vaccine_card_name);
            holder.targetValueTV = rowView.findViewById(R.id.vaccine_card_target_value);
            holder.givenValueTV = rowView.findViewById(R.id.vaccine_card_given_value);

            rowView.setTag(holder);
        } else {
            holder = (CardHolder) rowView.getTag();
        }

        TextView vaccineNameTV = holder.vaccineNameTV;
        TextView targetValueTV = holder.targetValueTV;
        TextView givenValueTV = holder.givenValueTV;

        Vaccine result = (Vaccine) getItem(position);

        vaccineNameTV.setText(result.getName());
        //TODO: set the target and given dose counts

        renderMonthSpinner(rowView);

        return rowView;
    }

    private void renderMonthSpinner(View view) {
        final Spinner spinner = view.findViewById(R.id.vaccine_card_month_spinner);
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.vaccine_card_months_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filterAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0) {
                    //TODO: user picks a month, new stats get displayed
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class CardHolder {
        TextView vaccineNameTV;
        TextView targetValueTV;
        TextView givenValueTV;

    }


}
