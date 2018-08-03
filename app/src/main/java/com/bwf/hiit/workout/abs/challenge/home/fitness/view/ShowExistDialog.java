//package com.bwf.hiit.workout.abs.challenge.home.fitness.view.;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//
//import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
//import com.google.android.gms.ads.AdView;
//
//
//
///**
// * Created by saqibmirza on 22/12/2016.
// */
//
//public class ShowExistDialog {
//
//    Dialog dialog;
//    Context context;
//    View myView;
//
//    AdView adView;
//
//    Button dialogRateUs;
//
//    Button dialogNo;
//
//    Button dialogYes;
//
//   // CloseAppListener closeAppListener;
//
//    public ShowExistDialog(Context context) {
//        this.context = context;
//       // closeAppListener = (CloseAppListener) context;
//        createDialog();
//    }
//
//    public void createDialog() {
//        LayoutInflater myInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        myView = myInflator.inflate(R.layout.exit_dailog_ad, null, false);
//
//      //  ButterKnife.bind(this, myView);
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            dialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
//        } else {
//            dialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
//        }
//
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(myView);
//
//
//
//       // AdManager.getInstance(context).createAndShowBanner(adView);
//
//    }
//
//    public void showDialog() {
//
//        dialog.show();
//    }
//
//    public void hideDialog() {
//        dialog.hide();
//    }
//
//    public void setCancelable(boolean isCancelable) {
//        dialog.setCancelable(isCancelable);
//    }
//
//
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.dialog_rate_us:
//                dialog.dismiss();
//                context.startActivity(AppUtils.getLikeUsIntent());
//
//                break;
//            case R.id.dialog_no:
//                dialog.dismiss();
//                break;
//            case R.id.dialog_yes:
//
////                if (closeAppListener != null) {
////                    dialog.dismiss();
////                    dialog.cancel();
////                    closeAppListener.closeApp();
//                } else
//                    dialog.dismiss();
//                break;
//        }
//    }
//}
