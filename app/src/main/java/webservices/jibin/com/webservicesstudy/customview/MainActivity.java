package webservices.jibin.com.webservicesstudy.customview;


import android.app.ProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;


import com.facebook.Session;
import com.facebook.model.GraphUser;

import webservices.jibin.com.webservicesstudy.R;

public class MainActivity extends Activity {


	ProgressDialog mProgressDialog  ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


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
}
