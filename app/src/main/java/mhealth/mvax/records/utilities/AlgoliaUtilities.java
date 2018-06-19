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
package mhealth.mvax.records.utilities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.SearchResult;

/**
 * @author Robert Steilberg
 * <p>
 * Encapsulates functionality for initializing and using an Algolia
 * search index
 */
public class AlgoliaUtilities {

    private Activity mActivity;
    private Index mIndex;

    public AlgoliaUtilities(Activity activity, Runnable onIndexInitListener) {
        mActivity = activity;
        initSearchIndex(onIndexInitListener);
    }

    private void initSearchIndex(Runnable onInitListener) {
        FirebaseDatabase.getInstance().getReference()
                .child(mActivity.getString(R.string.configTable))
                .child(mActivity.getString(R.string.algoliaTable))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> configVars = dataSnapshot.getValue(t);
                        if (configVars != null) {
                            String applicationId = configVars.get(mActivity.getString(R.string.algoliaApplicationIdKey));
                            String apiKey = configVars.get(mActivity.getString(R.string.algoliaApiKey));
                            String indexName = configVars.get(mActivity.getString(R.string.algoliaPatientIndex));

                            Client algoliaClient = new Client(applicationId, apiKey);
                            mIndex = algoliaClient.getIndex(indexName);
                            onInitListener.run();
                        } else {
                            Toast.makeText(mActivity, R.string.search_init_fail, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mActivity, R.string.search_init_fail, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void search(String query, TypeRunnable<SearchResult> onHitListener, Runnable onCompleteListener) {
        if (mIndex == null) {
            throw new SearchException("search not possible with uninitialized index");
        }
        mIndex.searchAsync(new Query(query), (results, error) -> {
            try {
                String algoliaHitsKey = mActivity.getString(R.string.algoliaHitsKey);
                JSONArray hits = (JSONArray) results.get(algoliaHitsKey);
                for (int i = 0; i < hits.length(); i++) {
                    try {
                        String objectIdField = mActivity.getString(R.string.algoliaObjectID);
                        String firstNameField = mActivity.getString(R.string.patientFirstName);
                        String lastNameField = mActivity.getString(R.string.patientLastName);
                        String medicalIdField = mActivity.getString(R.string.patientMedicalId);
                        String dobField = mActivity.getString(R.string.patientDob);

                        JSONObject patient = (JSONObject) hits.get(i);
                        SearchResult result = new SearchResult((String) patient.get(objectIdField));

                        result.setFirstName((String) patient.get(firstNameField));
                        result.setLastName((String) patient.get(lastNameField));
                        result.setMedicalId((String) patient.get(medicalIdField));
                        result.setDOB((Long) patient.get(dobField));

                        onHitListener.run(result);
                    } catch (JSONException e) {
                        Toast.makeText(mActivity, R.string.search_incomplete, Toast.LENGTH_SHORT).show();
                    }
                }
                onCompleteListener.run();
            } catch (JSONException e) {
                Toast.makeText(mActivity, R.string.search_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteObject(String objectID, Runnable onCompleteListener) {
        if (mIndex == null) {
            throw new SearchException("index deletion not possible with uninitialized index");
        }
        mIndex.deleteObjectAsync(objectID, (jsonObject, error) -> {
            if (error == null) {
                onCompleteListener.run();
            } else {
                Toast.makeText(mActivity, R.string.patient_delete_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveObject(Patient patient, Runnable onCompleteListener) {
        if (mIndex == null) {
            throw new SearchException("index save not possible with uninitialized index");
        }

        JSONObject searchObject = new JSONObject();
        JSONArray array = new JSONArray();

        String objectIdField = mActivity.getString(R.string.algoliaObjectID);
        String firstNameField = mActivity.getString(R.string.patientFirstName);
        String lastNameField = mActivity.getString(R.string.patientLastName);
        String medicalIdField = mActivity.getString(R.string.patientMedicalId);
        String dobField = mActivity.getString(R.string.patientDob);
        String dobSearchField = mActivity.getString(R.string.patientDobSearch);

        String guardianField = mActivity.getString(R.string.patientGuardianName);

        try {
            searchObject.put(objectIdField, patient.getDatabaseKey());
            searchObject.put(firstNameField, patient.getFirstName());
            searchObject.put(lastNameField, patient.getLastName());
            searchObject.put(medicalIdField, patient.getMedicalId());

            // save DOB as a string, formatted dd/mm/yyyy for searching
            String dobSearch = NullableDateFormat.getString("dd/mm/yyyy", patient.getDOB());
            searchObject.put(dobSearchField, dobSearch);

            searchObject.put(dobField, patient.getDOB());
            searchObject.put(guardianField, patient.getGuardianName());
            array.put(searchObject);
        } catch (JSONException e) {
            Toast.makeText(mActivity, R.string.patient_save_fail, Toast.LENGTH_SHORT).show();
        }

        mIndex.partialUpdateObjectsAsync(array, true, (jsonObject, error) -> {
            if (error == null) {
                onCompleteListener.run();
            } else {
                Toast.makeText(mActivity, R.string.patient_save_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
