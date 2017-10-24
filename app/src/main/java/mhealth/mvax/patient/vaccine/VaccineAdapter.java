package mhealth.mvax.patient.vaccine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.search.PatientDetailActivity;
import mhealth.mvax.search.RecordDetailFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing vaccines and their doses
 */

// TODO: Implement sorting vaccines in the ListView

public class VaccineAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private RecordDetailFragment _RecordDetailFragment;

    private Context _Context;

    private LayoutInflater _Inflater;

    private List<Vaccine> _DataSource;

    private VaccineAdapter _VaccineAdapter;

    //================================================================================
    // Constructors
    //================================================================================

    public VaccineAdapter(RecordDetailFragment fragment, Context context, List<Vaccine> dataSource) {
        _RecordDetailFragment = fragment;
        _Context = context;
        _DataSource = dataSource;
        _Inflater = (LayoutInflater) _Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _VaccineAdapter = this;
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

    public LayoutInflater getInflater() {
        return _Inflater;
    }

    /**
     * Populates each row with a list of doses for the vaccine
     */
    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        ViewHolder holder;
        Vaccine result = (Vaccine) getItem(position);

        if (rowView == null) {
            rowView = _Inflater.inflate(R.layout.list_item_vaccine, parent, false);
            holder = new ViewHolder();
            holder.vaccineTextView = rowView.findViewById(R.id.vaccine_name);
            holder.dosesLinearLayout = rowView.findViewById(R.id.doses_linear_layout);
            addDoses(result, holder.dosesLinearLayout, rowView);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.vaccineTextView.setText(result.getName());
        return rowView;
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void addDoses(Vaccine vaccine, LinearLayout layout, View rowView) {

        for (Dose dose : vaccine.getDoseList()) {

            // create LinearLayout to hold the label and date for each dose
            LinearLayout doseLinearLayout = new LinearLayout(rowView.getContext());
            doseLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            doseLinearLayout.setPadding(0, 15, 0, 15);

            // create dose label
            TextView label = new TextView(rowView.getContext());
            label.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            label.setText(dose.getLabel());
            label.setTextSize(22);
            label.setGravity(Gravity.CENTER);
            label.setPadding(0, 0, 15, 0);

            // create dose date
            DoseDateView dateView = new DoseDateView(rowView.getContext(), vaccine, dose);
            dateView.setLayoutParams(new LinearLayout.LayoutParams(
                    250,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            dateView.setPadding(5, 5, 5, 5);
            dateView.setGravity(Gravity.CENTER);
            dateView.setTextSize(22);
            SimpleDateFormat sdf = new SimpleDateFormat(_Context.getResources().getString(R.string.date_format));
            if (dose.hasBeenCompleted()) {
                dateView.setText(sdf.format(dose.getDate()));
            }
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.LTGRAY);
            dateView.setBackground(gd);
            dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _RecordDetailFragment.createNewDose(v, _VaccineAdapter);
                }
            });

            // add dose label and date to the view
            doseLinearLayout.addView(label);
            doseLinearLayout.addView(dateView);

            // add the total dose view to the vaccine view
            layout.addView(doseLinearLayout);
        }
    }

    private static class ViewHolder {
        TextView vaccineTextView;
        LinearLayout dosesLinearLayout;
    }

}
