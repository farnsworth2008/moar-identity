package tools.moar.identity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class FacebookLoginHandler extends IdentityHandler {
    private LoginButton facebookLoginButton;
    final CallbackManager facebookCallbackManager = CallbackManager.Factory.create();
    private int loginButtonId;

    public FacebookLoginHandler(AppCompatActivity activity, int loginButtonId) {
        super(activity);
        this.loginButtonId = loginButtonId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        facebookLoginButton = (LoginButton) findViewById(loginButtonId);
        facebookLoginButton.setReadPermissions(Arrays.asList("email"));
        FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                doSignIn(accessToken);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException exception) {
                FacebookLoginHandler.this.onCatch(exception);
            }
        };
        facebookLoginButton.registerCallback(facebookCallbackManager, facebookCallback);
    }

    private void doSignIn(final AccessToken accessToken) {
        String uid = accessToken.getUserId();
        hide();
        doSignIn(new Account() {
            @Override
            public String getDisplayName() {
                return accessToken.getUserId();
            }
        });
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        facebookLoginButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn) {
            doSignIn(accessToken);
        }

    }

    @Override
    public void hide() {
        facebookLoginButton.setVisibility(View.GONE);
    }
}
