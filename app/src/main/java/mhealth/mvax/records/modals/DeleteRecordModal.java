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
package mhealth.mvax.records.modals;

import android.app.AlertDialog;
import android.view.View;

import mhealth.mvax.R;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for deleting a record
 */
public class DeleteRecordModal extends CustomModal {

    private Runnable mCallback;

    public DeleteRecordModal(View view, Runnable callback) {
        super(view);
        mCallback = callback;
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mInflater.inflate(R.layout.modal_delete_record, mParent, false))
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    mCallback.run();
                    dismiss();
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        show();
    }
}
