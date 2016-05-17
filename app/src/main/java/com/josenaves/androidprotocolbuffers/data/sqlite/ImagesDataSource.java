package com.josenaves.androidprotocolbuffers.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.josenaves.androidprotocolbuffers.data.Image;

import java.util.ArrayList;
import java.util.List;

public class ImagesDataSource {

    private static final String TAG = ImagesDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private ImagesDatabaseHelper dbHelper;

    private String[] columns = { ImagesDatabaseHelper.COLUMN_ID, ImagesDatabaseHelper.COLUMN_NAME,
            ImagesDatabaseHelper.COLUMN_DATE, ImagesDatabaseHelper.COLUMN_IMAGE };

    public ImagesDataSource(Context context) {
        dbHelper = ImagesDatabaseHelper.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createImage(Image image) {
        Log.d(TAG, "createImage...");

        //database.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(ImagesDatabaseHelper.COLUMN_NAME, image.name);
        values.put(ImagesDatabaseHelper.COLUMN_DATE, image.date);
        values.put(ImagesDatabaseHelper.COLUMN_IMAGE, image.imageData);
        long id = database.insert(ImagesDatabaseHelper.TABLE_IMAGES, null, values);
        //database.endTransaction();

        Log.d(TAG, "Image saved in the database - id = " + id);
    }

    public List<Image> getAllImages(){
        Log.d(TAG, "getAllImages...");

        List<Image> images = new ArrayList<>();
        Cursor cursor = database.query(ImagesDatabaseHelper.TABLE_IMAGES, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            images.add(imageFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return images;
    }

    private Image imageFromCursor(Cursor cursor) {
        Image image = new Image();
        image.id = cursor.getLong(cursor.getColumnIndex(ImagesDatabaseHelper.COLUMN_ID));
        image.name = cursor.getString(cursor.getColumnIndex(ImagesDatabaseHelper.COLUMN_NAME));
        image.date = cursor.getString(cursor.getColumnIndex(ImagesDatabaseHelper.COLUMN_DATE));
        image.imageData = cursor.getBlob(cursor.getColumnIndex(ImagesDatabaseHelper.COLUMN_IMAGE));
        return image;
    }

}
