package com.example.databasesql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;
    private static String DATABASE_NAME = "mydb.db";
    private static int DATABASE_VERSION = 1;
    private static String creatTableQuery = "Create table imageInfo (imageName TEXT" + ",image BLOB)";
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(creatTableQuery);
            Toast.makeText(context, "Table Succesfully inside our Database ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storeImage(ModelClass objectModelClass) {
        try {
            SQLiteDatabase objectSQLiteDatabase = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = objectModelClass.getImage();
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);

            imageInBytes = objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("imageName", objectModelClass.getImageName());
            objectContentValues.put("image", imageInBytes);
            long checkIfQueryRun = objectSQLiteDatabase.insert("imageInfo", null, objectContentValues);
            if (checkIfQueryRun != -1) {
                Toast.makeText(context, "Data Added inti Our Table", Toast.LENGTH_SHORT).show();
                objectSQLiteDatabase.close();
            } else {
                Toast.makeText(context, "Data not Added into our table", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public ArrayList<ModelClass> getAllImagesData() {
        try {
            SQLiteDatabase objectsqLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> objectModelClassList = new ArrayList<>();

            Cursor objectCursor = objectsqLiteDatabase.rawQuery("select * from imageInfo", null);
            if (objectCursor.getCount() != 0) {
                while (objectCursor.moveToNext()) {
                    String nameOfImage = objectCursor.getString(0);
                    byte[] imageBytes=objectCursor.getBlob(1);
                    Bitmap objectBitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                    objectModelClassList.add(new ModelClass(nameOfImage,objectBitmap));

                }
                return objectModelClassList;
            } else {
                Toast.makeText(context, "NO Values Exist in Database", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }



}
