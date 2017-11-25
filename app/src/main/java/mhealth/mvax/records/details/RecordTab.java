package mhealth.mvax.records.details;

import mhealth.mvax.model.record.Record;

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
     * @param updatedRecord the new record with which to update
     *                      the view
     */
    void update(Record updatedRecord);

}
