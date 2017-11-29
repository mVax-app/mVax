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
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.VaccinationRecord;

/**
 * Created by mtribby on 11/6/17.
 */

public class SINOVA2Builder {
    public static final int maxRows = 25;
    private Activity context = null;

    private ArrayList<VaccinationRecord> records;
    private PdfStamper stamper;
    private AcroFields form;
    private PdfReader reader;
    private List<String> sinova2_vaccines;


    /**
     * Constructor for if the SINOVA Builder needs the activity for context / assets / file directory
     * @param context
     */
    public SINOVA2Builder(Activity context){
        this.context = context;
        sinova2_vaccines = Arrays.asList(context.getResources().getStringArray(R.array.sinova2_vaccines));
    }

    public String autoFill(int day, int month, int year) {
        final String fDay = String.valueOf(day);
        final String fMonth = String.valueOf(month + 1);
        final String fYear = String.valueOf(year);
        final String date = fYear + fMonth + fDay;

        String extension = context.getResources().getString(R.string.sinova2_extension) + day + month + year + context.getResources().getString(R.string.destination_file_extension);
        final File file = new File(context.getExternalFilesDir(null), extension);

        Log.e("WORKS", date);
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
                        VaccinationRecord record = data.getValue(VaccinationRecord.class);
                        if(sinova2_vaccines.contains(record.getType())) {
                            records.add(data.getValue(VaccinationRecord.class));
                        }
                    }
                    fillInForm(file, fDay, fMonth, fYear);
                }
                else{
                    Log.e("WORKS", "noChildren");
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
            reader = new PdfReader(assetManager.open(context.getResources().getString(R.string.sinova_2_file_name)));

            stamper = new PdfStamper(reader, new FileOutputStream(file));
            //Filling in of available information
            form = stamper.getAcroFields();

            //Insert the header info on the SINOVA 2
            form.setField(context.getResources().getString(R.string.establishment),context.getResources().getString(R.string.form_clinic_name));
            form.setField(context.getResources().getString(R.string.name_of_responsible_person), context.getResources().getString(R.string.form_vaccinator_name));
            form.setField(context.getResources().getString(R.string.department), context.getResources().getString(R.string.form_department_name));
            //TODO FIGURE OUT WHAT CODE IS
            form.setField(context.getResources().getString(R.string.code),"2543");
            form.setField(context.getResources().getString(R.string.municipality), context.getResources().getString(R.string.form_city_name));
            form.setField(context.getResources().getString(R.string.date_day), day);
            form.setField(context.getResources().getString(R.string.date_month), month);
            form.setField(context.getResources().getString(R.string.date_year), year);
            form.setField(context.getResources().getString(R.string.location_place), context.getResources().getString(R.string.form_address));


            //Starts at row 1
            if(records.size() != 0) {
                buildRow(1);
            }
            else{
                closePDF();
            }

        }
        catch(Exception e){
            // Log.d("pdfError", "error in saving pdf");
            e.printStackTrace();
        }

    }

    private void buildRow(int row){
        final int rowNumber = row;
        final String uid = records.get(row - 1).getPatientUID();

        DatabaseReference patients = FirebaseDatabase.getInstance().getReference()
                .child(context.getResources().getString(R.string.masterTable))
                .child(context.getResources().getString(R.string.recordTable));

        patients.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Record record = dataSnapshot.getValue(Record.class);
                try {
                    form.setField(context.getResources().getString(R.string.sinova2_complete_name) + rowNumber, record.getFullName());
                    form.setField(context.getResources().getString(R.string.sinova2_origin) + rowNumber, record.getPlaceOfBirth());

                    //VACCINE SPECIFIC
                    form.setField(context.getResources().getString(context.getResources().getIdentifier(records.get(rowNumber-1).getType(), "string", context.getPackageName())) + rowNumber, "X");

                    if(rowNumber + 1 > records.size() || rowNumber == maxRows){
                        closePDF();
                    }
                    else{
                        buildRow(rowNumber + 1);
                    }
                }
                catch(DocumentException e){
                    //TODO resource file error messages
                    Log.e("pdfError", "DocumentException when inputting fields");
                }
                catch(IOException io){
                    //Log.d("pdfError", "IOExcpetion when inputting fields");
                    io.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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

}
