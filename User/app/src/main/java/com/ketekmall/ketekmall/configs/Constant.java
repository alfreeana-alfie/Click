package com.ketekmall.ketekmall.configs;

import android.app.Activity;
import android.text.Layout;
import android.view.inputmethod.InputMethodManager;

import com.ketekmall.ketekmall.R;

import java.util.regex.Pattern;

public class Constant {
    public static String HOST = "https://ketekmall.com/ketekmall/";
    public static final String ONESIGNAL_APP_ID = "6236bfc3-df4d-4f44-82d6-754332044779";

    public static final long serialVersionUID = 0;

    public static final int RC_SIGN_IN = 1;
    public static Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");
    public static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static int PICK_IMAGE_REQUEST = 22;

    public static int checkout = R.layout.checkout;
    public static int home = R.layout.homepage;

    // Users variable
    public static String sID = "id";
    public static String sNAME = "name";
    public static String sEMAIL = "email";
    public static String sPHONE_NO = "phone_no";
    public static String sPASSWORD = "password";
    public static String sADDRESS_01 = "address_01";
    public static String sADDRESS_02 = "address_02";
    public static String sDIVISION = "division";
    public static String sDISTRICT = "district";
    public static String sPOSTCODE = "postcode";
    public static String sBIRTHDAY = "birthday";
    public static String sGENDER = "gender";
    public static String sPHOTO = "photo";
    public static String sPHOTO02 = "photo02";
    public static String sPHOTO03 = "photo03";
    public static String sPHOTO04 = "photo04";
    public static String sPHOTO05 = "photo05";
    public static String sPHOTO0 = "photo0";
    public static String sCREATION_DATE = "creation_date";
    public static String sIC_NO = "ic_no";
    public static String sBANK_NAME = "bank_name";
    public static String sBANK_ACCOUNT = "bank_acc";
    public static String sVERIFICATION = "verification";
    public static String sTOKEN = "token";
    public static String sFILENAME = "filename";
    public static String sFILENAME_TEMP = "filename_temp";


    // Cart, Order, Product Variables
    public static String sORDER_ID = "order_id";
    public static String sCUSTOMER_ID = "customer_id";
    public static String sCUSTOMER_NAME = "customer_name";
    public static String sMAIN_CATEGORY = "main_category";
    public static String sSUB_CATEGORY = "sub_category";
    public static String sAD_DETAIL = "ad_detail";
    public static String sBRAND_MAT = "brand_material";
    public static String sINNER_MAT = "inner_material";
    public static String sSTOCK = "stock";
    public static String sDESCRIPTION = "description";
    public static String sPRICE = "price";
    public static String sSELLER_ID = "seller_id";
    public static String sITEM_ID = "item_id";
    public static String sQUANTITY = "quantity";
    public static String sWEIGHT = "weight";
    public static String sSELLER_DIVISION = "seller_division";
    public static String sSELLER_DISTRICT = "seller_district";
    public static String sSELLER_POSTCODE = "seller_postcode";
    public static String sREMARKS = "remarks";
    public static String sORDER_DATE = "order_date";
    public static String sDATE = "date";
    public static String sSTATUS = "status";
    public static String sTRACKING_NO = "tracking_no";
    public static String sDELIVERY_PRICE = "delivery_price";
    public static String sDELIVERY_DATE = "delivery_date";
    public static String sDELIVERY_ADDRESS = "delivery_addr";
    public static String sREF_NO = "refno";
    public static String sPICK_UP = "pick_up";
    public static String sTOTAL = "total";
    public static String sREVIEW = "review";
    public static String sRATING = "rating";
    public static String sSOLD = "sold";
    public static String sMAX_ORDER = "max_order";
    public static String sUSER_ID = "user_id";

    public static String sUPPER_WEIGHT = "Weight";
    public static String sTOT_AMO = "TotalAmount";

    // Pos Laju
    public static String sPROD_CODE = "ProductCode";
    public static String sCON_NO = "ConnoteNo";
    public static String sCON_DATE = "ConnoteDate";
    public static String sSEND_NAME = "SenderName";
    public static String sSEND_PHONE = "SenderPhone";
    public static String sSEND_POSTCODE = "SenderPostcode";
    public static String sRECI_ACC_NO = "RecipientAccountNo";


    // Seller
    public static String sSELLER_NAME = "seller_name";
    public static String sSELLER_EMAIL = "seller_email";
    public static String sSELLER_PHOTO = "seller_photo";
    public static String sSELLER_LOCATION = "seller_location";
    public static String sSELLER_PHONE = "seller_phone";

    // Chat variables
    public static String sUSER_CHAT_WITH = "user_chatwith";
    public static String sCHAT_KEY = "chat_key";
    public static String sIS_READ = "is_read";

    // CHAT CONTENT
    public static String sUPPER_NAME = "Name";
    public static String sOS_USER_ID = "UserID";
    public static String sCHAT_USER_PHOTO = "UserPhoto";
    public static String sCHAT_WITH = "ChatWith";
    public static String sCHAT_WITH_ID = "ChatWithID";
    public static String sCHAT_WITH_PHOTO = "ChatWithPhoto";
    public static String sCHAT_CONTENT = "Content";
    public static String sCHAT_IS_READ = "IsRead";
    public static String sCHAT_TYPE = "Type";
    public static String sCHAT_CREATED_DATE_TIME = "CreatedDateTime";

    public static String sNULL = "null";
    public static String sXUserKey = "X-User-Key";

    public static String sPLAYER_ID = "PlayerID";
    public static String sWORDS = "Words";
    public static String sAUTH = "Authorization";
    public static String sCON_TYPE = "Content-Type";

    public static String sENG = "en";
    public static String sMS = "ms";

    public static String sSUCCESS = "success";
    public static String sREAD = "read";
    public static String sONE = "1";
    public static String sDT_FORMAT = "yyyy-MM-dd";
    public static String s2DP = "%.2f";
    public static String s1DP = "%.1f";

    // Server Key
    public static String serverPoslajuDomesticbyPostcode = "N1hHVHJFRW95cjRkQ0NyR3dialdrZUF4NGxaNm9Na1U=";

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    }
}
