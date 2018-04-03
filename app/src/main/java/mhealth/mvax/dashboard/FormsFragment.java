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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import mhealth.mvax.R;

/**
 * This form represents a simple page where users can select which form they want to export which
 * then brings up a calendar chooser which then sends off the auto-fill pdf job to be done by a builder.
 *
 * @author Matthew Tribby
 * November, 2017
 */
public class FormsFragment extends android.support.v4.app.Fragment {
    private LayoutInflater inflater;

    public static FormsFragment newInstance() {
        return new FormsFragment();
    }

    public FormsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forms, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getLayoutInflater();

        Button sinovaAdolescent = (Button) view.findViewById(R.id.SINOVA_Adolescent);
        Button sinova2Adolescent = (Button) view.findViewById(R.id.SINOVA2_Adolescent);
        Button sinovaAdult = (Button) view.findViewById(R.id.SINOVA_Adult);
        Button sinova2Adult = (Button) view.findViewById(R.id.SINOVA2_Adult);
        Button linv = (Button)view.findViewById(R.id.LINV);
        Button pdfConfig = (Button) view.findViewById(R.id.pdf_config);
        ImageView info = (ImageView) view.findViewById(R.id.info);

        sinovaAdolescent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinovaClicked();
            }
        });
        sinovaAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinovaClicked();
            }
        });

        sinova2Adolescent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinova2Clicked();
            }
        });
        sinova2Adult.setOnClickListener(new View.OnClickListener() {
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
        pdfConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConfigModal();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayHelpModal();
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    private void sinovaClicked(){

        final AlertDialog.Builder builder = createBasicDateChooseModal();

        final View dialogView = inflater.inflate(R.layout.modal_date_picker, null);
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

        final View dialogView = inflater.inflate(R.layout.modal_date_picker, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);



        builder.setPositiveButton(getResources().getString(R.string.export), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();

                buildSINOVA2(month, year);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    private void linvClicked(){
        AlertDialog.Builder builder = createBasicDateChooseModal();

        final View dialogView = inflater.inflate(R.layout.modal_date_picker, null);
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

    private void buildSINOVA(int day, int month, int year){
        SINOVABuilder sinovaBuilder = new SINOVABuilder(getActivity());
        String filePath = sinovaBuilder.autoFill(day, month, year);

        String title = getResources().getString(R.string.sinova) + " " + getResources().getString(R.string.email_header_insert) + " " + day+"/"+month+"/"+year;
        sendFile(title, new File(filePath));

    }

    private void buildSINOVA2(int month, int year){
        SINOVA2Builder sinova2Builder = new SINOVA2Builder(getActivity());
        String fileName = sinova2Builder.autoFill(month, year);

        File pdf = new File(fileName);
        String title = getResources().getString(R.string.sinova2) + " " + getResources().getString(R.string.email_header_insert) + " " + month + "/" + year;
        sendFile(title,  pdf);
    }

    //Sends email with file as an attachment
    private void sendFile(String title, File pdf){
        //Code in this method with help from Stack Overflow: https://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_SUBJECT, title);
        email.putExtra(Intent.EXTRA_TEXT   , "");

        //Info for how to send email with an attachment was from: https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed/38858040#38858040
        Uri path = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".dashboard.GenericFileProvider", pdf);

        email .putExtra(Intent.EXTRA_STREAM, path);
        try {
            startActivityForResult(Intent.createChooser(email, getResources().getString(R.string.send_email)), 2);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_LONG).show();
        }
    }

    private void displayHelpModal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View dialogView = inflater.inflate(R.layout.modal_form_info, null);
        builder.setView(dialogView);

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    private void displayConfigModal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View dialogView = inflater.inflate(R.layout.modal_pdf_config, null);
        builder.setView(dialogView);

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

}
