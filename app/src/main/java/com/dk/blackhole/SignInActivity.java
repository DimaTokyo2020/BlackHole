package com.dk.blackhole;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = SignInActivity.class.getSimpleName();
    static final int GOOGLE_SIGN = 123;
    private EditText setNameET;
    FirebaseAuth mAuth;
    Button btn_login, btn_logout;
    TextView text;
    ImageView image;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.login);
        btn_logout = findViewById(R.id.logout);
        text = findViewById(R.id.text);
        image = findViewById(R.id.image);
        progressBar = findViewById(R.id.progressCircular);
        setNameET = findViewById(R.id.setNameET);
        initEditTextPressedDone(setNameET);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btn_login.setOnClickListener(v->SignInGoogle());
        btn_logout.setOnClickListener(v->logOut());

        if(mAuth.getCurrentUser() != null){

            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }


    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent sigIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(sigIntent, GOOGLE_SIGN);
        Log.i(TAG, "SignInGoogle");
    }


    /**
     * Here we get google sign answer
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){ firebaseAuthWithGoogle(account); }
            }catch (ApiException e){ e.printStackTrace(); }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task ->{
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "signIn success");

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "signIn failure" , task.getException());

                        Toast.makeText(this, "SignIn failed!", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                    
                });
    }

    /**
     *
     * @param user
     */
    private void updateUI(FirebaseUser user) {

//
            if (user != null) {
                // User is signed in
                String displayName = user.getDisplayName();
                String userEmail = user.getEmail();
                Uri profileUri = user.getPhotoUrl();

                // If the above were null, iterate the provider data
                // and set with the first non null data
                for (UserInfo userInfo : user.getProviderData()) {
                    if (displayName == null && userInfo.getDisplayName() != null) {
                        displayName = userInfo.getDisplayName();
                    }
                    if (profileUri == null && userInfo.getPhotoUrl() != null) {
                        profileUri = userInfo.getPhotoUrl();
                    }
                    if (userEmail == null && userInfo.getEmail() != null) {
                        userEmail = userInfo.getEmail();
                    }
                }
                if(displayName == null){ askUserForAName(); }//If the user don't have firebase user name we ask him/her for a name other way we just open next activity.
                else {
                    setNameEmailImage(displayName, userEmail, profileUri);
                    startHomeActivity();
                }
            btn_login.setVisibility(View.INVISIBLE);
            btn_logout.setVisibility(View.VISIBLE);
        }

        else{
            text.setText(getString(R.string.firebase_login));
            Picasso.get().load(R.drawable.ic_firebase_logo);
            btn_login.setVisibility(View.VISIBLE);;
            btn_logout.setVisibility(View.INVISIBLE);
        }
    }



    private void setNameEmailImage(String displayName, String userEmail, Uri profileUri) {

        Picasso.get().load(profileUri).into(image);

        text.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.append(" Info: \n");
            text.append(displayName + "\n");
            text.append(userEmail);
            }
        },100);
    }

    void logOut(){

        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this,task -> updateUI(null));
    }


    /**
     * Open keyboard and wait for user to input his/ her name
     */
    private void askUserForAName(){
        setNameET.setVisibility(View.VISIBLE);
        openKeyboard();
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * Here we initialize edit text, so when the user finish to write his/ her name we open next activity
     * @param editText - the text that hold the user name
     */
    private void initEditTextPressedDone( EditText editText) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editText.setVisibility(View.INVISIBLE);
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(editText.getText().toString()).build());
                    startHomeActivity();
                    return false;
                }
                return false;
            }
        });
    }


    /**
     * Open HomeActivity
     */
    private void startHomeActivity(){
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}





//    public void test(View view) {
//
//
//
//        FirebaseUser user = mAuth.getCurrentUser();
//        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName("").build());
//        String displayName = user.getDisplayName();
//        String userEmail = user.getEmail();
//        Uri profileUri = user.getPhotoUrl();
//
//        // If the above were null, iterate the provider data
//        // and set with the first non null data
//        for (UserInfo userInfo : user.getProviderData()) {
//            if (displayName == null && userInfo.getDisplayName() != null) {
//                displayName = userInfo.getDisplayName();
//            }
//            if (profileUri == null && userInfo.getPhotoUrl() != null) {
//                profileUri = userInfo.getPhotoUrl();
//            }
//            if (userEmail == null && userInfo.getEmail() != null) {
//                userEmail = userInfo.getEmail();
//            }
//        }
//
//        setNameEmailImage(displayName, userEmail, profileUri);
//    }
