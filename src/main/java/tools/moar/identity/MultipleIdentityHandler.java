package tools.moar.identity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MultipleIdentityHandler extends IdentityHandler implements SignOnObserver {
    private final IdentityHandler[] loginHandlers;

    public MultipleIdentityHandler(AppCompatActivity activity, IdentityHandler[] loginHandlers) {
        super(activity);
        this.loginHandlers = loginHandlers;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for(IdentityHandler handler: loginHandlers) {
            handler.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        for(IdentityHandler handler: loginHandlers) {
            handler.onCreate(savedInstanceState);
            handler.withSignOnObserver(this);
        }
    }

    @Override
    public void logout() {
        for(IdentityHandler handler: loginHandlers) {
            handler.logout();
        }
    }

    @Override
    public void onStart() {
        for(IdentityHandler handler: loginHandlers) {
            handler.onStart();
        }
    }

    @Override
    public void hide() {
        for(IdentityHandler handler: loginHandlers) {
            handler.hide();
        }
    }

    @Override
    public final void onSignIn(Account account) {
        super.doSignIn(account);
        for(IdentityHandler handler: loginHandlers) {
            handler.hide();
        }
    }
}
