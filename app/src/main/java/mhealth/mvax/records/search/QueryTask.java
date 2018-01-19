package mhealth.mvax.records.search;

import java.util.TimerTask;

/**
 * @author Robert Steilberg
 *         <p>
 *         DESCRIPTION HERE
 */
public class QueryTask extends TimerTask {

    String mQuery;

    QueryTask(String query) {
        mQuery = query;
    }

    @Override
    public void run() {
        String f = mQuery;
    }
}
