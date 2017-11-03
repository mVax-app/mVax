package mhealth.mvax.dashboard;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;

import mhealth.mvax.R;

public class DashboardFragment extends Fragment {
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = (Button)view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testPDF();
            }
        });

    }

    public void testPDF(){

        try {
            AssetManager assetManager = getActivity().getAssets();
            PdfReader reader = new PdfReader(assetManager.open("sample.pdf"));


            String outPath = getActivity().getFilesDir().toString() + "/sampleEdited.pdf";
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPath, true));
            AcroFields form = stamper.getAcroFields();

            String testField = "Department";
            String testValue = "Test succeeds";

            form.setField(testField, testValue);
            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();

        }
        catch(Exception e){
            Log.d("pdfError", "error in saving pdf");
        }
    }


}