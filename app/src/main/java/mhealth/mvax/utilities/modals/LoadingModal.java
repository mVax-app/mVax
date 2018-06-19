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
package mhealth.mvax.utilities.modals;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 * <p>
 * Simple loading modal that disables all backgroudn views
 */
public class LoadingModal extends CustomModal {

    public LoadingModal(View view) {
        super(view);
    }

    @Override
    public AlertDialog show() {
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if (dialog.getWindow() != null) dialog.getWindow().setLayout(236, 236);
        return dialog;
    }

    @Override
    public AlertDialog create() {
        mBuilder = new AlertDialog.Builder(getContext())
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_loading, (ViewGroup) getView().getParent(), false))
                .create();
        return mBuilder;
    }

}
