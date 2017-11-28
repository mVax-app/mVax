package mhealth.mvax.dashboard;

import android.app.Activity;
import android.content.res.AssetManager;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import mhealth.mvax.R;


public class SINOVABuilder {
    public static final int maxRows = 15;
    private Activity context = null;

    /**
     * Constructor for if the SINOVA Builder needs the activity for context / assets / file directory
     * @param context
     */
    public SINOVABuilder(Activity context){
        this.context = context;

    }

    public String autoFill(int day, int month, int year){
        //TODO checking for correct input
        //Indicating where output pdf should go
        //Commented out is for internal storage, second is for external storage
//        String outPath = context.getFilesDir().toString() + context.getResources().getString(R.string.destination_subfolder_sinova_1)
//                + day + month + year + context.getResources().getString(R.string.destination_file_extension);

        String extension = context.getResources().getString(R.string.sinova_extension) + day + month + year + context.getResources().getString(R.string.destination_file_extension);
        File file = new File(context.getExternalFilesDir(null), extension);


        //Insert Firebase code
        try {
            //Retrieval of the template
            AssetManager assetManager = context.getAssets();
            PdfReader reader = new PdfReader(assetManager.open(context.getResources().getString(R.string.sinova_1_file_name)));

            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(file));
            //Filling in of available information
            AcroFields form = stamper.getAcroFields();

            //Insert the header info on the SINOVA
            //TODO Connection to Firebase data, another issue
            form.setField(context.getResources().getString(R.string.establishment),"Clinica Esperanza");
            form.setField(context.getResources().getString(R.string.name_of_responsible_person), "Marlin LastName");
            form.setField(context.getResources().getString(R.string.department), "");
            form.setField(context.getResources().getString(R.string.code),"");
            form.setField(context.getResources().getString(R.string.municipality), "");
            form.setField(context.getResources().getString(R.string.date_day), "5");
            form.setField(context.getResources().getString(R.string.date_month), "May");
            form.setField(context.getResources().getString(R.string.date_year), "2017");
            form.setField(context.getResources().getString(R.string.location_place), "Roatan, Honduras");


            //Inserting the rows of data
            int rowNumber = 1;
            while(rowNumber <= maxRows){// || no more patients to add)
                buildRow(form, rowNumber);
                rowNumber++;
            }

            //Closing of tools
            stamper.setFormFlattening(false);
            stamper.close();
            reader.close();

        }
        catch(Exception e){
           // Log.d("pdfError", "error in saving pdf");
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void buildRow(AcroFields form, int rowNumber){
        try {

            //TODO connect to firebase data now that it's mapped, remove dummy data
            form.setField(context.getResources().getString(R.string.temporary_number_mothers_id) + rowNumber, "532732345");
            form.setField(context.getResources().getString(R.string.number_of_children) + rowNumber, "1");
            form.setField(context.getResources().getString(R.string.child_national_reg_number) + rowNumber, "823488682");
            form.setField(context.getResources().getString(R.string.child_name)+ rowNumber, "FirstName MiddleName LastName1 LastName2");
            form.setField(context.getResources().getString(R.string.date_of_birth) + rowNumber, "05/05/17");
            form.setField(context.getResources().getString(R.string.sex_male) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.sex_female) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.birth_department) + rowNumber, "some department");
            form.setField(context.getResources().getString(R.string.birth_municipal)+ rowNumber, "some municipality");
            form.setField(context.getResources().getString(R.string.residence_department) + rowNumber, "some residence");
            form.setField(context.getResources().getString(R.string.residence_municipal) + rowNumber, "residence municipality");
            form.setField(context.getResources().getString(R.string.residence_town) + rowNumber, "residence town");
            form.setField(context.getResources().getString(R.string.residence_address) + rowNumber, "Next to the red truck");
            form.setField(context.getResources().getString(R.string.cell_number) + rowNumber, "954-629-1345");
            form.setField(context.getResources().getString(R.string.population_group) + rowNumber, "Other");
            form.setField(context.getResources().getString(R.string.full_name_guardian) + rowNumber, "Alison MiddleName LastName1 LastName2");

            //vaccinations
            form.setField(context.getResources().getString(R.string.HepB_DU) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.BCG_RN_DU) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.BCG_1A_DU) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.BCG_14A_DU) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.POLIO_2M_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.POLIO_1ANO_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.POLIO_1ANO_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.POLIO_14ANO_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.POLIO_14ANO_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.POLIO_14ANO_3A) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.POLIO_R) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.PENTA_1ANO_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.PENTA_1ANO_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.PENTA_1ANO_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.PENTA_14ANO_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.PENTA_14ANO_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.PENTA_14ANO_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.NEUM_1ANO_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.NEUM_1ANO_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.NEUM_1ANO_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.NEUM_14ANO_DU) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.ROTA_2M1A_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.ROTA_2M1A_2A) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.SRP_1223M_DU) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.SRP_24ANO_DU) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.DPT_18M_1R) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.DPT_4A_2R) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VPI_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VPI_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VPI_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.DT_PED_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.DT_PED_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.DT_PED_1R) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.DT_PED_2R) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.VITA_611M_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VITA_14A_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VITA_14A_2A) + rowNumber, "");
        }
        catch(DocumentException e){
            //TODO resource file error messages
            //Log.d("pdfError", "DocumentException when inputting fields");
            //TODO remove the error printing
            e.printStackTrace();
        }
        catch(IOException io){
            //Log.d("pdfError", "IOExcpetion when inputting fields");
            io.printStackTrace();
        }
    }

}
