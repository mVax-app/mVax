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

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Date;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.records.modals.DateModal;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 * <p>
 * Adapter for displaying vaccines, their doses, and due dates
 */
public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ViewGroup mParent;

    private String mPatientKey;
    private List<Vaccine> mVaccines;
    private Map<String, Vaccination> mVaccinations;
    private Map<String, DueDate> mDueDates;

    VaccineAdapter(String patientKey) {
        mVaccines = new ArrayList<>();
        mVaccinations = new HashMap<>();
        mPatientKey = patientKey;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vaccineName, dueDate;
        LinearLayout vaccineDoses;

        ViewHolder(View itemView) {
            super(itemView);
            vaccineName = itemView.findViewById(R.id.vaccine_name);
            vaccineDoses = itemView.findViewById(R.id.vaccine_doses);
            dueDate = itemView.findViewById(R.id.due_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        mParent = parent;
        final View row = mInflater.inflate(R.layout.list_item_vaccine, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Vaccine vaccine = mVaccines.get(position);
        holder.vaccineName.setText(vaccine.getName());

        // add doses
        holder.vaccineDoses.removeAllViews(); // clear out previous views
        for (Dose d : vaccine.getDoses()) {
            View doseView = mInflater.inflate(R.layout.list_item_dose, mParent, false);
            TextView label = doseView.findViewById(R.id.dose_label);
            label.setText(d.getLabel());

            TextView date = doseView.findViewById(R.id.dose_date);
            if (mVaccinations.containsKey(d.getDatabaseKey())) {
                Vaccination vaccination = mVaccinations.get(d.getDatabaseKey());
                final String dateString = NullableDateFormat.getString(vaccination.getDate());
                date.setText(dateString);
            }
            date.setOnClickListener(v -> promptForVaccinationDate(d.getDatabaseKey()));

            holder.vaccineDoses.addView(doseView);
        }

        // add due date
        TextView dateView = holder.dueDate.findViewById(R.id.due_date);
        if (mDueDates.containsKey(vaccine.getDatabaseKey())) {
            DueDate dueDate = mDueDates.get(vaccine.getDatabaseKey());
            final String dateString = NullableDateFormat.getString(dueDate.getDate());
            dateView.setText(dateString);
        }
        dateView.setOnClickListener(v -> promptForDueDate(vaccine.getDatabaseKey()));
    }

    @Override
    public int getItemCount() {
        return mVaccines.size();
    }

    public void refresh(List<Vaccine> vaccines, Map<String, Vaccination> vaccinations, Map<String, DueDate> dueDates) {
        mVaccines = vaccines;
        mVaccinations = vaccinations;
        mDueDates = dueDates;
        Collections.sort(mVaccines); // TODO determine vaccine sort
        notifyDataSetChanged();
    }

    private void promptForVaccinationDate(String key) {
        Long existingDate = null;
        if (mVaccinations.containsKey(key)) {
            existingDate = mVaccinations.get(key).getDate();
        }
        final TypeRunnable<Long> positiveAction = date -> saveDate(key, date, R.string.vaccination_table);
        final DialogInterface.OnClickListener neutralAction = (dialog, which) -> {
            if (mVaccinations.containsKey(key)) {
                final Vaccination dateToDelete = mVaccinations.get(key);
                deleteDate(dateToDelete.getDatabaseKey(), R.string.vaccination_table);
            }
        };
        final DateModal dateModal = new DateModal(existingDate, positiveAction, neutralAction, mParent);
        dateModal.createAndShow();
    }

    private void promptForDueDate(String key) {
        Long existingDate = null;
        if (mDueDates.containsKey(key)) {
            existingDate = mDueDates.get(key).getDate();
        }
        final TypeRunnable<Long> positiveAction = date -> saveDate(key, date, R.string.due_date_table);
        final DialogInterface.OnClickListener neutralAction = (dialog, which) -> {
            if (mDueDates.containsKey(key)) {
                final DueDate dateToDelete = mDueDates.get(key);
                deleteDate(dateToDelete.getDatabaseKey(), R.string.due_date_table);
            }
        };
        final DateModal dateModal = new DateModal(existingDate, positiveAction, neutralAction, mParent);
        dateModal.createAndShow();
    }

    private void saveDate(String key, Long date, int database) {
        final String masterTable = getString(R.string.data_table);
        final String dataTable = getString(database);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable);

        Date d;
        if (mVaccinations.containsKey(key)) { // changing existing date
            d = mVaccinations.get(key);
            d.setDate(date);
        } else { // creating a new date
            switch (database) {
                case R.string.vaccination_table:
                    d = new Vaccination(databaseRef.push().getKey(), mPatientKey, key, date);
                    break;
                case R.string.due_date_table:
                    d = new DueDate(databaseRef.push().getKey(), mPatientKey, key, date);
                    break;
                default:
                    return;
            }
        }

        databaseRef.child(d.getDatabaseKey()).setValue(d).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mParent.getContext(), R.string.date_save_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mParent.getContext(), R.string.date_save_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDate(String key, int database) {
        final String masterTable = getString(R.string.data_table);
        final String dataTable = getString(database);
        FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable)
                .child(key)
                .setValue(null).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mParent.getContext(), R.string.date_delete_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mParent.getContext(), R.string.date_delete_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private String getString(int id) {
        return mParent.getContext().getString(id);
    }

}
