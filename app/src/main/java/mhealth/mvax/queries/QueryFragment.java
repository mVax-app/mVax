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
package mhealth.mvax.queries;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.records.modals.DateModal;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for exporting and visualizing data
 */
public class QueryFragment extends Fragment {

    private View mView;
    private Map<String, List<Vaccination>> mPatients;
    private QueryAdapter mAdapter;

    public QueryFragment() {
        mPatients = new HashMap<>();
    }

    public static QueryFragment newInstance() {
        return new QueryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_query, container, false);
        Button sinova2 = mView.findViewById(R.id.sinova_2);
        sinova2.setOnClickListener(v -> promptForDate());
        return mView;
    }

    private void promptForDate() {
        final TypeRunnable<Long> positiveAction = date -> {
            render();
            queryDataForDate(date);
        };
        final DateModal dateModal = new DateModal(null, positiveAction, mView);
        dateModal.createAndShow();
    }

    private void queryDataForDate(Long date) {
        final String masterTable = getResources().getString(R.string.data_table);
        final String vaccinationTable = getResources().getString(R.string.vaccination_table);
        final String dateField = getResources().getString(R.string.date);
        Query vaccinationQuery = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccinationTable)
                .orderByChild(dateField)
                .equalTo(date);

        vaccinationQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Vaccination vaccination = dataSnapshot.getValue(Vaccination.class);
                if (vaccination != null) {
                    String patient = vaccination.getPatientDatabaseKey();
                    if (mPatients.containsKey(patient)) {
                        mPatients.get(patient).add(vaccination);
                    } else {
                        ArrayList<Vaccination> vaccinations = new ArrayList<>();
                        vaccinations.add(vaccination);
                        mPatients.put(patient, vaccinations);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Vaccination vaccination = dataSnapshot.getValue(Vaccination.class);
                if (vaccination != null) {
                    String patient = vaccination.getPatientDatabaseKey();
                    if (mPatients.containsKey(patient)) {
                        mPatients.get(patient)
                                .removeIf(v -> v.getDatabaseKey().equals(vaccination.getDatabaseKey()));
                        mPatients.get(patient).add(vaccination);
                    }
                }
                Toast.makeText(mView.getContext(), R.string.vaccinations_updated, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Vaccination vaccination = dataSnapshot.getValue(Vaccination.class);
                if (vaccination != null) {
                    String patient = vaccination.getPatientDatabaseKey();
                    if (mPatients.containsKey(patient)) {
                        mPatients.get(patient)
                                .removeIf(v -> v.getDatabaseKey().equals(vaccination.getDatabaseKey()));
                    }
                    if (mPatients.get(patient).isEmpty()) mPatients.remove(patient);
                }
                Toast.makeText(mView.getContext(), R.string.vaccinations_updated, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mView.getContext(), R.string.vaccinations_download_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void render() {
        final RecyclerView queryResults = mView.findViewById(R.id.search_results);
        queryResults.setHasFixedSize(true);
        queryResults.setLayoutManager(new LinearLayoutManager(getContext()));
        queryResults.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new QueryAdapter();
        queryResults.setAdapter(mAdapter);
    }

    private void refresh() {

    }

}
