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

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Sex;
import mhealth.mvax.model.record.VaccinationRecord;


public class SINOVABuilder {
    public static final int maxRows = 15;
    private Activity context = null;
    private DatabaseReference users;
    private ArrayList<VaccinationRecord> records;
    private PdfStamper stamper;
    private AcroFields form;
    private PdfReader reader;

    /**
     * Constructor for if the SINOVA Builder needs the activity for context / assets / file directory
     * @param context
     */
    public SINOVABuilder(Activity context){
        this.context = context;
        users = FirebaseDatabase.getInstance().getReference()
                .child(context.getResources().getString(R.string.masterTable))
                .child(context.getResources().getString(R.string.recordTable));

    }

    public String autoFill(final int day, int month, int year) {
        final String fDay = String.valueOf(day);
        final String fMonth = String.valueOf(month+1);
        final String fYear = String.valueOf(year);
        final String date = fYear + fMonth + fDay;

        Log.e("WORKS", date);

        String extension = context.getResources().getString(R.string.sinova_extension) + day + month + year + context.getResources().getString(R.string.destination_file_extension);
        final File file = new File(context.getExternalFilesDir(null), extension);

        //Reset instance variable for another fill
        records = new ArrayList<>();

        //FIREBASE RECORD FETCHING:
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(context.getResources().getString(R.string.masterTable)).child(context.getResources().getString(R.string.vaccinationsTable));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(date)) {
                    DataSnapshot child = dataSnapshot.child(date);

                    for (DataSnapshot data : child.getChildren()) {
                        records.add(data.getValue(VaccinationRecord.class));
                        Log.e("WORKS", "ADDS");
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

    private void fillInForm(File file, String day, String month, String year){

            try {

                //Retrieval of the template
                AssetManager assetManager = context.getAssets();
                reader = new PdfReader(assetManager.open(context.getResources().getString(R.string.sinova_1_file_name)));

                stamper = new PdfStamper(reader, new FileOutputStream(file));
                //Filling in of available information
                form = stamper.getAcroFields();

                //Insert the header info on the SINOVA
                form.setField(context.getResources().getString(R.string.establishment), context.getResources().getString(R.string.form_clinic_name));
                form.setField(context.getResources().getString(R.string.name_of_responsible_person), context.getResources().getString(R.string.form_vaccinator_name));
                form.setField(context.getResources().getString(R.string.department), context.getResources().getString(R.string.form_department_name));
                form.setField(context.getResources().getString(R.string.code), "2354");
                form.setField(context.getResources().getString(R.string.municipality), context.getResources().getString(R.string.form_city_name));
                form.setField(context.getResources().getString(R.string.date_day), day);
                form.setField(context.getResources().getString(R.string.date_month), month);
                form.setField(context.getResources().getString(R.string.date_year), year);
                form.setField(context.getResources().getString(R.string.location_place), context.getResources().getString(R.string.form_address));


                buildRow(1);
            } catch (Exception e) {
                // Log.d("pdfError", "error in saving pdf");
                //TODO REMOVE
                e.printStackTrace();
            }

        }


    private void buildRow(int row){
        final int rowNumber = row;
        final String uid = records.get(row - 1).getPatientUID();

            //PATIENT SPECIFIC DATA
            users.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Record record = dataSnapshot.getValue(Record.class);
                    Log.e("WORKS", record.getCommunity());

                    try {
                        form.setField(context.getResources().getString(R.string.temporary_number_mothers_id) + rowNumber, record.getParentId());
                        form.setField(context.getResources().getString(R.string.number_of_children) + rowNumber, record.getNumDependents());
                        form.setField(context.getResources().getString(R.string.child_national_reg_number) + rowNumber, record.getId());
                        form.setField(context.getResources().getString(R.string.child_name)+ rowNumber, record.getFullName());
                        form.setField(context.getResources().getString(R.string.date_of_birth) + rowNumber, String.valueOf(record.getDOB()));
                        if(record.getSex() == Sex.MALE) {
                            form.setField(context.getResources().getString(R.string.sex_male) + rowNumber, "X");
                        }
                        else {
                            form.setField(context.getResources().getString(R.string.sex_female) + rowNumber, "X");
                        }
                        form.setField(context.getResources().getString(R.string.birth_department) + rowNumber, record.getPlaceOfBirth());
                        form.setField(context.getResources().getString(R.string.birth_municipal)+ rowNumber, record.getPlaceOfBirth());
                        form.setField(context.getResources().getString(R.string.residence_department) + rowNumber,  context.getResources().getString(R.string.unknown));
                        form.setField(context.getResources().getString(R.string.residence_municipal) + rowNumber, context.getResources().getString(R.string.na));
                        form.setField(context.getResources().getString(R.string.residence_town) + rowNumber, record.getCommunity());
                        form.setField(context.getResources().getString(R.string.residence_address) + rowNumber, record.getParentAddress());
                        form.setField(context.getResources().getString(R.string.cell_number) + rowNumber, record.getParentPhone());
                        form.setField(context.getResources().getString(R.string.population_group) + rowNumber, context.getResources().getString(R.string.unknown));
                        form.setField(context.getResources().getString(R.string.full_name_guardian) + rowNumber, record.getParentFullName());

                        form.setField(context.getResources().getString(context.getResources().getIdentifier(records.get(rowNumber-1).getType(), "string", context.getPackageName())) + rowNumber, "X");

                        if(rowNumber + 1 > records.size()){
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
        Log.e("WORKS", "CLOSING!");
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


}
