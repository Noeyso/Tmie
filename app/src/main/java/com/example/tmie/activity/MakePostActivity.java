package com.example.tmie.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tmie.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakePostActivity extends AppCompatActivity {

    private EditText edit_title;
    private EditText edit_body;
    private Button btn_upload,btn_gallery,btn_camera;

    private static final String TAG = "tmieTest";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        btn_upload=findViewById(R.id.btn_upload);
        edit_title = findViewById(R.id.edit_title);
        edit_body = findViewById(R.id.edit_body);
        btn_gallery=findViewById(R.id.btn_gallery);
        btn_camera = findViewById(R.id.btn_camera);

        btn_upload.setOnClickListener(onClickListener);
        btn_gallery.setOnClickListener(onClickListener);
        btn_camera.setOnClickListener(onClickListener);


        Html.ImageGetter imageGetter  =new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = getResources().getDrawable( R.drawable.gallery );

                drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );

                return drawable;
            }
        };
        Spanned htmlText = Html.fromHtml( "", imageGetter, null);
        edit_body.setText(htmlText);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_upload:
                    Task task = new Task();
                    task.execute();
                    break;
                case R.id.btn_gallery:
                    System.out.println("????????? ??????!");
                    tedPermission();
                    if(isPermission) goToAlbum();
                    else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_camera:
                    if(isPermission)  takePhoto();
                    else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    public class Task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                request();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(getApplicationContext(),PostListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            myStartActivity(PostListActivity.class);
//            onBackPressed();
        }
    }
    public void request() throws JSONException {
        String url = "http://172.30.1.10:3000/api/posts";
        //JSON ???????????? ????????? ?????? ??????
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("title",edit_title.getText().toString());
        jsonObject.put("body",edit_body.getText().toString());
        String jsonString = jsonObject.toString();
        System.out.println("????????? :"+jsonString);

        //????????? ??????
        final RequestQueue requestQueue = Volley.newRequestQueue(MakePostActivity.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(getClass().getName(),"?????? ??????");
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        //intent.putExtra("Code", meetingCode);
        //startActivityForResult(intent, 0);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " ?????? ??????");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri ????????????
                 *  content:/// ?????? file:/// ???  ????????????.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }
    }

    /**
     *  ???????????? ????????? ????????????
     */
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     *  ??????????????? ????????? ????????????
     */
    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "????????? ?????? ??????! ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            Uri photoUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    /**
     *  ?????? ??? ?????? ?????????
     */
    private File createImageFile() throws IOException {

        // ????????? ?????? ?????? ( blackJin_{??????}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "tmie" + timeStamp + "_";

        // ???????????? ????????? ?????? ?????? ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Image/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // ?????? ??????
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     *  tempFile ??? bitmap ?????? ?????? ??? ImageView ??? ????????????.
     */
    private void setImage() {

        ImageView imageView = findViewById(R.id.gallery_image);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);

        /**
         *  tempFile ?????? ??? null ????????? ????????? ?????????.
         *  (resultCode != RESULT_OK) ??? ??? tempFile ??? ???????????? ?????????
         *  ????????? ???????????? ?????? ?????? ?????? ?????? ?????? ????????? ???????????????.
         */
        tempFile = null;

    }

    /**
     *  ?????? ??????
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // ?????? ?????? ??????
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // ?????? ?????? ??????
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
}
