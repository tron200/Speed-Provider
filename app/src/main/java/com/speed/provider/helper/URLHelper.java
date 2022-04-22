package com.speed.provider.helper;


public interface URLHelper {
    String APP_URL = "";
    int client_id = 2;
    String client_secret = "WifS1rMi3LvuorP1G2UdtKZairUNSH2iMqrKivPf";

    String BASE = "https://speed-limo.com/";
    String STRIPE_TOKEN = "pk_test_LTXZTPA9yepu9dEodKsJm6GA";
    String GET_USERREVIEW = BASE + "api/provider/review";
    String GET_NOTIFICATIONS = BASE + "api/provider/notification";
    String UPCOMING_TRIP_DETAILS = BASE + "api/provider/requests/upcoming/details";
    String UPCOMING_TRIPS = BASE + "api/provider/requests/upcoming";
    String CANCEL_REQUEST_API = BASE + "api/provider/cancel";
    String TARGET_API = BASE + "api/provider/target";
    String RESET_PASSWORD = BASE + "api/provider/reset/password";
    String FORGET_PASSWORD = BASE + "api/provider/forgot/password";
    String FACEBOOK_LOGIN = BASE + "api/provider/auth/facebook";
    String GOOGLE_LOGIN = BASE + "api/provider/auth/google";
    String LOGOUT = BASE + "api/provider/logout";
    String SUMMARY = BASE + "api/provider/summary";
    String HELP = BASE + "api/provider/help";
    String COMPLAINT = BASE + "api/user/createComplaint";
    String SAVE_LOCATION = BASE + "api/user/createDefaultLocation";
    String ADD_COUPON_API = BASE + "api/user/promocode/add";
    String COUPON_LIST_API = BASE + "api/user/promocodes";
    String GET_WITHDRAW_LIST = BASE + "api/provider/withdrawaList";

    String GET_CARD_LIST_DETAILS = BASE + "api/provider/BankList";
    String GET_ADD_BANK_DETAILS = BASE + "api/provider/addBank?account_name=";
    String WITHDRAW_REQUEST = BASE + "api/provider/withdrawalRequest?provider_id=";
    String HELP_URL = BASE;
    String login = BASE + "api/provider/oauth/token";
    String register = BASE + "api/provider/register";
    String email_check = BASE + "api/provider/check-email";
    String USER_PROFILE_API = BASE + "api/provider/profile";
    String UPDATE_AVAILABILITY_API = BASE + "api/provider/profile/available";
    String GET_HISTORY_API = BASE + "api/provider/requests/history";
    String GET_HISTORY_DETAILS_API = BASE + "api/provider/requests/history/details";
    String CHANGE_PASSWORD_API = BASE + "api/provider/profile/password";

    String CHECK_DOCUMENT = BASE + "api/provider/document/checkDocument";
    String COMPLETE_DOCUMENT = BASE + "api/provider/document/checkDocument?term_n=1";

    String ChatGetMessage = BASE + "api/provider/firebase/getChat?request_id=";
}
