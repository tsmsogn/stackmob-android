package com.tsmsogn.stackmob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

public class ResetPasswordWithTemporaryPasswordActivity extends ActionBarActivity
        implements OnClickListener {
    private final static String TAG = ResetPasswordWithTemporaryPasswordActivity.class
            .getCanonicalName();
    private Context mContext;
    private EditText mNewPasswordEditText;
    private EditText mTemporaryPasswordEditText;
    private Button mResetButton;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_with_temporary_password);

        mContext = getApplicationContext();
        StackMobAndroid.init(mContext, 0, Constants.STACKMOB_API_KEY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTemporaryPasswordEditText = (EditText) findViewById(R.id.editText1);
        mNewPasswordEditText = (EditText) findViewById(R.id.editText2);
        mResetButton = (Button) findViewById(R.id.button1);
        mResetButton.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getExtras().containsKey(Constants.USERNAME)) {
            mUsername = intent.getStringExtra(Constants.USERNAME);
        }
    }

    private String getUsername() {
        return mUsername;
    }

    private String getTemporaryPassword() {
        return mTemporaryPasswordEditText.getText().toString();
    }

    private String getNewPassword() {
        return mNewPasswordEditText.getText().toString();
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == mResetButton) {
            User user = new User(getUsername(), getTemporaryPassword());
            user.loginResettingTemporaryPassword(getNewPassword(),
                    new StackMobModelCallback() {
                        @Override
                        public void success() {
                            startActivity(new Intent(mContext,
                                    MainActivity.class));
                            finish();
                        }

                        @Override
                        public void failure(StackMobException e) {
                            // TODO
                            threadAgnosticToast(mContext, e.getMessage(),
                                    Toast.LENGTH_LONG);
                        }
                    });
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }

}
