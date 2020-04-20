package com.example.databasesql;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText imagDetailET;
    private ImageView objectImageView;
    private static final int PICK_IMAGE_REQUEST=102;
    private Uri ImageFilePath;
    private Bitmap ImageToStore;
    DatabaseHandler objectDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            imagDetailET=findViewById(R.id.imageNameET);
            objectImageView=findViewById(R.id.image);
            objectDatabaseHandler=new DatabaseHandler(this);

        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void chooseImage(View objectView)
    {
        try {
            Intent objectIntent= new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,PICK_IMAGE_REQUEST);
        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    ///overRide method


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
            {
ImageFilePath=data.getData();
ImageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(),ImageFilePath);

objectImageView.setImageBitmap(ImageToStore);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void storeImage(View view)
    {
        try {
            if(!imagDetailET.getText().toString().isEmpty() && objectImageView.getDrawable()!=null && ImageToStore!=null)
            {
                objectDatabaseHandler.storeImage(new ModelClass(imagDetailET.getText().toString(),ImageToStore));
            }
            else
            {
                Toast.makeText(this,"Pleae Select Image name and image",Toast.LENGTH_SHORT).show();
            }


            }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }
    public void moveToShow(View view){
startActivity(new Intent(this,ShowImageActivity.class));
    }
}
