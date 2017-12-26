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
package mhealth.mvax.settings;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Sex;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         Factory for populating the database
 *         with dummy data
 */

class DataGenerator {

    private DatabaseReference mDatabase;

    private String mDataTable;

    private String mPatientTable;
    private String mGuardianTable;
    private String mVaccineTable;

    private String mVaccinationTable;
    private String mDueDateTable;

    private String mPatientDatabaseKey;
    private String mDoseDatabaseKey;
    private String mDueVaccineDatabaseKey;

    DataGenerator(Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDataTable = context.getString(R.string.dataTable);
        mPatientTable = context.getString(R.string.patientTable);
        mGuardianTable = context.getString(R.string.guardianTable);
        mVaccineTable = context.getString(R.string.vaccineTable);
        mVaccinationTable = context.getString(R.string.vaccinationsTable);
        mDueDateTable = context.getString(R.string.dueDatesTable);

        // clear out existing data
        mDatabase.child(mDataTable).setValue(null);
    }

    void generateData() {
        generatePatientData();
        generateVaccineData();
        generateVaccinationData();
    }

    private void generatePatientData() {
        DatabaseReference patientRef = mDatabase.child(mDataTable).child(mPatientTable).push();
        DatabaseReference parentRef = mDatabase.child(mDataTable).child(mGuardianTable).push();

        Patient rob = new Patient(patientRef.getKey());
        mPatientDatabaseKey = rob.getDatabaseKey();
        rob.setMedicalID("64573829174");
        rob.setFirstName("Robert");
        rob.setMiddleName("Hays");
        rob.setFirstSurname("Kemp");
        rob.setLastSurname("Steilberg");
        rob.setSex(Sex.MALE);
        rob.setGuardianDatabaseKey(parentRef.getKey());
        rob.setDOB(823237200000L);
        rob.setCommunity("Alspaugh");
        rob.setPlaceOfBirth("Harrisonburg");
        rob.setAddress("2504 Vesson Ave, Durham, NC");
        rob.setPhoneNumber("1234567890");
        patientRef.setValue(rob);

        Guardian matt = new Guardian(parentRef.getKey(), patientRef.getKey());
        matt.setMedicalID("1239248354");
        matt.setFirstName("Matt");
        matt.setMiddleName("Muffin");
        matt.setFirstSurname("Leroy");
        matt.setLastSurname("Tribby");
        matt.setSex(Sex.MALE);
//        matt.setDependents(new ArrayList<>(Collections.singletonList(patientRef.getKey())));
        parentRef.setValue(matt);
    }

    private void generateVaccineData() {

        // create doses
        Dose hepB1 = new Dose(mDatabase.push().getKey());
        hepB1.setFormCode("HEPB_DU_1");
        hepB1.setLabel1("1");
        hepB1.setLabel2("PRI");

        Dose hepB2 = new Dose(mDatabase.push().getKey());
        mDoseDatabaseKey = hepB2.getDatabaseKey(); // for dummy Vaccination creation
        hepB2.setFormCode("HEPB_DU_1");
        hepB2.setLabel1("2");
        hepB2.setLabel2("SEG");

        Dose hepB3 = new Dose(mDatabase.push().getKey());
        hepB3.setFormCode("HEPB_DU_1");
        hepB3.setLabel1("3");
        hepB3.setLabel2("SEG");

        // create vaccine that will contain the doses
        DatabaseReference vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine hepB = new Vaccine(vaccineRef.push().getKey());
        mDueVaccineDatabaseKey = hepB.getDatabaseKey(); // for dummy due date creation
        hepB.setName("Hepatitis B");
        hepB.setTargetCount(400);
        hepB.setGivenCount(100);
        hepB.addDoses(hepB1, hepB2, hepB3);

        vaccineRef.setValue(hepB);

        // another vaccine

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine yellow = new Vaccine(vaccineRef.getKey());
        yellow.setName("Yellow Fever");
        yellow.setTargetCount(300);
        yellow.setGivenCount(50);

        for (int i = 0; i < 5; i++) {
            Dose d = new Dose(mDatabase.push().getKey());
            d.setFormCode("CODE");
            d.setLabel1(Integer.toString(i));
            yellow.addDoses(d);
        }
        vaccineRef.setValue(yellow);

        // another vaccine

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine rota = new Vaccine(vaccineRef.getKey());
        rota.setName("Rotavirus");
        rota.setTargetCount(30);
        rota.setGivenCount(12);

        for (int i = 0; i < 3; i++) {
            Dose d = new Dose(mDatabase.push().getKey());
            d.setFormCode("CODE");
            d.setLabel1(Integer.toString(i));
            d.setLabel2("VOP");
            rota.addDoses(d);
        }
        vaccineRef.setValue(rota);

        // another vaccine

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine muffin = new Vaccine(vaccineRef.getKey());
        muffin.setName("Muffin Flu");
        muffin.setTargetCount(3440);
        muffin.setGivenCount(200);

        for (int i = 0; i < 1; i++) {
            Dose d = new Dose(mDatabase.push().getKey());
            d.setFormCode("CODE");
            d.setLabel1(Integer.toString(i));
            d.setLabel2("ROT");
            muffin.addDoses(d);
        }
        vaccineRef.setValue(muffin);

        // another vaccine

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine ebola = new Vaccine(vaccineRef.getKey());
        ebola.setName("Ebola");
        ebola.setTargetCount(30);
        ebola.setGivenCount(1);

        for (int i = 0; i < 10; i++) {
            Dose d = new Dose(mDatabase.push().getKey());
            d.setFormCode("CODE");
            d.setLabel1(Integer.toString(i));
            d.setLabel2("LOL");
            ebola.addDoses(d);
        }
        vaccineRef.setValue(ebola);
    }

    private void generateVaccinationData() {
        DatabaseReference vaccinationRef = mDatabase.child(mDataTable).child(mVaccinationTable).push();

        Vaccination vaccination = new Vaccination(vaccinationRef.getKey(), mPatientDatabaseKey, mDoseDatabaseKey, 823237200000L);
        vaccinationRef.setValue(vaccination);

        vaccinationRef = mDatabase.child(mDataTable).child(mVaccinationTable).push();
        Vaccination badVaccination = new Vaccination("badRef", "badPatient", "badDose", 823237200000L);
        vaccinationRef.setValue(badVaccination);

        generateDueDateData();
    }

    private void generateDueDateData() {
        DatabaseReference dueDateRef = mDatabase.child(mDataTable).child(mDueDateTable).push();

        DueDate dueDate = new DueDate(dueDateRef.getKey(), mPatientDatabaseKey, mDueVaccineDatabaseKey, 823237200000L);
        dueDateRef.setValue(dueDate);

        dueDateRef = mDatabase.child(mDataTable).child(mDueDateTable).push();
        DueDate badDueDate = new DueDate("badRef", "badPatient", "badVaccine", 823637200000L);
        dueDateRef.setValue(badDueDate);
    }

}
