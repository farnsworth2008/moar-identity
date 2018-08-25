package tools.moar.identity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class IdentityHandler {
    private final AppCompatActivity activity;
    private SignOnObserver signOnObserver;

    protected IdentityHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    public IdentityHandler withSignOnObserver(SignOnObserver signOnObserver) {
        this.signOnObserver =  signOnObserver;
        return this;
    }

    public final View findViewById(int id) {
        return activity.findViewById(id);
    }

    public final AppCompatActivity getActivity() {
        return activity;
    }


    public void onCatch(Exception e) {};
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);
    public abstract void onCreate(Bundle savedInstanceState);
    public abstract void logout();

    protected final void doSignIn(Account account) {
        if(signOnObserver != null) {
            signOnObserver.onSignIn(account);
        }
    }

    public abstract void onStart();

    public abstract void hide();
}
