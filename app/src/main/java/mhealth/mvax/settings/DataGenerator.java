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
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;

/**
 * @author Robert Steilberg
 * <p>
 * Factory for populating the database
 * with dummy data
 */

class DataGenerator {

    private DatabaseReference mDatabase;

    private String mDataTable;

    private String mPatientTable;
    private String mVaccineTable;

    private String mVaccinationTable;
    private String mDueDateTable;

    private String mPatientDatabaseKey;
    private String mDoseDatabaseKey;
    private String mDueVaccineDatabaseKey;

    DataGenerator(Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDataTable = context.getString(R.string.data_table);
        mPatientTable = context.getString(R.string.patient_table);
        mVaccineTable = context.getString(R.string.vaccine_table);
        mVaccinationTable = context.getString(R.string.vaccination_table);
        mDueDateTable = context.getString(R.string.due_date_table);

        // clear out existing data
//        mDatabase.child(mDataTable).child(mDueDateTable).setValue(null);
//        mDatabase.child(mDataTable).child(mVaccinationTable).setValue(null);
//        mDatabase.child(mDataTable).child(mVaccineTable).setValue(null);

    }

    void generateData() {
//        generatePatientData();
        generateVaccineData();
//        generateVaccinationData();
    }

    private void generatePatientData() {
//        DatabaseReference patientRef = mDatabase.child(mDataTable).child(mPatientTable).push();
//
//        Patient rob = new Patient(patientRef.getKey());
//        mPatientDatabaseKey = rob.getDatabaseKey();
//        rob.setMedicalId("64573829174");
//        rob.setFirstName("Robert Hays");
//        rob.setLastName("Steilberg II");
//        rob.setSex(Sex.MALE);
//        rob.setDOB(823237200000L);
//        rob.setCommunity("Alspaugh");
//        rob.setPlaceOfBirth("Harrisonburg");
//        rob.setResidence("2504 Vesson Ave, Durham, NC");
//        rob.setPhoneNumber("1234567890");
//        rob.setGuardianName("Ann Kemp Steilberg");
//
//        patientRef.setValue(rob);
    }

    private void generateVaccineData() {

        DatabaseReference vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Dose a = new Dose(vaccineRef.push().getKey());
        a.setLabels("RN", "DU");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine b = new Vaccine(vaccineRef.getKey());
        b.addDoses(a);
        b.setSortOrder(0);
        b.setName("Hepatitis Pediátrica");
        vaccineRef.setValue(b);

        Dose c = new Dose(vaccineRef.push().getKey());
        c.setLabels("RN", "DU");
        Dose d = new Dose(vaccineRef.push().getKey());
        d.setLabels("<1A", "DU");
        Dose e = new Dose(vaccineRef.push().getKey());
        e.setLabels("1-4A", "DU");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine f = new Vaccine(vaccineRef.getKey());
        f.addDoses(c,d,e);
        f.setSortOrder(1);
        f.setName("BCG");
        vaccineRef.setValue(f);

        Dose g = new Dose(vaccineRef.push().getKey());
        g.setLabels("<1A", "1a");
        Dose h = new Dose(vaccineRef.push().getKey());
        h.setLabels("<1A", "2a");
        Dose i = new Dose(vaccineRef.push().getKey());
        i.setLabels("1-4A", "1a");
        Dose j = new Dose(vaccineRef.push().getKey());
        j.setLabels("1-4A", "2a");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine k = new Vaccine(vaccineRef.getKey());
        k.addDoses(g,h,i,j);
        k.setSortOrder(2);
        k.setName("Polio (VPI)");
        vaccineRef.setValue(k);

        Dose l = new Dose(vaccineRef.push().getKey());
        l.setLabels("<1A", "2a");
        Dose m = new Dose(vaccineRef.push().getKey());
        m.setLabels("<1A", "3a");
        Dose n = new Dose(vaccineRef.push().getKey());
        n.setLabels("1-4A", "3a");
        Dose o = new Dose(vaccineRef.push().getKey());
        o.setLabels("1-4A", "R 18M");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine p = new Vaccine(vaccineRef.getKey());
        p.addDoses(l,m,n,o);
        p.setSortOrder(3);
        p.setName("Polio (VOP)");
        vaccineRef.setValue(p);

        Dose q = new Dose(vaccineRef.push().getKey());
        q.setLabels("<1A", "1a");
        Dose r = new Dose(vaccineRef.push().getKey());
        r.setLabels("<1A", "2a");
        Dose s = new Dose(vaccineRef.push().getKey());
        s.setLabels("<1A", "3a");
        Dose t = new Dose(vaccineRef.push().getKey());
        t.setLabels("1-4A", "1a");
        Dose u = new Dose(vaccineRef.push().getKey());
        u.setLabels("1-4A", "2a");
        Dose v = new Dose(vaccineRef.push().getKey());
        v.setLabels("1-4A", "3a");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine w = new Vaccine(vaccineRef.getKey());
        w.addDoses(q,r,s,t,u,v);
        w.setSortOrder(4);
        w.setName("Pentavalente");
        vaccineRef.setValue(w);

        Dose x = new Dose(vaccineRef.push().getKey());
        x.setLabels("<1A", "1a");
        Dose y = new Dose(vaccineRef.push().getKey());
        y.setLabels("<1A", "2a");
        Dose z = new Dose(vaccineRef.push().getKey());
        z.setLabels("<1A", "3a");
        Dose aa = new Dose(vaccineRef.push().getKey());
        aa.setLabels("1-4A", "DU");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine bb = new Vaccine(vaccineRef.getKey());
        bb.addDoses(x,y,z,aa);
        bb.setSortOrder(5);
        bb.setName("Neumococo");
        vaccineRef.setValue(bb);

        Dose cc = new Dose(vaccineRef.push().getKey());
        cc.setLabels("2M-1A", "1a");
        Dose dd = new Dose(vaccineRef.push().getKey());
        dd.setLabels("2M-1A", "2a");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine ee = new Vaccine(vaccineRef.getKey());
        ee.addDoses(cc,dd);
        ee.setSortOrder(6);
        ee.setName("Rotavirus");
        vaccineRef.setValue(ee);

        Dose ff = new Dose(vaccineRef.push().getKey());
        ff.setLabels("12M", "1a");
        Dose gg = new Dose(vaccineRef.push().getKey());
        gg.setLabels("18M", "2a");
        Dose hh = new Dose(vaccineRef.push().getKey());
        hh.setLabels("2-4A", "1a");
        Dose ii = new Dose(vaccineRef.push().getKey());
        ii.setLabels("2-4A", "2a");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine jj = new Vaccine(vaccineRef.getKey());
        jj.addDoses(ff,gg,hh,ii);
        jj.setSortOrder(7);
        jj.setName("SRP");
        vaccineRef.setValue(jj);

        Dose kk = new Dose(vaccineRef.push().getKey());
        kk.setLabels("18M", "1R");
        Dose ll = new Dose(vaccineRef.push().getKey());
        ll.setLabels("4A", "2R");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine mm = new Vaccine(vaccineRef.getKey());
        mm.addDoses(kk,ll);
        mm.setSortOrder(8);
        mm.setName("DPT");
        vaccineRef.setValue(mm);

        Dose oo = new Dose(vaccineRef.push().getKey());
        oo.setLabels("2M-4A", "1a");
        Dose pp = new Dose(vaccineRef.push().getKey());
        pp.setLabels("2M-4A", "2a");
        Dose qq = new Dose(vaccineRef.push().getKey());
        qq.setLabels("2M-4A", "3a");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine rr = new Vaccine(vaccineRef.getKey());
        rr.addDoses(oo,pp,qq);
        rr.setSortOrder(9);
        rr.setName("VPI GR");
        vaccineRef.setValue(rr);

        Dose ss = new Dose(vaccineRef.push().getKey());
        ss.setLabels("<1A", "2a");
        Dose tt = new Dose(vaccineRef.push().getKey());
        tt.setLabels("<1A", "3a");
        Dose uu = new Dose(vaccineRef.push().getKey());
        uu.setLabels("18M", "1R");
        Dose vv = new Dose(vaccineRef.push().getKey());
        vv.setLabels("4A", "2R");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine ww = new Vaccine(vaccineRef.getKey());
        ww.addDoses(ss,tt,uu,vv);
        ww.setSortOrder(10);
        ww.setName("DT Pediátrica");
        vaccineRef.setValue(ww);

        Dose xx = new Dose(vaccineRef.push().getKey());
        xx.setLabels("6-11M", "1a");
        Dose yy = new Dose(vaccineRef.push().getKey());
        yy.setLabels("1-4A", "1a");
        Dose zz = new Dose(vaccineRef.push().getKey());
        zz.setLabels("1-4A", "2a");
        vaccineRef = mDatabase.child(mDataTable).child(mVaccineTable).push();
        Vaccine aaa = new Vaccine(vaccineRef.getKey());
        aaa.addDoses(xx,yy,zz);
        aaa.setSortOrder(11);
        aaa.setName("Vitamina A");
        vaccineRef.setValue(aaa);


    }

//    private void generateVaccinationData() {
//        DatabaseReference vaccinationRef = mDatabase.child(mDataTable).child(mVaccinationTable).push();
//
//        Vaccination vaccination = new Vaccination(vaccinationRef.getKey(), mPatientDatabaseKey, mDoseDatabaseKey, 823237200000L);
//        vaccinationRef.setValue(vaccination);
//
//        vaccinationRef = mDatabase.child(mDataTable).child(mVaccinationTable).push();
//        Vaccination badVaccination = new Vaccination("badRef", "badPatient", "badDose", 823237200000L);
//        vaccinationRef.setValue(badVaccination);
//
//        generateDueDateData();
//    }
//
//    private void generateDueDateData() {
//        DatabaseReference dueDateRef = mDatabase.child(mDataTable).child(mDueDateTable).push();
//
//        DueDate dueDate = new DueDate(dueDateRef.getKey(), mPatientDatabaseKey, mDueVaccineDatabaseKey, 823237200000L);
//        dueDateRef.setValue(dueDate);
//
//        dueDateRef = mDatabase.child(mDataTable).child(mDueDateTable).push();
//        DueDate badDueDate = new DueDate("badRef", "badPatient", "badVaccine", 823637200000L);
//        dueDateRef.setValue(badDueDate);
//    }

}
