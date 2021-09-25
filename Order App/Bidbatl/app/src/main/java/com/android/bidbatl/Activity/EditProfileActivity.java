package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Model.Profile;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.bidbatl.Utility.FileAPIManager;
import com.android.bidbatl.Utility.Utility;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfileActivity extends BaseActivity {
    TextInputEditText inputEditTextEmail;
    TextInputEditText inputEditTextUserName;
    TextInputEditText inputEditTextBusinessName;
    String url;
    Profile.UserProfile profile;
    Button buttonUpdateProfile;
    CircleImageView userPhoto;
    private static final int CAMERA_REQUEST_CODE = 201;
    private static final int GALLERY_REQUEST_CODE = 200;
    String mSelectedImagePath;
    Boolean isProfilePicChanged = false;

    private static final int PERMISSION_REQUEST_CODE = 1;
    String [] appPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile = (Profile.UserProfile) getIntent().getSerializableExtra("profile");
        url = profile.photo;
        initUI();
        initToolBar();
    }
    private void initUI(){
        buttonUpdateProfile = findViewById(R.id.button_submit);
        buttonUpdateProfile.setOnClickListener(updateProfileAction);
        inputEditTextUserName = findViewById(R.id.inputEditTextName);
        inputEditTextEmail = findViewById(R.id.inputEditTextEmail);
        inputEditTextBusinessName = findViewById(R.id.inputEditTextbussName);
        userPhoto = findViewById(R.id.imageView_user);
        userPhoto.setOnClickListener(selectPhotoAction);

        Glide.with(this).load(url).placeholder(R.drawable.user_icon_placeholder).into(userPhoto);
        inputEditTextUserName.setText(profile.name);
        inputEditTextEmail.setText(profile.email);
        inputEditTextBusinessName.setText(profile.registered_name);
    }
    private void initToolBar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Edit Profile");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }
    private Boolean checkAndRequestPermission(){

        List<String> listPermissiomNeeded = new ArrayList<>();
        for (String perm:appPermission){
            if (ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED){
                listPermissiomNeeded.add(perm);
            }
        }
        // ask permission
        if (!listPermissiomNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermissiomNeeded.toArray(new String[listPermissiomNeeded.size()]),PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;

    }
    private View.OnClickListener selectPhotoAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkAndRequestPermission()) {
                showPictureDialog();
            }

        }
    };
    private View.OnClickListener updateProfileAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isProfilePicChanged){
//                uploadProfilePic();
                new ImageCompressionAsyncTask(EditProfileActivity.this).execute(mSelectedImagePath, getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/");
            }
            else {
                updateUserDetail();
            }
        }
    };
    private Boolean validateUserDetail(){
        if (inputEditTextUserName.getText().toString().length() == 0){
            Toast.makeText(this,"Please enter your name",Toast.LENGTH_LONG).show();
            return false;
        }
       else if (inputEditTextEmail.getText().toString().length() == 0){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_LONG).show();
            return false;
        }
       else if (!Utility.isValidEmailId(inputEditTextEmail.getText().toString())){
            Toast.makeText(this,"Please enter valid email",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (inputEditTextBusinessName.getText().toString().length() == 0){
            Toast.makeText(this,"Please enter your business name",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return  true;
        }
    }
    private void uploadProfilePic(){
       showDialog("");
       final File finalFile = new File(mSelectedImagePath);
        FileAPIManager.getInstance().callAPI(finalFile, getApplicationContext(), new FileAPIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String message) {
                Log.d("file",message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.getString("error_status").equalsIgnoreCase("0")){
                        url = jsonObject.getString("full_url");
                        updateUserDetail();
                    }
                    else {
                        dismissDialog();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Failed to update profile!",Toast.LENGTH_LONG).show();
                    dismissDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorFinished(String result) {
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                updateUserDetail();
            }
        });
    }
    private void updateUserDetail() {
        if (validateUserDetail()) {
        HashMap params = new HashMap();
        params.put("name", inputEditTextUserName.getText().toString());
        params.put("registered_name", inputEditTextBusinessName.getText().toString());
        params.put("email", inputEditTextEmail.getText().toString());
        params.put("photo", url);
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.UPDATE_USER, params, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200){
                        Toast.makeText(EditProfileActivity.this,object.getString("message"),Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(EditProfileActivity.this,result,Toast.LENGTH_LONG).show();
            }
        });
    }
    }
    private void captureImageFromCamera(int requestCode) {
        Intent captureImage = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (captureImage.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(EditProfileActivity.this,
                        "com.app.bidbatl.media.provider", photoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(captureImage,
                        requestCode);
            }
        }
    }
    private void pickFromGallery() {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Camera",
                "Gallery"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                captureImageFromCamera(CAMERA_REQUEST_CODE);
                                break;
                            case 1:
                                pickFromGallery();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpeg", storageDir);

        mSelectedImagePath = image.getAbsolutePath();
        return image;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    isProfilePicChanged = true;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(Objects.requireNonNull(selectedImage), filePathColumn, null, null, null);
                    Objects.requireNonNull(cursor).moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e(EditProfileActivity.class.getSimpleName(), "onActivityResult: imgDecodableString: " + imgDecodableString);
                    mSelectedImagePath = imgDecodableString;
                    Log.d("selected Path", mSelectedImagePath);

                    Glide.with(this).load(mSelectedImagePath).into(userPhoto);

                    break;

                case CAMERA_REQUEST_CODE:
                    isProfilePicChanged = true;
                    Log.d("Path",mSelectedImagePath);
                    Glide.with(EditProfileActivity.this).load(mSelectedImagePath).into(userPhoto);
break;
            }
    }
    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        private Context mContext;

        ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            File file = null;
            try {
                file = new Compressor(EditProfileActivity.this).compressToFile(new File(mSelectedImagePath));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return Objects.requireNonNull(file).getAbsolutePath();

        }

        @Override
        protected void onPostExecute(String s) {
            mSelectedImagePath = s;
            uploadProfilePic();
        }
    }
}
