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

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import mhealth.mvax.R;
import mhealth.mvax.model.record.DueDate;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Sex;
import mhealth.mvax.model.record.Dose;
import mhealth.mvax.model.record.Vaccination;
import mhealth.mvax.model.record.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         Generator class for populating the database with dummy data
 */

public class DummyDataGenerator {

    private DatabaseReference mDatabase;

    private String mMasterTable;
    private String mPatientTable;
    private String mGuardianTable;
    private String mVaccineTable;
    private String mDoseTable;
    private String mVaccinationTable;
    private String mDueDateTable;

    private String mPatientDatabaseKey;
    private String mDoseDatabaseKey;
    private String mDueVaccineDatabaseKey;

    public DummyDataGenerator(Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMasterTable = context.getString(R.string.dataTable);
        mPatientTable = context.getString(R.string.patientTable);
        mGuardianTable = context.getString(R.string.guardianTable);
        mVaccineTable = context.getString(R.string.vaccineTable);
        mDoseTable = context.getString(R.string.doseTable);
        mVaccinationTable = context.getString(R.string.vaccinationTable);
        mDueDateTable = context.getString(R.string.dueDatesTable);
        mDatabase.child(mMasterTable).setValue(null);
    }

    public void generateData() {
        generatePatientData();
        generateVaccineData();
        generateVaccinationData();
    }

    private void generatePatientData() {
        DatabaseReference patientRef = mDatabase.child(mMasterTable).child(mPatientTable).push();
        DatabaseReference parentRef = mDatabase.child(mMasterTable).child(mGuardianTable).push();

        Patient rob = new Patient(patientRef.getKey());
        mPatientDatabaseKey = rob.getDatabaseKey();
        rob.setMedicalID("64573829174");
        rob.setFirstName("Robert");
        rob.setMiddleName("Hays");
        rob.setFirstSurname("Kemp");
        rob.setLastSurname("Steilberg");
        rob.setSex(Sex.MALE);
        rob.setGuardianDatabaseID(parentRef.getKey());
        rob.setDOB(823237200000L);
        rob.setCommunity("Alspaugh");
        rob.setPlaceOfBirth("Harrisonburg");
        rob.setAddress("2504 Vesson Ave, Durham, NC");
        rob.setPhoneNumber("1234567890");
        patientRef.setValue(rob);

        Guardian matt = new Guardian(parentRef.getKey());
        matt.setMedicalID("1239248354");
        matt.setFirstName("Matt");
        matt.setMiddleName("Muffin");
        matt.setFirstSurname("Leroy");
        matt.setLastSurname("Tribby");
        matt.setSex(Sex.MALE);
        matt.setDependents(new ArrayList<>(Collections.singletonList(patientRef.getKey())));
        parentRef.setValue(matt);
    }

    private void generateVaccineData() {

        DatabaseReference vaccineRef = mDatabase.child(mMasterTable).child(mVaccineTable).push();
        DatabaseReference dose1Ref = mDatabase.child(mMasterTable).child(mDoseTable).push();
        DatabaseReference dose2Ref = mDatabase.child(mMasterTable).child(mDoseTable).push();
        DatabaseReference dose3Ref = mDatabase.child(mMasterTable).child(mDoseTable).push();

        Vaccine hepB = new Vaccine(vaccineRef.getKey());
        mDueVaccineDatabaseKey = hepB.getDatabaseKey();
        hepB.setName("Hepatitis B");
        hepB.setTargetCount(400);
        hepB.setGivenCount(100);
//        hepB.setDoses(new ArrayList<>(Arrays.asList(dose1Ref.getKey(), dose2Ref.getKey(), dose3Ref.getKey())));
//        vaccineRef.setValue(hepB);

        Dose hepB1 = new Dose(dose1Ref.getKey());
//        hepB1.setVaccineDatabaseKey(vaccineRef.getKey());
        hepB1.setFormCode("HEPB_DU_1");
        hepB1.setLabel1("1");
        hepB1.setLabel2("PRI");
//        dose1Ref.setValue(hepB1);

        Dose hepB2 = new Dose(dose2Ref.getKey());
        mDoseDatabaseKey = hepB2.getDatabaseKey();
//        hepB2.setVaccineDatabaseKey(vaccineRef.getKey());
        hepB2.setFormCode("HEPB_DU_1");
        hepB2.setLabel1("2");
        hepB2.setLabel2("SEG");
//        dose2Ref.setValue(hepB2);

        Dose hepB3 = new Dose(dose3Ref.getKey());
//        hepB3.setVaccineDatabaseKey(vaccineRef.getKey());
        hepB3.setFormCode("HEPB_DU_1");
        hepB3.setLabel1("3");
        hepB3.setLabel2("SEG");
//        dose3Ref.setValue(hepB3);

        hepB.addDoses(hepB1, hepB2, hepB3);
        vaccineRef.setValue(hepB);

    }

    private void generateVaccinationData() {
        DatabaseReference vaccinationRef = mDatabase.child(mMasterTable).child(mVaccinationTable).push();

        Vaccination vaccination = new Vaccination(vaccinationRef.getKey(), mPatientDatabaseKey, mDoseDatabaseKey, 823237200000L);
        vaccinationRef.setValue(vaccination);

        vaccinationRef = mDatabase.child(mMasterTable).child(mVaccinationTable).push();
        Vaccination badVaccination = new Vaccination("badRef", "badPatient", "badDose", 823237200000L);
        vaccinationRef.setValue(badVaccination);

        generateDueDateData();
    }

    private void generateDueDateData() {
        DatabaseReference dueDateRef = mDatabase.child(mMasterTable).child(mDueDateTable).push();

        DueDate dueDate = new DueDate(dueDateRef.getKey(), mPatientDatabaseKey, mDueVaccineDatabaseKey, 823237200000L);
        dueDateRef.setValue(dueDate);

        dueDateRef = mDatabase.child(mMasterTable).child(mDueDateTable).push();
        DueDate badDueDate = new DueDate("badRef", "badPatient", "badVaccine", 823637200000L);
        dueDateRef.setValue(badDueDate);
    }

}
