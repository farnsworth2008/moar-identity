package tools.moar.identity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GoogleLoginHandler extends IdentityHandler {
    private static int RC_SIGN_IN = 1000;
    private GoogleSignInClient googleSignInClient;
    private View googleSignInButton;
    private final int signInButtonId;

    public GoogleLoginHandler(AppCompatActivity activity, int sign_in_button_id) {
        super(activity);
        this.signInButtonId = sign_in_button_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        googleSignInButton = findViewById(signInButtonId);
        findViewById(signInButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() != signInButtonId) {
                    throw new RuntimeException();
                }
                Intent signInIntent = googleSignInClient.getSignInIntent();
                getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                doSignIn(account);
            } catch (ApiException e) {
                onCatch(e);
            }
        }
    }

    private void doSignIn(final GoogleSignInAccount account) {
        hide();
        doSignIn(new Account() {
            @Override
            public String getDisplayName() {
                return account.getDisplayName();
            }
        });
    }

    @Override
    public void logout() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            googleSignInButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            doSignIn(account);
        }
    }

    @Override
    public void hide() {
        googleSignInButton.setVisibility(View.GONE);
    }
}
