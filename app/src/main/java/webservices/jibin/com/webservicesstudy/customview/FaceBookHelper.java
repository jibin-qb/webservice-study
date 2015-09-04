package webservices.jibin.com.webservicesstudy.customview;

import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;


public abstract class FaceBookHelper {

    private static final String sGRAPH_PATH = "me";
    private static final String sGRAPH_PATH_posts = "Post";
    public static final List<String> sPERMISSIONS = Arrays.asList(
            "public_profile","user_posts", "publish_actions");

    private final Session.StatusCallback mFbStatusCallback = new SessionStatusCallback();
    private MainActivity mPreLoginActivity;
    private Session mFbSession;
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String PICTURE = "picture";
    public static final String MESSAGE = "message";


    public FaceBookHelper(MainActivity context) {
        this.mPreLoginActivity = context;
        startFaceBookSession();
    }

    public static void fbSessionClearClose() {
        if (Session.getActiveSession() != null) {
            Session.getActiveSession().closeAndClearTokenInformation();
            Session.getActiveSession().close();
        }
        Session.setActiveSession(null);
    }

    public abstract void userDataFetchedFromFaceBook(GraphUser user);

    public abstract void postToFacebookWall(Session session);

    public void startFaceBookSession() {
        mFbSession = Session.getActiveSession();
        if (mFbSession == null) {
            mFbSession = new Session(mPreLoginActivity);
            Session.setActiveSession(mFbSession);
        }
        if (mFbSession != null && !mFbSession.isOpened()
                && !mFbSession.isClosed()) {
            if (mFbSession.getState() == SessionState.OPENING) {
                mFbSession.close();
            }
            mPreLoginActivity.showProgressDialog();
            mFbSession
                    .openForPublish(new Session.OpenRequest(mPreLoginActivity)
                            .setPermissions(sPERMISSIONS).setCallback(
                                    mFbStatusCallback));
        } else {
            Session.openActiveSession(mPreLoginActivity, true,
                    mFbStatusCallback);
        }
        if (mFbSession.isClosed()) {
            fbSessionClearClose();
            //startFaceBookSession();
        }
    }

    private void fetchUserInfoFromFB(final Session session) {
        // callback after Graph API response with user object
        Request.GraphUserCallback graphUserCallback;
        graphUserCallback = new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                mPreLoginActivity.dismissProgressDialog();
                userDataFetchedFromFaceBook(user);
                postToFacebook(session);
            }
        };
        final Request.GraphUserCallback finalCallback = graphUserCallback;
        final Request.Callback wrapperCallback = new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                finalCallback.onCompleted(
                        response.getGraphObjectAs(GraphUser.class), response);
            }
        };
        final Bundle params = new Bundle();
        final Request request = new Request(session, sGRAPH_PATH, params, null,
                wrapperCallback);
        request.executeAsync();
    }

    private void getPostsFromFb(final Session session){
        final Request.Callback wrapperCallback = new Request.Callback() {
            @Override
            public void onCompleted(Response response) {

                //Log.d("response>>>",response.getRawResponse());
            }
        };
        final Bundle params = new Bundle();
        final Request request = new Request(session, sGRAPH_PATH_posts, params, null,
                wrapperCallback);
        request.executeAsync();
    }
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            mPreLoginActivity.dismissProgressDialog();

            if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
                fbSessionClearClose();
            }
            if (session.isOpened()) {
                fetchUserInfoFromFB(session);
            }
            if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
                fbSessionClearClose();
            }
        }
    }
    private void postToFacebook(Session mSession) {
        Request.Callback callback = new Request.Callback() {
            public void onCompleted(Response response) {

            }
        };
        Request request = new Request(mSession, sGRAPH_PATH,
                createBundleValuesForFb(), HttpMethod.POST, callback);
        RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();
    }
    private Bundle createBundleValuesForFb() {
        Bundle bundle = new Bundle();


        bundle.putString(MESSAGE, "cxjkvhxckxchkxcvh");

            bundle.putString(LINK, "cbnkvbkxcvbxcvkb.com");
           // bundle.putString(NHConstants.PICTURE, mImageUrl);

        return bundle;
    }

}
