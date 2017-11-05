package mhealth.mvax.dashboard;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mtribby on 11/5/17.
 */

public class SINOVABuilder {
    private Activity context = null;

    public SINOVABuilder(){

    }

    /**
     * Constructor for if the SINOVA Builder needs the activity for context / assets / file directory
     * @param context
     */
    public SINOVABuilder(Activity context){
        this.context = context;

    }

    public void autoFill(int day, String month, int year){
        //TODO checking for correct input

        //Insert Firebase code
        try {
            //Retrieval of the template
            AssetManager assetManager = context.getAssets();
            PdfReader reader = new PdfReader(assetManager.open("sinova.pdf"));

            //Indicating where output pdf should go
            String outPath = context.getFilesDir().toString() + "/sinova" + day + month + year + ".pdf";
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPath, true));

            //Filling in of available information
            AcroFields form = stamper.getAcroFields();

            int rowNumber = 1;
            while(rowNumber != 1){// || no more patients to add)
                buildRow(form, rowNumber);
                rowNumber++;
            }

            //Closing of tools
            stamper.setFormFlattening(false);
            stamper.close();
            reader.close();

        }
        catch(Exception e){
            Log.d("pdfError", "error in saving pdf");
        }
    }

    private void buildRow(AcroFields form, int rowNumber){
        try {
            form.setField("", "");

            //Insert Connection to Firebase data, another issue
        }
        catch(DocumentException e){
            //TODO resource file error messages
            Log.d("pdfError", "DocumentException when inputting fields");
        }
        catch(IOException io){
            Log.d("pdfError", "IOExcpetion when inputting fields");
        }
    }

}
