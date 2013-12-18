package com.tsmsogn.stackmob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobUser;


public class ForgotPasswordActivity extends ActionBarActivity implements
        OnClickListener {
    private final static String TAG = ForgotPasswordActivity.class
            .getCanonicalName();
    private Button mResetButton;
    private EditText mUsernameEditText;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mContext = getApplicationContext();
        StackMobAndroid.init(mContext, 0, Constants.STACKMOB_API_KEY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsernameEditText = (EditText) findViewById(R.id.editText1);
        mResetButton = (Button) findViewById(R.id.button1);
        mResetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mResetButton) {
            StackMobUser.sentForgotPasswordEmail(getUsername(),
                    new StackMobCallback() {

                        @Override
                        public void success(String arg0) {
                            Intent intent = new Intent(
                                    mContext,
                                    ResetPasswordWithTemporaryPasswordActivity.class);
                            intent.putExtra(Constants.USERNAME, getUsername());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void failure(StackMobException arg0) {
                            // TODO Auto-generated method stub
                            Log.v(TAG, arg0.getMessage());
                            threadAgnosticToast(mContext, arg0.getMessage(),
                                    Toast.LENGTH_LONG);
                        }
                    });
        }
    }

    private String getUsername() {
        return mUsernameEditText.getText().toString();
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
