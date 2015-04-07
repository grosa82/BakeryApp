package com.cs246.bakery.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class UploadPhoto extends ActionBarActivity {

    private Helper helper = new Helper(this);
    private ProgressDialog progressDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 2;
    private String cakeId;
    private Bitmap bitmap;
    private ImageView imageView;
    private int buttonSelected;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        Intent intent = getIntent();
        cakeId = intent.getStringExtra("cakeId");
        imageView = (ImageView)findViewById(R.id.photo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about_us) {
            helper.displayCompanyInfo().show();
            return true;
        }

        if (id == R.id.call_me) {
            helper.displayOkCancelDialog("We can call you to help you with your order", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper.callMe();
                }
            }).show();
            return true;
        }

        if (id == R.id.my_profile) {
            helper.goToProfile();
            return true;
        }

        if (id == R.id.action_signOut) {
            helper.signOut();
            return true;
        }

        if (id == R.id.my_cakes) {
            helper.goToMyCakes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens dialog picker, so the user can select image from the gallery. The
     * result is returned in the method <code>onActivityResult()</code>
     */
    public void selectImageFromGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        buttonSelected = 1;
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        buttonSelected = 2;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (buttonSelected == 2) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                File file = new File("cakeapp_picture.jpg");
                try
                {
                    FileOutputStream fOut = openFileOutput("cakeapp_picture.jpg", MODE_PRIVATE);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                filePath = file.getAbsolutePath();
            }
        } else {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                Bitmap imageBitmap = BitmapFactory.decodeFile(selectedImagePath);
                imageView.setImageBitmap(imageBitmap);
                filePath = selectedImagePath;
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    public void uploadPhoto(View view) {
        new AsyncTask<Void, Void, Response>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UploadPhoto.this, "Please wait ...", "Uploading Picture", true);
            }

            @Override
            protected Response doInBackground(Void... params) {
                filePath = getFilesDir() + filePath;
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return helper.sendPictureToWebService(fileInputStream, filePath, cakeId).toResponse();
            }

            @Override
            protected void onPostExecute(Response response) {
                progressDialog.dismiss();
                helper.displayMessage(response.message);
            }
        }.execute(null, null, null);
    }
}
