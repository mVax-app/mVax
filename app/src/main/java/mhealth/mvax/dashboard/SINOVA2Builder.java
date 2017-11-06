package mhealth.mvax.dashboard;

import android.app.Activity;
import android.content.res.AssetManager;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;

import mhealth.mvax.R;

/**
 * Created by mtribby on 11/6/17.
 */

public class SINOVA2Builder {
    public static final int maxRows = 25;
    private Activity context = null;

    /**
     * Constructor for if the SINOVA Builder needs the activity for context / assets / file directory
     * @param context
     */
    public SINOVA2Builder(Activity context){
        this.context = context;

    }

    public void autoFill(int day, String month, int year){
        //TODO checking for correct input

        //Insert Firebase code
        try {
            //Retrieval of the template
            AssetManager assetManager = context.getAssets();
            PdfReader reader = new PdfReader(assetManager.open(context.getResources().getString(R.string.sinova_2_file_name)));

            //Indicating where output pdf should go
            String outPath = context.getFilesDir().toString() + context.getResources().getString(R.string.destination_subfolder_sinova_2)
                    + day + month + year + context.getResources().getString(R.string.destination_file_extension);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPath, true));

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
    }

    private void buildRow(AcroFields form, int rowNumber){
        try {

            //TODO connect to firebase data now that it's mapped, remove dummy data
            form.setField(context.getResources().getString(R.string.sinova2_complete_name) + rowNumber, "FirstName MiddleName LastName1");
            form.setField(context.getResources().getString(R.string.sinova2_origin) + rowNumber, "some place");
            form.setField(context.getResources().getString(R.string.HEPB_GER_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.HEPB_GER_2A)+ rowNumber, "");
            form.setField(context.getResources().getString(R.string.HEPB_GER_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.FA_1A_DU) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.SR_5A_DA) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.TD_11A_R) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.TD_EM_1A)+ rowNumber, "X");
            form.setField(context.getResources().getString(R.string.TD_EM_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.TD_EM_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.TD_EM_4A) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.TD_EM_5A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.OG_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.OG_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.OG_3A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.OG_R) + rowNumber, "X");
            form.setField(context.getResources().getString(R.string.VPH_11A_1A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VPH_11A_2A) + rowNumber, "");
            form.setField(context.getResources().getString(R.string.VITA_PU_DU) + rowNumber, "X");

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
