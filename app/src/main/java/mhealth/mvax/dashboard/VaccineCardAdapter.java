package mhealth.mvax.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mhealth.mvax.record.Record;
import mhealth.mvax.record.vaccine.Vaccine;

/**
 * Created by AlisonHuang on 11/1/17.
 */

public class VaccineCardAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private Context mContext;

    private LayoutInflater mInflater;

    private List<Vaccine> mDataSource;


    //================================================================================
    // Constructors
    //================================================================================

    VaccineCardAdapter(Context context, Collection<Vaccine> vaccines) {
        mContext = context;
        mDataSource = new ArrayList<>(vaccines);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void refresh(Collection<Vaccine> values) {
        mDataSource = new ArrayList<>(values);
        notifyDataSetChanged();
    }

    String getPatientIdFromDataSource(int position) {
        return mDataSource.get(position).getId();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
