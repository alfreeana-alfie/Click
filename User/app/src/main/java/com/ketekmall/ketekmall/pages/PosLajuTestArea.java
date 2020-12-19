package com.ketekmall.ketekmall.pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ketekmall.ketekmall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class PosLajuTestArea extends AppCompatActivity {
    // Header
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET};

    // Variables
    TextView Status;

    ImageView BarCodeViewResult;

    int pageWidth = 420;
    int pageHeight = 595;

    Bitmap PosLajuBitMap, ScaledPosLajuBitMap;



    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poslaju_home);

        Status = findViewById(R.id.status);
        BarCodeViewResult = findViewById(R.id.BarCodeViewResult);
        PosLajuBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.poslaju_black);
        ScaledPosLajuBitMap = Bitmap.createScaledBitmap(PosLajuBitMap, 100, 45, false);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        Status.setText(date);
        ActivityCompat.requestPermissions(PosLajuTestArea.this, PERMISSIONS, 112);
        Log.v("TAG", "onCreate() Method invoked ");
        PreAcceptanceSingle();

//        if(!hasPermissions(PosLajuTestArea.this, PERMISSIONS)){
//            Log.v("TAG", "download() Method DON'T HAVE PERMISSIONS ");
//
//        }else{
//            GeneratePDF(date,
//                    "1.01",
//                    "654",
//                    "Alfreeana",
//                    "0138940023",
//                    "LOT 245, NO. 3G",
//                    "96000",
//                    "NANA",
//                    "0138940023",
//                    "93050",
//                    "312323213",
//                    "LOT 245, NO. 3G",
//                    "LORONG SIBU JAYA 6",
//                    "SIBU",
//                    "SARAWAK",
//                    "ann@gmail.com",
//                    "092132",
//                    "Parcel",
//                    "KCU-SB-SBW",
//                    "ER000249760MY",
//                    date);
//        }

//
    }

    private void GenerateBarCode(){
        try {
            String productId = "ER000249760MY";
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128,128, 37, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            BarCodeViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void GeneratePDF(String ShipDate,
                             String Weight,
                             String OrderID,
                             String SenderName,
                             String SenderPhone,
                             String SenderAddress,
                             String SenderPostcode,
                             String RecipientName,
                             String RecipientPhone,
                             String RecipientPostcode,
                             String RecipientAccoutNo,
                             String RecipientAddress01,
                             String RecipientAddress02,
                             String RecipientCity,
                             String RecipientState,
                             String RecipientEmail,
                             String ProductCode,
                             String Type,
                             String RoutingCode,
                             String ConnoteNo,
                             String ConnoteDate){
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Paint TitleTag = new Paint();
        Paint Details = new Paint();

        //Design - Outer Border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(5, 5, pageWidth-5, pageHeight-5, paint);

        canvas.drawBitmap(ScaledPosLajuBitMap, 13, 20, paint);

        // Logo & Barcode
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 9, pageWidth-12, 75, paint);

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(ConnoteNo, BarcodeFormat.CODE_128,128, 37, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            canvas.drawBitmap(bitmap,230, 14, paint);
            Details.setTextAlign(Paint.Align.LEFT);
            Details.setColor(Color.BLACK);
            Details.setTextSize(12f);
            Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(ConnoteNo, 270, 66, Details);

            BarCodeViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Order Details - 01
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 82, 271, 174, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 82, 271, 97, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Order Details", 14, 93, TitleTag);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Ship By Date:", 14, 108, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Weight (kg):", 14, 119, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Order ID:", 14, 130, Details);

        // RIGHT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(ShipDate, 84, 108, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(Weight, 84, 119, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(OrderID, 84, 130, Details);

        // Order Details - 02
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(278, 82, 408, 174, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(278, 82, 408, 97, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Order Details (Courier)", 279, 93, TitleTag);

        // LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(14f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Account Number:", 281, 111, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(14f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("8800472220", 281, 126, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Product:", 281, 140, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Domestic", 281, 150, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Type:", 281, 162, Details);

        // Right
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Courier Charges", 320, 140, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(Type, 310, 162, Details);

        // Sender, Receiver, POD Details
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 177, 271, pageHeight-18, paint);
        // Sender Details
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 177, 271, 333, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 177, 271, 192, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Sender Details (Pengirim)", 14, 188, TitleTag);

        // LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Name:", 14, 203, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Phone:", 14, 238, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Address:", 14, 253, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Postcode:", 14, 330, Details);

        // RIGHT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(SenderName, 64, 203, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(SenderPhone, 64, 238, Details);

        TextPaint mTextPaintSender=new TextPaint();
        mTextPaintSender.setTextSize(10f);
        mTextPaintSender.setTextAlign(Paint.Align.LEFT);
        StaticLayout mTextLayoutSender = new StaticLayout(SenderAddress, mTextPaintSender, 170, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();

        canvas.translate(64, 253);
        mTextLayoutSender.draw(canvas);
        canvas.restore();

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(SenderPostcode, 64, 330, Details);

        // Recipient Details
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 333, 271, 489, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 333, 271, 348, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Recipient Details (Penerima)", 14, 344, TitleTag);

        //LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Name:", 14, 359, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Phone:", 14, 394, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Address:", 14, 409, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Postcode:", 14, 486, Details);

        // RIGHT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(RecipientName, 64, 359, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(RecipientPhone, 64, 394, Details);

        TextPaint mTextPaintRecipient=new TextPaint();
        mTextPaintRecipient.setTextSize(10f);
        mTextPaintRecipient.setTextAlign(Paint.Align.LEFT);
        StaticLayout mTextLayoutRecipient = new StaticLayout(RecipientAddress01 + ", " + RecipientAddress02, mTextPaintRecipient, 170, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();

        canvas.translate(64, 409);
        mTextLayoutRecipient.draw(canvas);
        canvas.restore();

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(RecipientPostcode, 64, 486, Details);

        // POD
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 489, 271, pageHeight-18, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 489, 271, 504, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("POD", 14, 500, TitleTag);

        //LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Name:", 14, 515, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("I.C.:", 14, 530, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Signature:", 14, 545, Details);

        // QR CODE
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 1.5);
        canvas.drawRect(278, 177, pageWidth-12, pageHeight-18, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0.5);
        canvas.drawRect(278, 192, pageWidth-12, pageHeight-18, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0.5);
        canvas.drawRect(278, 242, pageWidth-12, pageHeight-18, paint);

        TextPaint mTextPaint=new TextPaint();
        mTextPaint.setTextSize(22f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        StaticLayout mTextLayout = new StaticLayout(RoutingCode, mTextPaint, 80, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();

        canvas.translate(342, 192);
        mTextLayout.draw(canvas);
        canvas.restore();

        Details.setTextAlign(Paint.Align.CENTER);
        Details.setColor(Color.BLACK);
        Details.setTextSize(32f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(RecipientPostcode, 342, 292, Details);

        try {
            String productId = "A2^"+
                    ConnoteNo +
                    "^" +
                    ConnoteDate +
                    "^" +
                    "MY^" +
                    ProductCode +
                    "^" +
                    SenderName +
                    "^" +
                    SenderPhone +
                    "^"+
                    "^" +
                    SenderPostcode +
                    "^" +
                    RecipientAccoutNo +
                    "^" +
                    RecipientName +
                    "^" +
                    "^" +
                    RecipientAddress01 +
                    "^" +
                    RecipientAddress02 +
                    "^" +
                    RecipientPostcode +
                    "^" +
                    RecipientCity +
                    "^" +
                    RecipientState +
                    "^" +
                    RecipientPhone +
                    "^" +
                    RecipientEmail +
                    "^" +
                    Weight +
                    "^" +
                    "^" +
                    "^" +
                    "^" +
                    "^" +
                    "^" +
                    Type +
                    "^";
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.QR_CODE,15, 15, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            canvas.drawBitmap(bitmap,310, 430, paint);

            BarCodeViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            String productId = "ER000249760MY";
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128,128, 37, hintMap);
            int width = 110;
            int height = 37;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            canvas.drawBitmap(bitmap,285, 515, paint);
            Details.setTextAlign(Paint.Align.LEFT);
            Details.setColor(Color.BLACK);
            Details.setTextSize(12f);
            Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(productId, 295, 565, Details);

            BarCodeViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        document.finishPage(page);
        String directory_path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        File file = new File(directory_path);
        try {
            file.mkdirs();
            File filePath = new File(file,"test.pdf");
            filePath.createNewFile();
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();


    }

    String HTTP_PreAcceptanceSingle = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1";
    String serverKey_PreAcceptanceSingle = "M1djdzdrbTZod0pXOTZQdnFWVU5jWVpGNU9nUDVzb0M=";
    private void PreAcceptanceSingle() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_PreAcceptanceSingle +
                "?subscriptionCode=admin@ketekmall.com" +
                "&requireToPickup=FALSE"+
                "&requireWebHook=FALSE"+
                "&accountNo=8800546487"+
                "&callerName=Nana"+
                "&callerPhone=0138940023"+
                "&pickupLocationID=31231"+
                "&pickupLocationName=Sibu"+
                "&contactPerson=0138940023"+
                "&phoneNo=0138940023"+
                "&pickupAddress=LOT245, NO.3G, LORONG SIBU JAYA 6"+
                "&postCode=96000"+
                "&ItemType=2"+
                "&totalQuantityToPickup=1"+
                "&totalWeight=1.01"+
                "&consignmentNoteNumber=ER000249760MY"+
                "&PaymentType=2"+
                "&amount=13.90"+
                "&readyToCollectAt=12:00 AM"+
                "&closeAt=12:00 AM"+
                "&receiverName=Kalina"+
                "&receiverID=14323"+
                "&receiverAddress=LOT245, NO.3G, LORONG SIBU JAYA 6"+
                "&receiverPostCode=96000"+
                "&receiverEmail=ann@gmail.com"+
                "&receiverPhone01=0138940023"+
                "&receiverPhone02=0138940023"+
                "&sellerReferenceNo=123123"+
                "&itemDescription=sad"+
                "&sellerOrderNo=23123"+
                "&comments="+
                "&pickupDistrict=Sibu Jaya"+
                "&pickupProvince=Sibu"+
                "&pickupEmail=ann@gmail.com"+
                "&pickupCountry=MY"+
                "&pickupLocation="+
                "&receiverFname=Kalina"+
                "&receiverLname=Ann"+
                "&receiverAddress2=Lorong Sibu Jaya 6"+
                "&receiverDistrict=Lorong Sibu Jaya 6"+
                "&receiverProvince=Sibu Jaya"+
                "&receiverCity=Sibu"+
                "&receiverCountry=MY"+
                "&ShipmentName=PosLaju"+
                "&currency=MYR"+
                "&countryCode=MY",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String RoutingCode       = jsonObject.getString("RoutingCode");

                            Log.i("ObjectRequest", RoutingCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_PreAcceptanceSingle);
                return params;
            }

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("subscriptionCode", "admin@ketekmall.com");
//                params.put("requireToPickup", "FALSE");
//                params.put("requireWebHook", "FALSE");
//                params.put("accountNo", "4799110862895245");
//                params.put("callerName", "Alfreeana Alfie");
//                params.put("callerPhone", "0138940023");
//                params.put("pickupLocationID", "M34123998");
//                params.put("pickupLocationName", "Sibu");
//                params.put("contactPerson", "0138940023");
//                params.put("phoneNo", "0138940023");
//                params.put("pickupAddress", "LOT245, NO.3G, LORONG SIBU JAYA 6");
//                params.put("ItemType", "2");
//                params.put("totalQuantityToPickup", "2");
//                params.put("totalWeight", "1.01");
//                params.put("PaymentType", "2");
//                params.put("Amount", "10.00");
//                params.put("readyToCollectAt", "12.00PM");
//                params.put("closeAt", "6.00PM");
//                params.put("receiverName", "Kalina Ann");
//                params.put("receiverID", "");
//                params.put("receiverAddress", "LO24, NO.3, LORONG SIBU JAYA 6");
//                params.put("receiverPostCode", "96000");
//                params.put("receiverEmail", "");
//                params.put("receiverPhone01", "0138940023");
//                params.put("receiverPhone02", "0189232002");
//                params.put("sellerReferenceNo", "");
//                params.put("itemDescription", "");
//                params.put("sellerOrderNo", "");
//                params.put("comments", "Fragile");
//                params.put("pickupDistrict", "SIBU JAYA");
//                params.put("pickupProvince", "SIBU");
//                params.put("pickupEmail", "annkalina53@gmail.com");
//                params.put("pickupCountry", "MY");
//                params.put("pickupLocation", "");
//                params.put("receiverFname", "Kalina");
//                params.put("receiverLname", "Ann");
//                params.put("receiverAddress2", "");
//                params.put("receiverDistrict", "Lorong Sibu Jaya 6");
//                params.put("receiverProvince", "Sibu Jaya");
//                params.put("receiverCity", "Sibu");
//                params.put("receiverCountry", "MY");
//                params.put("packDesc", "");
//                params.put("packVol", "");
//                params.put("packLeng", "");
//                params.put("postCode", "");
//                params.put("ConsignmentNoteNumber", "ER000249760MY");
//                params.put("packWidth", "");
//                params.put("packHeight", "");
//                params.put("packTotalitem", "");
//                params.put("orderDate", "");
//                params.put("packDeliveryType", "");
//                params.put("ShipmentName", "PosLaju");
//                params.put("pickupProv", "");
//                params.put("deliveryProv", "");
//                params.put("postalCode", "");
//                params.put("currency", "MYR");
//                params.put("countryCode", "MY");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String HTTP_PoslajuDomesticbyPostcode = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/poslajudomesticbypostcode/v1";
    String serverKey_PoslajuDomesticbyPostcode = "a1g2cmM2VmowNm00N1lZekFmTGR0MldpRHhKaFRHSks=";
    private void PostCodeDomestic() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HTTP_PoslajuDomesticbyPostcode +
                "?postcodeFrom=93050" +
                "&postcodeTo=96000"+
                "&Weight=1.01",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", "PostCode" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_PoslajuDomesticbyPostcode);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("postcodeFrom", "93050");
                params.put("postcodeTo", "96000");
                params.put("Weight", "1.01");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String HTTP_RoutingCode = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/routingcode/v1";
    String serverKey_RoutingCode = "UVREb1NFZkJqZEd6YXFRWUg2c3BPMTlRbDdTS1I4eEM=";
    private void RoutingCode() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HTTP_RoutingCode +
                "?Origin=93050" +
                "&Destination=96000",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", "RoutingCode" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_RoutingCode);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Origin", "93050");
                params.put("Destination", "96000");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}