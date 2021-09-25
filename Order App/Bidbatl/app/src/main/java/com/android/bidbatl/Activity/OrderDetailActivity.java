package com.android.bidbatl.Activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Adapter.OrderDetailAdapter;
import com.android.bidbatl.Model.OrderDetails;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.bidbatl.Utility.DownloadFileFromURL;
import com.android.bidbatl.Utility.FileDownloader;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.preference.PowerPreference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class OrderDetailActivity extends BaseActivity {

ImageView imageViewCart;
    ConstraintLayout bottomHeader;
    ConstraintLayout checkOut;
    TextView textViewCount;
    TextView textViewSubtotal;
    TextView textViewDelCharges;
    RecyclerView recyclerViewOrderDetail;
    TextView textViewGrandTotal;
    OrderDetailAdapter adapter;

    TextView placedCircle,shippedCircle,deliveryCircle;
    TextView placedLine,shippedLine,deliveryLine;
    TextView placedTitle,shippedTitle,deliveryTitle;
    TextView textViewDownloadInvoice;
    TextView textViewPaymentStatus,textViewDeliveryStatus;
    String billURL;

    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        bottomHeader = findViewById(R.id.constraintLayout_bottom);
        checkOut =  findViewById(R.id.constraintLayout_checkout);
        textViewCount = findViewById(R.id.textView_count);
        textViewGrandTotal = findViewById(R.id.textView_grand_total);
        textViewSubtotal = findViewById(R.id.textView_subtotal);
        textViewDelCharges = findViewById(R.id.textView_del_charge);
        textViewPaymentStatus = findViewById(R.id.textView_payment);
        textViewDeliveryStatus = findViewById(R.id.textView_del_status);
        bottomHeader.setVisibility(View.GONE);
        textViewDownloadInvoice = findViewById(R.id.textView_download_invoice);
        textViewDownloadInvoice.setOnClickListener(downloadInvoice);
        initToobar();
        initRecyclerView();
        initTimelineViews();
        String orderID = getIntent().getStringExtra("orderID");
        String status = getIntent().getStringExtra("status");
        String invoice_url = getIntent().getStringExtra("invoice_url");
        String paymentStatus = getIntent().getStringExtra("payment_status");
        if (paymentStatus.equals("accepted") || paymentStatus.equals("") || status.equals("delivered")){
            status = "delivered";

        }
        textViewDeliveryStatus.setText(status.toUpperCase());

        setTimeLineView(status);

        textViewPaymentStatus.setText(paymentStatus.toUpperCase());

        initGSON();
        getOrderDetail();
    }
    private View.OnClickListener downloadInvoice = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           downloadInvoice();
        }
    } ;
    private void initTimelineViews(){
        placedCircle = findViewById(R.id.placed_circle);
        shippedCircle = findViewById(R.id.shipped_circle);
        deliveryCircle = findViewById(R.id.delivery_circle);

        placedLine = findViewById(R.id.placed_line);
        shippedLine = findViewById(R.id.shipped_line);
        deliveryLine = findViewById(R.id.delivery_line);

        placedTitle = findViewById(R.id.placed_title);
        shippedTitle = findViewById(R.id.shipped_title);
        deliveryTitle = findViewById(R.id.delivery_title);

    }
    private void setTimeLineView(String status){
        if (status.equalsIgnoreCase("shipped")){
            placedTitle.setTextColor(getResources().getColor(R.color.blue_color));
            placedLine.setBackgroundColor(getResources().getColor(R.color.blue_color));
            placedCircle.setBackground(getResources().getDrawable(R.drawable.circle_blue));

            shippedTitle.setTextColor(getResources().getColor(R.color.blue_color));
            shippedLine.setBackgroundColor(getResources().getColor(R.color.blue_color));
            shippedCircle.setBackground(getResources().getDrawable(R.drawable.circle_blue));

            deliveryTitle.setTextColor(getResources().getColor(R.color.gray_text_color));
            deliveryLine.setBackgroundColor(getResources().getColor(R.color.gray_text_color));
            deliveryCircle.setBackground(getResources().getDrawable(R.drawable.circle_gray));

        }
        else if (status.equalsIgnoreCase("delivered")){
            placedTitle.setTextColor(getResources().getColor(R.color.blue_color));
            placedLine.setBackgroundColor(getResources().getColor(R.color.blue_color));
            placedCircle.setBackground(getResources().getDrawable(R.drawable.circle_blue));


            shippedTitle.setTextColor(getResources().getColor(R.color.blue_color));
            shippedLine.setBackgroundColor(getResources().getColor(R.color.blue_color));
            shippedCircle.setBackground(getResources().getDrawable(R.drawable.circle_blue));

            deliveryTitle.setTextColor(getResources().getColor(R.color.blue_color));
            deliveryLine.setBackgroundColor(getResources().getColor(R.color.blue_color));
            deliveryCircle.setBackground(getResources().getDrawable(R.drawable.circle_blue));

        }
        else {
            placedTitle.setTextColor(getResources().getColor(R.color.blue_color));
            placedLine.setBackgroundColor(getResources().getColor(R.color.blue_color));
            placedCircle.setBackground(getResources().getDrawable(R.drawable.circle_blue));

            shippedTitle.setTextColor(getResources().getColor(R.color.gray_text_color));
            shippedLine.setBackgroundColor(getResources().getColor(R.color.gray_text_color));
            shippedCircle.setBackground(getResources().getDrawable(R.drawable.circle_gray));

            deliveryTitle.setTextColor(getResources().getColor(R.color.gray_text_color));
            deliveryLine.setBackgroundColor(getResources().getColor(R.color.gray_text_color));
            deliveryCircle.setBackground(getResources().getDrawable(R.drawable.circle_gray));

        }
    }
    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }
    private void initRecyclerView(){
        recyclerViewOrderDetail = findViewById(R.id.recyclervw_order_detail);
        recyclerViewOrderDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
    private void initToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Order Details");
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        imageViewCart.setVisibility(View.GONE);
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }
    private void getOrderDetail() {
        showDialog("Loading...");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.ORDER_DETAIL + getIntent().getStringExtra("orderID"), new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                OrderDetails orderDetails = gson.fromJson(result, OrderDetails.class);
                if (orderDetails.data.orderDetailLists != null) {
                    adapter = new OrderDetailAdapter(getApplicationContext(), orderDetails.data.orderDetailLists);
                    recyclerViewOrderDetail.setAdapter(adapter);
                    textViewCount.setText("(" + orderDetails.data.count + ")");
                    if (orderDetails.data.delivery_charges == null){
                        textViewDelCharges.setText(Constants.rupee + "0");

                    }
                    else {
                        textViewDelCharges.setText(Constants.rupee + orderDetails.data.delivery_charges);

                    }
                    textViewSubtotal.setText(Constants.rupee + orderDetails.data.subtotal);
                    textViewGrandTotal.setText(Constants.rupee + orderDetails.data.grandtotal);
                    bottomHeader.setVisibility(View.VISIBLE);
                } else {
                    bottomHeader.setVisibility(View.GONE);
                    recyclerViewOrderDetail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });
    }
    public void downloadFile() {
        String invoice_url = getIntent().getStringExtra("invoice_url");

        if (invoice_url == null || invoice_url.isEmpty()){
            Toast.makeText(this,"Invoice Not available",Toast.LENGTH_LONG).show();
            return;
        }
            String orderID = getIntent().getStringExtra("orderID");
        String status = getIntent().getStringExtra("status");
        String fileName = "invoice_" + orderID + ".pdf";
        String DownloadUrl = Constants.BASEURL + "uploads/" + invoice_url; //"http://www.africau.edu/images/default/sample.pdf";
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setDescription("Sample Music File");   //appears the same in Notification bar while downloading
        request1.setTitle("File1.mp3");
        request1.setVisibleInDownloadsUi(false);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager1).enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ filename);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
//            DownloadSuccess();
        }
    }

    void downloadInvoice() {
        downloadFile();
//        if (!PowerPreference.getDefaultFile().getString("bill_url").isEmpty()) {
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maven.apache.org/maven-1.x/maven.pdf"));
//        startActivity(browserIntent);
    }
//        else {
//            Toast.makeText(this,"Invoice Not available",Toast.LENGTH_LONG).show();
//        }
//            //new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");
//        }

        void view()
        {
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(OrderDetailActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }

        class DownloadFile extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... strings) {
                String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
                String fileName = strings[1];  // -> maven.pdf
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "testthreepdf");
                folder.mkdir();

                File pdfFile = new File(folder, fileName);

                try{
                    pdfFile.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                FileDownloader.downloadFile(fileUrl, pdfFile);
                return null;
            }

    }
}
