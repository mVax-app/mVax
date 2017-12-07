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
