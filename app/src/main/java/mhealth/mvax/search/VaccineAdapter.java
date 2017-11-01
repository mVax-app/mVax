package mhealth.mvax.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mhealth.mvax.R;
import mhealth.mvax.model.Record;
import mhealth.mvax.record.vaccine.Dose;
import mhealth.mvax.record.vaccine.DoseDateView;
import mhealth.mvax.record.vaccine.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing vaccines and their doses
 */

// TODO: Implement sorting vaccines in the ListView

class VaccineAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private LayoutInflater mInflater;

    private Context mContext;

    private List<Vaccine> mDataSource;

    private Record mCurrRecord;


    //================================================================================
    // Constructors
    //================================================================================

    VaccineAdapter(Context context, List<Vaccine> dataSource, Record currRecord) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mDataSource = dataSource;
        mCurrRecord = currRecord;
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


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Populates each row with a list of doses for the vaccine
     */
    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        ViewHolder holder;
        Vaccine result = (Vaccine) getItem(position);

        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_vaccine, parent, false);
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
            SimpleDateFormat sdf = new SimpleDateFormat(mContext.getResources().getString(R.string.date_format), Locale.getDefault());
            if (dose.hasBeenCompleted()) {
                dateView.setText(sdf.format(dose.getDate()));
            }
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.LTGRAY);
            dateView.setBackground(gd);
            dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View dateView) {
                    createNewDose(dateView);
                }
            });

            // add dose label and date to the view
            doseLinearLayout.addView(label);
            doseLinearLayout.addView(dateView);

            // add the total dose view to the vaccine view
            layout.addView(doseLinearLayout);
        }
    }


    //================================================================================
    // Private methods
    //================================================================================

    /**
     * Render a modal for modifying a vaccine dose record
     *
     * @param view is the DoseDateView object that displays the dose date
     */
    private void createNewDose(final View view) {
        final DoseDateView dateView = (DoseDateView) view;

        // create modal
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.modal_new_dosage_title);
        View dialogView = mInflater.inflate(R.layout.modal_new_dose, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.dose_date_picker);

        builder.setPositiveButton(mContext.getResources().getString(R.string.modal_new_dosage_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.YEAR, datePicker.getYear());

                updateDose(dateView.getVaccine(), dateView.getDose(), cal.getTimeInMillis());

                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(mContext.getResources().getString(R.string.modal_new_dosage_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(R.string.modal_new_dosage_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDose(dateView.getVaccine(), dateView.getDose(), null);
            }
        });

        builder.show();
    }

    private void updateDose(Vaccine vaccine, Dose dose, Long doseDate) {
        // TODO push exceptions to UI
        dose.setDate(doseDate);
        if (vaccine.updateDose(dose)) {
            if (mCurrRecord.updateVaccine(vaccine)) {
                // push the update to the database, which will trigger update listeners,
                // updating the view
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("patientRecords").child(mCurrRecord.getDatabaseId()).setValue(mCurrRecord);
            } else {
                // TODO throw unable to update vaccine in record
            }
        } else {
            // TODO throw unable to update dose in vaccine
        }
    }

    private static class ViewHolder {
        TextView vaccineTextView;
        LinearLayout dosesLinearLayout;
    }

}
