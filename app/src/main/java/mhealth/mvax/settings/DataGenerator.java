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

import java.util.ArrayList;
import java.util.Collections;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.DueDate;
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

public class DataGenerator {

    private DatabaseReference mDatabase;

    private String mDataTable;

    private String mPatientTable;
    private String mVaccineTable;

    private String mVaccinationTable;
    private String mDueDateTable;

    private String mPatientDatabaseKey;
    private String mDoseDatabaseKey;
    private String mDueVaccineDatabaseKey;

    public DataGenerator(Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDataTable = context.getString(R.string.dataTable);
        mPatientTable = context.getString(R.string.patientTable);
        mVaccineTable = context.getString(R.string.vaccineTable);
        mVaccinationTable = context.getString(R.string.vaccinationsTable);
        mDueDateTable = context.getString(R.string.dueDatesTable);

        // clear out existing data
        mDatabase.child(mDataTable).child(mPatientTable).setValue(null);
        mDatabase.child(mDataTable).child(mDueDateTable).setValue(null);
        mDatabase.child(mDataTable).child(mVaccinationTable).setValue(null);
        mDatabase.child(mDataTable).child(mVaccineTable).setValue(null);



    }

    public void generateData() {
        generatePatientData();
        generateVaccineData();
        generateVaccinationData();
    }

    private void generatePatientData() {
        DatabaseReference patientRef = mDatabase.child(mDataTable).child(mPatientTable).push();

        Patient rob = new Patient(patientRef.getKey());
        mPatientDatabaseKey = rob.getDatabaseKey();
        rob.setMedicalId("64573829174");
        rob.setFirstName("Robert Hays");
        rob.setLastName("Steilberg II");
        rob.setSex(Sex.MALE);
        rob.setDOB(823237200000L);
        rob.setCommunity("Alspaugh");
        rob.setPlaceOfBirth("Harrisonburg");
        rob.setResidence("2504 Vesson Ave, Durham, NC");
        rob.setPhoneNumber("1234567890");
        rob.setGuardianName("Ann Kemp Steilberg");

        patientRef.setValue(rob);
    }

    private void generateVaccineData() {

        // Neumococo

        DatabaseReference vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine neu = new Vaccine(vaccineRef.getKey());
        neu.setName("Neumococo");
        neu.setTargetCount(300);
        neu.setGivenCount(50);

        Dose neu1 = new Dose(mDatabase.push().getKey());
        neu1.setFormCode("Neu1");
        neu1.setLabel1("Label 1");
        neu1.setLabel2("Label 2");
        neu1.setGivenCount(0);
        neu1.setMonthsUntilNext(2);

        Dose neu2 = new Dose(mDatabase.push().getKey());
        mDueVaccineDatabaseKey = neu2.getDatabaseKey();
        neu2.setFormCode("Neu2");
        neu2.setLabel1("Label 1");
        neu2.setLabel2("Label 2");
        neu2.setGivenCount(0);
        neu2.setMonthsUntilNext(2);

        Dose neu3 = new Dose(mDatabase.push().getKey());
        neu3.setFormCode("Neu3");
        neu3.setLabel1("Label 1");
        neu3.setLabel2("Label 2");
        neu3.setGivenCount(0);
        neu3.setMonthsUntilNext(0);

        neu1.setNextDoseID(neu2.getDatabaseKey());
        neu2.setNextDoseID(neu3.getDatabaseKey());
        neu3.setNextDoseID(null);

        neu.addDoses(neu1, neu2, neu3);
        vaccineRef.setValue(neu);

        // VPI
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine VPI = new Vaccine(vaccineRef.getKey());
        VPI.setName("VPI");
        VPI.setTargetCount(200);
        VPI.setGivenCount(70);

        Dose VPI1 = new Dose(mDatabase.push().getKey());
        VPI1.setFormCode("VPI");
        VPI1.setLabel1("Label 1");
        VPI1.setLabel2("Label 2");
        VPI1.setGivenCount(0);
        VPI1.setMonthsUntilNext(0);

        VPI1.setNextDoseID(null);

        VPI.addDoses(VPI1);
        vaccineRef.setValue(VPI);

        // Sabin

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine sab = new Vaccine(vaccineRef.getKey());
        sab.setName("Sabin");
        sab.setTargetCount(300);
        sab.setGivenCount(50);

        Dose sab1 = new Dose(mDatabase.push().getKey());
        sab1.setFormCode("Sab1");
        sab1.setLabel1("Label 1");
        sab1.setLabel2("Label 2");
        sab1.setGivenCount(0);
        sab1.setMonthsUntilNext(2);

        Dose sab2 = new Dose(mDatabase.push().getKey());
        sab2.setFormCode("Sab2");
        sab2.setLabel1("Label 1");
        sab2.setLabel2("Label 2");
        sab2.setGivenCount(0);
        sab2.setMonthsUntilNext(12);

        Dose sab3 = new Dose(mDatabase.push().getKey());
        sab3.setFormCode("Sab3");
        sab3.setLabel1("Label 1");
        sab3.setLabel2("Label 2");
        sab3.setGivenCount(0);
        sab3.setMonthsUntilNext(0);

        sab1.setNextDoseID(sab2.getDatabaseKey());
        sab2.setNextDoseID(sab3.getDatabaseKey());
        sab3.setNextDoseID(null);

        sab.addDoses(sab1, sab2, sab3);
        vaccineRef.setValue(sab);

        // Rotavirus

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine rota = new Vaccine(vaccineRef.getKey());
        rota.setName("Rotavirus");
        rota.setTargetCount(300);
        rota.setGivenCount(50);

        Dose rota1 = new Dose(mDatabase.push().getKey());
        rota1.setFormCode("Rota1");
        rota1.setLabel1("Label 1");
        rota1.setLabel2("Label 2");
        rota1.setGivenCount(0);
        rota1.setMonthsUntilNext(2);

        Dose rota2 = new Dose(mDatabase.push().getKey());
        rota2.setFormCode("Rota2");
        rota2.setLabel1("Label 1");
        rota2.setLabel2("Label 2");
        rota2.setGivenCount(0);
        rota2.setMonthsUntilNext(0);

        rota1.setNextDoseID(rota2.getDatabaseKey());
        rota2.setNextDoseID(null);

        rota.addDoses(rota1, rota2);
        vaccineRef.setValue(rota);

        // Pentavalente

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine penval = new Vaccine(vaccineRef.getKey());
        penval.setName("Pentavalente");
        penval.setTargetCount(300);
        penval.setGivenCount(50);

        Dose penval1 = new Dose(mDatabase.push().getKey());
        penval1.setFormCode("PenVal1");
        penval1.setLabel1("Label 1");
        penval1.setLabel2("Label 2");
        penval1.setGivenCount(0);
        penval1.setMonthsUntilNext(2);

        Dose penval2 = new Dose(mDatabase.push().getKey());
        penval2.setFormCode("PenVal2");
        penval2.setLabel1("Label 1");
        penval2.setLabel2("Label 2");
        penval2.setGivenCount(0);
        penval2.setMonthsUntilNext(2);

        Dose penval3 = new Dose(mDatabase.push().getKey());
        penval3.setFormCode("PenVal3");
        penval3.setLabel1("Label 1");
        penval3.setLabel2("Label 2");
        penval3.setGivenCount(0);
        penval3.setMonthsUntilNext(0);

        penval1.setNextDoseID(penval2.getDatabaseKey());
        penval2.setNextDoseID(penval3.getDatabaseKey());
        penval3.setNextDoseID(null);

        penval.addDoses(penval1, penval2, penval3);
        vaccineRef.setValue(penval);

        // Vitamin A

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine vitA = new Vaccine(vaccineRef.getKey());
        vitA.setName("Vitamin A");
        vitA.setTargetCount(300);
        vitA.setGivenCount(50);

        Dose vitA1 = new Dose(mDatabase.push().getKey());
        vitA1.setFormCode("VitA1");
        vitA1.setLabel1("Label 1");
        vitA1.setLabel2("Label 2");
        vitA1.setGivenCount(0);
        vitA1.setMonthsUntilNext(6);

        Dose vitA2 = new Dose(mDatabase.push().getKey());
        vitA2.setFormCode("VitA2");
        vitA2.setLabel1("Label 1");
        vitA2.setLabel2("Label 2");
        vitA2.setGivenCount(0);
        vitA2.setMonthsUntilNext(0);

        vitA1.setNextDoseID(vitA2.getDatabaseKey());
        vitA2.setNextDoseID(null);

        vitA.addDoses(vitA1, vitA2);
        vaccineRef.setValue(vitA);

        // SRP

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine srp = new Vaccine(vaccineRef.getKey());
        srp.setName("SRP");
        srp.setTargetCount(300);
        srp.setGivenCount(50);

        Dose srp1 = new Dose(mDatabase.push().getKey());
        srp1.setFormCode("SRP1");
        srp1.setLabel1("Label 1");
        srp1.setLabel2("Label 2");
        srp1.setGivenCount(0);
        srp1.setMonthsUntilNext(6);

        Dose srp2 = new Dose(mDatabase.push().getKey());
        srp2.setFormCode("SRP2");
        srp2.setLabel1("Label 1");
        srp2.setLabel2("Label 2");
        srp2.setGivenCount(0);
        srp2.setMonthsUntilNext(0);

        srp1.setNextDoseID(srp2.getDatabaseKey());
        srp2.setNextDoseID(null);

        srp.addDoses(srp1, srp2);
        vaccineRef.setValue(srp);

        // DPT

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine dpt = new Vaccine(vaccineRef.getKey());
        dpt.setName("DPT");
        dpt.setTargetCount(300);
        dpt.setGivenCount(50);

        Dose dpt1 = new Dose(mDatabase.push().getKey());
        dpt1.setFormCode("DPT1");
        dpt1.setLabel1("Label 1");
        dpt1.setLabel2("Label 2");
        dpt1.setGivenCount(0);
        dpt1.setMonthsUntilNext(6);

        Dose dpt2 = new Dose(mDatabase.push().getKey());
        dpt2.setFormCode("DPT2");
        dpt2.setLabel1("Label 1");
        dpt2.setLabel2("Label 2");
        dpt2.setGivenCount(0);
        dpt2.setMonthsUntilNext(0);

        dpt1.setNextDoseID(dpt2.getDatabaseKey());
        dpt2.setNextDoseID(null);

        dpt.addDoses(dpt1, dpt2);
        vaccineRef.setValue(dpt);

        // TD

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine td = new Vaccine(vaccineRef.getKey());
        td.setName("TD");
        td.setTargetCount(200);
        td.setGivenCount(70);

        Dose td1 = new Dose(mDatabase.push().getKey());
        td1.setFormCode("TD1");
        td1.setLabel1("Label 1");
        td1.setLabel2("Label 2");
        td1.setGivenCount(0);
        td1.setMonthsUntilNext(0);

        td1.setNextDoseID(null);

        td.addDoses(td1);
        vaccineRef.setValue(td);

        // VPH

        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine vph = new Vaccine(vaccineRef.getKey());
        vph.setName("VPH");
        vph.setTargetCount(200);
        vph.setGivenCount(70);

        Dose vph1 = new Dose(mDatabase.push().getKey());
        vph1.setFormCode("VPH1");
        vph1.setLabel1("Label 1");
        vph1.setLabel2("Label 2");
        vph1.setGivenCount(0);
        vph1.setMonthsUntilNext(0);

        vph1.setNextDoseID(null);

        vph.addDoses(vph1);
        vaccineRef.setValue(vph);


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
    }

}
