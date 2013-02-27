package com.tsmsogn.stackmob;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class SignInActivity extends SherlockActivity implements OnClickListener {
    private static final String TAG = SignInActivity.class.getCanonicalName();

    private Context mContext;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mSignInButton;

    private Button mSignUpButton;

    private TextView mForgotYourPasswordButton;

    private ImageView mSignInWithTwitterImageView;

    final private CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
            Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET);
    final private CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
            Constants.TWITTER_REQUEST_TOKEN_ENDPOINT_URL,
            Constants.TWITTER_ACCESS_TOKEN_ENDPOINT_URL,
            Constants.TWITTER_AUTHORIZATION_WEBSITE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Constants.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mContext = getApplicationContext();
        StackMobAndroid.init(mContext, 0, Constants.STACKMOB_API_KEY);

        mUsernameEditText = (EditText) findViewById(R.id.editText1);
        mPasswordEditText = (EditText) findViewById(R.id.editText2);
        mSignInButton = (Button) findViewById(R.id.button1);
        mSignUpButton = (Button) findViewById(R.id.button2);
        mSignInWithTwitterImageView = (ImageView) findViewById(R.id.imageView1);
        mForgotYourPasswordButton = (TextView) findViewById(R.id.textView1);
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mForgotYourPasswordButton.setOnClickListener(this);
        mSignInWithTwitterImageView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if (StackMob.getStackMob().isLoggedIn()) {
        // startActivity(new Intent(this, MainActivity.class));
        // finish();
        // }
    }

    @Override
    public void onClick(View v) {
        if (v == mSignInButton) {
            signIn(getUser());
        } else if (v == mSignUpButton) {
            signUp(getUser());
        } else if (v == mSignInWithTwitterImageView) {
            try {
                String authUrl = provider.retrieveRequestToken(consumer,
                        Constants.TWITTER_CALLBACK_URL);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v == mForgotYourPasswordButton) {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
            finish();
        }
    }

    private void signUp(final User user) {
        user.save(new StackMobCallback() {

            @Override
            public void success(String arg0) {
                signIn(user);
            }

            @Override
            public void failure(StackMobException arg0) {
                // TODO
                threadAgnosticToast(getApplicationContext(), arg0.getMessage(),
                        Toast.LENGTH_LONG);
            }
        });

    }

    private void signIn(final User user) {
        user.login(new StackMobCallback() {

            @Override
            public void failure(StackMobException arg0) {
                threadAgnosticToast(mContext, arg0.getMessage(),
                        Toast.LENGTH_LONG);
            }

            @Override
            public void success(String arg0) {
                Intent intent = getIntent();
                intent.putExtra(Constants.LOGGED_IN_USER, user.toJson());
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }

    private User getUser() {
        return new User(mUsernameEditText.getText().toString(),
                mPasswordEditText.getText().toString());
    }

    private void threadAgnosticToast(final Context ctx, final String txt,
            final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, txt, duration).show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null
                && uri.toString().startsWith(Constants.TWITTER_CALLBACK_URL)) {
            String verifier = uri
                    .getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

            try {
                // this will populate token and token_secret in consumer
                provider.retrieveAccessToken(consumer, verifier);
                final String twitterToken = consumer.getToken();
                final String twitterSecret = consumer.getTokenSecret();
                final User user = new User();

                Log.v(TAG, "twitterToken: " + twitterToken);
                Log.v(TAG, "twitterSecret: " + twitterSecret);
                
                user.createWithTwitter(twitterToken, twitterSecret, new StackMobCallback() {
                    
                    @Override
                    public void success(String arg0) {
                        Log.v(TAG, arg0);
                    }
                    
                    @Override
                    public void failure(StackMobException arg0) {
                        Log.v(TAG, arg0.getMessage());
                    }
                });

//                user.loginWithTwitter(twitterToken, twitterSecret,
//                        new StackMobModelCallback() {
//                            @Override
//                            public void success() {
//                                signIn(user);
//                            }
//
//                            @Override
//                            public void failure(StackMobException e) {
//                                Log.v(TAG, e.getMessage());
//                                user.createWithTwitter(twitterToken,
//                                        twitterSecret,
//                                        new StackMobModelCallback() {
//                                            @Override
//                                            public void success() {
//                                                // signIn(user);
//                                            }
//
//                                            @Override
//                                            public void failure(
//                                                    StackMobException e) {
//                                            }
//                                        });
//                            }
//                        });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
