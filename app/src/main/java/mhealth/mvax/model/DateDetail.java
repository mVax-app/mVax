package mhealth.mvax.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.LocalDate;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 */

public class DateDetail extends Detail<Long> {

    DateDetail(String label, String hint, Long value, Context context) {
        super(label, hint, value, context);
    }

    @Override
    public void updateStringValue(Long value) {
        RecordDateFormat dateFormat = new RecordDateFormat(getContext().getString(R.string.date_format));
        setStringValue(dateFormat.getString(value));
    }

    @Override
    public void valueViewListener(final EditText valueView) {
        final Context context = getContext();

        // init date picker dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.modal_title_choose_date));
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        // set DatePicker date if current value is not null
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        if (getValue() != null) {
            LocalDate date = new LocalDate(getValue());
            datePicker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }

        builder.setPositiveButton(context.getString(R.string.modal_positive_choose_date), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                long millis = new LocalDate(year, month, day).toDate().getTime();

                updateValue(millis);
                valueView.setText(getStringValue());
            }
        });
        builder.setNegativeButton(context.getString(R.string.modal_negative_choose_date), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(context.getString(R.string.modal_neutral_choose_date), new DialogInterface.OnClickListener() {
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
