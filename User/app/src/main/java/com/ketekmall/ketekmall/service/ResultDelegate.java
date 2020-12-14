package com.ketekmall.ketekmall.service;

import android.util.Log;

import com.ipay.IPayIHResultDelegate;

import java.io.Serializable;

/**
 * Created by pnduy on 3/9/2016.
 */
public class ResultDelegate implements IPayIHResultDelegate, Serializable {
//    public static final long serialVersionUID = 42L;
    private static final transient String TAG = ResultDelegate.class.getSimpleName();

    @Override
    public void onRequeryResult(String s, String s1, String s2, String s3) {
        Log.d(TAG, "REQUERY");
    }

    @Override
    public void onConnectionError(String s, String s1, String s2, String s3, String s4, String s5, String s6) {
        Log.d(TAG, "CONNECTION ERROR");
    }

    @Override
    public void onPaymentSucceeded(String s, String s1, String s2, String s3, String s4) {
        Log.d(TAG, "SUCCESS");
    }

    @Override
    public void onPaymentFailed(String s, String s1, String s2, String s3, String s4) {
        Log.d(TAG, "FAILED");

    }

    @Override
    public void onPaymentCanceled(String s, String s1, String s2, String s3, String s4) {
        Log.d(TAG, "CANCELED");
    }
//    @Override
//    public void onPaymentSucceeded(String TransId, String RefNo, String Amount,
//                                   String Remark, String AuthCode, String CCName,
//                                   String CCNo, String S_bankname, String S_country) {
////        ActivityIpay88Payment.resultTitle = "SUCCESS";
////        ActivityIpay88Payment.resultInfo = "You have successfully completed your transaction.";
//
//        String extra = "";
//        extra = extra + "TransId\t= " + TransId + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "AuthCode\t= " + AuthCode + "\n";
////        extra = extra + "CCName\t= " + CCName + "\n";
////        extra = extra + "CCNo\t= " + CCNo + "\n";
////        extra = extra + "S_bankname\t= " + S_bankname + "\n";
////        extra = extra + "S_country\t= " + S_country;
////        ActivityIpay88Payment.resultExtra = extra;
//        Log.d(TAG, "Remark: " + Remark);
//    }
//
//    @Override
//    public void onPaymentFailed(String TransId, String RefNo, String Amount,
//                                String Remark, String ErrDesc, String CCName,
//                                String CCNo, String S_bankname, String S_country) {
////        ActivityIpay88Payment.resultTitle = "FAILURE";
////        ActivityIpay88Payment.resultInfo = ErrDesc;
//
//        String extra = "";
//        extra = extra + "TransId\t= " + TransId + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "ErrDesc\t= " + ErrDesc + "\n";
////        extra = extra + "CCName\t= " + CCName + "\n";
////        extra = extra + "CCNo\t= " + CCNo + "\n";
////        extra = extra + "S_bankname\t= " + S_bankname + "\n";
////        extra = extra + "S_country\t= " + S_country;
////        ActivityIpay88Payment.resultExtra = extra;
//        Log.d(TAG, "ErrDesc: " + ErrDesc);
//        Log.d(TAG, "Remark: " + Remark);
//    }
//
//    @Override
//    public void onPaymentCanceled(String TransId, String RefNo, String Amount,
//                                  String Remark, String ErrDesc, String CCName,
//                                  String CCNo, String S_bankname, String S_country) {
//
////        ActivityIpay88Payment.resultTitle = "CANCELED";
////        ActivityIpay88Payment.resultInfo = "The transaction has been cancelled.";
//
//        String extra = "";
//        extra = extra + "TransId\t= " + TransId + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "ErrDesc\t= " + ErrDesc + "\n";
////        extra = extra + "CCName\t= " + CCName + "\n";
////        extra = extra + "CCNo\t= " + CCNo + "\n";
////        extra = extra + "S_bankname\t= " + S_bankname + "\n";
////        extra = extra + "S_country\t= " + S_country;
////        ActivityIpay88Payment.resultExtra = extra;
//        Log.d(TAG, "ErrDesc: " + ErrDesc);
//        Log.d(TAG, "Remark: " + Remark);
//
//    }
//
//    @Override
//    public void onRequeryResult(String MerchantCode, String RefNo,
//                                String Amount, String Result) {
////        ActivityIpay88Payment.resultTitle = "Requery Result";
////        ActivityIpay88Payment.resultInfo = "";
//
//        String extra = "";
//        extra = extra + "MerchantCode\t= " + MerchantCode + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Result\t= " + Result;
////        ActivityIpay88Payment.resultExtra = extra;
//
//    }
//
//    @Override
//    public void onConnectionError(String merchantCode, String merchantKey,
//                                  String RefNo, String Amount, String Remark, String lang, String country) {
////        ActivityIpay88Payment.resultTitle = "CONNECTION ERROR";
////        ActivityIpay88Payment.resultInfo = "The transaction has been unsuccessful.";
//
//        String extra = "";
//        extra = extra + "Merchant Code\t= " + merchantCode + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "Language\t= " + lang + "\n";
//        extra = extra + "Country\t= " + country + "\n";
////        extra = extra + "ErrDesc\t= " + "Had connection error while connecting to IPay server";
////        ActivityIpay88Payment.resultExtra = extra;
//    }
}
