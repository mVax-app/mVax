package mhealth.mvax.dashboard;

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
 */

public class VaccinationFetcher {
    private Activity mContext;
    private Map<String, String> doses;
    private Map<Patient, ArrayList<Vaccination>> patientVacc;

    public VaccinationFetcher(Activity context){
        this.mContext = context;
    }


    public VaccinationPdfBundle getVaccinationsByDay(String day, String month, String year, List<String> possibleDoses){

        patientVacc = new HashMap<>();

        final String date = year+month+day;
        final List<Vaccination> vaccinations = new ArrayList<Vaccination>();

        getPossibleFormCodes(possibleDoses);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable));
        DatabaseReference vaxRef = dbRef.child(mContext.getResources().getString(R.string.vaccinationsTable));

        //The following listener queries for all records of the day and adds them to instance variable, records
        vaxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(date)) {
                    DataSnapshot child = dataSnapshot.child(date);

                    for (DataSnapshot data : child.getChildren()) {
                        Vaccination vacc = data.getValue(Vaccination.class);
                        if(doses.containsKey(vacc.getDoseDatabaseKey())) {
                            vaccinations.add(vacc);
                        }
                    }

                    for(Vaccination vaccination : vaccinations){
                        matchAssociatedPatient(vaccination);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "DatabaseError");
            }
        });

        return new VaccinationPdfBundle(patientVacc, getVaccFormCodes(vaccinations));
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
                    ArrayList<Vaccination> records = patientVacc.get(patient);
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


    private void getPossibleFormCodes(final List<String> possibleDoses){
        doses = new HashMap<>();

        DatabaseReference vaccineRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable))
                .child(mContext.getResources().getString(R.string.vaccineTable));
        vaccineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Vaccine vacc = snapshot.getValue(Vaccine.class);
                    for(Dose dose : vacc.getDoses()){
                        if(possibleDoses.contains(dose.getFormCode())) {
                            doses.put(dose.getDatabaseKey(), dose.getFormCode());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private Map<Vaccination, String> getVaccFormCodes(List<Vaccination> vaccinations){
        //doses has the key and the value is the form code
        //Vaccinations have the key
        Map<Vaccination, String> formCodes = new HashMap<>();
        for(Vaccination vacc : vaccinations){
            formCodes.put(vacc, doses.get(vacc.getDoseDatabaseKey()));
        }

        return formCodes;
    }



}
