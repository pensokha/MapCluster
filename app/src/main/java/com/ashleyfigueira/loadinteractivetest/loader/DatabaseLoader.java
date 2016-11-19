package com.ashleyfigueira.loadinteractivetest.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ashleyfigueira.loadinteractivetest.model.DatabaseHandler;
import com.ashleyfigueira.loadinteractivetest.model.Establishment;

import java.util.ArrayList;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

public class DatabaseLoader extends AsyncTaskLoader<ArrayList<Establishment>>
{
    private ArrayList<Establishment> mData;
    private Context context;

    public DatabaseLoader(Context context)
    {
        super(context);
        this.context = context;
    }

    @Override
    public ArrayList<Establishment> loadInBackground()
    {
        DatabaseHandler mDatabaseHandler = DatabaseHandler.getInstance(context);
        ArrayList<Establishment> mList = mDatabaseHandler.getAllEstablishments();
        return mList;
    }

    /**
     * Runs on the UI thread, routing the results from the background thread to
     * whatever is using the dataList.
     */
    @Override
    public void deliverResult(ArrayList<Establishment> data)
    {
        if (isReset()){
            // The Loader has been reset;
            // ignore the result and invalidate the data.
            emptyDataList(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        ArrayList<Establishment> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            emptyDataList(oldData);
        }
    }

    /**
     * Starts an asynchronous load of the list data. When the result is ready
     * the callbacks will be called on the UI thread. If a previous load has
     * been completed and is still valid the result may be passed to the
     * callbacks immediately.
     * Must be called from the UI thread.
     */
    @Override
    protected void onStartLoading()
    {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null || mData.size() == 0) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread, triggered by a call to stopLoading().
     */
    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null && mData.size() > 0) {
            emptyDataList(mData);
        }
        mData = null;
    }

    /**
     * Must be called from the UI thread, triggered by a call to cancel(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already
     * closed.
     */
    @Override
    public void onCanceled(ArrayList<Establishment> data)
    {
        if (data != null && data.size() > 0) {
            // The load has been canceled, so we should release the resources
            // associated with 'data'.
            emptyDataList(data);
        }

    }

    protected void emptyDataList(ArrayList<Establishment> data) {
        if (data != null && data.size() > 0)
        {
            for (int i = 0; i < data.size(); i++) {
                data.remove(i);
            }
        }
    }
}
