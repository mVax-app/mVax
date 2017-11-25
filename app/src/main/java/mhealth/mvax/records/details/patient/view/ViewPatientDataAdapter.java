package mhealth.mvax.records.details.patient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mhealth.mvax.R;
import mhealth.mvax.records.views.detail.Detail;
import mhealth.mvax.records.details.patient.PatientDataAdapter;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter for rendering the ListView for viewing patient details
 */

public class ViewPatientDataAdapter extends PatientDataAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private LayoutInflater mInflater;

    //================================================================================
    // Constructors
    //================================================================================

    ViewPatientDataAdapter(Context context, LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
        super(sectionedData);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        ViewHolder holder;
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
            holder.fieldView.setText(mDataSource.get(position).getLabel());
            holder.valueView.setText(mDataSource.get(position).getStringValue());
            holder.valueView.setFocusable(false);
        }

        return rowView;
    }
}
