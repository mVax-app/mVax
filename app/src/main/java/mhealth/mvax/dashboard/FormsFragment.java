package mhealth.mvax.dashboard;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.File;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.VaccinationDummyDataGenerator;

/**
 * This form represents a simple page where users can select which form they want to export which
 * then brings up a calendar chooser which then sends off the auto-fill pdf job to be done by a builder.
 *
 * @author Matthew Tribby
 * November, 2017
 */
public class FormsFragment extends android.support.v4.app.Fragment {
    private LayoutInflater inflater;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    public FormsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forms2, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getLayoutInflater();

        Button sinova = (Button) view.findViewById(R.id.SINOVA);
        Button sinova2 = (Button) view.findViewById(R.id.SINOVA2);
        Button linv = (Button)view.findViewById(R.id.LINV);

        sinova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinovaClicked();
            }
        });

        sinova2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinova2Clicked();
            }
        });

        linv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linvClicked();
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    private void sinovaClicked(){
        //TODO REMOVE AFTER OTHER SIDE HOOKED UP
        VaccinationDummyDataGenerator generator = new VaccinationDummyDataGenerator();
        generator.generateRecord();


        final AlertDialog.Builder builder = createBasicDateChooseModal();

        final View dialogView = inflater.inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);

        builder.setPositiveButton(getResources().getString(R.string.export), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePicker.getYear();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();

                buildSINOVA(day, month, year);
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    private void sinova2Clicked(){
        AlertDialog.Builder builder = createBasicDateChooseModal();

        final View dialogView = inflater.inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);

        builder.setPositiveButton(getResources().getString(R.string.export), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePicker.getYear();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();

                buildSINOVA2(day, month, year);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void linvClicked(){
        AlertDialog.Builder builder = createBasicDateChooseModal();

        final View dialogView = inflater.inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);

        builder.setPositiveButton(getResources().getString(R.string.export), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePicker.getYear();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();

                //need to create LINV builder
                //buildLINV(day, month, year);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private AlertDialog.Builder createBasicDateChooseModal(){
        //https://stackoverflow.com/questions/18371883/how-to-create-modal-dialog-box-in-android
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.modal_choose_date));

        return builder;
    }

    public void buildSINOVA(int day, int month, int year){
        SINOVABuilder sinovaBuilder = new SINOVABuilder(getActivity());
        String fileName = sinovaBuilder.autoFill(day, month, year);

        File pdf = new File(fileName);
        String title = getResources().getString(R.string.sinova) + " " + getResources().getString(R.string.email_header_insert) + " " + day + "/" + month + "/" + year;
        sendFile(title, "", pdf);

    }

    public void buildSINOVA2(int day, int month, int year){
        SINOVA2Builder sinova2Builder = new SINOVA2Builder(getActivity());
        String fileName = sinova2Builder.autoFill(day, month, year);

        File pdf = new File(fileName);
        String title = getResources().getString(R.string.sinova2) + " " + getResources().getString(R.string.email_header_insert) + " " + day + "/" + month + "/" + year;
        sendFile(title, "", pdf);
    }


    //Sends email with file as an attachment
    private void sendFile(String title, String body, File pdf){
        //Code in this method with help from Stack Overflow: https://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_SUBJECT, title);
        email.putExtra(Intent.EXTRA_TEXT   , body);

        //Info for how to send email with an attachment was from: https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed/38858040#38858040
        Uri path = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".dashboard.GenericFileProvider", pdf);

        email .putExtra(Intent.EXTRA_STREAM, path);
        try {
            startActivityForResult(Intent.createChooser(email, getResources().getString(R.string.send_email)), 2);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_LONG).show();
        }
    }

}
