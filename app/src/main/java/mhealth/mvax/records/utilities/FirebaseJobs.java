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

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;

/**
 * @author Robert Steilberg
 *         <p>
 *         Utilities for performing common Firebase actions
 */
public class FirebaseJobs {

    public static void deleteRecord(Patient patient) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = "mVax-data";
        String patientTable = "patients";
        String guardianTable = "guardians";
        db.child(masterTable).child(patientTable).child(patient.getDatabaseKey()).setValue(null);
        db.child(masterTable).child(guardianTable).child(patient.getGuardianDatabaseKey()).setValue(null);

        // todo delete all vaccinations
    }








    public static void saveRecord(Patient patient, Guardian guardian) {
//        mPatientDatabaseRef.child(patient.getDatabaseKey()).setValue(patient);
//        mGuardianDatabaseRef.child(guardian.getDatabaseKey()).setValue(guardian);
    }
}
