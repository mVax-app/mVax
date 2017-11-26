package mhealth.mvax.dashboard;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import mhealth.mvax.R;

/**
 * A simple {@link Fragment} subclass.
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
        sinovaBuilder.autoFill(day, month, year);

    }

    public void buildSINOVA2(int day, int month, int year){
        SINOVA2Builder sinova2Builder = new SINOVA2Builder(getActivity());
        sinova2Builder.autoFill(day, month, year);
    }

}
