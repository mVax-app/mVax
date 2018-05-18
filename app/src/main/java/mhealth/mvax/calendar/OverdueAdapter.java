package mhealth.mvax.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.records.record.vaccine.VaccineAdapter;
import mhealth.mvax.records.utilities.NullableDateFormat;

/**
 * @author Alison Huang
 * <p>
 * DESCRIPTION HERE
 */
public class OverdueAdapter extends BaseAdapter {

    private final Context mContext;
    private final NullableDateFormat mDateFormat;
    private List<DueDate> mDueDates;



    OverdueAdapter(Context context, HashMap<String, DueDate> dueDates) {
        this.mContext = context;
        this.mDueDates = new ArrayList<>(dueDates.values());
        mDateFormat = new NullableDateFormat(context.getString(R.string.date_format));
    }

    @Override
    public int getCount() {
        return mDueDates.size();
    }

    @Override
    public DueDate getItem(int position) {
        return mDueDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        final DueDate dueDate = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ViewHolder holder;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item_patient, viewGroup, false);
            holder = new ViewHolder();
            holder.patientName = rowView.findViewById(R.id.calender_LV_patient_name);
            holder.dateTitle = rowView.findViewById(R.id.calendar_LV__title_date);
            holder.date = rowView.findViewById(R.id.calendar_LV__date);
            holder.vaccineTitle = rowView.findViewById(R.id.calendar_LV_title_vaccine);
            holder.vaccine = rowView.findViewById(R.id.calendar_LV_vaccine);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }



        holder.patientName.setText(dueDate.getVaccineDatabaseKey());
        holder.dateTitle.setText(mContext.getString(R.string.title_date));
        holder.date.setText(mDateFormat.getString(dueDate.getDate()));
        holder.vaccineTitle.setText(mContext.getString(R.string.title_vaccine));
        holder.vaccine.setText(dueDate.getVaccineDatabaseKey());


        return rowView;
    }

    public void refresh(HashMap<String,DueDate> dueDates) {
        mDueDates = new ArrayList<>(dueDates.values());
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView patientName;
        TextView dateTitle;
        TextView date;
        TextView vaccineTitle;
        TextView vaccine;
    }
}