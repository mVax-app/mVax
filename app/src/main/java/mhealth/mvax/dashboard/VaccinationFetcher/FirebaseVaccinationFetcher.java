package mhealth.mvax.dashboard.VaccinationFetcher;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.model.record.Patient;

/**
 * @author Matthew Tribby
 *         Class acts as a Firebase getter for Vaccination Data
 *         Dependency Firebase
 *         FB implementation of the VaccinationFetcher interface
 */

public class FirebaseVaccinationFetcher implements VaccinationFetcher {
    private Activity mContext;

    private Map<Patient, List<Vaccination>> patientVacc;
    private Map<Vaccination, String> formCodes;

    private Map<String, String> possibleFormCodes;

    private final DatabaseReference vaxRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable)).child(mContext.getResources().getString(R.string.vaccinationsTable));


    public FirebaseVaccinationFetcher(Activity context){
        this.mContext = context;
    }


    public VaccinationBundle getVaccinationsByDay(String day, String month, String year, List<String> possibleDoses){

        patientVacc = new HashMap<>();
        formCodes = new HashMap<>();

        final String date = year+month+day;
        findPossibleFormCodes(possibleDoses);

        //The following listener queries for all records of the day and adds them to instance variable, records
        vaxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "DatabaseError");
            }
        });

        return new VaccinationBundle(patientVacc, formCodes);
    }

    public VaccinationBundle getVaccinationsByMonth(String month, String year, List<String> possibleDoses){
        patientVacc = new HashMap<>();
        formCodes = new HashMap<>();

        final String date = year+month;
        findPossibleFormCodes(possibleDoses);

        vaxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 1; i <= 31; i++){
                    collectDataFromDb(dataSnapshot, date + i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "DatabaseError");
            }
        });
        
        return new VaccinationBundle(patientVacc, formCodes);
    }

    private void collectDataFromDb(DataSnapshot dataSnapshot, String date){
        if (dataSnapshot.hasChild(date)) {
            DataSnapshot child = dataSnapshot.child(date);

            for (DataSnapshot data : child.getChildren()) {
                Vaccination vacc = data.getValue(Vaccination.class);
                if (possibleFormCodes.containsKey(vacc.getDoseDatabaseKey())) {
                    matchAssociatedPatient(vacc);
                    formCodes.put(vacc, possibleFormCodes.get(vacc.getDoseDatabaseKey()));
                }
            }
        }
    }

    private void matchAssociatedPatient(Vaccination record){

        final Vaccination vaccination = record;

        DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable))
                .child(mContext.getResources().getString(R.string.patientTable));


        patientRef.child(vaccination.getPatientDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                if(patientVacc.containsKey(patient)){
                    List<Vaccination> records = patientVacc.get(patient);
                    records.add(vaccination);
                    patientVacc.put(patient, records);
                }
                else{
                    ArrayList<Vaccination> records = new ArrayList<>();
                    records.add(vaccination);
                    patientVacc.put(patient, records);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void findPossibleFormCodes(final List<String> possibleDoses){
        this.possibleFormCodes = new HashMap<>();

        DatabaseReference vaccineRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable))
                .child(mContext.getResources().getString(R.string.vaccineTable));
        vaccineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Vaccine vacc = snapshot.getValue(Vaccine.class);
                    for(Dose dose : vacc.getDoses()){
                        if(possibleDoses.contains(dose.getFormCode())) {
                            possibleFormCodes.put(dose.getDatabaseKey(), dose.getFormCode());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
