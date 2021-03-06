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
package com.mvax.records.record.patient.modify;

import android.app.Activity;
import android.view.inputmethod.EditorInfo;

import java.util.List;

import com.mvax.R;
import com.mvax.records.record.patient.PatientDetailsAdapter;
import com.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 * <p>
 * Adapter that allows users to edit record details
 */
public class ModifyPatientAdapter extends PatientDetailsAdapter {

    private Activity mActivity;

    ModifyPatientAdapter(Activity activity, List<Detail> details) {
        super(details);
        mActivity = activity;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.value.clearTextChangedListeners();

        final Detail detail = mDetails.get(position);

        detail.configureValueView(holder.value);

        holder.field.setText(detail.getLabelStringId());
        String hint = holder.value.getResources().getString(detail.getHintStringId());
        holder.value.setHint(hint + " ");
        holder.value.setText(detail.getStringValue());

        holder.row.setFocusable(false);
        // trigger onclick listener no matter where the row is tapped
        holder.row.setOnClickListener(v ->
                detail.getValueViewListener(mActivity, holder.value));
        holder.value.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                detail.getValueViewListener(mActivity, holder.value);
            }
        });
        holder.value.setOnClickListener(v ->
                detail.getValueViewListener(mActivity, holder.value));

        // Done button on final field
        if (position == mDetails.size() - 1) {
            holder.value.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            holder.value.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
        // place edittext_cursor at end of text
        holder.value.setSelection(detail.getStringValue().length());

        if (detail.hasError()) {
            holder.value.setError(mActivity.getString(R.string.empty_field));
            holder.value.requestFocus();
        } else {
            holder.value.setError(null);
        }

    }

}
