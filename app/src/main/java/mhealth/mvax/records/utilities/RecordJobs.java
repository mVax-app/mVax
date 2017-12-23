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
package mhealth.mvax.records.utilities;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Person;
import mhealth.mvax.records.views.detail.Detail;

/**
 * @author FIRST LAST
 *         <p>
 *         DESCRIPTION HERE
 */
public class RecordJobs {

    public static LinkedHashMap<String, List<Detail>> getSectionedDetails(Context context, Person... people) {
        LinkedHashMap<String, List<Detail>> details = new LinkedHashMap<>();

        for (Person p : people) {
            details.put(context.getString(p.getSectionTitleStringID()), p.getDetails(context));
        }

//        details.put(context.getString(R.string.patient_detail_section_title), mPatient.getDetails(getContext()));
//        details.put(context.getString(R.string.guardian_detail_section_title), mGuardian.getDetails(getContext()));
        return details;
    }

}
