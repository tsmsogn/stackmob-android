package com.tsmsogn.stackmob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        }else if (v == mSignInWithTwitterImageView) {
            startActivity(new Intent(this, TwitterOAuth.class));
            finish();
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

}
