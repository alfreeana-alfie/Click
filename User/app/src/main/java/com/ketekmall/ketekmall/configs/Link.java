package com.ketekmall.ketekmall.configs;

// import static com.ketekmall.ketekmall.config.Link.*;

import static com.ketekmall.ketekmall.configs.Constant.host;

public class Link {

    // AUTHENTICATION
    public static String LOGIN =  host + "login.php";
    public static String REGISTER = host + "register.php";
    public static String VERIFY = host + "verify.php";
    public static String EMAIL_RESET_PASSWORD = host + "sendEmail_getPassword.php";
    public static String EDIT_PASSWORD = host + "edit.php";

    // USER PROFILE
    public static String GET_PROFILE_DETAILS  = host + "read_detail.php";
    public static String EDIT_PROFILE_DETAILS = host + "edit_detail.php";
    public static String PROFILE_IMAGE_UPLOAD = host + "profile_image/upload.php";
    public static String GET_INCOME = host + "read_order_buyer_done_profile.php";
    public static String UPDATE_SELLER_PROFILE_DETAILS = host + "edit_detail_seller.php"; // SELLER USER PROFILE DETAILS

    // CHAT
    public static String ADD_NEW_CHAT = host + "add_chat.php";
    public static String CREATE_NEW_CHAT = host + "createChat.php";
    public static String EDIT_CHAT = host + "edit_chat.php";
    public static String UPDATE_CHAT = host + "updateChat.php";
    public static String GET_CHAT = host + "getChat.php";
    public static String GET_SINGLE_CHAT = host + "getChatSingle.php";
    public static String GET_CHAT_IS_READ = host + "getChatIsRead.php"; // CHECK WHETHER MESSAGE IS READ OR NOT
    public static String GET_ALL_CHAT = host + "getChatIsReadAll.php";

    // NOTIFICATION - ONESIGNAL
    public static String ADD_PLAYER_ID = host + "add_playerID.php";
    public static String GET_PLAYER_ID = host + "getPlayerID.php";
    public static String SEND_NOTIFICATION = host + "onesignal_noti.php";

    // CART
    public static String ADD_TO_CART = host + "add_to_cart.php";
    public static String DELETE_CART_PRODUCT = host + "delete_cart.php";
    public static String GET_CART = host + "readcart.php";
    public static String GET_SINGLE_CART_ITEM = host + "readcart_single.php";

    // TEMP CART
    public static String ADD_TO_TEMP_CART = host + "add_to_cart_temp_two.php";
    public static String GET_TEMP_CART = host + "readcart_temp.php";
    public static String DELETE_TEMP_CART = host + "delete_cart_temp.php";
    public static String DELETE_TEMP_USER_CART = host + "delete_cart_temp_user.php";

    // PROMOTION
    public static String GET_PROMOTION = host + "read_promo.php";

    // FAVOURITE
    public static String ADD_TO_FAVOURITE = host + "add_to_fav.php";
    public static String GET_FAVOURITE_LIST = host + "readfav.php";
    public static String GET_SEARCH_FAVOURITE_PRODUCT = host + "search/search_fav.php";
    public static String DELETE_FAVOURITE_PRODUCT = host + "delete_fav.php";

    // CATEGORY, FILTER & SEARCH PRODUCTS
    public static String GET_CATEGORY = host + "category/";
    public static String GET_SEARCH_CATEGORY = host + "search/";
    public static String GET_FILTER_DISTRICT = host + "filter_district/";
    public static String GET_FILTER_DIVISION = host + "filter_division/";
    public static String GET_SEARCH_FILTER_CATEGORY = host + "filter_search_division/";

    // RATING & SOLD COUNT
    public static String UPDATE_RATING = host + "edit_detail_rating.php";
    public static String UPDATE_PRODUCT_SOLD = host + "edit_detail_sold.php";

    // BOOST AD
    public static String ADD_TO_BOOST = host + "edit_boost_ad.php";
    public static String GET_BOOST_LIST = host + "read_products_boost.php";
    public static String CANCEL_BOOST = host + "edit_boost_ad_cancel.php";

    // PRODUCT
    public static String ADD_PRODUCT = host + "products/add.php";
    public static String DELETE_PRODUCT = host + "products/delete.php";
    public static String UPDATE_PRODUCT_DETAILS = host + "products/update.php";
    public static String GET_PRODUCT_LIST = host + "readuser.php";
    public static String GET_SELLER_PRODUCT_LIST = host + "readall_seller.php";
    public static String GET_PRODUCT_SOLD = host + "read_order_done_seller.php";
    public static String GET_SELLER_PRODUCT_SOLD_NO  = host + "read_order_done_seller_shop.php";
    public static String GET_PRODUCTS_THREE = host + "read_products_three.php";
    public static String GET_INCOME_PRODUCT_DETAILS = host + "read_order_two.php";

    // QUANTITY
    public static String INCREASE_QUANTITY_NO = host + "readcart_single_two.php";
    public static String DECREASE_QUANTITY_NO = host + "readcart_single_two_minus.php";

    // ORDER
    public static String DELETE_ORDER = host + "delete_order_buyer.php";
    public static String EDIT_ORDER_DETAILS = host + "edit_order.php";
    public static String UPDATE_ORDER_DETAILS = host + "updateOrder.php";
    public static String GET_SELLER_ORDER_LIST = host + "read_order_buyer_done_two.php";
    public static String GET_BUYER_ORDER_LIST = host + "read_order_buyer_done.php";

    // CHECKOUT
    public static String ADD_TO_CHECKOUT = host + "add_to_checkout.php";

    // TRACKING NO
    public static String UPDATE_TRACKING_NO = host + "edit_tracking_no.php";

    // CONNOTE NO - FROM DB
    public static String CREATE_CONNOTE_NO = host + "createConnote.php";
    public static String GET_CONNOTE_NO = host + "getConnote.php";

    // EMAIL
    public static String EMAIL_ORDER_REJECTED = host + "sendEmail_product_reject.php"; // ORDER GET REJECTED/ CANCELLED
    public static String EMAIL_ORDER_ACCEPTED = host + "sendEmail_product_accept.php"; // ORDER GET ACCEPTED
    public static String EMAIL_ORDER_SUMMARY = host + "sendEmail_buyer_three.php";
    public static String EMAIL_ORDER_RECEIPT = host + "sendEmail_buyer.php";
    public static String EMAIL_NOTIFY_SELLER = host + "sendEmail_seller.php";

    // PAYMENT iPay88 - FROM DB
    public static String GET_PAYMENT_DETAILS = host + "getPayment.php";

    // IMAGES
    public static String IMAGE_DEFAULT = host + "profile_image/main_photo.png"; // DEFAULT USER IMAGE
    public static String ADD_TEMP_IMAGE = host + "products/add_temp.php";
    public static String DELETE_TEMP_IMAGE = host + "products/delete_temp.php"; // DELETE TEMP DATA AND UNLINK IMAGES
    public static String DELETE_DB_TEMP_IMAGES = host + "products/delete_db_temp.php"; // DELETE ONLY TEMP DATA, NO UNLINK IMAGES

    // REVIEW
    public static String ADD_NEW_REVIEW = host + "add_review.php";
    public static String UPDATE_REVIEW = host + "edit_remarks_done.php";
    public static String GET_REVIEW_LIST = host + "read_review.php";
    public static String GET_SELLER_SIDE_REVIEW = host + "read_review_seller.php"; // SELLER SIDE
    public static String GET_BUYER_SIDE_REVIEW = host + "read_review_user.php"; // BUYER SIDE
    public static String GET_PRODUCT_REVIEW = host + "read_products_review.php";

    // POSLAJU
    public static String PRE_ACCEPTANCE_SINGLE = "https://apis.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1"; // PRODUCTION
    public static String POSLAJU_DOMESTIC_BY_POSTCODE = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/poslajudomesticbypostcode/v1"; // STAGING
    public static String ROUTING_CODE = "https://apis.pos.com.my/apigateway/as01/api/routingcode/v1"; // PRODUCTION
    public static String GENERATE_CONNOTE_NO = "https://apis.pos.com.my/apigateway/as01/api/genconnote/v1"; // PRODUCTION
}
