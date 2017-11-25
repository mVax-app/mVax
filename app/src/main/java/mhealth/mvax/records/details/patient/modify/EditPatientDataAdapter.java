package mhealth.mvax.records.details.patient.modify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mhealth.mvax.R;
import mhealth.mvax.records.views.detail.Detail;
import mhealth.mvax.records.details.patient.PatientDataAdapter;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter that allows user interaction with fields to edit data;
 *         changes automatically trigger listeners that save data to the
 *         database
 */

public class EditPatientDataAdapter extends PatientDataAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private LayoutInflater mInflater;


    //================================================================================
    // Constructors
    //================================================================================

    EditPatientDataAdapter(Context context, LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
        super(sectionedData);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View getView(final int position, View rowView, ViewGroup viewGroup) {
        final ViewHolder holder;
        int rowType = getItemViewType(position);

        if (rowView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_SECTION:
                    rowView = mInflater.inflate(R.layout.list_item_record_detail_section, viewGroup, false);
                    holder.fieldView = rowView.findViewById(R.id.record_detail_separator);
                    break;
                case TYPE_FIELD:
                    rowView = mInflater.inflate(R.layout.list_item_record_detail, viewGroup, false);
                    holder.fieldView = rowView.findViewById(R.id.textview_field);
                    holder.valueView = rowView.findViewById(R.id.textview_value);
                    break;
            }
            assert rowView != null;
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        // populate row with data
        if (rowType == TYPE_SECTION) {
            holder.fieldView.setText(mHeaders.get(position));
        } else if (rowType == TYPE_FIELD) {

            // set values
            holder.fieldView.setText(mDataSource.get(position).getLabel());
            holder.valueView.setHint(mDataSource.get(position).getHint());
            holder.valueView.setText(mDataSource.get(position).getStringValue());

            // perform any setup operations defined by the Detail type
            mDataSource.get(position).configureValueView(holder.valueView);

            // attach listeners
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataSource.get(position).setValueViewListener(holder.valueView);
                }
            });
            holder.valueView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataSource.get(position).setValueViewListener(holder.valueView);
                }
            });

            // set keyboard tab button
            if (position == mDataSource.size() - 1) {
                holder.valueView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            } else {
                holder.valueView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }

        }

        return rowView;
    }

}
