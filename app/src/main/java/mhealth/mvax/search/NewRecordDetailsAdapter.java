package mhealth.mvax.search;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 */

public class NewRecordDetailsAdapter extends RecordDetailsAdapter {

    private LayoutInflater mInflater;

    private Context mContext;

    NewRecordDetailsAdapter(Context context, LinkedHashMap<String, ArrayList<Pair<String, String>>> sectionedData) {
        super(context, sectionedData);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        final ViewHolder holder;
        int rowType = getItemViewType(position);
        if (rowView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_SECTION:
                    rowView = mInflater.inflate(R.layout.list_item_record_detail_section, null);
                    holder.fieldView = rowView.findViewById(R.id.record_detail_separator);
                    break;
                case TYPE_FIELD:
                    rowView = mInflater.inflate(R.layout.list_item_record_detail, null);
                    holder.fieldView = rowView.findViewById(R.id.textview_field);
                    holder.valueView = rowView.findViewById(R.id.edittext_value);
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
            holder.fieldView.setText(mDataSource.get(position).first);
            holder.fieldView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.valueView.requestFocus();
                    // force keyboard to pop up
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(holder.valueView, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            holder.valueView.setHint(mDataSource.get(position).first);
        }

        return rowView;
    }


}
