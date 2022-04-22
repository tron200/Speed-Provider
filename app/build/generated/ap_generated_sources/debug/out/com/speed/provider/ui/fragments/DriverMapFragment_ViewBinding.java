// Generated code from Butter Knife. Do not modify!
package com.speed.provider.ui.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.speed.provider.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DriverMapFragment_ViewBinding implements Unbinder {
  private DriverMapFragment target;

  private View view7f0a02ab;

  private View view7f0a0213;

  private View view7f0a00fd;

  private View view7f0a0104;

  private View view7f0a0101;

  private View view7f0a00fe;

  private View view7f0a00ff;

  private View view7f0a0100;

  private View view7f0a0102;

  private View view7f0a0103;

  private View view7f0a021a;

  private View view7f0a020c;

  private View view7f0a0224;

  private View view7f0a02fe;

  @UiThread
  public DriverMapFragment_ViewBinding(final DriverMapFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.menuIcon, "field 'menuIcon' and method 'menuIconClick'");
    target.menuIcon = Utils.castView(view, R.id.menuIcon, "field 'menuIcon'", ImageView.class);
    view7f0a02ab = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.menuIconClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.imgCurrentLoc, "field 'imgCurrentLoc' and method 'imgCurrentLocClick'");
    target.imgCurrentLoc = Utils.castView(view, R.id.imgCurrentLoc, "field 'imgCurrentLoc'", ImageView.class);
    view7f0a0213 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imgCurrentLocClick();
      }
    });
    target.ll_01_mapLayer = Utils.findRequiredViewAsType(source, R.id.ll_01_mapLayer, "field 'll_01_mapLayer'", LinearLayout.class);
    target.driverArrived = Utils.findRequiredViewAsType(source, R.id.driverArrived, "field 'driverArrived'", LinearLayout.class);
    target.driverPicked = Utils.findRequiredViewAsType(source, R.id.driverPicked, "field 'driverPicked'", LinearLayout.class);
    target.driveraccepted = Utils.findRequiredViewAsType(source, R.id.driveraccepted, "field 'driveraccepted'", LinearLayout.class);
    target.tvTrips = Utils.findRequiredViewAsType(source, R.id.tvTrips, "field 'tvTrips'", TextView.class);
    target.tvCommision = Utils.findRequiredViewAsType(source, R.id.tvCommision, "field 'tvCommision'", TextView.class);
    target.tvEarning = Utils.findRequiredViewAsType(source, R.id.tvEarning, "field 'tvEarning'", TextView.class);
    target.txtTotalEarning = Utils.findRequiredViewAsType(source, R.id.txtTotalEarning, "field 'txtTotalEarning'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_01_status, "field 'btn_01_status' and method 'btn_01_statusClick'");
    target.btn_01_status = Utils.castView(view, R.id.btn_01_status, "field 'btn_01_status'", Button.class);
    view7f0a00fd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_01_statusClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_rate_submit, "field 'btn_rate_submit' and method 'btn_rate_submitClick'");
    target.btn_rate_submit = Utils.castView(view, R.id.btn_rate_submit, "field 'btn_rate_submit'", Button.class);
    view7f0a0104 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_rate_submitClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_confirm_payment, "field 'btn_confirm_payment' and method 'btn_confirm_paymentClick'");
    target.btn_confirm_payment = Utils.castView(view, R.id.btn_confirm_payment, "field 'btn_confirm_payment'", Button.class);
    view7f0a0101 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_confirm_paymentClick();
      }
    });
    target.img_profile = Utils.findRequiredViewAsType(source, R.id.img_profile, "field 'img_profile'", CircleImageView.class);
    target.total_earn_layout = Utils.findRequiredViewAsType(source, R.id.total_earn_layout, "field 'total_earn_layout'", CardView.class);
    view = Utils.findRequiredView(source, R.id.btn_02_accept, "field 'btn_02_accept' and method 'btn_02_acceptClick'");
    target.btn_02_accept = Utils.castView(view, R.id.btn_02_accept, "field 'btn_02_accept'", Button.class);
    view7f0a00fe = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_02_acceptClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_02_reject, "field 'btn_02_reject' and method 'btn_02_rejectClick'");
    target.btn_02_reject = Utils.castView(view, R.id.btn_02_reject, "field 'btn_02_reject'", TextView.class);
    view7f0a00ff = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_02_rejectClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_cancel_ride, "field 'btn_cancel_ride' and method 'btn_cancel_rideClick'");
    target.btn_cancel_ride = Utils.castView(view, R.id.btn_cancel_ride, "field 'btn_cancel_ride'", Button.class);
    view7f0a0100 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_cancel_rideClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_go_offline, "field 'btn_go_offline' and method 'btn_go_offlineClick'");
    target.btn_go_offline = Utils.castView(view, R.id.btn_go_offline, "field 'btn_go_offline'", Button.class);
    view7f0a0102 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_go_offlineClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_go_online, "field 'btn_go_online' and method 'btn_go_onlineClick'");
    target.btn_go_online = Utils.castView(view, R.id.btn_go_online, "field 'btn_go_online'", Button.class);
    view7f0a0103 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btn_go_onlineClick();
      }
    });
    target.activeStatus = Utils.findRequiredViewAsType(source, R.id.activeStatus, "field 'activeStatus'", TextView.class);
    target.offline_layout = Utils.findRequiredViewAsType(source, R.id.offline_layout, "field 'offline_layout'", RelativeLayout.class);
    target.ll_01_contentLayer_accept_or_reject_now = Utils.findRequiredViewAsType(source, R.id.ll_01_contentLayer_accept_or_reject_now, "field 'll_01_contentLayer_accept_or_reject_now'", LinearLayout.class);
    target.ll_02_contentLayer_accept_or_reject_later = Utils.findRequiredViewAsType(source, R.id.ll_02_contentLayer_accept_or_reject_later, "field 'll_02_contentLayer_accept_or_reject_later'", LinearLayout.class);
    target.ll_03_contentLayer_service_flow = Utils.findRequiredViewAsType(source, R.id.ll_03_contentLayer_service_flow, "field 'll_03_contentLayer_service_flow'", LinearLayout.class);
    target.ll_04_contentLayer_payment = Utils.findRequiredViewAsType(source, R.id.ll_04_contentLayer_payment, "field 'll_04_contentLayer_payment'", LinearLayout.class);
    target.ll_05_contentLayer_feedback = Utils.findRequiredViewAsType(source, R.id.ll_05_contentLayer_feedback, "field 'll_05_contentLayer_feedback'", LinearLayout.class);
    target.lnrGoOffline = Utils.findRequiredViewAsType(source, R.id.lnrGoOffline, "field 'lnrGoOffline'", LinearLayout.class);
    target.layoutinfo = Utils.findRequiredViewAsType(source, R.id.layoutinfo, "field 'layoutinfo'", LinearLayout.class);
    target.lnrNotApproved = Utils.findRequiredViewAsType(source, R.id.lnrNotApproved, "field 'lnrNotApproved'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.imgNavigationToSource, "field 'imgNavigationToSource' and method 'imgNavigationToSourceClick'");
    target.imgNavigationToSource = Utils.castView(view, R.id.imgNavigationToSource, "field 'imgNavigationToSource'", ImageView.class);
    view7f0a021a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imgNavigationToSourceClick();
      }
    });
    target.img01User = Utils.findRequiredViewAsType(source, R.id.img01User, "field 'img01User'", ImageView.class);
    target.sos = Utils.findRequiredViewAsType(source, R.id.sos, "field 'sos'", ImageView.class);
    target.rat01UserRating = Utils.findRequiredViewAsType(source, R.id.rat01UserRating, "field 'rat01UserRating'", RatingBar.class);
    target.txt01Pickup = Utils.findRequiredViewAsType(source, R.id.txtPickup, "field 'txt01Pickup'", TextView.class);
    target.txtDropOff = Utils.findRequiredViewAsType(source, R.id.txtDropOff, "field 'txtDropOff'", TextView.class);
    target.txt01Timer = Utils.findRequiredViewAsType(source, R.id.txt01Timer, "field 'txt01Timer'", TextView.class);
    target.txt01UserName = Utils.findRequiredViewAsType(source, R.id.txt01UserName, "field 'txt01UserName'", TextView.class);
    target.tvDistance = Utils.findRequiredViewAsType(source, R.id.tvDistance, "field 'tvDistance'", TextView.class);
    target.txtSchedule = Utils.findRequiredViewAsType(source, R.id.txtSchedule, "field 'txtSchedule'", TextView.class);
    target.img02User = Utils.findRequiredViewAsType(source, R.id.img02User, "field 'img02User'", ImageView.class);
    target.txt02UserName = Utils.findRequiredViewAsType(source, R.id.txt02UserName, "field 'txt02UserName'", TextView.class);
    target.rat02UserRating = Utils.findRequiredViewAsType(source, R.id.rat02UserRating, "field 'rat02UserRating'", RatingBar.class);
    target.txt02ScheduledTime = Utils.findRequiredViewAsType(source, R.id.txt02ScheduledTime, "field 'txt02ScheduledTime'", TextView.class);
    target.txt02From = Utils.findRequiredViewAsType(source, R.id.txt02From, "field 'txt02From'", TextView.class);
    target.txt02To = Utils.findRequiredViewAsType(source, R.id.txt02To, "field 'txt02To'", TextView.class);
    target.img03User = Utils.findRequiredViewAsType(source, R.id.img03User, "field 'img03User'", CircleImageView.class);
    target.txt03UserName = Utils.findRequiredViewAsType(source, R.id.txt03UserName, "field 'txt03UserName'", TextView.class);
    target.lblCmfrmDestAddress = Utils.findRequiredViewAsType(source, R.id.lblCmfrmDestAddress, "field 'lblCmfrmDestAddress'", TextView.class);
    target.lblCmfrmSourceAddress = Utils.findRequiredViewAsType(source, R.id.lblCmfrmSourceAddress, "field 'lblCmfrmSourceAddress'", TextView.class);
    target.rat03UserRating = Utils.findRequiredViewAsType(source, R.id.rat03UserRating, "field 'rat03UserRating'", RatingBar.class);
    view = Utils.findRequiredView(source, R.id.img03Call, "field 'img03Call' and method 'img03CallClick'");
    target.img03Call = Utils.castView(view, R.id.img03Call, "field 'img03Call'", ImageButton.class);
    view7f0a020c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.img03CallClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_chat, "field 'img_chat' and method 'img_chatClick'");
    target.img_chat = Utils.castView(view, R.id.img_chat, "field 'img_chat'", ImageButton.class);
    view7f0a0224 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.img_chatClick();
      }
    });
    target.img03Status1 = Utils.findRequiredViewAsType(source, R.id.img03Status1, "field 'img03Status1'", ImageView.class);
    target.img03Status2 = Utils.findRequiredViewAsType(source, R.id.img03Status2, "field 'img03Status2'", ImageView.class);
    target.img03Status3 = Utils.findRequiredViewAsType(source, R.id.img03Status3, "field 'img03Status3'", ImageView.class);
    target.txt04InvoiceId = Utils.findRequiredViewAsType(source, R.id.invoice_txt, "field 'txt04InvoiceId'", TextView.class);
    target.txtTotal = Utils.findRequiredViewAsType(source, R.id.txtTotal, "field 'txtTotal'", TextView.class);
    target.txt04BasePrice = Utils.findRequiredViewAsType(source, R.id.txt04BasePrice, "field 'txt04BasePrice'", TextView.class);
    target.txt04Distance = Utils.findRequiredViewAsType(source, R.id.txt04Distance, "field 'txt04Distance'", TextView.class);
    target.txt04Tax = Utils.findRequiredViewAsType(source, R.id.txt04Tax, "field 'txt04Tax'", TextView.class);
    target.txt04Total = Utils.findRequiredViewAsType(source, R.id.txt04Total, "field 'txt04Total'", TextView.class);
    target.txt04PaymentMode = Utils.findRequiredViewAsType(source, R.id.txt04PaymentMode, "field 'txt04PaymentMode'", TextView.class);
    target.txt04Commision = Utils.findRequiredViewAsType(source, R.id.txt04Commision, "field 'txt04Commision'", TextView.class);
    target.destination = Utils.findRequiredViewAsType(source, R.id.destination, "field 'destination'", TextView.class);
    target.lblProviderName = Utils.findRequiredViewAsType(source, R.id.lblProviderName, "field 'lblProviderName'", TextView.class);
    target.paymentTypeImg = Utils.findRequiredViewAsType(source, R.id.paymentTypeImg, "field 'paymentTypeImg'", ImageView.class);
    target.errorLayout = Utils.findRequiredViewAsType(source, R.id.lnrErrorLayout, "field 'errorLayout'", LinearLayout.class);
    target.destinationLayer = Utils.findRequiredViewAsType(source, R.id.destinationLayer, "field 'destinationLayer'", LinearLayout.class);
    target.txtNotes = Utils.findRequiredViewAsType(source, R.id.txtNotes, "field 'txtNotes'", TextView.class);
    target.layoutNotes = Utils.findRequiredViewAsType(source, R.id.layoutNotes, "field 'layoutNotes'", LinearLayout.class);
    target.img05User = Utils.findRequiredViewAsType(source, R.id.img05User, "field 'img05User'", ImageView.class);
    target.rat05UserRating = Utils.findRequiredViewAsType(source, R.id.rat05UserRating, "field 'rat05UserRating'", RatingBar.class);
    target.user_name = Utils.findRequiredViewAsType(source, R.id.user_name, "field 'user_name'", TextView.class);
    target.user_type = Utils.findRequiredViewAsType(source, R.id.user_type, "field 'user_type'", TextView.class);
    target.user_total_ride_distanse = Utils.findRequiredViewAsType(source, R.id.user_total_ride_distanse, "field 'user_total_ride_distanse'", TextView.class);
    view = Utils.findRequiredView(source, R.id.online_offline_switch, "field 'online_offline_switch' and method 'online_offline_switchClick'");
    target.online_offline_switch = Utils.castView(view, R.id.online_offline_switch, "field 'online_offline_switch'", Switch.class);
    view7f0a02fe = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.online_offline_switchClick();
      }
    });
    target.active_Status = Utils.findRequiredViewAsType(source, R.id.active_Status, "field 'active_Status'", TextView.class);
    target.edt05Comment = Utils.findRequiredViewAsType(source, R.id.edt05Comment, "field 'edt05Comment'", EditText.class);
    target.topSrcDestTxtLbl = Utils.findRequiredViewAsType(source, R.id.src_dest_txt, "field 'topSrcDestTxtLbl'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DriverMapFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.menuIcon = null;
    target.imgCurrentLoc = null;
    target.ll_01_mapLayer = null;
    target.driverArrived = null;
    target.driverPicked = null;
    target.driveraccepted = null;
    target.tvTrips = null;
    target.tvCommision = null;
    target.tvEarning = null;
    target.txtTotalEarning = null;
    target.btn_01_status = null;
    target.btn_rate_submit = null;
    target.btn_confirm_payment = null;
    target.img_profile = null;
    target.total_earn_layout = null;
    target.btn_02_accept = null;
    target.btn_02_reject = null;
    target.btn_cancel_ride = null;
    target.btn_go_offline = null;
    target.btn_go_online = null;
    target.activeStatus = null;
    target.offline_layout = null;
    target.ll_01_contentLayer_accept_or_reject_now = null;
    target.ll_02_contentLayer_accept_or_reject_later = null;
    target.ll_03_contentLayer_service_flow = null;
    target.ll_04_contentLayer_payment = null;
    target.ll_05_contentLayer_feedback = null;
    target.lnrGoOffline = null;
    target.layoutinfo = null;
    target.lnrNotApproved = null;
    target.imgNavigationToSource = null;
    target.img01User = null;
    target.sos = null;
    target.rat01UserRating = null;
    target.txt01Pickup = null;
    target.txtDropOff = null;
    target.txt01Timer = null;
    target.txt01UserName = null;
    target.tvDistance = null;
    target.txtSchedule = null;
    target.img02User = null;
    target.txt02UserName = null;
    target.rat02UserRating = null;
    target.txt02ScheduledTime = null;
    target.txt02From = null;
    target.txt02To = null;
    target.img03User = null;
    target.txt03UserName = null;
    target.lblCmfrmDestAddress = null;
    target.lblCmfrmSourceAddress = null;
    target.rat03UserRating = null;
    target.img03Call = null;
    target.img_chat = null;
    target.img03Status1 = null;
    target.img03Status2 = null;
    target.img03Status3 = null;
    target.txt04InvoiceId = null;
    target.txtTotal = null;
    target.txt04BasePrice = null;
    target.txt04Distance = null;
    target.txt04Tax = null;
    target.txt04Total = null;
    target.txt04PaymentMode = null;
    target.txt04Commision = null;
    target.destination = null;
    target.lblProviderName = null;
    target.paymentTypeImg = null;
    target.errorLayout = null;
    target.destinationLayer = null;
    target.txtNotes = null;
    target.layoutNotes = null;
    target.img05User = null;
    target.rat05UserRating = null;
    target.user_name = null;
    target.user_type = null;
    target.user_total_ride_distanse = null;
    target.online_offline_switch = null;
    target.active_Status = null;
    target.edt05Comment = null;
    target.topSrcDestTxtLbl = null;

    view7f0a02ab.setOnClickListener(null);
    view7f0a02ab = null;
    view7f0a0213.setOnClickListener(null);
    view7f0a0213 = null;
    view7f0a00fd.setOnClickListener(null);
    view7f0a00fd = null;
    view7f0a0104.setOnClickListener(null);
    view7f0a0104 = null;
    view7f0a0101.setOnClickListener(null);
    view7f0a0101 = null;
    view7f0a00fe.setOnClickListener(null);
    view7f0a00fe = null;
    view7f0a00ff.setOnClickListener(null);
    view7f0a00ff = null;
    view7f0a0100.setOnClickListener(null);
    view7f0a0100 = null;
    view7f0a0102.setOnClickListener(null);
    view7f0a0102 = null;
    view7f0a0103.setOnClickListener(null);
    view7f0a0103 = null;
    view7f0a021a.setOnClickListener(null);
    view7f0a021a = null;
    view7f0a020c.setOnClickListener(null);
    view7f0a020c = null;
    view7f0a0224.setOnClickListener(null);
    view7f0a0224 = null;
    view7f0a02fe.setOnClickListener(null);
    view7f0a02fe = null;
  }
}
