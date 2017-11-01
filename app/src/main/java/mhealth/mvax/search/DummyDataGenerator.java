package mhealth.mvax.search;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.model.Sex;
import mhealth.mvax.model.Record;
import mhealth.mvax.record.vaccine.Dose;
import mhealth.mvax.record.vaccine.Vaccine;

/**
 * @author Robert Steilberg
 *
 * Generator class for populating the database with dummy data
 */

class DummyDataGenerator {

    void generateDummyData() {
        Vaccine hepatitis = new Vaccine("Hepatitis B");
        Dose dose1 = new Dose("R.N.");
        dose1.setDate(823237200000L);
        hepatitis.addDose(dose1);


        Vaccine BCG = new Vaccine("BCG");
        BCG.addDose(new Dose("1"));

        Vaccine polio = new Vaccine("Polio");
        polio.addDose(new Dose("1", "VPI"));
        polio.addDose(new Dose("2", "VOP"));
        polio.addDose(new Dose("3", "VOP"));
        polio.addDose(new Dose("Refuerzo", "VOP"));

        Vaccine rotavirus = new Vaccine("Rotavirus");
        rotavirus.addDose(new Dose("1"));
        rotavirus.addDose(new Dose("2"));
        rotavirus.addDose(new Dose("3"));
        rotavirus.addDose(new Dose("4"));

        Vaccine varicella = new Vaccine("Varicella");
        varicella.addDose(new Dose("F.N."));

        Vaccine syphilis = new Vaccine("Syphilis");
        syphilis.addDose(new Dose("R.N.", "1"));
        syphilis.addDose(new Dose("R.N.", "3"));
        syphilis.addDose(new Dose("R.N.", "3"));
        syphilis.addDose(new Dose("R.N.", "3"));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference patientRecords = db.child("patientRecords").push();
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

        rob.addVaccine(hepatitis);
        rob.addVaccine(BCG);
        rob.addVaccine(polio);
        rob.addVaccine(rotavirus);
        rob.addVaccine(varicella);
        rob.addVaccine(syphilis);
        patientRecords.setValue(rob);

//        patientRecords = _database.child("patientRecords").push();
//        Record alison = new Record(patientRecords.getKey(), "Alison", "Huang", Sex.FEMALE, 1428206400000l, "West Bay");
//        alison.addVaccine(hepatitis);
//        alison.addVaccine(BCG);
//        alison.addVaccine(polio);
//        alison.addVaccine(rotavirus);
//        alison.addVaccine(varicella);
//        alison.addVaccine(syphilis);
//        patientRecords.setValue(alison);
//
//        patientRecords = _database.child("patientRecords").push();
//        Record steven = new Record(patientRecords.getKey(), "Steven", "Yang", Sex.MALE, 1078635600000l, "Oakridge");
//        steven.addVaccine(hepatitis);
//        steven.addVaccine(BCG);
//        steven.addVaccine(polio);
//        steven.addVaccine(rotavirus);
//        steven.addVaccine(varicella);
//        steven.addVaccine(syphilis);
//        patientRecords.setValue(steven);
    }

}
