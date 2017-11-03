package mhealth.mvax.search;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mhealth.mvax.model.Sex;
import mhealth.mvax.model.Record;
import mhealth.mvax.model.Dose;
import mhealth.mvax.model.Vaccine;

/**
 * @author Robert Steilberg
 *
 * Generator class for populating the database with dummy data
 */

class DummyDataGenerator {

    private DatabaseReference mDatabase;

    DummyDataGenerator() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    void generateDummyPatientRecords() {

        DatabaseReference patientRecords = mDatabase.child("patientRecords").push();
        Record rob = new Record(patientRecords.getKey());
        rob.setId("0123456789012");
        rob.setFirstName("Robert");
        rob.setMiddleName("Hays");
        rob.setLastName("Steilberg");
        rob.setSuffix("II");
        rob.setSex(Sex.MALE);
        rob.setDOB(823237200000L);
        rob.setPlaceOfBirth("Harrisonburg, VA");
        rob.setCommunity("Alspaugh");
        rob.setParentFirstName("Ann");
        rob.setParentMiddleName("Kemp");
        rob.setParentLastName("Steilberg");
        rob.setParentSuffix("I");
        rob.setParentSex(Sex.FEMALE);
        rob.setParentId("1234567890123");
        rob.setNumDependents(3);
        rob.setParentAddress("9014 Tarrytown Drive, Richmond, VA 23229");
        rob.setParentPhone("8046904814");

        patientRecords.setValue(rob);

        patientRecords = mDatabase.child("patientRecords").push();
        Record muffin = new Record(patientRecords.getKey());
        muffin.setId("5748392019232");
        muffin.setFirstName("Muffin");
        muffin.setMiddleName("Lee");
        muffin.setLastName("Bob");
        muffin.setSuffix("VI");
        muffin.setSex(Sex.FEMALE);
        muffin.setDOB(823485940200L);
        muffin.setPlaceOfBirth("Atlanta, GA");
        muffin.setCommunity("Roatán");
        muffin.setParentFirstName("Gus");
        muffin.setParentMiddleName("Bert");
        muffin.setParentLastName("Gussy");
        muffin.setParentSuffix("IV");
        muffin.setParentSex(Sex.MALE);
        muffin.setParentId("3950481745324");
        muffin.setNumDependents(2);
        muffin.setParentAddress("1 Muffin Ln, Atlanta, GA");
        muffin.setParentPhone("3840185960");

        patientRecords.setValue(muffin);
        
    }

    void generateDummyVaccineMaster() {
        ArrayList<Vaccine> vaccines = new ArrayList<>();

        Vaccine hepatitis = new Vaccine("Hepatitis B");
        Dose dose1 = new Dose("R.N.");
        hepatitis.addDose(dose1);
        vaccines.add(hepatitis);

        Vaccine BCG = new Vaccine("BCG");
        BCG.addDose(new Dose("1"));
        vaccines.add(BCG);

        Vaccine polio = new Vaccine("Polio");
        polio.addDose(new Dose("1", "VPI"));
        polio.addDose(new Dose("2", "VOP"));
        polio.addDose(new Dose("3", "VOP"));
        polio.addDose(new Dose("Refuerzo", "VOP"));
        vaccines.add(polio);

        Vaccine rotavirus = new Vaccine("Rotavirus");
        rotavirus.addDose(new Dose("1"));
        rotavirus.addDose(new Dose("2"));
        rotavirus.addDose(new Dose("3"));
        rotavirus.addDose(new Dose("4"));
        vaccines.add(rotavirus);

        Vaccine varicella = new Vaccine("Varicella");
        varicella.addDose(new Dose("F.N."));
        vaccines.add(varicella);

        Vaccine yellow = new Vaccine("Yellow Fever");
        yellow.addDose(new Dose("R.N.", "1"));
        yellow.addDose(new Dose("R.N.", "3"));
        yellow.addDose(new Dose("R.N.", "3"));
        yellow.addDose(new Dose("R.N.", "3"));
        vaccines.add(yellow);

        mDatabase.child("vaccineMaster").setValue(vaccines);
    }

}
