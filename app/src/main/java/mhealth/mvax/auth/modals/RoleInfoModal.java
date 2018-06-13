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
package mhealth.mvax.auth.modals;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for displaying info about the available roles
 */
public class RoleInfoModal extends CustomModal {

    public RoleInfoModal(View view) {
        super(view);
    }

    @Override
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.role_info_title)
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_role_info, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(R.string.ok, null)
                .show();
        return mBuilder;
    }
}
