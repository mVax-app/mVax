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
package mhealth.mvax.dashboard;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Sex;

/**
 * The goal of this class is auto-fill in the SINOVA1.pdf which can be found in
 * app_vaccination > app > src > main > assets
 * SINOVA is a form from the Honduras Ministry of Health that the vaccinators need to fill in
 * This builder will auto-fill and save to external memory the filled in PDF
 *
 * Dependencies / Assumptions:
 * the SINOVA1.pdf needs to be in the correct location and have the correct name
 * Firebase integration is used because the querying of the data is done in this class
 * The Firebase integration should be eventually refactored out to make the design more flexible
 *
 * @author Matthew Tribby
 * November, 2017
 */
public class SINOVABuilder {
    public static final int maxRows = 15;
    private Activity mContext = null;

    private ArrayList<Vaccination> records;
    private Map<String, String> possibleDoses;
    private PdfStamper stamper;
    private AcroFields form;
    private PdfReader reader;
    private List<String> sinova_vaccines;

    /**
     * Constructor for if the SINOVA Builder needs the activity for mContext / assets / file directory
     * @param mContext
     */
    public SINOVABuilder(Activity mContext){
        this.mContext = mContext;
        sinova_vaccines = Arrays.asList(mContext.getResources().getStringArray(R.array.sinova_vaccines));

        readDosesFromDB();
    }

    /**
     * This method is the full functionality for the SINOVA builder. Given a specific day of the year,
     * an autofilled form will be created given the records in the Firebase database
     * @param day
     * @param month
     * @param year
     * @return the path of the form. This can be used to retrieve it from external memory if wanted.
     */
    public String autoFill(int day, int month, int year) {
        final String fDay = String.valueOf(day);
        final String fMonth = String.valueOf(month+1);
        final String fYear = String.valueOf(year);
        final String date = fYear + fMonth + fDay;

        String extension = mContext.getResources().getString(R.string.sinova_extension) + day + month + year + mContext.getResources().getString(R.string.destination_file_extension);
        final File file = new File(mContext.getExternalFilesDir(null), extension);

        //Reset instance variable for another fill
        records = new ArrayList<>();

        //FIREBASE RECORD FETCHING:
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable));
        DatabaseReference vaxRef = dbRef.child(mContext.getResources().getString(R.string.vaccinationsTable));

        //The following listener queries for all records of the day and adds them to instance variable, records
        vaxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(date)) {
                    DataSnapshot child = dataSnapshot.child(date);

                    for (DataSnapshot data : child.getChildren()) {
                        Vaccination vacc = data.getValue(Vaccination.class);
                        if(possibleDoses.containsKey(vacc.getDoseDatabaseKey())) {
                            records.add(vacc);
                        }
                    }

                    fillInForm(file, fDay, fMonth, fYear);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "DatabaseError");
            }
        });
        return file.getAbsolutePath();
    }

    //This private method is a helper method which begins the process of actually filling in the form
    //after the data was originally retrieved in autoFill()
    private void fillInForm(File file, String day, String month, String year){

            try {

                //Retrieval of the template
                AssetManager assetManager = mContext.getAssets();
                reader = new PdfReader(assetManager.open(mContext.getResources().getString(R.string.sinova_1_file_name)));

                stamper = new PdfStamper(reader, new FileOutputStream(file));
                //Filling in of available information
                form = stamper.getAcroFields();

                //Insert the header info on the SINOVA
                form.setField(mContext.getResources().getString(R.string.establishment), mContext.getResources().getString(R.string.form_clinic_name));
                form.setField(mContext.getResources().getString(R.string.name_of_responsible_person), mContext.getResources().getString(R.string.form_vaccinator_name));
                form.setField(mContext.getResources().getString(R.string.department), mContext.getResources().getString(R.string.form_department_name));
                form.setField(mContext.getResources().getString(R.string.code), "2354");
                form.setField(mContext.getResources().getString(R.string.municipality), mContext.getResources().getString(R.string.form_city_name));
                form.setField(mContext.getResources().getString(R.string.date_day), day);
                form.setField(mContext.getResources().getString(R.string.date_month), month);
                form.setField(mContext.getResources().getString(R.string.date_year), year);
                form.setField(mContext.getResources().getString(R.string.location_place), mContext.getResources().getString(R.string.form_address));


                //Starts at row 1
                if(records.size() != 0) {
                    buildRow(1);
                }
                else{
                    closePDF();
                }

            } catch (Exception e) {
                Log.d("pdfError", "error in saving pdf");
            }

        }

    //This helper method is where the actual filling in of information into the AcroForm occurs
    private void buildRow(int row){
        final int rowNumber = row;
        final String dbKey = records.get(row - 1).getPatientDatabaseKey();

        DatabaseReference patients = FirebaseDatabase.getInstance().getReference()
                .child(mContext.getResources().getString(R.string.dataTable))
                .child(mContext.getResources().getString(R.string.patientTable));

            //PATIENT SPECIFIC DATA
            patients.child(dbKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Patient record = dataSnapshot.getValue(Patient.class);

                    try {
                        //Commented out are not currently supported by DB
                        //form.setField(mContext.getResources().getString(R.string.temporary_number_mothers_id) + rowNumber, record.getParentId());
                        //form.setField(mContext.getResources().getString(R.string.number_of_children) + rowNumber, record.getNumDependents());
                        form.setField(mContext.getResources().getString(R.string.child_national_reg_number) + rowNumber, record.getMedicalId());
                        form.setField(mContext.getResources().getString(R.string.child_name)+ rowNumber, record.getName());
                        form.setField(mContext.getResources().getString(R.string.date_of_birth) + rowNumber, String.valueOf(record.getDOB()));
                        if(record.getSex() == Sex.MALE) {
                            form.setField(mContext.getResources().getString(R.string.sex_male) + rowNumber, "X");
                        }
                        else {
                            form.setField(mContext.getResources().getString(R.string.sex_female) + rowNumber, "X");
                        }
                        form.setField(mContext.getResources().getString(R.string.birth_department) + rowNumber, record.getPlaceOfBirth());
                        form.setField(mContext.getResources().getString(R.string.birth_municipal)+ rowNumber, record.getPlaceOfBirth());
                        form.setField(mContext.getResources().getString(R.string.residence_department) + rowNumber,  mContext.getResources().getString(R.string.unknown));
                        form.setField(mContext.getResources().getString(R.string.residence_municipal) + rowNumber, mContext.getResources().getString(R.string.na));
                        form.setField(mContext.getResources().getString(R.string.residence_town) + rowNumber, record.getCommunity());
                        form.setField(mContext.getResources().getString(R.string.residence_address) + rowNumber, record.getResidence());
                        form.setField(mContext.getResources().getString(R.string.cell_number) + rowNumber, record.getPhoneNumber());
                        form.setField(mContext.getResources().getString(R.string.population_group) + rowNumber, mContext.getResources().getString(R.string.unknown));
                        form.setField(mContext.getResources().getString(R.string.full_name_guardian) + rowNumber, record.getGuardianName());

                        form.setField(mContext.getResources().getString(mContext.getResources().getIdentifier(possibleDoses.get(records.get(rowNumber-1).getDoseDatabaseKey()), "string", mContext.getPackageName())) + rowNumber, "X");

                        if(rowNumber + 1 > records.size() || rowNumber == maxRows){
                            closePDF();
                        }
                        else{
                            buildRow(rowNumber + 1);
                        }
                    }
                    catch(DocumentException e){
                        Log.e("WORKS", "DocumentException");
                    }
                    catch(IOException io){
                        Log.e("WORKS", "IOException");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    private void closePDF(){
        Log.d("WORKS", "CLOSING!");
        //Closing of tools
        try {
            stamper.setFormFlattening(false);
            stamper.close();
            reader.close();
        }
        catch(DocumentException de){
            Log.e("WORKS", "DocumentException");
        }
        catch (IOException io){
            Log.e("WORKS", "IOException");
        }


    }

    private void readDosesFromDB(){
        possibleDoses = new HashMap<>();

        DatabaseReference vaccineRef = FirebaseDatabase.getInstance().getReference().child(mContext.getResources().getString(R.string.dataTable))
                .child(mContext.getResources().getString(R.string.vaccineTable));
        vaccineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Vaccine vacc = snapshot.getValue(Vaccine.class);
                    for(Dose dose : vacc.getDoses()){
                        if(sinova_vaccines.contains(dose.getFormCode())) {
                            possibleDoses.put(dose.getDatabaseKey(), dose.getFormCode());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
