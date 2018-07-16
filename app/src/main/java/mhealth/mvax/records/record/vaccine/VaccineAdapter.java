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
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.records.modals.DateModal;
import mhealth.mvax.records.modals.VaccinationModal;
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
    private int mVaccinationDatabaseId;
    private List<Vaccine> mVaccines;
    private Map<String, Vaccination> mVaccinations;
    private Map<String, DueDate> mDueDates;

    VaccineAdapter(String patientKey, int vaccinationDatabaseId) {
        mVaccines = new ArrayList<>();
        mVaccinations = new HashMap<>();
        mPatientKey = patientKey;
        mVaccinationDatabaseId = vaccinationDatabaseId;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vaccineName, dueDate;
        LinearLayout vaccineDoses;
        ProgressBar dueDateSpinner;

        ViewHolder(View itemView) {
            super(itemView);
            vaccineName = itemView.findViewById(R.id.vaccine_name);
            vaccineDoses = itemView.findViewById(R.id.vaccine_doses);
            dueDate = itemView.findViewById(R.id.due_date);
            dueDateSpinner = itemView.findViewById(R.id.spinner);
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
        for (Dose dose : vaccine.getDoses()) {
            View doseView = mInflater.inflate(R.layout.list_item_dose, mParent, false);
            TextView label = doseView.findViewById(R.id.dose_label);
            label.setText(dose.getLabel());

            TextView date = doseView.findViewById(R.id.dose_date);
            if (mVaccinations.containsKey(dose.getDatabaseKey())) {
                Vaccination vaccination = mVaccinations.get(dose.getDatabaseKey());
                final String dateString = NullableDateFormat.getString(mParent.getContext(), vaccination.getDate());
                date.setText(dateString);
            }

            ProgressBar spinner = doseView.findViewById(R.id.spinner);

            date.setOnClickListener(v -> promptForVaccinationDate(dose.getDatabaseKey(), vaccine.getDatabaseKey(), spinner));

            holder.vaccineDoses.addView(doseView);
        }

        // add due date
        holder.dueDate.setText(""); // clear out old due dates
        TextView dateView = holder.dueDate.findViewById(R.id.due_date);
        if (mDueDates.containsKey(vaccine.getDatabaseKey())) {
            DueDate dueDate = mDueDates.get(vaccine.getDatabaseKey());
            final String dateString = NullableDateFormat.getString(mParent.getContext(), dueDate.getDate());
            dateView.setText(dateString);
        }
        dateView.setOnClickListener(v -> promptForDueDate(vaccine.getDatabaseKey(), holder.dueDateSpinner));
    }

    @Override
    public int getItemCount() {
        return mVaccines.size();
    }

    public void refresh(List<Vaccine> vaccines, Map<String, Vaccination> vaccinations, Map<String, DueDate> dueDates) {
        mVaccines = vaccines;
        mVaccinations = vaccinations;
        mDueDates = dueDates;
        Collections.sort(mVaccines);
        notifyDataSetChanged();
    }

    private void promptForVaccinationDate(String doseKey, String vaccineKey, ProgressBar spinner) {
        Long existingDate = null;
        String existingMonths = null;
        String existingYears = null;
        if (mVaccinations.containsKey(doseKey)) {
            Vaccination v = mVaccinations.get(doseKey);
            existingDate = v.getDate();
            existingMonths = v.getMonths();
            existingYears = v.getYears();
        }

        final TypeRunnable<Bundle> positiveAction = args -> {
            spinner.setVisibility(View.VISIBLE);
            Long date = args.getLong("date");
            String months = args.getString("months");
            String years = args.getString("years");
            saveVaccination(doseKey, vaccineKey, date, months, years, spinner);
        };
        final DialogInterface.OnClickListener neutralAction = (dialog, which) -> {
            if (mVaccinations.containsKey(doseKey)) {
                final Vaccination dateToDelete = mVaccinations.get(doseKey);
                deleteDate(dateToDelete.getDatabaseKey(), mVaccinationDatabaseId, spinner);
            }
        };
        final VaccinationModal vaccinationModal = new VaccinationModal(
                existingDate,
                existingMonths,
                existingYears,
                positiveAction,
                neutralAction,
                mParent);
        vaccinationModal.createAndShow();
    }

    private void promptForDueDate(String vaccineKey, ProgressBar spinner) {
        Long existingDate = null;
        if (mDueDates.containsKey(vaccineKey)) {
            existingDate = mDueDates.get(vaccineKey).getDate();
        }
        final TypeRunnable<Long> positiveAction = date -> saveDueDate(vaccineKey, date, spinner);
        final DialogInterface.OnClickListener neutralAction = (dialog, which) -> {
            if (mDueDates.containsKey(vaccineKey)) {
                final DueDate dateToDelete = mDueDates.get(vaccineKey);
                deleteDate(dateToDelete.getDatabaseKey(), R.string.due_date_table, spinner);
            }
        };
        final DateModal dateModal = new DateModal(existingDate, positiveAction, neutralAction, mParent);
        dateModal.createAndShow();
    }

    private void saveVaccination(String doseKey, String vaccineKey, Long date, String months, String years, ProgressBar spinner) {
        final String masterTable = getString(R.string.data_table);
        final String dataTable = getString(mVaccinationDatabaseId);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable);

        Vaccination vaccination;
        if (mVaccinations.containsKey(doseKey)) { // changing existing date
            vaccination = mVaccinations.get(doseKey);
            vaccination.setDate(date);
        } else { // creating a new date
            vaccination = new Vaccination(databaseRef.push().getKey(), mPatientKey, vaccineKey, doseKey, date);
        }
        vaccination.setMonths(months);
        vaccination.setYears(years);

        databaseRef.child(vaccination.getDatabaseKey()).setValue(vaccination).addOnCompleteListener(write -> {
            spinner.setVisibility(View.GONE);
            if (write.isSuccessful()) {
                Toast.makeText(mParent.getContext(), R.string.vaccination_save_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mParent.getContext(), R.string.vaccination_save_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDueDate(String vaccineKey, Long date, ProgressBar spinner) {
        spinner.setVisibility(View.VISIBLE);

        final String masterTable = getString(R.string.data_table);
        final String dataTable = getString(R.string.due_date_table);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable);

        DueDate dueDate;
        if (mDueDates.containsKey(vaccineKey)) { // changing existing due date
            dueDate = mDueDates.get(vaccineKey);
            dueDate.setDate(date);
        } else { // creating a new due date
            dueDate = new DueDate(databaseRef.push().getKey(), mPatientKey, vaccineKey, date);
        }

        databaseRef.child(dueDate.getDatabaseKey()).setValue(dueDate).addOnCompleteListener(write -> {
            spinner.setVisibility(View.GONE);
            if (write.isSuccessful()) {
                Toast.makeText(mParent.getContext(), R.string.due_date_save_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mParent.getContext(), R.string.due_date_save_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteDate(String key, int database, ProgressBar spinner) {
        spinner.setVisibility(View.VISIBLE);

        final String masterTable = getString(R.string.data_table);
        final String dataTable = getString(database);
        FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dataTable)
                .child(key)
                .setValue(null).addOnCompleteListener(deletion -> {
            spinner.setVisibility(View.GONE);
            if (deletion.isSuccessful()) {
                Toast.makeText(mParent.getContext(), R.string.date_delete_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mParent.getContext(), R.string.date_delete_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getString(int id) {
        return mParent.getContext().getString(id);
    }

}
