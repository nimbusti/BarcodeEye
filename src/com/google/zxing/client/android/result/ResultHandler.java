package com.google.zxing.client.android.result;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.barcodeeye.CardPresenter;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;

/**
 * @author javier.romero
 */
public abstract class ResultHandler<T extends ParsedResult> {
    private final Context mContext;
    private final T mParsedResult;
    private final Result mRawResult;
    //private final Uri mPhotoUri;

    public ResultHandler(Context context, T parsedResult,
            Result result) {//, Uri photoUri) {
        mContext = context;
        mParsedResult = parsedResult;
        mRawResult = result;
        //mPhotoUri = photoUri;
    }

    public Context getContext() {
        return mContext;
    }

    public T getParsedResult() {
        return mParsedResult;
    }

    public Result getRawResult() {
        return mRawResult;
    }

    /*
    public Uri getPhotoUri() {
        return mPhotoUri;
    }
    */

    public abstract List<CardPresenter> getCardResults();

    public static PendingIntent createPendingIntent(Context context,
            Intent intent) {
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    public Object getType() {
      // TODO Auto-generated method stub
      return null;
    }

    public int getDisplayTitle() {
      // TODO Auto-generated method stub
      return 0;
    }

    public CharSequence getDisplayContents() {
      // TODO Auto-generated method stub
      return null;
    }

    public boolean areContentsSecure() {
      // TODO Auto-generated method stub
      return false;
    }
}
