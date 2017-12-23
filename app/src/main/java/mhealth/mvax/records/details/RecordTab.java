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
package mhealth.mvax.records.details;

import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Person;

/**
 * @author Robert Steilberg
 *         <p>
 *         Interface for defining the API of a record tab
 */

public interface RecordTab {

    /**
     * Performs the initial render of the tab views, using
     * the Record passed in as an argument to the Fragment
     */
    void render();

    /**
     * Called when the tab view needs to be updated with data
     * from an updated record
     *
     * @param patient the new patient with which to update
     *                      the view
     */
    void update(Person person);

}
