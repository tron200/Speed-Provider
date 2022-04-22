//package com.classluxdrive.providers.listeners;
//
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.NotificationManager;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.LayerDrawable;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RatingBar;
//import android.widget.RelativeLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.cardview.widget.CardView;
//import androidx.core.app.ActivityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//
//import com.android.volley.NetworkError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.classluxdrive.providers.ClassLuxApp;
//import com.classluxdrive.providers.R;
//import com.classluxdrive.providers.activities.DocUploadActivity;
//import com.classluxdrive.providers.activities.Profile;
//import com.classluxdrive.providers.activities.ShowProfile;
//import com.classluxdrive.providers.activities.SplashScreen;
//import com.classluxdrive.providers.chat.UserChatActivity;
//import com.classluxdrive.providers.helper.ConnectionHelper;
//import com.classluxdrive.providers.helper.CustomDialog;
//import com.classluxdrive.providers.helper.DataParser;
//import com.classluxdrive.providers.helper.SharedHelper;
//import com.classluxdrive.providers.helper.URLHelper;
//import com.classluxdrive.providers.models.User;
//import com.classluxdrive.providers.utills.LocationTracking;
//import com.classluxdrive.providers.utills.Utilities;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.MapStyleOptions;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms.tasks.Task;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static android.content.Context.NOTIFICATION_SERVICE;
//import static com.classluxdrive.providers.ClassLuxApp.trimMessage;
//
//
//public class DriverMapFragment extends Fragment implements GoogleMap.OnCameraMoveListener,
//        LocationListener {
//
//    private static final int REQUEST_CODE = 101;
//    private static final int REQUEST_LOCATION = 1450;
//    public static String TAG = "DriverMapFragment";
//    private static SupportMapFragment mapFragment = null;
//    private static int deviceHeight;
//    private static int deviceWidth;
//    @BindView(R.id.menuIcon)
//    ImageView menuIcon;
//    @BindView(R.id.imgCurrentLoc)
//    ImageView imgCurrentLoc;
//    @BindView(R.id.ll_01_mapLayer)
//    LinearLayout ll_01_mapLayer;
//    @BindView(R.id.driverArrived)
//    LinearLayout driverArrived;
//    @BindView(R.id.driverPicked)
//    LinearLayout driverPicked;
//    @BindView(R.id.driveraccepted)
//    LinearLayout driveraccepted;
//    @BindView(R.id.tvTrips)
//    TextView tvTrips;
//    @BindView(R.id.tvCommision)
//    TextView tvCommision;
//    @BindView(R.id.tvEarning)
//    TextView tvEarning;
//    @BindView(R.id.txtTotalEarning)
//    TextView txtTotalEarning;
//    @BindView(R.id.btn_01_status)
//    Button btn_01_status;
//    @BindView(R.id.btn_rate_submit)
//    Button btn_rate_submit;
//    @BindView(R.id.btn_confirm_payment)
//    Button btn_confirm_payment;
//    @BindView(R.id.img_profile)
//    CircleImageView img_profile;
//    @BindView(R.id.total_earn_layout)
//    CardView total_earn_layout;
//    @BindView(R.id.btn_02_accept)
//    Button btn_02_accept;
//    @BindView(R.id.btn_02_reject)
//    TextView btn_02_reject;
//    @BindView(R.id.btn_cancel_ride)
//    Button btn_cancel_ride;
//    @BindView(R.id.btn_go_offline)
//    Button btn_go_offline;
//    @BindView(R.id.btn_go_online)
//    Button btn_go_online;
//    @BindView(R.id.activeStatus)
//    TextView activeStatus;
//    @BindView(R.id.offline_layout)
//    RelativeLayout offline_layout;
//    @BindView(R.id.ll_01_contentLayer_accept_or_reject_now)
//    LinearLayout ll_01_contentLayer_accept_or_reject_now;
//    @BindView(R.id.ll_02_contentLayer_accept_or_reject_later)
//    LinearLayout ll_02_contentLayer_accept_or_reject_later;
//    @BindView(R.id.ll_03_contentLayer_service_flow)
//    LinearLayout ll_03_contentLayer_service_flow;
//    @BindView(R.id.ll_04_contentLayer_payment)
//    LinearLayout ll_04_contentLayer_payment;
//    @BindView(R.id.ll_05_contentLayer_feedback)
//    LinearLayout ll_05_contentLayer_feedback;
//    @BindView(R.id.lnrGoOffline)
//    LinearLayout lnrGoOffline;
//    @BindView(R.id.layoutinfo)
//    LinearLayout layoutinfo;
//    @BindView(R.id.lnrNotApproved)
//    LinearLayout lnrNotApproved;
//    @BindView(R.id.imgNavigationToSource)
//    ImageView imgNavigationToSource;
//    @BindView(R.id.img01User)
//    ImageView img01User;
//    @BindView(R.id.sos)
//    ImageView sos;
//    @BindView(R.id.rat01UserRating)
//    RatingBar rat01UserRating;
//    @BindView(R.id.txtPickup)
//    TextView txt01Pickup;
//    @BindView(R.id.txtDropOff)
//    TextView txtDropOff;
//    @BindView(R.id.txt01Timer)
//    TextView txt01Timer;
//    @BindView(R.id.txt01UserName)
//    TextView txt01UserName;
//    @BindView(R.id.tvDistance)
//    TextView tvDistance;
//    @BindView(R.id.txtSchedule)
//    TextView txtSchedule;
//    @BindView(R.id.img02User)
//    ImageView img02User;
//    @BindView(R.id.txt02UserName)
//    TextView txt02UserName;
//    @BindView(R.id.rat02UserRating)
//    RatingBar rat02UserRating;
//    @BindView(R.id.txt02ScheduledTime)
//    TextView txt02ScheduledTime;
//    @BindView(R.id.txt02From)
//    TextView txt02From;
//    @BindView(R.id.txt02To)
//    TextView txt02To;
//    @BindView(R.id.img03User)
//    CircleImageView img03User;
//    @BindView(R.id.txt03UserName)
//    TextView txt03UserName;
//    @BindView(R.id.lblCmfrmDestAddress)
//    TextView lblCmfrmDestAddress;
//    @BindView(R.id.lblCmfrmSourceAddress)
//    TextView lblCmfrmSourceAddress;
//    @BindView(R.id.rat03UserRating)
//    RatingBar rat03UserRating;
//    @BindView(R.id.img03Call)
//    ImageButton img03Call;
//    @BindView(R.id.img_chat)
//    ImageButton img_chat;
//    @BindView(R.id.img03Status1)
//    ImageView img03Status1;
//    @BindView(R.id.img03Status2)
//    ImageView img03Status2;
//    @BindView(R.id.img03Status3)
//    ImageView img03Status3;
//    @BindView(R.id.invoice_txt)
//    TextView txt04InvoiceId;
//    @BindView(R.id.txtTotal)
//    TextView txtTotal;
//    @BindView(R.id.txt04BasePrice)
//    TextView txt04BasePrice;
//    @BindView(R.id.txt04Distance)
//    TextView txt04Distance;
//    @BindView(R.id.txt04Tax)
//    TextView txt04Tax;
//    @BindView(R.id.txt04Total)
//    TextView txt04Total;
//    @BindView(R.id.txt04PaymentMode)
//    TextView txt04PaymentMode;
//    @BindView(R.id.txt04Commision)
//    TextView txt04Commision;
//    @BindView(R.id.destination)
//    TextView destination;
//    @BindView(R.id.lblProviderName)
//    TextView lblProviderName;
//    @BindView(R.id.paymentTypeImg)
//    ImageView paymentTypeImg;
//    @BindView(R.id.lnrErrorLayout)
//    LinearLayout errorLayout;
//    @BindView(R.id.destinationLayer)
//    LinearLayout destinationLayer;
//    @BindView(R.id.txtNotes)
//    TextView txtNotes;
//    @BindView(R.id.layoutNotes)
//    LinearLayout layoutNotes;
//    @BindView(R.id.img05User)
//    ImageView img05User;
//    @BindView(R.id.rat05UserRating)
//    RatingBar rat05UserRating;
//    @BindView(R.id.user_name)
//    TextView user_name;
//    @BindView(R.id.user_type)
//    TextView user_type;
//    @BindView(R.id.user_total_ride_distanse)
//    TextView user_total_ride_distanse;
//    @BindView(R.id.online_offline_switch)
//    Switch online_offline_switch;
//    @BindView(R.id.active_Status)
//    TextView active_Status;
//    @BindView(R.id.edt05Comment)
//    EditText edt05Comment;
//    @BindView(R.id.src_dest_txt)
//    TextView topSrcDestTxtLbl;
//    String docopen = "";
//    String CurrentStatus = " ";
//    String PreviousStatus = " ";
//    String request_id = " ";
//    int method;
//    CountDownTimer countDownTimer;
//    Marker currentMarker;
//    ParserTask parserTask;
//    boolean normalPlay = false;
//    boolean push = false;
//    int NAV_DRAWER = 0;
//    DrawerLayout drawer;
//    Utilities utils = new Utilities();
//    MediaPlayer mPlayer;
//    String crt_lat = "", crt_lng = "";
//    boolean isRunning = false, timerCompleted = false;
//    ConnectionHelper helper;
//    View view;
//    boolean doubleBackToExitPressedOnce = false;
//    //Animation
//    Animation slide_down, slide_up;
//    //Distance calculation
//    Intent service_intent;
//    boolean scheduleTrip = false;
//    String type = null, datas = null;
//    String getStatusVariable;
//    String providerId = "";
//    String userID = "";
//    String userFirstName = "";
//    String cancaltype = "";
//    String cancalReason = "";
//    SupportMapFragment supportMapFragment;
//    int value = 0;
//    private LocationCallback locationCallback;
//    private LocationRequest mLocationRequest;
//    private Location currentLocation;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private Handler ha;
//    private String myLat = "";
//    //map variable
//    private String myLng = "";
//    private String token;
//    private GoogleMap mMap;
//    private double srcLatitude = 0;
//    private double srcLongitude = 0;
//    private double destLatitude = 0;
//    private double destLongitude = 0;
//    private LatLng sourceLatLng;
//    private LatLng destLatLng;
//    private LatLng currentLatLng;
//    private String bookingId;
//    private String address;
//    private String daddress;
//    private User user = new User();
//    //Button layout
//    private CustomDialog customDialog;
//    private Object previous_request_id = " ";
//    private String count;
//    private JSONArray statusResponses;
//    private String feedBackRating;
//    private String feedBackComment;
//    private android.app.AlertDialog Waintingdialog;
//    private String earning = "";
//
//    public DriverMapFragment() {
//
//    }
//
//    @OnClick(R.id.btn_01_status)
//    void btn_01_statusClick() {
//        update(CurrentStatus, request_id);
//    }
//
//    @OnClick(R.id.btn_confirm_payment)
//    void btn_confirm_paymentClick() {
//        update(CurrentStatus, request_id);
//    }
//
//    @OnClick(R.id.btn_rate_submit)
//    void btn_rate_submitClick() {
//        update(CurrentStatus, request_id);
//    }
//
//    @OnClick(R.id.btn_go_offline)
//    void btn_go_offlineClick() {
//        update(CurrentStatus, request_id);
//    }
//
//    @OnClick(R.id.btn_go_online)
//    void btn_go_onlineClick() {
//        goOnline();
//    }
//
//    @OnClick(R.id.imgCurrentLoc)
//    void imgCurrentLocClick() {
//        Double crtLat, crtLng;
//        if (!crt_lat.equalsIgnoreCase("") && !crt_lng.equalsIgnoreCase("")) {
//            crtLat = Double.parseDouble(crt_lat);
//            crtLng = Double.parseDouble(crt_lng);
//            if (crtLat != null && crtLng != null) {
//                LatLng loc = new LatLng(crtLat, crtLng);
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(14).build();
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        }
//    }
//
//    @OnClick(R.id.btn_02_accept)
//    void btn_02_acceptClick() {
//        countDownTimer.cancel();
//        if (mPlayer != null && mPlayer.isPlaying()) {
//            mPlayer.stop();
//            mPlayer = null;
//        }
//        handleIncomingRequest("Accept", request_id);
//    }
//
//    @OnClick(R.id.btn_02_reject)
//    void btn_02_rejectClick() {
//        countDownTimer.cancel();
//        if (mPlayer != null && mPlayer.isPlaying()) {
//            mPlayer.stop();
//            mPlayer = null;
//        }
//        handleIncomingRequest("Reject", request_id);
//    }
//
//    @OnClick(R.id.btn_cancel_ride)
//    void btn_cancel_rideClick() {
//        showCancelDialog();
//    }
//
//    @OnClick(R.id.menuIcon)
//    void menuIconClick() {
//        if (NAV_DRAWER == 0) {
//            drawer.openDrawer(Gravity.LEFT);
//        } else {
//            NAV_DRAWER = 0;
//            drawer.closeDrawers();
//        }
//    }
//
//    @OnClick(R.id.img_chat)
//    void img_chatClick() {
//        Intent intentChat = new Intent(getActivity(), UserChatActivity.class);
//        intentChat.putExtra("requestId", request_id);
//        intentChat.putExtra("providerId", providerId);
//        intentChat.putExtra("userId", userID);
//        intentChat.putExtra("userName", userFirstName);
//        getActivity().startActivity(intentChat);
//    }
//
//    @OnClick(R.id.img03Call)
//    void img03CallClick() {
//        String mobile = SharedHelper.getKey(getActivity(), "provider_mobile_no");
//        if (mobile != null && !mobile.equalsIgnoreCase("null") && mobile.length() > 0) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
//            } else {
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:" + SharedHelper.getKey(getActivity(), "provider_mobile_no")));
//                startActivity(intent);
//            }
//        } else {
//            displayMessage(getActivity().getString(R.string.user_no_mobile));
//        }
//    }
//
//    @OnClick(R.id.imgNavigationToSource)
//    void imgNavigationToSourceClick() {
//        String url = "http://maps.google.com/maps?"
//                + "saddr=" + address
//                + "&daddr=" + daddress;
//        Log.e("url", url + "url");
//        if (btn_01_status.getText().toString().equalsIgnoreCase("ARRIVED")) {
//            Uri naviUri = Uri.parse("http://maps.google.com/maps?"
//                    + "saddr=" + crt_lat + "," + crt_lng
//                    + "&daddr=" + srcLatitude + "," + srcLongitude);
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, naviUri);
//            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//            startActivity(intent);
//        } else {
//            Uri naviUri2 = Uri.parse("http://maps.google.com/maps?"
//                    + "saddr=" + srcLatitude + "," + srcLongitude
//                    + "&daddr=" + destLatitude + "," + destLongitude);
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, naviUri2);
//            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//            startActivity(intent);
//        }
//    }
//
//    @OnClick(R.id.online_offline_switch)
//    void online_offline_switchClick() {
//        if (online_offline_switch.isChecked()) {
//            active_Status.setText(getActivity().getString(R.string.online));
//            offline_layout.setVisibility(View.GONE);
//            goOnline();
//        } else {
//            active_Status.setText(getActivity().getString(R.string.offline));
//            offline_layout.setVisibility(View.VISIBLE);
//            update(CurrentStatus, request_id);
//
//        }
//    }
//
//    private void findViewById(View view) {
//        drawer = getActivity().findViewById(R.id.drawer_layout);
//        LayerDrawable drawable = (LayerDrawable) rat01UserRating.getProgressDrawable();
//        drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
//        drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
//        drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
//        LayerDrawable stars02 = (LayerDrawable) rat02UserRating.getProgressDrawable();
//        stars02.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
//        LayerDrawable drawable_02 = (LayerDrawable) rat03UserRating.getProgressDrawable();
//        drawable_02.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
//        drawable_02.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
//        drawable_02.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
//        LayerDrawable stars05 = (LayerDrawable) rat05UserRating.getProgressDrawable();
//        stars05.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
//        stars05.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
//        stars05.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
//
//        earning = SharedHelper.getKey(getActivity(), "totalearning");
//        total_earn_layout.setVisibility(View.VISIBLE);
//        if (earning != null && !earning.isEmpty() && !earning.equals("")) {
//            txtTotalEarning.setText(earning);
//        } else
//            txtTotalEarning.setText(SharedHelper.getKey(getActivity(), "currency") + " 0.00");
//        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
//        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
//
//        user_name.setText(SharedHelper.getKey(getActivity(), "first_name"));
//        user_type.setText(SharedHelper.getKey(getActivity(), "service"));
//
//        Picasso.get().load(SharedHelper.getKey(getActivity(), "picture"))
//                .placeholder(R.drawable.ic_dummy_user)
//                .error(R.drawable.ic_dummy_user)
//                .into(img_profile);
//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        img_profile.setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile.class)));
//        view.setOnKeyListener((v, keyCode, event) -> {
//            if (event.getAction() != KeyEvent.ACTION_DOWN)
//                return true;
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                if (doubleBackToExitPressedOnce) {
//                    getActivity().finish();
//                    return false;
//                }
//                doubleBackToExitPressedOnce = true;
//                Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 5000);
//                return true;
//            }
//            return false;
//        });
//        sos.setOnClickListener(v -> showSosDialog());
//        lnrGoOffline.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            push = bundle.getBoolean("push");
//        }
//        if (push) {
//            isRunning = false;
//        }
//        Intent i = getActivity().getIntent();
//        type = i.getStringExtra("type");
//        datas = i.getStringExtra("datas");
//        if (type != null) {
//            checkStatusSchedule();
//        } else {
//            checkStatus();
//        }
//        Log.e(TAG, "TYPE: " + type);
//        Log.e(TAG, "DATAS: " + datas);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.fragment_map, container, false);
//        }
//        ButterKnife.bind(this, view);
//        findViewById(view);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(10 * 1000); // 10 seconds
//        mLocationRequest.setFastestInterval(5 * 1000);
//        fetchLocation();
//        service_intent = new Intent(getActivity(), LocationTracking.class);
//        token = SharedHelper.getKey(getActivity(), "access_token");
//        helper = new ConnectionHelper(getActivity());
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        deviceHeight = displayMetrics.heightPixels;
//        deviceWidth = displayMetrics.widthPixels;
//        customDialog = new CustomDialog(getActivity());
//        customDialog.setCancelable(true);
//        customDialog.show();
//
//        ha = new Handler();
//        if (type != null) {
//            checkStatusSchedule();
//        } else {
//            checkStatus();
//        }
//        //check status every 3 sec
//        ha.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (type != null) {
//                    checkStatusSchedule();
//                }
//                ha.postDelayed(this, 3000);
//            }
//        }, 3000);
//
//        online_offline_switch.setChecked(true);
//        active_Status.setText(getActivity().getString(R.string.online));
//        return view;
//    }
//
//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(location -> {
//            if (location != null) {
//                currentLocation = location;
////                Toast.makeText(getActivity(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.provider_map);
//                assert supportMapFragment != null;
//                supportMapFragment.getMapAsync(googleMap -> {
//                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                    googleMap.setMapStyle(
//                            MapStyleOptions.loadRawResourceStyle(
//                                    getActivity(), R.raw.style_json));
//                    mMap = googleMap;
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
//                    mMap.setMyLocationEnabled(false);
//                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                    mMap.setBuildingsEnabled(true);
//                    mMap.getUiSettings().setCompassEnabled(false);
//                    mMap.getUiSettings().setRotateGesturesEnabled(false);
//                    mMap.getUiSettings().setTiltGesturesEnabled(false);
//                    mMap.setOnCameraMoveListener(this);
//
//
////                    setupMap();
//                });
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    fetchLocation();
//                }
//                break;
//        }
//    }
//
//    private void mapClear() {
//        if (mMap != null) {
//            if (parserTask != null) {
//                parserTask.cancel(true);
//                parserTask = null;
//            }
//
//            if (!crt_lat.equalsIgnoreCase("") && !crt_lat.equalsIgnoreCase("")) {
//                LatLng myLocation = new LatLng(Double.parseDouble(crt_lat), Double.parseDouble(crt_lng));
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
//                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//
//            mMap.clear();
//            srcLatitude = 0;
//            srcLongitude = 0;
//            destLatitude = 0;
//            destLongitude = 0;
//        }
//    }
//
//    public void clearVisibility() {
//
//        if (ll_01_contentLayer_accept_or_reject_now.getVisibility() == View.VISIBLE) {
//            ll_01_contentLayer_accept_or_reject_now.startAnimation(slide_down);
//        } else if (ll_02_contentLayer_accept_or_reject_later.getVisibility() == View.VISIBLE) {
//            ll_02_contentLayer_accept_or_reject_later.startAnimation(slide_down);
//        } else if (ll_03_contentLayer_service_flow.getVisibility() == View.VISIBLE) {
//        } else if (ll_04_contentLayer_payment.getVisibility() == View.VISIBLE) {
//            ll_04_contentLayer_payment.startAnimation(slide_down);
//        } else if (ll_04_contentLayer_payment.getVisibility() == View.VISIBLE) {
//            ll_04_contentLayer_payment.startAnimation(slide_down);
//        } else if (ll_05_contentLayer_feedback.getVisibility() == View.VISIBLE) {
//            ll_05_contentLayer_feedback.startAnimation(slide_down);
//        }
//
//        ll_01_contentLayer_accept_or_reject_now.setVisibility(View.GONE);
//        ll_02_contentLayer_accept_or_reject_later.setVisibility(View.GONE);
//        ll_03_contentLayer_service_flow.setVisibility(View.GONE);
//        ll_04_contentLayer_payment.setVisibility(View.GONE);
//        ll_05_contentLayer_feedback.setVisibility(View.GONE);
//        lnrGoOffline.setVisibility(View.GONE);
//    }
//
//    private void setSourceLocationOnMap(LatLng latLng) {
//        if (latLng != null) {
//            mMap.clear();
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
//            MarkerOptions options = new MarkerOptions().position(latLng).anchor(0.5f, 0.75f);
//            options.position(latLng).isDraggable();
//            mMap.addMarker(options);
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
//    }
//
//    private void setPickupLocationOnMap() {
//        if (mMap != null) {
//            mMap.clear();
//        }
//        sourceLatLng = currentLatLng;
//        destLatLng = new LatLng(srcLatitude, srcLongitude);
//        MarkerOptions options = new MarkerOptions();
//        options.anchor(0.5f, 0.5f).position(destLatLng).isDraggable();
//        if (sourceLatLng != null && destLatLng != null) {
//            String url = getUrl(sourceLatLng.latitude, sourceLatLng.longitude, destLatLng.latitude, destLatLng.longitude);
//            FetchUrl fetchUrl = new FetchUrl();
//            fetchUrl.execute(url);
//        }
//    }
//
//    private void setDestinationLocationOnMap() {
//        if (currentLatLng != null) {
//            sourceLatLng = currentLatLng;
//            destLatLng = new LatLng(destLatitude, destLongitude);
//            String url = getUrl(sourceLatLng.latitude, sourceLatLng.longitude, destLatLng.latitude, destLatLng.longitude);
//            FetchUrl fetchUrl = new FetchUrl();
//            fetchUrl.execute(url);
//        }
//    }
//
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            // Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            // Connecting to url
//            urlConnection.connect();
//
//            // Reading data from url
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//            Log.d("downloadUrl", data);
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//    private String getUrl(double source_latitude,
//                          double source_longitude,
//                          double dest_latitude,
//                          double dest_longitude) {
//
//        String str_origin = "origin=" + source_latitude + "," + source_longitude;
//        String str_dest = "destination=" + dest_latitude + "," + dest_longitude;
//        String sensor = "sensor=false" + "&key=" + "AIzaSyDYbRQKyMGHPP-hh1jOTaPv3f3jSXQScUg";
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//        String output = "json";
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//        Log.v("directionUrl", url + " ");
//        return url;
//    }
//
//    private void checkDocumentStatus() {
//        try {
//
//            if (helper.isConnectingToInternet()) {
//                String url = URLHelper.CHECK_DOCUMENT;
//                utils.print("Destination Current Lat", "" + crt_lat);
//                final JsonObjectRequest jsonObjectRequest = new
//                        JsonObjectRequest(Request.Method.GET,
//                                url,
//                                null,
//                                response -> {
//                                    Log.e("checkDocumentStatus", response + "Document");
//                                    try {
//                                        if (response.getString("status").equalsIgnoreCase("0")) {
//
//                                            JSONArray jsonArray = response.getJSONArray("data");
//                                            for (int i = 0; i < jsonArray.length(); i++) {
//                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                                SharedHelper.putKey(getActivity(), jsonObject.optString("name"),
//                                                        jsonObject.optString("id"));
//                                            }
//                                            if (Waintingdialog == null) {
//
//                                                if (docopen.equalsIgnoreCase("")) {
//                                                    docopen = "yes";
//                                                    Intent intent1 = new Intent(getActivity(), DocUploadActivity.class);
//                                                    getActivity().startActivity(intent1);
//                                                }
//                                            }
//                                        } else {
//                                            ha.removeMessages(0);
//                                            lnrGoOffline.setVisibility(View.GONE);
//                                            lnrNotApproved.setVisibility(View.VISIBLE);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }, error -> utils.print("Error", error.toString())) {
//                            @Override
//                            public java.util.Map<String, String> getHeaders() {
//                                HashMap<String, String> headers = new HashMap<>();
//                                headers.put("X-Requested-With", "XMLHttpRequest");
//                                headers.put("Authorization", "Bearer " + token);
//                                return headers;
//                            }
//                        };
//                ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//            } else {
//                displayMessage(getActivity().getString(R.string.oops_connect_your_internet));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void checkStatus() {
//        try {
//
//            if (helper.isConnectingToInternet()) {
//                String url = URLHelper.BASE + "api/provider/trip?latitude=" + crt_lat + "&longitude=" + crt_lng;
//
//                utils.print("Destination Current Lat", "" + crt_lat);
//                utils.print("Destination Current Lng", "" + crt_lng);
//                Log.i(TAG, "checkStatus url : " + url);
//
//                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//                    if ((customDialog != null) && (customDialog.isShowing()))
//                        customDialog.dismiss();
//                    if (errorLayout.getVisibility() == View.VISIBLE) {
//                        errorLayout.setVisibility(View.GONE);
//                    }
//                    Log.e("CheckStatus", "" + response.toString());
//                    try {
//                        if (response.optString("service_status").equalsIgnoreCase("offline")) {
//
//                            online_offline_switch.setChecked(false);
//                            active_Status.setText(getActivity().getString(R.string.offline));
//                            offline_layout.setVisibility(View.VISIBLE);
//                        }
//                        try {
//                            tvCommision.setText(response.optString("commision"));
//                            tvEarning.setText(response.optString("earnings"));
//                            tvTrips.setText(response.optString("trips"));
//                            txtTotalEarning.setText(SharedHelper.getKey(getActivity(), "currency") + response.optString("earnings"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (response.optJSONArray("requests").length() > 0) {
//
//                            providerId = response.optJSONArray("requests")
//                                    .getJSONObject(0).optJSONObject("request")
//                                    .optString("provider_id");
//                            userID = response.optJSONArray("requests")
//                                    .getJSONObject(0).optJSONObject("request")
//                                    .optString("user_id");
//
//                            JSONObject jsonObject = response.optJSONArray("requests")
//                                    .getJSONObject(0).optJSONObject("request").optJSONObject("user");
//                            userFirstName = jsonObject.optString("first_name");
//                            user.setFirstName(jsonObject.optString("first_name"));
////                                user.setLastName(jsonObject.optString("last_name"));
//                            user.setEmail(jsonObject.optString("email"));
//                            if (jsonObject.optString("picture").startsWith("http"))
//                                user.setImg(jsonObject.optString("picture"));
//                            else
//                                user.setImg(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("picture"));
//                            user.setRating(jsonObject.optString("rating"));
//                            user.setMobile(jsonObject.optString("mobile"));
//                            bookingId = response.optJSONArray("requests").getJSONObject(0)
//                                    .optJSONObject("request").optString("booking_id");
//                            address = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request").optString("s_address");
//                            daddress = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request").optString("d_address");
//
//
//                            lblCmfrmSourceAddress.setText(address);
//                            lblCmfrmDestAddress.setText(daddress);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    if (response.optString("account_status").equals("new") ||
//                            response.optString("account_status").equals("onboarding")) {
//                        ha.removeMessages(0);
//                        checkDocumentStatus();
//                    } else {
//
//                        if (response.optString("service_status").equals("offline")) {
//                            ha.removeMessages(0);
//                        } else {
//
//                            if (response.optJSONArray("requests") != null && response.optJSONArray("requests").length() > 0) {
//                                JSONObject statusResponse = null;
//                                try {
//                                    statusResponses = response.optJSONArray("requests");
//                                    statusResponse = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request");
//                                    request_id = response.optJSONArray("requests").getJSONObject(0).optString("request_id");
//                                    if (statusResponse.optString("special_note") != null &&
//                                            statusResponse.optString("special_note") != "null") {
//                                        layoutNotes.setVisibility(View.VISIBLE);
//                                        txtNotes.setText(statusResponse.getString("special_note"));
//                                    }
//
//                                    Log.e("request_idjson", request_id + "");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if (statusResponse.optString("status").equals("PICKEDUP")) {
////                                        lblDistanceTravelled.setText("Distance Travelled :"
////                                                + String.format("%f", Float.parseFloat(LocationTracking.distance * 0.001 + "")) + " Km");
//                                }
//                                if ((statusResponse != null) && (request_id != null)) {
//                                    if ((!previous_request_id.equals(request_id) || previous_request_id.equals(" ")) && mMap != null) {
//                                        previous_request_id = request_id;
//                                        srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
//                                        srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
//
//                                        destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
//                                        destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
//                                        //noinspection deprecation
//                                        setSourceLocationOnMap(currentLatLng);
//                                        setPickupLocationOnMap();
//                                        sos.setVisibility(View.GONE);
//
//                                    }
//                                    utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + statusResponse.optString("status"));
////                                        String ok = "ok";
////                                        if (ok.equals(ok))
//                                    if (!PreviousStatus.equals(statusResponse.optString("status"))) {
////                                            || statusResponse.optString("paid").equals("1") || statusResponse.optString("paid").equals("0")
//                                        PreviousStatus = statusResponse.optString("status");
//                                        clearVisibility();
//                                        utils.print("responseObj(" + request_id + ")", statusResponse.toString());
//                                        utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + statusResponse.optString("status"));
//                                        if (!statusResponse.optString("status").equals("SEARCHING")) {
//                                            timerCompleted = false;
//                                            if (mPlayer != null && mPlayer.isPlaying()) {
//                                                mPlayer.stop();
//                                                mPlayer = null;
//                                                countDownTimer.cancel();
//                                            }
//                                        }
//                                        if (statusResponse.optString("status").equals("SEARCHING")) {
//                                            scheduleTrip = false;
//                                            if (!timerCompleted) {
//                                                setValuesTo_ll_01_contentLayer_accept_or_reject_now(statusResponses);
//                                                if (ll_01_contentLayer_accept_or_reject_now.getVisibility() == View.GONE) {
//                                                    ll_01_contentLayer_accept_or_reject_now.startAnimation(slide_up);
//                                                }
//                                                ll_01_contentLayer_accept_or_reject_now.setVisibility(View.VISIBLE);
//                                            }
//                                            CurrentStatus = "STARTED";
//                                        } else if (statusResponse.optString("status").equals("STARTED")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_arrived));
//                                            } catch (NullPointerException ne) {
//                                                btn_01_status.setText("ARRIVED");
//                                            }
//                                            CurrentStatus = "ARRIVED";
//                                            sos.setVisibility(View.GONE);
//                                            if (srcLatitude == 0 && srcLongitude == 0 && destLatitude == 0 && destLongitude == 0) {
//                                                mapClear();
//                                                srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
//                                                srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
//                                                destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
//                                                destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
//                                                //noinspection deprecation
//                                                //
//                                                setSourceLocationOnMap(currentLatLng);
//                                                setPickupLocationOnMap();
//                                            }
//                                            sos.setVisibility(View.GONE);
//                                            btn_cancel_ride.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            layoutinfo.setVisibility(View.GONE);
//                                            String address = statusResponse.optString("s_address");
//                                            if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                destination.setText(address);
//                                            else
//                                                destination.setText(getAddress(statusResponse.optString("s_latitude"),
//                                                        statusResponse.optString("s_longitude")));
//                                            try {
//                                                topSrcDestTxtLbl.setText(getActivity().getString(R.string.pick_up));
//                                            } catch (NullPointerException ne) {
//                                                ne.printStackTrace();
//                                                topSrcDestTxtLbl.setText("Pick up Location");
//                                            }
//
//
//                                        } else if (statusResponse.optString("status").equals("ARRIVED")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_pickedup));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            sos.setVisibility(View.GONE);
//                                            img03Status1.setImageResource(R.drawable.arrived_select);
//                                            img03Status2.setImageResource(R.drawable.pickeddisable);
//                                            driveraccepted.setVisibility(View.VISIBLE);
//                                            driverArrived.setVisibility(View.GONE);
//                                            driverPicked.setVisibility(View.GONE);
//                                            CurrentStatus = "PICKEDUP";
//                                            driveraccepted.setVisibility(View.GONE);
//                                            driverArrived.setVisibility(View.VISIBLE);
//                                            driverPicked.setVisibility(View.GONE);
//
//                                            btn_cancel_ride.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            String address = statusResponse.optString("d_address");
//                                            try {
//                                                if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                    destination.setText(address);
//                                                else
//                                                    destination.setText(getAddress(statusResponse.optString("d_latitude"),
//                                                            statusResponse.optString("d_longitude")));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            try {
//                                                topSrcDestTxtLbl.setText(getActivity().getString(R.string.drop_at));
//                                            } catch (Exception e) {
//                                                topSrcDestTxtLbl.setText("Drop Location");
//                                            }
//
//
//                                        } else if (statusResponse.optString("status").equals("PICKEDUP")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_dropped));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            sos.setVisibility(View.VISIBLE);
////                                                navigate.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arrived_select);
//                                            img03Status2.setImageResource(R.drawable.pickup_select);
//                                            driveraccepted.setVisibility(View.GONE);
//                                            driverArrived.setVisibility(View.GONE);
//                                            driverPicked.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "DROPPED";
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            layoutinfo.setVisibility(View.GONE);
//                                            btn_cancel_ride.setVisibility(View.GONE);
//                                            String address = statusResponse.optString("d_address");
//                                            try {
//                                                if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                    destination.setText(address);
//                                                else
//                                                    destination.setText(getAddress(statusResponse.optString("d_latitude"),
//                                                            statusResponse.optString("d_longitude")));
//                                            } catch (NullPointerException ne) {
//                                                ne.printStackTrace();
//                                            }
//                                            topSrcDestTxtLbl.setText(getActivity().getString(R.string.drop_at));
//
//                                            mapClear();
//                                            srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
//                                            srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
//                                            destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
//                                            destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
//                                            //noinspection deprecation
//                                            //
//                                            setSourceLocationOnMap(currentLatLng);
//                                            setPickupLocationOnMap();
//
//
//                                        } else if (statusResponse.optString("status").equals("DROPPED")
//                                                && statusResponse.optString("paid").equals("0")) {
//                                            setValuesTo_ll_04_contentLayer_payment(statusResponses);
//                                            if (ll_04_contentLayer_payment.getVisibility() == View.GONE) {
//                                                ll_04_contentLayer_payment.startAnimation(slide_up);
//                                            }
//                                            ll_04_contentLayer_payment.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arriveddisable);
//                                            img03Status2.setImageResource(R.drawable.pickeddisable);
//                                            driveraccepted.setVisibility(View.VISIBLE);
//                                            driverArrived.setVisibility(View.GONE);
//                                            driverPicked.setVisibility(View.GONE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.confirm_payment));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            sos.setVisibility(View.VISIBLE);
////                                                navigate.setVisibility(View.GONE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "COMPLETED";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("DROPPED")
//                                                && statusResponse.optString("paid").equals("0")) {
//                                            setValuesTo_ll_04_contentLayer_payment(statusResponses);
//                                            if (ll_04_contentLayer_payment.getVisibility() == View.GONE) {
//                                                ll_04_contentLayer_payment.startAnimation(slide_up);
//                                            }
//                                            ll_04_contentLayer_payment.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arriveddisable);
//                                            img03Status2.setImageResource(R.drawable.pickeddisable);
//                                            driveraccepted.setVisibility(View.VISIBLE);
//                                            driverArrived.setVisibility(View.GONE);
//                                            driverPicked.setVisibility(View.GONE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.confirm_payment));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            sos.setVisibility(View.VISIBLE);
////                                                navigate.setVisibility(View.GONE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "COMPLETED";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("COMPLETED")
//                                                && statusResponse.optString("paid").equals("0")) {
//
//                                            setValuesTo_ll_04_contentLayer_payment(statusResponses);
//                                            if (ll_04_contentLayer_payment.getVisibility() == View.GONE) {
//                                                ll_04_contentLayer_payment.startAnimation(slide_up);
//                                            }
//                                            ll_04_contentLayer_payment.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arriveddisable);
//                                            img03Status2.setImageResource(R.drawable.pickeddisable);
//                                            driveraccepted.setVisibility(View.VISIBLE);
//                                            driverArrived.setVisibility(View.GONE);
//                                            driverPicked.setVisibility(View.GONE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.confirm_payment));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            sos.setVisibility(View.VISIBLE);
////                                                navigate.setVisibility(View.GONE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "COMPLETED";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("COMPLETED")
//                                                && statusResponse.optString("paid").equals("1")) {
////                                                ok = "not";
//                                            if (ll_04_contentLayer_payment.getVisibility() == View.VISIBLE) {
//                                                ll_04_contentLayer_payment.setVisibility(View.GONE);
//                                            }
//
//                                            setValuesTo_ll_05_contentLayer_feedback(statusResponses);
//                                            if (ll_05_contentLayer_feedback.getVisibility() == View.GONE) {
//                                                ll_05_contentLayer_feedback.startAnimation(slide_up);
//                                            }
//                                            ll_04_contentLayer_payment.setVisibility(View.GONE);
//                                            edt05Comment.setText("");
//                                            ll_05_contentLayer_feedback.setVisibility(View.VISIBLE);
//                                            sos.setVisibility(View.GONE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.submit));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            CurrentStatus = "RATE";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("SCHEDULED")) {
//                                            if (mMap != null) {
//                                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                                    return;
//                                                }
//                                                mMap.clear();
//                                            }
//                                            clearVisibility();
//                                            CurrentStatus = "SCHEDULED";
//                                            utils.print("statusResponse", "null");
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//
//                                            LocationTracking.distance = 0.0f;
//                                        }
//                                    }
//                                } else {
//                                    if (mMap != null) {
//                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            return;
//                                        }
//                                        timerCompleted = false;
//                                        mMap.clear();
//                                        if (mPlayer != null && mPlayer.isPlaying()) {
//                                            mPlayer.stop();
//                                            mPlayer = null;
//                                            countDownTimer.cancel();
//                                        }
//
//                                    }
//
//                                    LocationTracking.distance = 0.0f;
//
//                                    clearVisibility();
//                                    destinationLayer.setVisibility(View.GONE);
//                                    layoutinfo.setVisibility(View.VISIBLE);
//                                    CurrentStatus = "ONLINE";
//                                    PreviousStatus = "NULL";
//                                    utils.print("statusResponse", "null");
//                                }
//
//                            } else {
//                                timerCompleted = false;
//                                if (!PreviousStatus.equalsIgnoreCase("NULL")) {
//                                    utils.print("response", "null");
//                                    if (mMap != null) {
//                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            return;
//                                        }
//                                        mMap.clear();
//                                    }
//                                    if (mPlayer != null && mPlayer.isPlaying()) {
//                                        mPlayer.stop();
//                                        mPlayer = null;
//                                        countDownTimer.cancel();
//                                    }
//                                    clearVisibility();
//                                    lnrGoOffline.setVisibility(View.VISIBLE);
//                                    destinationLayer.setVisibility(View.GONE);
//                                    layoutinfo.setVisibility(View.VISIBLE);
//                                    CurrentStatus = "ONLINE";
//                                    PreviousStatus = "NULL";
//                                    utils.print("statusResponse", "null");
//
//                                    LocationTracking.distance = 0.0f;
//                                }
//
//                            }
//                        }
//                    }
//                }, error -> {
//                    utils.print("Error", error.toString());
//                    //errorHandler(error);
//                    timerCompleted = false;
//                    mapClear();
//                    clearVisibility();
//                    CurrentStatus = "ONLINE";
//                    PreviousStatus = "NULL";
//                    destinationLayer.setVisibility(View.GONE);
//                    layoutinfo.setVisibility(View.VISIBLE);
//                    if (mPlayer != null && mPlayer.isPlaying()) {
//                        mPlayer.stop();
//                        mPlayer = null;
//                        countDownTimer.cancel();
//                    }
//                    displayMessage(error.toString());
//                }) {
//                    @Override
//                    public java.util.Map<String, String> getHeaders() {
//                        HashMap<String, String> headers = new HashMap<>();
//                        headers.put("X-Requested-With", "XMLHttpRequest");
//                        headers.put("Authorization", "Bearer " + token);
//                        return headers;
//                    }
//                };
//                ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//            } else {
//                displayMessage(getActivity().getString(R.string.oops_connect_your_internet));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void checkStatusSchedule() {
//
//        try {
//
//            if (helper.isConnectingToInternet()) {
//
//                String url = URLHelper.BASE + "api/provider/trip?latitude=" + crt_lat + "&longitude=" + crt_lng;
//                Log.e(TAG, "URL:" + url);
//
//                utils.print("Destination Current Lat", "" + crt_lat);
//                utils.print("Destination Current Lng", "" + crt_lng);
//
//                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//                    if (errorLayout.getVisibility() == View.VISIBLE) {
//                        errorLayout.setVisibility(View.GONE);
//                    }
//                    Log.e("Schedule CheckStatus", "" + response.toString());
//
//                    if (response.optJSONArray("requests").length() > 0) {
//
//                        try {
//                            if (response.optJSONArray("requests").length() > 0) {
//                                JSONObject jsonObject = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request").optJSONObject("user");
//                                user.setFirstName(jsonObject.optString("first_name"));
//                                user.setEmail(jsonObject.optString("email"));
//                                if (jsonObject.optString("picture").startsWith("http"))
//                                    user.setImg(jsonObject.optString("picture"));
//                                else
//                                    user.setImg(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("picture"));
//                                user.setRating(jsonObject.optString("rating"));
//                                user.setMobile(jsonObject.optString("mobile"));
//                                bookingId = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request").optString("booking_id");
//                                address = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request").optString("s_address");
//                                daddress = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request").optString("d_address");
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (response.optString("service_status").equals("offline")) {
//                            ha.removeMessages(0);
//                        } else {
//
//                            if (response.optJSONArray("requests") != null && response.optJSONArray("requests").length() > 0) {
//                                JSONObject statusResponse = null;
//                                try {
//                                    statusResponses = response.optJSONArray("requests");
//                                    statusResponse = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request");
//                                    request_id = response.optJSONArray("requests").getJSONObject(0).optString("request_id");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if ((statusResponse != null) && (request_id != null)) {
//                                    if ((!previous_request_id.equals(request_id) || previous_request_id.equals(" ")) && mMap != null) {
//                                        previous_request_id = request_id;
//                                        srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
//                                        srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
//                                        destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
//                                        destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
//                                        //noinspection deprecation
//                                        setSourceLocationOnMap(currentLatLng);
//                                        setPickupLocationOnMap();
//                                        sos.setVisibility(View.GONE);
//                                    }
//                                    utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + statusResponse.optString("status"));
//                                    if (!PreviousStatus.equals(statusResponse.optString("status"))) {
//                                        PreviousStatus = statusResponse.optString("status");
//                                        clearVisibility();
//                                        utils.print("responseObj(" + request_id + ")", statusResponse.toString());
//                                        utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + statusResponse.optString("status"));
//                                        if (!statusResponse.optString("status").equals("SEARCHING")) {
//                                            timerCompleted = false;
//                                            if (mPlayer != null && mPlayer.isPlaying()) {
//                                                mPlayer.stop();
//                                                mPlayer = null;
//                                                countDownTimer.cancel();
//                                            }
//                                        }
//                                        if (statusResponse.optString("status").equals("SEARCHING")) {
//                                            scheduleTrip = false;
//                                            if (!timerCompleted) {
//                                                setValuesTo_ll_01_contentLayer_accept_or_reject_now(statusResponses);
//                                                if (ll_01_contentLayer_accept_or_reject_now.getVisibility() == View.GONE) {
//                                                    ll_01_contentLayer_accept_or_reject_now.startAnimation(slide_up);
//                                                }
//                                                ll_01_contentLayer_accept_or_reject_now.setVisibility(View.VISIBLE);
//                                            }
//                                            CurrentStatus = "STARTED";
//                                        } else if (statusResponse.optString("status").equals("STARTED")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_arrived));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            CurrentStatus = "ARRIVED";
//                                            sos.setVisibility(View.GONE);
//                                            if (srcLatitude == 0 && srcLongitude == 0 && destLatitude == 0 && destLongitude == 0) {
//                                                mapClear();
//                                                srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
//                                                srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
//                                                destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
//                                                destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
//                                                //noinspection deprecation
//                                                //
//                                                setSourceLocationOnMap(currentLatLng);
//                                                setPickupLocationOnMap();
//                                            }
//                                            sos.setVisibility(View.GONE);
//                                            btn_cancel_ride.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            String address = statusResponse.optString("s_address");
//                                            if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                destination.setText(address);
//                                            else
//                                                destination.setText(getAddress(statusResponse.optString("s_latitude"),
//                                                        statusResponse.optString("s_longitude")));
//                                            topSrcDestTxtLbl.setText(getActivity().getString(R.string.pick_up));
//
//
//                                        } else if (statusResponse.optString("status").equals("ARRIVED")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_pickedup));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            sos.setVisibility(View.GONE);
//                                            img03Status1.setImageResource(R.drawable.arrived_select);
//                                            driveraccepted.setVisibility(View.GONE);
//                                            driverArrived.setVisibility(View.VISIBLE);
//                                            driverPicked.setVisibility(View.GONE);
//                                            CurrentStatus = "PICKEDUP";
//
//                                            btn_cancel_ride.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            String address = statusResponse.optString("d_address");
//                                            if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                destination.setText(address);
//                                            else
//                                                destination.setText(getAddress(statusResponse.optString("d_latitude"),
//                                                        statusResponse.optString("d_longitude")));
//                                            topSrcDestTxtLbl.setText(getActivity().getString(R.string.drop_at));
//
//
//                                        } else if (statusResponse.optString("status").equals("PICKEDUP")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_dropped));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            sos.setVisibility(View.VISIBLE);
////                                                navigate.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arrived_select);
//                                            img03Status2.setImageResource(R.drawable.pickup_select);
//                                            CurrentStatus = "DROPPED";
//                                            driveraccepted.setVisibility(View.GONE);
//                                            driverArrived.setVisibility(View.GONE);
//                                            driverPicked.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            layoutinfo.setVisibility(View.GONE);
//                                            btn_cancel_ride.setVisibility(View.GONE);
//                                            String address = statusResponse.optString("d_address");
//                                            if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                destination.setText(address);
//                                            else
//                                                destination.setText(getAddress(statusResponse.optString("d_latitude"),
//                                                        statusResponse.optString("d_longitude")));
//                                            topSrcDestTxtLbl.setText(getActivity().getString(R.string.drop_at));
////
//                                            srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
//                                            srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
//                                            destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
//                                            destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
//
//                                            setSourceLocationOnMap(currentLatLng);
//                                            setDestinationLocationOnMap();
//
//
//                                        } else if (statusResponse.optString("status").equals("DROPPED")
//                                                && statusResponse.optString("paid").equals("0")) {
//                                            setValuesTo_ll_04_contentLayer_payment(statusResponses);
//                                            if (ll_04_contentLayer_payment.getVisibility() == View.GONE) {
//                                                ll_04_contentLayer_payment.startAnimation(slide_up);
//                                            }
//                                            ll_04_contentLayer_payment.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arrived);
//                                            img03Status2.setImageResource(R.drawable.pickup);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_paid));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            sos.setVisibility(View.VISIBLE);
////                                                navigate.setVisibility(View.GONE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "COMPLETED";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("COMPLETED")
//                                                && statusResponse.optString("paid").equals("0")) {
//                                            setValuesTo_ll_04_contentLayer_payment(statusResponses);
//                                            if (ll_04_contentLayer_payment.getVisibility() == View.GONE) {
//                                                ll_04_contentLayer_payment.startAnimation(slide_up);
//                                            }
//                                            ll_04_contentLayer_payment.setVisibility(View.VISIBLE);
//                                            img03Status1.setImageResource(R.drawable.arrived);
//                                            img03Status2.setImageResource(R.drawable.pickup);
//                                            try {
//                                                btn_01_status.setText(getActivity().getString(R.string.tap_when_paid));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            sos.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "COMPLETED";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("DROPPED") && statusResponse.optString("paid").equals("1")) {
//                                            setValuesTo_ll_05_contentLayer_feedback(statusResponses);
//                                            if (ll_05_contentLayer_feedback.getVisibility() == View.GONE) {
//                                                ll_05_contentLayer_feedback.startAnimation(slide_up);
//                                            }
//                                            ll_05_contentLayer_feedback.setVisibility(View.VISIBLE);
//                                            btn_01_status.setText(getActivity().getString(R.string.rate_user));
//                                            sos.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            CurrentStatus = "RATE";
//
//                                            LocationTracking.distance = 0.0f;
//                                        } else if (statusResponse.optString("status").equals("COMPLETED") && statusResponse.optString("paid").equals("1")) {
//
//                                            if (ll_05_contentLayer_feedback.getVisibility() == View.VISIBLE) {
//                                                ll_05_contentLayer_feedback.setVisibility(View.GONE);
//                                            }
//                                            setValuesTo_ll_05_contentLayer_feedback(statusResponses);
//                                            if (ll_05_contentLayer_feedback.getVisibility() == View.GONE) {
//                                                ll_05_contentLayer_feedback.startAnimation(slide_up);
//                                            }
//                                            ll_05_contentLayer_feedback.setVisibility(View.VISIBLE);
//                                            edt05Comment.setText("");
//                                            sos.setVisibility(View.GONE);
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//                                            btn_01_status.setText(getActivity().getString(R.string.rate_user));
//                                            CurrentStatus = "RATE";
//
//                                            LocationTracking.distance = 0.0f;
//                                            type = null;
//
//                                        } else if (statusResponse.optString("status").equals("SCHEDULED")) {
//                                            if (mMap != null) {
//                                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                                    return;
//                                                }
//                                                mMap.clear();
//                                            }
//                                            clearVisibility();
//                                            CurrentStatus = "SCHEDULED";
//                                            utils.print("statusResponse", "null");
//                                            destinationLayer.setVisibility(View.GONE);
//                                            layoutinfo.setVisibility(View.VISIBLE);
//
//                                            LocationTracking.distance = 0.0f;
//                                        }
//                                    }
//                                } else {
//                                    if (mMap != null) {
//                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            return;
//                                        }
//                                        timerCompleted = false;
//                                        mMap.clear();
//                                        if (mPlayer != null && mPlayer.isPlaying()) {
//                                            mPlayer.stop();
//                                            mPlayer = null;
//                                            countDownTimer.cancel();
//                                        }
//
//                                    }
//
//                                    LocationTracking.distance = 0.0f;
//
//                                    clearVisibility();
//                                    destinationLayer.setVisibility(View.GONE);
//                                    layoutinfo.setVisibility(View.VISIBLE);
//                                    CurrentStatus = "ONLINE";
//                                    PreviousStatus = "NULL";
//                                    utils.print("statusResponse", "null");
//                                }
//
//                            } else {
//                                timerCompleted = false;
//                                if (!PreviousStatus.equalsIgnoreCase("NULL")) {
//                                    utils.print("response", "null");
//                                    if (mMap != null) {
//                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            return;
//                                        }
//                                        mMap.clear();
//                                    }
//                                    if (mPlayer != null && mPlayer.isPlaying()) {
//                                        mPlayer.stop();
//                                        mPlayer = null;
//                                        countDownTimer.cancel();
//                                    }
//                                    clearVisibility();
//                                    destinationLayer.setVisibility(View.GONE);
//                                    layoutinfo.setVisibility(View.VISIBLE);
//                                    CurrentStatus = "ONLINE";
//                                    PreviousStatus = "NULL";
//                                    utils.print("statusResponse", "null");
//
//                                    LocationTracking.distance = 0.0f;
//                                }
//
//                            }
//                        }
//
//                    } else {
//                        try {
//                            JSONArray statusResponses = new JSONArray(datas);
//                            Log.e(TAG, "new_array: " + statusResponses);
//                            for (int i = 0; i < statusResponses.length(); i++) {
//
//                                JSONObject getjsonobj = statusResponses.getJSONObject(i);
//                                JSONObject jsonobj = getjsonobj.getJSONObject("request");
//                                Log.e(TAG, "jsonobj: " + jsonobj);
//                                getStatusVariable = jsonobj.optString("status");
//                                request_id = jsonobj.optString("id");
//                                Log.e(TAG, "REQ_ID: " + request_id);
//                                Log.e(TAG, "getStatusVariable: " + getStatusVariable);
//
//
//                                if ((jsonobj != null) && (request_id != null)) {
//                                    if ((!previous_request_id.equals(request_id) || previous_request_id.equals(" ")) && mMap != null) {
//                                        Log.e(TAG, "Previous req");
//                                        previous_request_id = request_id;
//                                        srcLatitude = Double.valueOf(jsonobj.optString("s_latitude"));
//                                        srcLongitude = Double.valueOf(jsonobj.optString("s_longitude"));
//                                        destLatitude = Double.valueOf(jsonobj.optString("d_latitude"));
//                                        destLongitude = Double.valueOf(jsonobj.optString("d_longitude"));
//                                        //noinspection deprecation
//                                        setSourceLocationOnMap(currentLatLng);
//                                        setPickupLocationOnMap();
//                                        sos.setVisibility(View.GONE);
//                                    }
//                                    utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + jsonobj.optString("status"));
//                                    if (!PreviousStatus.equals(jsonobj.optString("status"))) {
//                                        Log.e(TAG, "Previous req1111");
//                                        PreviousStatus = getStatusVariable;
//                                        clearVisibility();
//                                        utils.print("responseObj(" + request_id + ")", jsonobj.toString());
//                                        utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + jsonobj.optString("status"));
//                                        if (!getStatusVariable.equals("SEARCHING")) {
//                                            timerCompleted = false;
//                                            if (mPlayer != null && mPlayer.isPlaying()) {
//                                                mPlayer.stop();
//                                                mPlayer = null;
//                                                countDownTimer.cancel();
//                                            }
//                                        }
//
//                                        if (getStatusVariable.equals("SCHEDULED")) {
//                                            setValuesTo_ll_03_contentLayer_service_flow(statusResponses, response);
//                                            ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
//                                            btn_01_status.setText(getActivity().getString(R.string.tap_when_arrived));
//                                            CurrentStatus = "ARRIVED";
//                                            sos.setVisibility(View.GONE);
//                                            if (srcLatitude == 0 && srcLongitude == 0 && destLatitude == 0 && destLongitude == 0) {
//                                                mapClear();
//                                                srcLatitude = Double.valueOf(jsonobj.optString("s_latitude"));
//                                                srcLongitude = Double.valueOf(jsonobj.optString("s_longitude"));
//                                                destLatitude = Double.valueOf(jsonobj.optString("d_latitude"));
//                                                destLongitude = Double.valueOf(jsonobj.optString("d_longitude"));
//                                                //noinspection deprecation
//                                                //
//                                                setSourceLocationOnMap(currentLatLng);
//                                                setPickupLocationOnMap();
//                                            }
//                                            sos.setVisibility(View.GONE);
//                                            btn_cancel_ride.setVisibility(View.VISIBLE);
//                                            destinationLayer.setVisibility(View.VISIBLE);
//                                            layoutinfo.setVisibility(View.GONE);
//                                            String address = jsonobj.optString("s_address");
//                                            if (address != null && !address.equalsIgnoreCase("null") && address.length() > 0)
//                                                destination.setText(address);
//                                            else
//                                                destination.setText(getAddress(jsonobj.optString("s_latitude"),
//                                                        jsonobj.optString("s_longitude")));
//                                            topSrcDestTxtLbl.setText(getActivity().getString(R.string.pick_up));
//
//
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, error -> {
//                    utils.print("Error", error.toString());
//                    //errorHandler(error);
//                    timerCompleted = false;
//                    mapClear();
//                    clearVisibility();
//                    CurrentStatus = "ONLINE";
//                    PreviousStatus = "NULL";
//                    destinationLayer.setVisibility(View.GONE);
//                    layoutinfo.setVisibility(View.VISIBLE);
//                    if (mPlayer != null && mPlayer.isPlaying()) {
//                        mPlayer.stop();
//                        mPlayer = null;
//                        countDownTimer.cancel();
//                    }
//                    if (errorLayout.getVisibility() != View.VISIBLE) {
//                        errorLayout.setVisibility(View.VISIBLE);
//                        sos.setVisibility(View.GONE);
//                    }
//                }) {
//                    @Override
//                    public java.util.Map<String, String> getHeaders() {
//                        HashMap<String, String> headers = new HashMap<>();
//                        headers.put("X-Requested-With", "XMLHttpRequest");
//                        headers.put("Authorization", "Bearer " + token);
//                        Log.e(TAG, "HEADERS: " + headers.toString());
//                        return headers;
//                    }
//                };
//                ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//            } else {
//                displayMessage(getActivity().getString(R.string.oops_connect_your_internet));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setValuesTo_ll_01_contentLayer_accept_or_reject_now(JSONArray status) {
//        JSONObject statusResponse = new JSONObject();
//        try {
//            statusResponse = status.getJSONObject(0).getJSONObject("request");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (!status.getJSONObject(0).optString("time_left_to_respond").equals("")) {
//                count = status.getJSONObject(0).getString("time_left_to_respond");
//            } else {
//                count = "0";
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        countDownTimer = new CountDownTimer(Integer.parseInt(count) * 1000, 1000) {
//
//            @SuppressLint("SetTextI18n")
//            public void onTick(long millisUntilFinished) {
//                txt01Timer.setText("" + millisUntilFinished / 1000);
//                if (mPlayer == null) {
//                    mPlayer = MediaPlayer.create(getActivity(), R.raw.alert_tone);
//                } else {
//                    if (!mPlayer.isPlaying()) {
//                        mPlayer.start();
//                    }
//                }
//                isRunning = true;
//                timerCompleted = false;
//
//            }
//
//            public void onFinish() {
//                txt01Timer.setText("0");
//                mapClear();
//                clearVisibility();
//                if (mMap != null) {
//                    mMap.clear();
//                }
//                if (mPlayer != null && mPlayer.isPlaying()) {
//                    mPlayer.stop();
//                    mPlayer = null;
//                }
//                ll_01_contentLayer_accept_or_reject_now.setVisibility(View.GONE);
//                CurrentStatus = "ONLINE";
//                PreviousStatus = "NULL";
//                destinationLayer.setVisibility(View.GONE);
//                isRunning = false;
//                timerCompleted = true;
//                handleIncomingRequest("Reject", request_id);
//            }
//        };
//
//
//        countDownTimer.start();
//
//        try {
//            if (!statusResponse.optString("schedule_at").trim().equalsIgnoreCase("") && !statusResponse.optString("schedule_at").equalsIgnoreCase("null")) {
//                txtSchedule.setVisibility(View.VISIBLE);
//                String strSchedule = "";
//                try {
//                    strSchedule = getDate(statusResponse.optString("schedule_at")) + "th " + getMonth(statusResponse.optString("schedule_at"))
//                            + " " + getYear(statusResponse.optString("schedule_at")) + " at " + getTime(statusResponse.optString("schedule_at"));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                txtSchedule.setText(getActivity().getString(R.string.schedulet_at) + strSchedule);
//            } else {
//                txtSchedule.setVisibility(View.GONE);
//            }
//
//            final JSONObject user = statusResponse.getJSONObject("user");
//            if (user != null) {
//                if (!user.optString("picture").equals("null")) {
//                    if (user.optString("picture").startsWith("http"))
//                        Picasso.get().load(user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img01User);
//                    else
//                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img01User);
//                } else {
//                    img01User.setImageResource(R.drawable.ic_dummy_user);
//                }
//                final User userProfile = this.user;
//                img01User.setOnClickListener(v -> {
//                    Intent intent = new Intent(getActivity(), ShowProfile.class);
//                    intent.putExtra("user", userProfile);
//                    startActivity(intent);
//                });
//                txt01UserName.setText(user.optString("first_name"));
//                if (!statusResponse.isNull("distance")) {
//                    Double d = Double.parseDouble(statusResponse.optString("distance"));
//                    tvDistance.setText(Math.round(d) + "KM");
//                }
//                if (statusResponse.getJSONObject("user").getString("rating") != null) {
//                    rat01UserRating.setRating(Float.valueOf(user.getString("rating")));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        txt01Pickup.setText(address);
//        txtDropOff.setText(daddress);
//    }
//
//    private void setValuesTo_ll_03_contentLayer_service_flow(JSONArray status, JSONObject responess) {
//        JSONObject statusResponse = new JSONObject();
//        Log.e(TAG, "statusResponse: " + statusResponse);
//        try {
//            statusResponse = status.getJSONObject(0).getJSONObject("request");
//            lblCmfrmSourceAddress.setText(statusResponse.optString("s_address"));
//            lblCmfrmDestAddress.setText(statusResponse.optString("d_address"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            JSONObject user = statusResponse.getJSONObject("user");
//            if (user != null) {
//                if (!user.optString("mobile").equals("null")) {
//                    SharedHelper.putKey(getActivity(), "provider_mobile_no", "" + user.optString("mobile"));
//                } else {
//                    SharedHelper.putKey(getActivity(), "provider_mobile_no", "");
//                }
//
//                if (!user.optString("picture").equals("null")) {
//                    if (user.optString("picture").startsWith("http"))
//                        Picasso.get().load(user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img03User);
//                    else
//                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img03User);
//                } else {
//                    img03User.setImageResource(R.drawable.ic_dummy_user);
//                }
//                final User userProfile = this.user;
//                img03User.setOnClickListener(v -> {
//                    Intent intent = new Intent(getActivity(), ShowProfile.class);
//                    intent.putExtra("user", userProfile);
//                    startActivity(intent);
//                });
//
//                txt03UserName.setText(user.optString("first_name"));
//
//                if (statusResponse.getJSONObject("user").getString("rating") != null) {
//                    rat03UserRating.setRating(Float.valueOf(user.getString("rating")));
//                } else {
//                    rat03UserRating.setRating(0);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void setValuesTo_ll_04_contentLayer_payment(JSONArray status) {
//        JSONObject statusResponse = new JSONObject();
//        try {
//            statusResponse = status.getJSONObject(0).getJSONObject("request");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            txt04InvoiceId.setText(bookingId);
//            txt04BasePrice.setText(SharedHelper.getKey(getActivity(), "currency") + "" + statusResponse.getJSONObject("payment").optString("fixed"));
//            txt04Distance.setText(SharedHelper.getKey(getActivity(), "currency") + "" + statusResponse.getJSONObject("payment").optString("distance"));
//            txt04Tax.setText(SharedHelper.getKey(getActivity(), "currency") + "" + statusResponse.getJSONObject("payment").optString("tax"));
//            txt04Total.setText(SharedHelper.getKey(getActivity(), "currency") + "" + statusResponse.getJSONObject("payment").optString("total"));
//            txt04PaymentMode.setText(statusResponse.getString("payment_mode"));
//            txt04Commision.setText(SharedHelper.getKey(getActivity(), "currency") + "" + statusResponse.getJSONObject("payment").optString("commision"));
//            txtTotal.setText(SharedHelper.getKey(getActivity(), "currency") + "" + statusResponse.getJSONObject("payment").optString("total"));
//            if (statusResponse.getString("payment_mode").equals("CASH")) {
//                paymentTypeImg.setImageResource(R.drawable.money1);
//                btn_confirm_payment.setVisibility(View.VISIBLE);
//            } else {
//                paymentTypeImg.setImageResource(R.drawable.visa_icon);
//                btn_confirm_payment.setVisibility(View.GONE);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void setValuesTo_ll_05_contentLayer_feedback(JSONArray status) {
//        rat05UserRating.setRating(1.0f);
//        feedBackRating = "1";
//        rat05UserRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
//            utils.print("rating", rating + "");
//            if (rating < 1.0f) {
//                rat05UserRating.setRating(1.0f);
//                feedBackRating = "1";
//            }
//            feedBackRating = String.valueOf((int) rating);
//        });
//        JSONObject statusResponse = new JSONObject();
//        try {
//            statusResponse = status.getJSONObject(0).getJSONObject("request");
//            JSONObject user = statusResponse.getJSONObject("user");
//            if (user != null) {
//                lblProviderName.setText(user.optString("first_name"));
//                if (!user.optString("picture").equals("null")) {
//                    if (user.optString("picture").startsWith("http"))
//                        Picasso.get().load(user.getString("picture"))
//                                .placeholder(R.drawable.ic_dummy_user)
//                                .error(R.drawable.ic_dummy_user).into(img05User);
//                    else
//                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + user
//                                .getString("picture")).placeholder(R.drawable.ic_dummy_user)
//                                .error(R.drawable.ic_dummy_user).into(img05User);
//                } else {
//                    img05User.setImageResource(R.drawable.ic_dummy_user);
//                }
//                final User userProfile = this.user;
//                img05User.setOnClickListener(v -> {
//                    Intent intent = new Intent(getActivity(), ShowProfile.class);
//                    intent.putExtra("user", userProfile);
//                    startActivity(intent);
//                });
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        feedBackComment = edt05Comment.getText().toString();
//    }
//
//    private void update(final String status, String id) {
//        Log.v("Status", status + " ");
//        customDialog = new CustomDialog(getActivity());
//        customDialog.setCancelable(true);
//        customDialog.show();
//        if (status.equals("ONLINE")) {
//
//            JSONObject param = new JSONObject();
//            try {
//                param.put("service_status", "offline");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.UPDATE_AVAILABILITY_API, param, response -> {
//                customDialog.dismiss();
//                if (response != null) {
//                    if (response.optJSONObject("service").optString("status").equalsIgnoreCase("offline")) {
//                        activeStatus.setText(getActivity().getString(R.string.offline));
//                    } else {
//                        displayMessage(getActivity().getString(R.string.something_went_wrong));
//                    }
//                }
//            }, error -> {
//                customDialog.dismiss();
//                utils.print("Error", error.toString());
//                errorHandler(error);
//            }) {
//                @Override
//                public java.util.Map<String, String> getHeaders() {
//                    HashMap<String, String> headers = new HashMap<>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    headers.put("Authorization", "Bearer " + token);
//                    return headers;
//                }
//            };
//            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//        } else {
//            String url;
//            JSONObject param = new JSONObject();
//            if (status.equals("RATE")) {
//                url = URLHelper.BASE + "api/provider/trip/" + id + "/rate";
//                try {
//                    param.put("rating", feedBackRating);
//                    param.put("comment", edt05Comment.getText().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                utils.print("Input", param.toString());
//            } else {
//                url = URLHelper.BASE + "api/provider/trip/" + id;
//                try {
//                    param.put("_method", "PATCH");
//                    param.put("status", status);
//                    if (status.equalsIgnoreCase("DROPPED")) {
//                        param.put("latitude", crt_lat);
//                        param.put("longitude", crt_lng);
//                        param.put("distance", LocationTracking.distance * 0.001);
//                    }
//                    utils.print("Input", param.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, param, response -> {
//                if ((customDialog != null) && (customDialog.isShowing()))
//                    customDialog.dismiss();
//
//                if (response.optJSONObject("requests") != null) {
//                    utils.print("request", response.optJSONObject("requests").toString());
//                }
//                if (status.equals("RATE")) {
//                    destinationLayer.setVisibility(View.GONE);
//                    layoutinfo.setVisibility(View.VISIBLE);
//                    LatLng myLocation = new LatLng(Double.parseDouble(crt_lat), Double.parseDouble(crt_lng));
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
//                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    mapClear();
//                    clearVisibility();
//                    mMap.clear();
//                }
//            }, error -> {
//                if ((customDialog != null) && (customDialog.isShowing()))
//                    customDialog.dismiss();
//
//                utils.print("Error", error.toString());
//                if (status.equals("RATE")) {
//                    destinationLayer.setVisibility(View.GONE);
//                    layoutinfo.setVisibility(View.VISIBLE);
//                }
//                //errorHandler(error);
//            }) {
//                @Override
//                public java.util.Map<String, String> getHeaders() {
//                    HashMap<String, String> headers = new HashMap<>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    headers.put("Authorization", "Bearer " + token);
//                    return headers;
//                }
//            };
//            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//        }
//    }
//
//    public void cancelRequest(String id, String reason) {
//
//        customDialog = new CustomDialog(getActivity());
//        customDialog.setCancelable(false);
//        customDialog.show();
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("request_id", id);
//            object.put("cancel_reason", reason);
//            Log.e("", "request_id" + id);
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, response -> {
//                customDialog.dismiss();
//                utils.print("CancelRequestResponse", response.toString());
//                Toast.makeText(getActivity(), "" + "You have cancelled the request", Toast.LENGTH_SHORT).show();
//                mapClear();
//                clearVisibility();
//                layoutinfo.setVisibility(View.VISIBLE);
//                destinationLayer.setVisibility(View.GONE);
//                CurrentStatus = "ONLINE";
//                PreviousStatus = "NULL";
//            }, error -> {
//                customDialog.dismiss();
//                String json = null;
//                String Message;
//                NetworkResponse response = error.networkResponse;
//                if (response != null && response.data != null) {
//
//                    try {
//                        JSONObject errorObj = new JSONObject(new String(response.data));
//
//                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
//                            try {
//                                displayMessage(errorObj.optString("message"));
//                            } catch (Exception e) {
//                                displayMessage("Something went wrong");
//                                e.printStackTrace();
//                            }
//                        } else if (response.statusCode == 401) {
//                            GoToBeginActivity();
//                        } else if (response.statusCode == 422) {
//
//                            json = trimMessage(new String(response.data));
//                            if (json != "" && json != null) {
//                                displayMessage(json);
//                            } else {
//                                displayMessage(getActivity().getString(R.string.please_try_again));
//                            }
//                        } else if (response.statusCode == 503) {
//                            displayMessage(getActivity().getString(R.string.server_down));
//                        } else {
//                            displayMessage(getActivity().getString(R.string.please_try_again));
//                        }
//
//                    } catch (Exception e) {
//                        displayMessage("Something went wrong");
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    displayMessage(getActivity().getString(R.string.please_try_again));
//                }
//            }) {
//                @Override
//                public java.util.Map<String, String> getHeaders() {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(getActivity(), "access_token"));
//                    Log.e("", "Access_Token" + SharedHelper.getKey(getActivity(), "access_token"));
//                    return headers;
//                }
//            };
//
//            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleIncomingRequest(final String status, String id) {
//        if (!getActivity().isFinishing()) {
//            customDialog = new CustomDialog(getActivity());
//            customDialog.setCancelable(false);
//            customDialog.show();
//        }
//        String url = URLHelper.BASE + "api/provider/trip/" + id;
//
//        if (status.equals("Accept")) {
//            method = Request.Method.POST;
//        } else {
//            method = Request.Method.DELETE;
//        }
//
//        Log.v("handlerequest", url + " " + method);
//        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(method, url, null, response -> {
//            if (isAdded()) {
//                customDialog.dismiss();
//                if (status.equals("Accept")) {
//                    Toast.makeText(getActivity(), "Request accepted successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (!timerCompleted) {
//                        if (ll_01_contentLayer_accept_or_reject_now.getVisibility() == View.VISIBLE) {
//
//
//                            mapClear();
//                            clearVisibility();
//                            if (mMap != null) {
//                                mMap.clear();
//                            }
//                            ll_01_contentLayer_accept_or_reject_now.setVisibility(View.GONE);
//                            CurrentStatus = "ONLINE";
//                            PreviousStatus = "NULL";
//                            destinationLayer.setVisibility(View.GONE);
//                            layoutinfo.setVisibility(View.VISIBLE);
//                            isRunning = false;
//                            timerCompleted = true;
//                            handleIncomingRequest("Reject", request_id);
//                        }
//                        Toast.makeText(getActivity(), "Request rejected successfully", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(), "Request Timeout", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }, error -> {
//            customDialog.dismiss();
//            utils.print("Error", error.toString());
//            //errorHandler(error);
//
//        }) {
//            @Override
//            public java.util.Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//    }
//
//    public void errorHandler(VolleyError error) {
//        utils.print("Error", error.toString());
//        String json = null;
//        String Message;
//        NetworkResponse response = error.networkResponse;
//        if (response != null && response.data != null) {
//
//            try {
//                JSONObject errorObj = new JSONObject(new String(response.data));
//                utils.print("ErrorHandler", "" + errorObj.toString());
//                if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
//                    try {
//                        displayMessage(errorObj.optString("message"));
//                    } catch (Exception e) {
//                        displayMessage(getActivity().getString(R.string.something_went_wrong));
//                    }
//                } else if (response.statusCode == 401) {
//                    SharedHelper.putKey(getActivity(), "loggedIn", getString(R.string.False));
//                    GoToBeginActivity();
//                } else if (response.statusCode == 422) {
//                    json = trimMessage(new String(response.data));
//                    if (json != "" && json != null) {
//                        displayMessage(json);
//                    } else {
//                        displayMessage(getActivity().getString(R.string.please_try_again));
//                    }
//
//                } else if (response.statusCode == 503) {
//                    displayMessage(getActivity().getString(R.string.server_down));
//                } else {
//                    displayMessage(getActivity().getString(R.string.please_try_again));
//                }
//
//            } catch (Exception e) {
//                displayMessage(getActivity().getString(R.string.something_went_wrong));
//            }
//
//        } else {
//            displayMessage(getActivity().getString(R.string.please_try_again));
//        }
//    }
//
//    public void displayMessage(String toastString) {
//        utils.print("displayMessage", "" + toastString);
//    }
//
//    public void GoToBeginActivity() {
//        SharedHelper.putKey(getActivity(), "loggedIn", getString(R.string.False));
//        Intent mainIntent = new Intent(getActivity(), SplashScreen.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(mainIntent);
//        getActivity().finish();
//    }
//
//    public void goOnline() {
//        customDialog = new CustomDialog(getActivity());
//        customDialog.setCancelable(false);
//        customDialog.show();
//        JSONObject param = new JSONObject();
//        try {
//            param.put("service_status", "active");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.UPDATE_AVAILABILITY_API, param, response -> {
//            if (response != null) {
//                try {
//                    customDialog.dismiss();
//                    if (response.optJSONObject("service").optString("status").equalsIgnoreCase("active")) {
//                        activeStatus.setText(getActivity().getString(R.string.online));
//                    } else {
//                        displayMessage(getActivity().getString(R.string.something_went_wrong));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                customDialog.dismiss();
//            }
//        }, error -> {
//            customDialog.dismiss();
//            utils.print("Error", error.toString());
//            String json = null;
//            String Message;
//            NetworkResponse response = error.networkResponse;
//            if (response != null && response.data != null) {
//
//                try {
//                    JSONObject errorObj = new JSONObject(new String(response.data));
//
//                    if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
//                        try {
//                            displayMessage(errorObj.optString("message"));
//                        } catch (Exception e) {
//                            displayMessage(getActivity().getString(R.string.something_went_wrong));
//                        }
//                    } else if (response.statusCode == 401) {
//                        SharedHelper.putKey(getActivity(), "loggedIn", getString(R.string.False));
//                        GoToBeginActivity();
//                    } else if (response.statusCode == 422) {
//                        json = trimMessage(new String(response.data));
//                        if (json != "" && json != null) {
//                            displayMessage(json);
//                        } else {
//                            displayMessage(getActivity().getString(R.string.please_try_again));
//                        }
//
//                    } else if (response.statusCode == 503) {
//                        displayMessage(getActivity().getString(R.string.server_down));
//                    } else {
//                        displayMessage(getActivity().getString(R.string.please_try_again));
//                    }
//
//                } catch (Exception e) {
//                    displayMessage(getActivity().getString(R.string.something_went_wrong));
//                }
//
//            } else {
//                if (error instanceof NoConnectionError) {
//                    displayMessage(getActivity().getString(R.string.oops_connect_your_internet));
//                } else if (error instanceof NetworkError) {
//                    displayMessage(getActivity().getString(R.string.oops_connect_your_internet));
//                } else if (error instanceof TimeoutError) {
//                    goOnline();
//                }
//            }
//        }) {
//            @Override
//            public java.util.Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//    }
//
//    @Override
//    public void onDestroy() {
//        if (mPlayer != null && mPlayer.isPlaying()) {
//            mPlayer.stop();
//            mPlayer = null;
//        }
//        ha.removeCallbacksAndMessages(null);
//        super.onDestroy();
//    }
//
//    private String getMonth(String date) throws ParseException {
//        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
//        return monthName;
//    }
//
//    private String getDate(String date) throws ParseException {
//        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
//        return dateName;
//    }
//
//    private String getYear(String date) throws ParseException {
//        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
//        return yearName;
//    }
//
//    private String getTime(String date) throws ParseException {
//        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
//        return timeName;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_LOCATION) {
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(getActivity(), "Request Cancelled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public String getAddress(String strLatitude, String strLongitude) {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        double latitude = Double.parseDouble(strLatitude);
//        double longitude = Double.parseDouble(strLongitude);
//        String address = "", city = "", state = "";
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            city = addresses.get(0).getLocality();
//            state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (address.length() > 0 || city.length() > 0)
//            return address + ", " + city;
//        else
//            return getString(R.string.no_address);
//    }
//
//    @Override
//    public void onPause() {
//
//        super.onPause();
//        Utilities.onMap = false;
//        if (customDialog != null) {
//            if (customDialog.isShowing()) {
//                customDialog.dismiss();
//            }
//        }
//        if (ha != null) {
//            isRunning = true;
//            if (mPlayer != null && mPlayer.isPlaying()) {
//                normalPlay = true;
//                mPlayer.stop();
//            } else {
//                normalPlay = false;
//            }
//            ha.removeCallbacksAndMessages(null);
//        }
//    }
//
//    private void showCancelDialog() {
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getActivity().getString(R.string.cancel_confirm));
//
//        builder.setPositiveButton(R.string.yes, (dialog, which) -> showReasonDialog());
//
//        builder.setNegativeButton(R.string.no, (dialog, which) -> {
//            //Reset to previous seletion menu in navigation
//            dialog.dismiss();
//        });
//        builder.setCancelable(false);
//        final AlertDialog dialog = builder.create();
//        dialog.setOnShowListener(arg -> {
//        });
//        dialog.show();
//    }
//
//    private void showReasonDialog() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.cancel_dialog, null);
//        final EditText reasonEtxt = view.findViewById(R.id.reason_etxt);
//        Button submitBtn = view.findViewById(R.id.submit_btn);
//        RadioGroup radioCancel = view.findViewById(R.id.radioCancel);
//        radioCancel.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.driver) {
//                reasonEtxt.setVisibility(View.VISIBLE);
//                cancaltype = getResources().getString(R.string.plan_changed);
//            }
//            if (checkedId == R.id.vehicle) {
//                reasonEtxt.setVisibility(View.VISIBLE);
//                cancaltype = getResources().getString(R.string.booked_another_cab);
//            }
//            if (checkedId == R.id.app) {
//                reasonEtxt.setVisibility(View.VISIBLE);
//                cancaltype = getResources().getString(R.string.my_reason_is_not_listed);
//            }
//        });
//        builder.setView(view)
//                .setCancelable(true);
//        final AlertDialog dialog = builder.create();
//        submitBtn.setOnClickListener(v -> {
//
//            if (cancaltype.isEmpty()) {
//                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_reason), Toast.LENGTH_SHORT).show();
//
//            } else {
//                cancalReason = reasonEtxt.getText().toString();
//                if (cancalReason.isEmpty()) {
//                    reasonEtxt.setError(getResources().getString(R.string.please_specify_reason));
//                } else {
//                    if (reasonEtxt.getText().toString().length() > 0)
//                        cancelRequest(request_id, reasonEtxt.getText().toString());
//                    else
//                        cancelRequest(request_id, "");
//                    dialog.dismiss();
//                }
//            }
//        });
//        dialog.show();
//
//    }
//
//    private void showSosDialog() {
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getActivity().getString(R.string.sos_confirm));
//
//        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
//            //cancelRequest(request_id);
//            dialog.dismiss();
//            String mobile = SharedHelper.getKey(getActivity(), "sos");
//            if (mobile != null && !mobile.equalsIgnoreCase("null") && mobile.length() > 0) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 3);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + mobile));
//                    startActivity(intent);
//                }
//            } else {
//                displayMessage(getActivity().getString(R.string.user_no_mobile));
//            }
//
//        });
//
//        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
//        builder.setCancelable(false);
//        final AlertDialog dialog = builder.create();
//        dialog.setOnShowListener(arg -> {
//        });
//        dialog.show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Utilities.onMap = true;
//        if (Utilities.clearSound) {
//            NotificationManager notificationManager = (NotificationManager) getActivity()
//                    .getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.cancelAll();
//        }
//        utils.print(TAG, "onResume: Handler Call out" + isRunning);
//        if (isRunning) {
//            if (mPlayer != null && normalPlay) {
//                mPlayer = MediaPlayer.create(getActivity(), R.raw.alert_tone);
//                mPlayer.start();
//            }
//            utils.print(TAG, "onResume: Handler Call" + isRunning);
//            ha.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //call function
//                    if (type != null) {
//                        checkStatusSchedule();
//                    } else {
//                        checkStatus();
//                    }
//                    ha.postDelayed(this, 3000);
//                }
//            }, 3000);
//        }
//    }
//
//    @Override
//    public void onCameraMove() {
//        utils.print("Current marker", "Zoom Level " + mMap.getCameraPosition().zoom);
//        if (currentMarker != null) {
//            if (!mMap.getProjection().getVisibleRegion().latLngBounds.contains(currentMarker.getPosition())) {
//                utils.print("Current marker", "Current Marker is not visible");
//                if (imgCurrentLoc.getVisibility() == View.GONE) {
//                    imgCurrentLoc.setVisibility(View.VISIBLE);
//                }
//            } else {
//                utils.print("Current marker", "Current Marker is visible");
//                if (imgCurrentLoc.getVisibility() == View.VISIBLE) {
//                    imgCurrentLoc.setVisibility(View.GONE);
//                }
//                if (mMap.getCameraPosition().zoom < 15.0f) {
//                    if (imgCurrentLoc.getVisibility() == View.GONE) {
//                        imgCurrentLoc.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        if ((customDialog != null) && (customDialog.isShowing()))
//            customDialog.dismiss();
//        if (mMap != null) {
//            if (currentMarker != null) {
//                currentMarker.remove();
//            }
//
//            MarkerOptions markerOptions1 = new MarkerOptions()
//                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
//                    .anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_current_location));
//            currentMarker = mMap.addMarker(markerOptions1);
//
//            Log.e("DriverSide", "DriveronLocationChanged: " + location.getLatitude());
//            Log.e("DriverSide", "DriveronLocationChanged: " + location.getLongitude());
//
//            if (value == 0) {
//                myLat = String.valueOf(location.getLatitude());
//                myLng = String.valueOf(location.getLongitude());
//
//                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(12).build();
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(myLocation).anchor(0.5f, 0.75f);
//                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                value++;
//            }
//
//            crt_lat = String.valueOf(location.getLatitude());
//            Log.e(TAG, "crt_lat" + crt_lat);
//            crt_lng = String.valueOf(location.getLongitude());
//            Log.e(TAG, "crt_lng" + crt_lng);
//            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//            if (type != null) {
//                checkStatusSchedule();
//            } else {
//                checkStatus();
//            }
//
//        }
//    }
//
//
//    // Fetches data from url passed
//    private class FetchUrl extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            // For storing data from web service
//            String data = "";
//
//            try {
//                // Fetching the data from web service
//                data = downloadUrl(url[0]);
//                Log.d("Background Task data", data);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask parserTask = new ParserTask();
//
//            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
//
//        }
//    }
//
//    /**
//     * A class to parse the Google Places in JSON format
//     */
//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                Log.d("ParserTask", jsonData[0]);
//                DataParser parser = new DataParser();
//                Log.d("ParserTask", parser.toString());
//
//                // Starts parsing data
//                routes = parser.parse(jObject);
//                Log.d("ParserTask", "Executing routes");
//                Log.d("ParserTask", routes.toString());
//
//            } catch (Exception e) {
//                Log.d("ParserTask", e.toString());
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        // Executes in UI thread, after the parsing process
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            try {
//
//                ArrayList<LatLng> points = null;
//                PolylineOptions lineOptions = null;
//
//                // Traversing through all the routes
//                for (int i = 0; i < result.size(); i++) {
//                    points = new ArrayList<>();
//                    lineOptions = new PolylineOptions();
//
//                    // Fetching i-th route
//                    List<HashMap<String, String>> path = result.get(i);
//
//                    // Fetching all the points in i-th route
//                    for (int j = 0; j < path.size(); j++) {
//                        HashMap<String, String> point = path.get(j);
//
//                        double lat = Double.parseDouble(point.get("lat"));
//                        double lng = Double.parseDouble(point.get("lng"));
//                        LatLng position = new LatLng(lat, lng);
//
//                        points.add(position);
//                    }
//                    mMap.clear();
//                    MarkerOptions markerOptions = new MarkerOptions().title("Source").anchor(0.5f, 0.75f)
//                            .position(sourceLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location));
//                    mMap.addMarker(markerOptions);
//                    MarkerOptions markerOptions1 = new MarkerOptions().title("Destination").anchor(0.5f, 0.75f)
//                            .position(destLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_provider_marker));
//                    mMap.addMarker(markerOptions);
//                    mMap.addMarker(markerOptions1);
//
//
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    LatLngBounds bounds;
//                    builder.include(sourceLatLng);
//                    builder.include(destLatLng);
//                    bounds = builder.build();
//                    if (CurrentStatus.equalsIgnoreCase("STARTED") ||
//                            CurrentStatus.equalsIgnoreCase("ARRIVED")) {
//
//                    } else {
//                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 600, 600, 20);
//                        mMap.moveCamera(cu);
//                    }
//                    mMap.getUiSettings().setMapToolbarEnabled(false);
//
//
//                    // Adding all the points in the route to LineOptions
//                    lineOptions.addAll(points);
//                    lineOptions.width(5);
//                    lineOptions.color(Color.BLACK);
//
//                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");
//
//                }
//
//                // Drawing polyline in the Google DriverMapFragment for the i-th route
//                if (lineOptions != null && points != null) {
//                    mMap.addPolyline(lineOptions);
//                } else {
//                    Log.d("onPostExecute", "without Polylines drawn");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//}
