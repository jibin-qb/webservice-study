package webservices.jibin.com.webservicesstudy.customview;


import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.view.View;


import com.facebook.Session;
import com.facebook.model.GraphUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import webservices.jibin.com.webservicesstudy.R;

public class MainActivity extends Activity {


    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            PackageInfo info = getPackageManager().getPackageInfo(

                    "com.gallery.gallerybaraweez",

                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                Log.d("#####KeyHash:",

                        Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {

//

        } catch (NoSuchAlgorithmException e) {

//

        }
    }

    public void showProgressDialog(String message) {

        try {
            dismissProgressDialog();
            if (!(this).isFinishing()) {
                mProgressDialog = ProgressDialog.show(this, "", message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void showProgressDialog() {
        showProgressDialog(getString(R.string.please_wait));
    }


    public void dismissProgressDialog() {
        if (!(this).isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;

        }
    }

    public void postFB(View view) {
        new FaceBookHelper(this) {
            @Override
            public void userDataFetchedFromFaceBook(GraphUser user) {

            }

            @Override
            public void postToFacebookWall(Session session) {

            }
        }.startFaceBookSession();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (Session.getActiveSession() != null) {
                Session.getActiveSession().onActivityResult(
                        MainActivity.this, requestCode, resultCode, data);
            }

        }
    }
}
