package mhealth.mvax.search;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.Detail;

/**
 * @author Robert Steilberg
 */

public class NewRecordDetailsAdapter extends RecordDetailsAdapter {

    private LayoutInflater mInflater;

    private Context mContext;

    NewRecordDetailsAdapter(Context context, LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
        super(context, sectionedData);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View rowView, ViewGroup viewGroup) {
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

            // perform any setup operations
            mDataSource.get(position).configureValueView(holder.valueView);

            // attach listeners
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataSource.get(position).valueViewListener(holder.valueView);
                }
            });
            holder.valueView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataSource.get(position).valueViewListener(holder.valueView);
                }
            });

            // set keyboard
            if (position == mDataSource.size() - 1) {
                holder.valueView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            } else {
                holder.valueView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }

        }

        return rowView;
    }

}
