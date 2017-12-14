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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mhealth.mvax.model.record.Sex;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Dose;
import mhealth.mvax.model.record.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         Generator class for populating the database with dummy data
 */

public class DummyDataGenerator {

    private DatabaseReference mDatabase;

    private String mMasterTable;

    private String mRecordTable;

    private String mVaccineTable;

    public DummyDataGenerator(String table, String records, String vaccines) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMasterTable = table;
        mRecordTable = records;
        mVaccineTable = vaccines;
    }

    public void generateDummyPatientRecords() {


        ArrayList<Vaccine> vaccines = new ArrayList<>();

        Vaccine BCG = new Vaccine("1", "BCG");
        BCG.addDose(new Dose("1"));
        vaccines.add(BCG);

        Vaccine polio = new Vaccine("2", "Polio");
        polio.addDose(new Dose("1", "VPI"));
        polio.addDose(new Dose("2", "VOP"));
        polio.addDose(new Dose("3", "VOP"));
        polio.addDose(new Dose("Refuerzo", "VOP"));
        vaccines.add(polio);

        Vaccine rotavirus = new Vaccine("3", "Rotavirus");
        rotavirus.addDose(new Dose("1"));
        rotavirus.addDose(new Dose("2"));
        rotavirus.addDose(new Dose("3"));
        rotavirus.addDose(new Dose("4"));
        vaccines.add(rotavirus);

        Vaccine yellowFever = new Vaccine("4", "Yellow Fever");
        rotavirus.addDose(new Dose("1"));
        rotavirus.addDose(new Dose("2"));
        rotavirus.addDose(new Dose("3"));
        rotavirus.addDose(new Dose("4"));
        rotavirus.addDose(new Dose("5"));
        rotavirus.addDose(new Dose("6"));
        rotavirus.addDose(new Dose("7"));
        vaccines.add(yellowFever);


        DatabaseReference patientRecords = mDatabase.child(mMasterTable).child(mRecordTable).push();
        Record rob = new Record(patientRecords.getKey(), vaccines);
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
        rob.setNumDependents("4");
        rob.setParentAddress("9014 Tarrytown Drive, Richmond, VA 23229");
        rob.setParentPhone("8046904814");

//        patientRecords.setValue(rob);

        patientRecords = mDatabase.child(mMasterTable).child(mRecordTable).push();
        Record muffin = new Record(patientRecords.getKey(), vaccines);
        muffin.setId("5748392019232");
//        muffin.setFirstName("Muffin");
//        muffin.setMiddleName("Lee");
//        muffin.setLastName("Bob");
        muffin.setFirstName(RandomStringGenerator.randomString(8));
        muffin.setMiddleName(RandomStringGenerator.randomString(5));
        muffin.setLastName(RandomStringGenerator.randomString(12));
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
        muffin.setNumDependents("0");
        muffin.setParentAddress("1 Muffin Ln, Atlanta, GA");
        muffin.setParentPhone("3840185960");

        patientRecords.setValue(muffin);

    }

    public void generateDummyVaccineMaster() {

        DatabaseReference vaccineRecords = mDatabase.child(mMasterTable).child(mVaccineTable).push();

        Vaccine hepatitis = new Vaccine(vaccineRecords.getKey(), "Hepatitis B");
        Dose dose1 = new Dose("R.N.");
        hepatitis.setTargetCount(100);
        hepatitis.setGivenCount(50);
        hepatitis.addDose(dose1);

        vaccineRecords.setValue(hepatitis);

        vaccineRecords = mDatabase.child(mMasterTable).child(mVaccineTable).push();

        Vaccine BCG = new Vaccine(vaccineRecords.getKey(), "BCG");
        BCG.setTargetCount(300);
        BCG.setGivenCount(100);
        BCG.addDose(new Dose("1"));
        vaccineRecords.setValue(BCG);

        vaccineRecords = mDatabase.child(mMasterTable).child(mVaccineTable).push();

        Vaccine polio = new Vaccine(vaccineRecords.getKey(), "Polio");
        polio.setTargetCount(400);
        polio.setGivenCount(300);
        polio.addDose(new Dose("1", "VPI"));
        polio.addDose(new Dose("2", "VOP"));
        polio.addDose(new Dose("3", "VOP"));
        polio.addDose(new Dose("Refuerzo", "VOP"));
        vaccineRecords.setValue(polio);

        vaccineRecords = mDatabase.child(mMasterTable).child(mVaccineTable).push();
        Vaccine rotavirus = new Vaccine(vaccineRecords.getKey(), "Rotavirus");
        rotavirus.setTargetCount(500);
        rotavirus.setGivenCount(450);
        rotavirus.addDose(new Dose("1"));
        rotavirus.addDose(new Dose("2"));
        rotavirus.addDose(new Dose("3"));
        rotavirus.addDose(new Dose("4"));
        vaccineRecords.setValue(rotavirus);

        vaccineRecords = mDatabase.child(mMasterTable).child(mVaccineTable).push();
        Vaccine varicella = new Vaccine(vaccineRecords.getKey(), "Varicella");
        varicella.setTargetCount(200);
        varicella.setGivenCount(200);
        varicella.addDose(new Dose("F.N."));
        vaccineRecords.setValue(varicella);

        vaccineRecords = mDatabase.child(mMasterTable).child(mVaccineTable).push();
        Vaccine yellow = new Vaccine(vaccineRecords.getKey(), "Yellow Fever");
        yellow.setTargetCount(600);
        yellow.setGivenCount(100);
        yellow.addDose(new Dose("R.N.", "1"));
        yellow.addDose(new Dose("R.N.", "3"));
        yellow.addDose(new Dose("R.N.", "3"));
        yellow.addDose(new Dose("R.N.", "3"));
        vaccineRecords.setValue(yellow);

    }

}
