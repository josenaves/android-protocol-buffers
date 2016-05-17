package com.josenaves.androidprotocolbuffers.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.josenaves.androidprotocolbuffers.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static final byte[] getImageData(Context context) {
        Log.d(TAG, "getImageData...");

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.florianopolis);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static final String getCurrentDateTime() {
        String dateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateTime = iso8601Format.format(date);

        return dateTime;
    }

}
