package com.devdelhi.crypto.UI.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.devdelhi.crypto.R;

import de.mateware.snacky.Snacky;

public class StartActivity extends AppCompatActivity {

    // TAG is for show some tag logs in LOG screen.
    public static final String TAG = "real";

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;/*

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    // Google API Client object.
    public GoogleApiClient googleApiClient;

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;*/

    private TextView loginTextView;

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginTextView = findViewById(R.id.loginText);
        Typeface mediumBoldFont = Typeface.createFromAsset(StartActivity.this.getAssets(), "fonts/mediumbold.ttf");
        loginTextView.setTypeface(mediumBoldFont);

     /*   signInButton = findViewById(R.id.googleSignInButton);
        // Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignInMethod();
            }
        });

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startMainActivity();
                }
            }
        };

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(StartActivity.this)
                .enableAutoManage(StartActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Snacky.builder()
                                .setView(findViewById(android.R.id.content))
                                .setText("Google Connection Failed")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                    }
                } *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void UserSignInMethod() {
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignInCode) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                Log.d(TAG, "onActivitySuccess");
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {
                        if (AuthResultTask.isSuccessful()) {
                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            Snacky.builder()
                                    .setView(findViewById(android.R.id.content))
                                    .setText("Welcome, " + firebaseUser.getDisplayName().toString() + " .")
                                    .setDuration(Snacky.LENGTH_INDEFINITE)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();

                            writeDataToSharedPreference(firebaseUser.getDisplayName());
                            startMainActivity();
                        } else {
                            Snacky.builder()
                                    .setView(findViewById(android.R.id.content))
                                    .setText("Something Went Wrong")
                                    .setDuration(Snacky.LENGTH_INDEFINITE)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();
                        }
                    }
                });
    }

    private void writeDataToSharedPreference(String s) {
        SharedPreferences sharedPref = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("UserName", s);
        Log.d(TAG, "Write Data : " + s);
        editor.apply();
    }

    private void startMainActivity() {
        startActivity(new Intent(StartActivity.this, real.class));
        finish();
    }*/}
}
