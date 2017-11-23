package mhealth.mvax.records.views.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Sex;

/**
 * @author Robert Steilberg
 */

public class SexDetail extends Detail<Sex> {

    public SexDetail(String label, String hint, Sex sex, Context context) {
        super(label, hint, sex, context);
    }

    @Override
    public void updateStringValue(Sex value) {
        if (value == Sex.MALE) {
            setStringValue(getContext().getString(R.string.male_enum));
        } else if (value == Sex.FEMALE) {
            setStringValue(getContext().getString(R.string.female_enum));
        } else {
            setStringValue("");
        }
    }

    @Override
    public void valueViewListener(final EditText valueView) {

        final Context context = getContext();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.modal_title_choose_sex));

        final View dialogView = LayoutInflater.from(context).inflate(R.layout.modal_choose_sex, null);
        builder.setView(dialogView);

        final RadioGroup radioGroup = dialogView.findViewById(R.id.choose_sex_radio_group);
        if (getValue() == Sex.MALE) {
            radioGroup.check(R.id.radiobutton_male);
        } else if (getValue() == Sex.FEMALE) {
            radioGroup.check(R.id.radiobutton_female);
        }


        builder.setPositiveButton(context.getString(R.string.modal_positive_choose_sex), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int chosenButtonID = radioGroup.getCheckedRadioButtonId();
                int chosenIdx = radioGroup.indexOfChild(radioGroup.findViewById(chosenButtonID));
                switch (chosenIdx) {
                    case 0:
                        updateValue(Sex.MALE);
                        break;
                    case 1:
                        updateValue(Sex.FEMALE);
                        break;
                }
                valueView.setText(getStringValue());
            }
        });
        builder.setNegativeButton(context.getString(R.string.modal_negative_choose_sex), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(context.getString(R.string.modal_neutral_choose_sex), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                updateValue(null);
                valueView.setText(getStringValue());
            }
        });

        builder.show();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false);
    }

}
