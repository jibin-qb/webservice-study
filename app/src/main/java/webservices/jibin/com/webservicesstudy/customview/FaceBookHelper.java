package webservices.jibin.com.webservicesstudy.customview;

import java.util.Arrays;
import java.util.List;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;


public abstract class FaceBookHelper {

    private static final String sGRAPH_PATH = "me";
    public static final List<String> sPERMISSIONS = Arrays.asList(
            "user_posts ", "publish_actions");

    private final Session.StatusCallback mFbStatusCallback = new SessionStatusCallback();
    private MainActivity mPreLoginActivity;
    private Session mFbSession;

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
            startFaceBookSession();
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
                postToFacebookWall(session);
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

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            mPreLoginActivity.dismissProgressDialog();
            if (session.isOpened()) {
                mPreLoginActivity.showProgressDialog();

                    return;
                }
                fetchUserInfoFromFB(session);

            if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
                fbSessionClearClose();
            }
        }
    }
}
