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
package mhealth.mvax.dashboard;

import android.support.v4.content.FileProvider;

/**
 * Created as a way to safely retrieve files from Android storage. Android discourages the passage
 * of URIs solely and a FileProvider allows for the safe passage of files.
 * This is used when a user exports a form when the form is given to an email chooser as an attachment
 *
 * Currently no additional functionality to FileProvider but may want to add in the future as forms
 * increases in complexity
 *
 * This class was a byproduct of using Stack: https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed/38858040#38858040
 */
public class GenericFileProvider extends FileProvider{
}
