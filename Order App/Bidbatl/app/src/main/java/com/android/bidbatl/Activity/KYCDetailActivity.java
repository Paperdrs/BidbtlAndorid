package com.android.bidbatl.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.bidbatl.Utility.FileAPIManager;
import com.android.volley.Request;
import com.preference.PowerPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;


public class KYCDetailActivity extends BaseActivity implements View.OnClickListener {
    TextInputEditText textKYCName;
    TextInputEditText textKYCNo;
    TextInputLayout inputLayout;
    ImageView imageViewKYCPhoto, imageViewRefresh;
    String url = "";
    Button buttonSubmitKyc;
    String kycString;
    String mSelectedImagePath;
    private File actualImage;
    private File compressedImage;

    private final int CAMERA_PIC_REQUEST_INVOICE = 101;
    private final int GALLERY_REQUEST = 102;
    String kycType;
    TextView textViewTitle;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String[] appPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycdetail);
        kycType = getIntent().getStringExtra("kyc");
        initViews();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Upload " + kycString);
        if (kycType.equalsIgnoreCase("gst")) {
            textViewTitle.setText("Upload GST No.");
        }
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }

    private void initViews() {
        textKYCName = findViewById(R.id.textInput_kyc_name);
        textKYCNo = findViewById(R.id.textInput_kyc_no);
        imageViewKYCPhoto = findViewById(R.id.imageView_kyc_photo);
        inputLayout = findViewById(R.id.textInputLayoutkycno);
        buttonSubmitKyc = findViewById(R.id.button_submit_kyc);
        imageViewRefresh = findViewById(R.id.imageView_refresh);
        imageViewRefresh.setOnClickListener(refresh);
        imageViewKYCPhoto.setOnClickListener(this);
        buttonSubmitKyc.setOnClickListener(this);
        if (kycType.equalsIgnoreCase("aadhaar")) {
            kycString = "Aadhar";
            inputLayout.setHint("Aadhar Card Number");
            textKYCNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            textKYCNo.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(12) // 12 is max length
            });

        } else if (kycType.equalsIgnoreCase("pan")) {
            kycString = "Pan";
            textKYCNo.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(10) // 10 is max length
            });

            textKYCNo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            inputLayout.setHint("Pan Card Number");

        } else if (kycType.equalsIgnoreCase("gst")) {
            kycString = "GST";

            textKYCNo.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(15) // 15 is max length
            });
            inputLayout.setHint("GST Number");
            textKYCNo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        }
        getKYCDoc();
    }

    View.OnClickListener refresh = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            uploadKYCPhoto(mSelectedImagePath);
        }
    };

    private boolean validateDetail() {
        if (textKYCName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (textKYCNo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter KYC ID", Toast.LENGTH_LONG).show();
            return false;

        } else if (url.isEmpty()) {
            Toast.makeText(this, "Please Upload KYC Photo", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void addKYC() {
        if (validateDetail()) {
            showDialog("Please Wait...");
            HashMap map = new HashMap();
            map.put("user_id", PowerPreference.getDefaultFile().getString("user_id"));
            map.put("type", kycType);
            map.put("name", textKYCName.getText().toString());
            map.put("id_number", textKYCNo.getText().toString());
            map.put("photo", url);
            APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.ADD_KYC, map, this, new APIManager.APICallbackInterface() {
                @Override
                public void onSuccessFinished(String result) {
                    Toast.makeText(getApplicationContext(), "Your " + kycString + " data has been submitted.it will take us sometime to order the same.", Toast.LENGTH_LONG).show();
                    dismissDialog();
                    finish();
                }

                @Override
                public void onErrorFinished(String result) {
                    dismissDialog();
                    Toast.makeText(KYCDetailActivity.this, result, Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void getKYCDoc() {
        showDialog("Loading...");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_KYC, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(KYCDetailActivity.this, result, Toast.LENGTH_LONG).show();

            }
        });
    }

    private Boolean checkAndRequestPermission() {

        List<String> listPermissiomNeeded = new ArrayList<>();
        for (String perm : appPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissiomNeeded.add(perm);
            }
        }
        // ask permission
        if (!listPermissiomNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissiomNeeded.toArray(new String[listPermissiomNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        if (v == imageViewKYCPhoto) {
            if (checkAndRequestPermission()) {
                showPictureDialog();
            }
        } else if (v == buttonSubmitKyc) {
            addKYC();

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
                Uri photoURI = FileProvider.getUriForFile(KYCDetailActivity.this,
                        "com.app.bidbatl.media.provider", photoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(captureImage,
                        requestCode);
            }
        }
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

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST);

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Gallery"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                pickFromGallery();
                                break;
                            case 1:

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void uploadKYCPhoto(String mSelectedImagePath) {
        showDialog("Uploading...");
        final File finalFile = new File(mSelectedImagePath);
        FileAPIManager.getInstance().callAPI(finalFile, getApplicationContext(), new FileAPIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String message) {
                Log.d("file", message);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.getString("error_status").equalsIgnoreCase("0")) {
                        imageViewRefresh.setVisibility(View.INVISIBLE);
                        url = jsonObject.getString("full_url");// full_url
                        Log.d("productURL", url);
                    } else {
                        imageViewRefresh.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                imageViewRefresh.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (data.getData() == null) {
                    return;
                }
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

                Glide.with(this).load(mSelectedImagePath).into(imageViewKYCPhoto);
                new ImageCompressionAsyncTask(KYCDetailActivity.this).execute(mSelectedImagePath, getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/");

                break;
            case CAMERA_PIC_REQUEST_INVOICE: {
                Log.d("Path", mSelectedImagePath);
                Glide.with(KYCDetailActivity.this).load(mSelectedImagePath).into(imageViewKYCPhoto);
                uploadKYCPhoto(mSelectedImagePath);


                break;

            }

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
                file = new Compressor(KYCDetailActivity.this).compressToFile(new File(mSelectedImagePath));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return Objects.requireNonNull(file).getAbsolutePath();

        }

        @Override
        protected void onPostExecute(String s) {
            mSelectedImagePath = s;
            uploadKYCPhoto(mSelectedImagePath);
        }
    }
}