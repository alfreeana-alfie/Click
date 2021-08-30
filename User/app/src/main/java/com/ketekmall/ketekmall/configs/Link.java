package com.ketekmall.ketekmall.configs;

// import static com.ketekmall.ketekmall.config.Link.*;

import com.firebase.client.Firebase;

import static com.ketekmall.ketekmall.configs.Constant.HOST;

public class Link {

    // AUTHENTICATION
    public static String LOGIN =  HOST + "login.php";
    public static String REGISTER = HOST + "register.php";
    public static String VERIFY = HOST + "verify.php";
    public static String EMAIL_RESET_PASSWORD = HOST + "sendEmail_getPassword.php";
    public static String EDIT_PASSWORD = HOST + "edit.php";

    // USER PROFILE
    public static String GET_PROFILE_DETAILS  = "https://ketekmall.com/ketekmall/read_detail.php";
    public static String EDIT_PROFILE_DETAILS = HOST + "edit_detail.php";
    public static String PROFILE_IMAGE_UPLOAD = HOST + "profile_image/upload.php";
    public static String GET_INCOME = HOST + "read_order_buyer_done_profile.php";
    public static String UPDATE_SELLER_PROFILE_DETAILS = HOST + "edit_detail_seller.php"; // SELLER USER PROFILE DETAILS

    // CHAT
    public static String ADD_NEW_CHAT = HOST + "add_chat.php";
    public static String CREATE_NEW_CHAT = HOST + "createChat.php";
    public static String EDIT_CHAT = HOST + "edit_chat.php";
    public static String UPDATE_CHAT = HOST + "updateChat.php";
    public static String GET_CHAT = HOST + "getChat.php";
    public static String GET_SINGLE_CHAT = HOST + "getChatSingle.php";
    public static String GET_CHAT_IS_READ = HOST + "getChatIsRead.php"; // CHECK WHETHER MESSAGE IS READ OR NOT
    public static String GET_ALL_CHAT = HOST + "getChatIsReadAll.php";

    // NOTIFICATION - ONESIGNAL
    public static String ADD_PLAYER_ID = HOST + "add_playerID.php";
    public static String GET_PLAYER_ID = HOST + "getPlayerID.php";
    public static String SEND_NOTIFICATION = HOST + "onesignal_noti.php";

    // CART
    public static String ADD_TO_CART = HOST + "add_to_cart.php";
    public static String DELETE_CART_PRODUCT = HOST + "delete_cart.php";
    public static String GET_CART = HOST + "readcart.php";
    public static String GET_SINGLE_CART_ITEM = HOST + "readcart_single.php";

    // TEMP CART
    public static String ADD_TO_TEMP_CART = HOST + "add_to_cart_temp_two.php";
    public static String GET_TEMP_CART = HOST + "readcart_temp.php";
    public static String DELETE_TEMP_CART = HOST + "delete_cart_temp.php";
    public static String DELETE_TEMP_USER_CART = HOST + "delete_cart_temp_user.php";

    // PROMOTION
    public static String GET_PROMOTION = HOST + "read_promo.php";

    // FAVOURITE
    public static String ADD_TO_FAVOURITE = HOST + "add_to_fav.php";
    public static String GET_FAVOURITE_LIST = HOST + "readfav.php";
    public static String GET_SEARCH_FAVOURITE_PRODUCT = HOST + "search/search_fav.php";
    public static String DELETE_FAVOURITE_PRODUCT = HOST + "delete_fav.php";

    // CATEGORY, FILTER & SEARCH PRODUCTS
    public static String GET_CATEGORY = HOST + "category/";
    public static String GET_SEARCH_CATEGORY = HOST + "search/";
    public static String GET_FILTER_DISTRICT = HOST + "filter_district/";
    public static String GET_FILTER_DIVISION = HOST + "filter_division/";
    public static String GET_SEARCH_FILTER_CATEGORY = HOST + "filter_search_division/";

    // RATING & SOLD COUNT
    public static String UPDATE_RATING = HOST + "edit_detail_rating.php";
    public static String UPDATE_PRODUCT_SOLD = HOST + "edit_detail_sold.php";

    // BOOST AD
    public static String ADD_TO_BOOST = HOST + "edit_boost_ad.php";
    public static String GET_BOOST_LIST = HOST + "read_products_boost.php";
    public static String CANCEL_BOOST = HOST + "edit_boost_ad_cancel.php";

    // PRODUCT
    public static String ADD_PRODUCT = HOST + "products/add.php";
    public static String DELETE_PRODUCT = HOST + "products/delete.php";
    public static String UPDATE_PRODUCT_DETAILS = HOST + "products/update.php";
    public static String GET_PRODUCT_LIST = HOST + "readuser.php";
    public static String GET_SELLER_PRODUCT_LIST = HOST + "readall_seller.php";
    public static String GET_PRODUCT_SOLD = HOST + "read_order_done_seller.php";
    public static String GET_SELLER_PRODUCT_SOLD_NO  = HOST + "read_order_done_seller_shop.php";
    public static String GET_PRODUCTS_THREE = HOST + "read_products_three.php";
    public static String GET_INCOME_PRODUCT_DETAILS = HOST + "read_order_two.php";

    // QUANTITY
    public static String INCREASE_QUANTITY_NO = HOST + "readcart_single_two.php";
    public static String DECREASE_QUANTITY_NO = HOST + "readcart_single_two_minus.php";

    // ORDER
    public static String DELETE_ORDER = HOST + "delete_order_buyer.php";
    public static String EDIT_ORDER_DETAILS = HOST + "edit_order.php";
    public static String UPDATE_ORDER_DETAILS = HOST + "updateOrder.php";
    public static String GET_SELLER_ORDER_LIST = HOST + "read_order_buyer_done_two.php";
    public static String GET_BUYER_ORDER_LIST = HOST + "read_order_buyer_done.php";

    // CHECKOUT
    public static String ADD_TO_CHECKOUT = HOST + "add_to_checkout.php";

    // TRACKING NO
    public static String UPDATE_TRACKING_NO = HOST + "edit_tracking_no.php";

    // CONNOTE NO - FROM DB
    public static String CREATE_CONNOTE_NO = HOST + "createConnote.php";
    public static String GET_CONNOTE_NO = HOST + "getConnote.php";

    // EMAIL
    public static String EMAIL_ORDER_REJECTED = HOST + "sendEmail_product_reject.php"; // ORDER GET REJECTED/ CANCELLED
    public static String EMAIL_ORDER_ACCEPTED = HOST + "sendEmail_product_accept.php"; // ORDER GET ACCEPTED
    public static String EMAIL_ORDER_SUMMARY = HOST + "sendEmail_buyer_three.php";
    public static String EMAIL_ORDER_RECEIPT = HOST + "sendEmail_buyer.php";
    public static String EMAIL_NOTIFY_SELLER = HOST + "sendEmail_seller.php";

    // PAYMENT iPay88 - FROM DB
    public static String GET_PAYMENT_DETAILS = HOST + "getPayment.php";

    // IMAGES
    public static String IMAGE_DEFAULT = HOST + "profile_image/main_photo.png"; // DEFAULT USER IMAGE
    public static String ADD_TEMP_IMAGE = HOST + "products/add_temp.php";
    public static String DELETE_TEMP_IMAGE = HOST + "products/delete_temp.php"; // DELETE TEMP DATA AND UNLINK IMAGES
    public static String DELETE_DB_TEMP_IMAGES = HOST + "products/delete_db_temp.php"; // DELETE ONLY TEMP DATA, NO UNLINK IMAGES

    // REVIEW
    public static String ADD_NEW_REVIEW = HOST + "add_review.php";
    public static String UPDATE_REVIEW = HOST + "edit_remarks_done.php";
    public static String GET_REVIEW_LIST = HOST + "read_review.php";
    public static String GET_SELLER_SIDE_REVIEW = HOST + "read_review_seller.php"; // SELLER SIDE
    public static String GET_BUYER_SIDE_REVIEW = HOST + "read_review_user.php"; // BUYER SIDE
    public static String GET_PRODUCT_REVIEW = HOST + "read_products_review.php";

    // POSLAJU
    public static String PRE_ACCEPTANCE_SINGLE = "https://apis.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1"; // PRODUCTION
    public static String POSLAJU_DOMESTIC_BY_POSTCODE = "https://apis.pos.com.my/apigateway/as2corporate/api/poslajubypostcodedomestic/v1"; // PRODUCTION
    public static String ROUTING_CODE = "https://apis.pos.com.my/apigateway/as01/api/routingcode/v1"; // PRODUCTION
    public static String GENERATE_CONNOTE_NO = "https://apis.pos.com.my/apigateway/as01/api/genconnote/v1"; // PRODUCTION

    // FIREBASE
    public static String FIREBASE_USER = "https://click-1595830894120.firebaseio.com/users.json";
}
