package mhealth.mvax.records.utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.model.record.VaccinationRecord;

/**
 * Created by mtribby on 11/26/17.
 */

public class VaccinationDummyDataGenerator {

    public VaccinationDummyDataGenerator(){

    }

    public void generateRecord(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference vaccination = ref.child("mVax").child("vaccinations").child("20171125").child(RandomStringGenerator.randomString(20));
        VaccinationRecord record = new VaccinationRecord("Polio, 1", "-KzkovJr_LAqyQZC1ORF");
        vaccination.setValue(record);
    }

}
