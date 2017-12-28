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
package mhealth.mvax.records.record.vaccine;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Date;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.records.views.DateModal;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter for displaying vaccines, their doses, and due dates;
 *         handles updating due dates and vaccinations in the database
 */
public class VaccineAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private final Context mContext;
    private final String mPatientDatabaseKey;
    private List<Vaccine> mVaccines;
    private HashMap<String, Date> mDates; // contains both Vaccinations and DueDates

    //================================================================================
    // Constructors
    //================================================================================

    VaccineAdapter(Context context, String patientKey, HashMap<String, Vaccine> vaccines, HashMap<String, Date> dates) {
        mContext = context;
        mPatientDatabaseKey = patientKey;
        mVaccines = new ArrayList<>(vaccines.values());
        Collections.sort(mVaccines);
        mDates = dates;
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public int getCount() {
        return mVaccines.size();
    }

    @Override
    public Vaccine getItem(int position) {
        return mVaccines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Populates each row with the vaccine's due date and
     * list of doses for the vaccine
     */
    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        final Vaccine vaccine = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ViewHolder holder;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item_vaccine, viewGroup, false);
            holder = new ViewHolder();
            holder.vaccineNameTextView = rowView.findViewById(R.id.vaccine_name);
            holder.vaccineLinearLayout = rowView.findViewById(R.id.vaccine_linear_layout);
            holder.dosesLinearLayout = rowView.findViewById(R.id.doses_linear_layout);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        // clear out old views from reused LinearLayout
        holder.vaccineLinearLayout.removeAllViews();
        // set vaccine name and add to LinearLayout
        holder.vaccineNameTextView.setText(vaccine.getName());
        holder.vaccineLinearLayout.addView(holder.vaccineNameTextView);
        // add due date to LinearLayout
        holder.vaccineLinearLayout.addView(linearLayoutForDate(vaccine.getDatabaseKey(),
                mContext.getString(R.string.due_date_label),
                R.string.dueDatesTable));

        // clear out old views from reused LinearLayout
        holder.dosesLinearLayout.removeAllViews();
        // add all doses to linear layout
        for (Dose dose : vaccine.getDoses()) {
            holder.dosesLinearLayout.addView(linearLayoutForDate(dose.getDatabaseKey(),
                    dose.getLabel(),
                    R.string.vaccinationsTable));
        }

        return rowView;
    }

    private static class ViewHolder {
        TextView vaccineNameTextView;
        LinearLayout vaccineLinearLayout;
        LinearLayout dosesLinearLayout;
    }

    //================================================================================
    // Public methods
    //================================================================================

    public void refresh(HashMap<String, Vaccine> vaccines,
                        HashMap<String, Date> dates) {
        mVaccines = new ArrayList<>(vaccines.values());
        Collections.sort(mVaccines);
        mDates = dates;
        notifyDataSetChanged();
    }

    //================================================================================
    // Private methods
    //================================================================================

    /**
     * Get the LinearLayout for a mVax Date object, which is a labeled gray box that
     * displays the date and is set with the proper listeners
     *
     * @param associatedDatabaseKey unique Firebase database key associated with the Date object
     *                              (i.e. vaccine database key for a DueDate)
     * @param label                 label to be displayed next to the date
     * @param databaseId            database string ID from resource file identifying the table
     *                              on which the listener should be set
     * @return LinearLayout initialized with proper listener and view properties
     */
    private DateLinearLayout linearLayoutForDate(final String associatedDatabaseKey,
                                                 String label,
                                                 final int databaseId) {
        final DateLinearLayout doseLinearLayout = new DateLinearLayout(mContext);

        doseLinearLayout.setLabel(label);

        if (mDates.containsKey(associatedDatabaseKey)) { // check if existing Date object exists
            final NullableDateFormat dateFormat =
                    new NullableDateFormat(mContext.getString(R.string.date_format));
            final String dateString =
                    dateFormat.getString(mDates.get(associatedDatabaseKey).getDate());
            doseLinearLayout.setDate(dateString);
        }

        doseLinearLayout.setDateViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View dateView) {
                promptForDate(associatedDatabaseKey, databaseId);
            }
        });

        return doseLinearLayout;
    }

    /**
     * Displays a modal containing a DatePicker for choosing and returning a date;
     * triggers listeners for saving the date to the proper object in the database
     *
     * @param associatedDatabaseKey unique Firebase database key associated with the Date object
     *                              (i.e. vaccine database key for a DueDate)
     * @param databaseId            database string ID from resource file identifying the table
     *                              on which the listener should be set
     */
    private void promptForDate(final String associatedDatabaseKey, final int databaseId) {
        Long existingDate = null;
        if (mDates.containsKey(associatedDatabaseKey)) {
            existingDate = mDates.get(associatedDatabaseKey).getDate();
        }

        final DateModal dateModal = new DateModal(existingDate, mContext);
        dateModal.setPositiveButtonAction(new TypeRunnable<Long>() {
            @Override
            public void run(Long date) {
                setDate(associatedDatabaseKey, date, databaseId);
            }
        });
        dateModal.setNeutralButtonAction(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (mDates.containsKey(associatedDatabaseKey)) {
                    final Date dateToDelete = mDates.get(associatedDatabaseKey);
                    deleteDate(dateToDelete.getDatabaseKey(), databaseId);
                }
            }
        });
        dateModal.show();
    }

    /**
     * Initialize and push a new Date object to the database, or update the existing
     * Date object in the database, depending on whether or not the Date object
     * already exists
     *
     * @param associatedDatabaseKey unique Firebase database key associated with the Date object
     *                              (i.e. vaccine database key for a DueDate)
     * @param date                  date with which to update, provided by the DatePicker
     *                              modal and represented by milliseconds since Unix epoch
     * @param databaseId            database string ID from resource file identifying the table
     *                              on which the Date object should be set
     */
    private void setDate(String associatedDatabaseKey, Long date, int databaseId) {
        final String masterTable = mContext.getString(R.string.dataTable);
        final String dataTable = mContext.getString(databaseId); // corresponding data table
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable);

        Date datePair;
        if (mDates.containsKey(associatedDatabaseKey)) {
            // changing date of existing vaccination
            datePair = mDates.get(associatedDatabaseKey);
            datePair.setDate(date);
            databaseRef.child(datePair.getDatabaseKey()).setValue(datePair);
        } else {
            // creating a new date
            databaseRef = databaseRef.push();
            if (databaseId == R.string.vaccinationsTable) { // creating a new Vaccination
                datePair = new Vaccination(databaseRef.getKey(), mPatientDatabaseKey, associatedDatabaseKey, date);
            } else { // creating a new DueDate
                datePair = new DueDate(databaseRef.getKey(), mPatientDatabaseKey, associatedDatabaseKey, date);
            }
            databaseRef.setValue(datePair);
        }

    }

    /**
     * Delete a Date object with the given databaseKey in the database table
     * with the given databaseId
     *
     * @param databaseKey databaseKey of Date object to delete
     * @param databaseId  String id representing the table in which the
     *                    Date obejct to delete is located
     */
    private void deleteDate(String databaseKey, int databaseId) {
        final String masterTable = mContext.getString(R.string.dataTable);
        final String dataTable = mContext.getString(databaseId);
        FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable)
                .child(databaseKey)
                .setValue(null);
    }

}
