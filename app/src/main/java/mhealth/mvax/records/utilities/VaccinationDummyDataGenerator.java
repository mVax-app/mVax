package mhealth.mvax.records.utilities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.VaccinationRecord;

/**
 * Created by mtribby on 11/26/17.
 */

public class VaccinationDummyDataGenerator {
    private ArrayList<String> possibleUid;

    public VaccinationDummyDataGenerator(){

    }

    public void generateRecord(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference vaccination = ref.child("mVax").child("vaccinations");

        vaccination.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                addNewValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addNewValues(){
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("mVax").child("records");

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                possibleUid = new ArrayList<String>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Record record = data.getValue(Record.class);
                    possibleUid.add(record.getDatabaseId());
                }
                makeVaccRecords();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    private void makeVaccRecords(){
        String dummyDate = "20171125";

        DatabaseReference parent = FirebaseDatabase.getInstance().getReference().child("mVax").child("vaccinations").child(dummyDate);

        if(possibleUid.size() >= 2) {
            DatabaseReference vacc1 = parent.child(RandomStringGenerator.randomString(20));
            VaccinationRecord record1 = new VaccinationRecord("HepB_DU", possibleUid.get(0));
            vacc1.setValue(record1);

            DatabaseReference vacc2 = parent.child(RandomStringGenerator.randomString(20));
            VaccinationRecord record2 = new VaccinationRecord("ROTA_2M1A_1A", possibleUid.get(1));
            vacc2.setValue(record2);
        }
    }

}
