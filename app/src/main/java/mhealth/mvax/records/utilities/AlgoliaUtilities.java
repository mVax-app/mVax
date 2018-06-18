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
 * DESCRIPTION
 */
public class AlgoliaUtilities {

    private Activity mActivity;
    private Index mIndex;

    public AlgoliaUtilities(Activity activity, Runnable onIndexInitListener) {
        mActivity = activity;
        initSearchIndex(onIndexInitListener);
    }

    public Index getIndex() {
        return mIndex;
    }

    private void initSearchIndex(Runnable listener) {
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

                            // TODO move to db file
                            String applicationId = configVars.get("application_id");
                            String apiKey = configVars.get("api_key");
                            String indexName = configVars.get("patient_index");
                            Client algoliaClient = new Client(applicationId, apiKey);
                            mIndex = algoliaClient.getIndex(indexName);
                            listener.run();
                        } else {
                            Toast.makeText(mActivity, "Unable to init search index", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mActivity, "Unable to init search index", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void search(String query, TypeRunnable<SearchResult> onHitListener) {
        if (mIndex == null) {
            throw new SearchException("search not possible with uninitialized index");
        }

        mIndex.searchAsync(new Query(query), (results, error) -> {
            try {
                JSONArray hits = (JSONArray) results.get("hits");
                for (int i = 0; i < hits.length(); i++) {
                    try {
                        String objectIdField = mActivity.getString(R.string.algoliaObjectID);
                        String firstNameField = mActivity.getString(R.string.patientFirstName);
                        String lastNameField = mActivity.getString(R.string.patientLastName);
                        String medicalIdField = mActivity.getString(R.string.patientMedicalId);
                        String dobField = mActivity.getString(R.string.patientdob);

                        JSONObject patient = (JSONObject) hits.get(i);
                        SearchResult result = new SearchResult((String) patient.get(objectIdField));

                        result.setFirstName((String) patient.get(firstNameField));
                        result.setLastName((String) patient.get(lastNameField));
                        result.setMedicalId((String) patient.get(medicalIdField));
                        result.setDOB((Long) patient.get(dobField));

                        onHitListener.run(result);
                    } catch (JSONException e) {
                        // TODO change search_incomplete string
                        Toast.makeText(mActivity, R.string.search_incomplete, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                // TODO fix
                Toast.makeText(mActivity, "search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void deleteObject(Activity activity, String objectID, Runnable onCompleteListener) {
//        AlgoliaUtilities.initSearchIndex(activity, index -> index.deleteObjectAsync(objectID, (jsonObject, error) -> {
//            if (error == null) {
//                onCompleteListener.run();
//            } else {
//                Toast.makeText(activity, R.string.patient_delete_fail, Toast.LENGTH_SHORT).show();
//            }
//        }));
    }

    public static void saveObject(Activity activity, Patient patient, Runnable onCompleteListener) {
//        initSearchIndex(activity, index -> {
//            JSONObject searchObject = new JSONObject();
//            JSONArray array = new JSONArray();
//            try {
//                // TODO get from db file
//                searchObject.put("firstName", patient.getFirstName());
//                searchObject.put("lastName", patient.getLastName());
//                searchObject.put("medicalId", patient.getMedicalId());
//                searchObject.put("dob", patient.getDOB());
//                searchObject.put("guardianName", patient.getGuardianName());
//                array.put(array);
//            } catch (JSONException e) {
//                Toast.makeText(activity, R.string.patient_save_fail, Toast.LENGTH_SHORT).show();
//            }
//
//            index.partialUpdateObjectsAsync(array, true, (jsonObject, error) -> {
//                if (error == null) {
//                    onCompleteListener.run();
//                } else {
//                    Toast.makeText(activity, R.string.patient_save_fail, Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
    }


}
