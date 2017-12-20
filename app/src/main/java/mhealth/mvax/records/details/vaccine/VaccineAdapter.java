/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
package mhealth.mvax.records.details.vaccine;

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

import java.util.Calendar;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Dose;
import mhealth.mvax.model.record.Vaccine;
import mhealth.mvax.records.details.patient.RecordDateFormat;
import mhealth.mvax.records.views.DoseDateView;
import mhealth.mvax.records.views.VaccineDateView;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter for listing vaccines and their doses
 */

// TODO: Implement sorting vaccines in the ListView by name and due date
// TODO: This class needs work

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

    VaccineAdapter(Context context, Record currRecord) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mDataSource = currRecord.getVaccines();
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

    /**
     * Populates each row with a list of doses for the vaccine
     */
    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        ViewHolder holder;
        Vaccine vaccine = (Vaccine) getItem(position);

        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_vaccine, parent, false);
            holder = new ViewHolder();
            holder.vaccineTextView = rowView.findViewById(R.id.vaccine_name);
            holder.dosesLinearLayout = rowView.findViewById(R.id.doses_linear_layout);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.vaccineTextView.setText(vaccine.getName());
        // clear out old view from reused LinearLayout
        holder.dosesLinearLayout.removeAllViews();
        renderDoses(rowView.getContext(), holder.dosesLinearLayout, vaccine);
        return rowView;
    }

    private static class ViewHolder {
        TextView vaccineTextView;
        LinearLayout dosesLinearLayout;
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Updates views with data from an updated Record
     *
     * @param updatedRecord the updated Record
     */
    public void refresh(Record updatedRecord) {
        mCurrRecord = updatedRecord;
        mDataSource = updatedRecord.getVaccines();
        notifyDataSetChanged();
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void renderDoses(Context rowContext, LinearLayout layout, Vaccine vaccine) {
        layout.addView(getDueDateLinearLayout(rowContext, vaccine));
//        for (Dose dose : vaccine.getDoses()) {
//            layout.addView(getDoseLinearLayout(dose, rowContext));
//        }
    }

    private LinearLayout getDueDateLinearLayout(Context rowContext, Vaccine vaccine) {
        // TODO fix DRY
        // create LinearLayout to hold the label and date for the next due date
        LinearLayout dueDateLinearLayout = new LinearLayout(rowContext);
        dueDateLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dueDateLinearLayout.setPadding(0, 15, 0, 15);

        // create due date label
        TextView label = new TextView(rowContext);
        label.setText(R.string.due_date);
        label.setTextSize(22);
        label.setGravity(Gravity.CENTER);
        label.setPadding(0, 0, 15, 0);
        //dueDateLinearLayout.addView(label);

        // render actual due date
        VaccineDateView dueDate = new VaccineDateView(rowContext, vaccine);

        dueDate.setLayoutParams(new LinearLayout.LayoutParams(
                250,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        dueDate.setPadding(5, 5, 5, 5);
        dueDate.setGravity(Gravity.CENTER);
        dueDate.setTextSize(22);

        RecordDateFormat dateFormat = new RecordDateFormat(mContext.getString(R.string.date_format));
//        dueDate.setText(dateFormat.getString(vaccine.getDueDate()));

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.LTGRAY);
        dueDate.setBackground(gd);
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View dueDate) {
                newVaccinePrompt((VaccineDateView) dueDate);
            }
        });

        // add dose label and date to the view
        dueDateLinearLayout.addView(label);
        dueDateLinearLayout.addView(dueDate);

        return dueDateLinearLayout;
    }

    private LinearLayout getDoseLinearLayout(Dose dose, Context rowContext) {
        // create LinearLayout to hold the label and date for each dose
        LinearLayout doseLinearLayout = new LinearLayout(rowContext);
        doseLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        doseLinearLayout.setPadding(0, 15, 0, 15);

        // create dose label
        TextView label = new TextView(rowContext);
        label.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        label.setText(dose.getLabel());
        label.setTextSize(22);
        label.setGravity(Gravity.CENTER);
        label.setPadding(0, 0, 15, 0);

        // create dose date view
        DoseDateView dateView = new DoseDateView(rowContext, dose);
        dateView.setLayoutParams(new LinearLayout.LayoutParams(
                250,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        dateView.setPadding(5, 5, 5, 5);
        dateView.setGravity(Gravity.CENTER);
        dateView.setTextSize(22);

        RecordDateFormat dateFormat = new RecordDateFormat(mContext.getString(R.string.date_format));
//        dateView.setText(dateFormat.getString(dose.getDateCompleted()));

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.LTGRAY);
        dateView.setBackground(gd);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View dateView) {
                newDosePrompt((DoseDateView) dateView);
            }
        });

        // add dose label and date to the view
        doseLinearLayout.addView(label);
        doseLinearLayout.addView(dateView);

        return doseLinearLayout;
    }

    /**
     * Render a modal for modifying a vaccine dose record
     *
     * @param view is the DoseDateView object that displays the dose date
     */
    private void newDosePrompt(final DoseDateView view) {
        final DoseDateView dateView = view;
        // TODO generalize this modal somewhere
        // create modal
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.modal_new_dosage_title);
        View dialogView = mInflater.inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);

        builder.setPositiveButton(mContext.getResources().getString(R.string.modal_new_dosage_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.YEAR, datePicker.getYear());
                // cal.getTimeInMillis() gets the date chosen
//                updateDueDate(dateView.getVaccine(), cal.getTimeInMillis());
                updateDose(dateView.getDose(), cal.getTimeInMillis());
            }
        });

        builder.setNegativeButton(mContext.getString(R.string.modal_new_dosage_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(R.string.modal_new_dosage_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDose(dateView.getDose(), null);
            }
        });

        builder.show();
    }


    /**
     *
     * TODO: Refactor this
     */

    private void newVaccinePrompt(final VaccineDateView view) {
        final VaccineDateView dateView = view;

        // create modal
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.modal_new_dosage_title);
        View dialogView = mInflater.inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);

        builder.setPositiveButton(mContext.getResources().getString(R.string.modal_new_dosage_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.YEAR, datePicker.getYear());
                // cal.getTimeInMillis() gets the date chosen
                updateDueDate(dateView.getVaccine(), cal.getTimeInMillis());
//                updateDose(dateView.getDose(), cal.getTimeInMillis());
            }
        });

        builder.setNegativeButton(mContext.getString(R.string.modal_new_dosage_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(R.string.modal_new_dosage_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDueDate(dateView.getVaccine(), null);
            }
        });

        builder.show();
    }



    private void updateDose(Dose dose, Long doseDate) {
//        dose.setDateCompleted(doseDate);
        pushRecordToDatabase();
    }

    private void updateDueDate(Vaccine vaccine, Long dueDate) {
//        vaccine.setDueDate(dueDate);
        pushRecordToDatabase();
    }

    private void pushRecordToDatabase() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = mContext.getString(R.string.masterTable);
        String recordTable = mContext.getString(R.string.recordTable);
        db.child(masterTable).child(recordTable).child(mCurrRecord.getDatabaseId()).setValue(mCurrRecord);
    }
}
