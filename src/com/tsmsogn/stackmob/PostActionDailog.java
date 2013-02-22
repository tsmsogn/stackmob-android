package com.tsmsogn.stackmob;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class PostActionDailog extends Dialog implements
        android.view.View.OnClickListener {

    private Context mContext;
    private PostActionDailogCallback mPostActionDailogCallback;
    private Button mDeleteButton;
    private Button mEditButton;
    private Button mCancelButton;
    private Button mShareButton;

    public PostActionDailog(Context context,
            PostActionDailogCallback postActionDailogCallback) {
        super(context, R.style.remove_title);
        mContext = context;
        mPostActionDailogCallback = postActionDailogCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_post_action);

        getWindow().setLayout(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);

        mDeleteButton = (Button) findViewById(R.id.button1);
        mEditButton = (Button) findViewById(R.id.button2);
        mShareButton = (Button) findViewById(R.id.button3);
        mCancelButton = (Button) findViewById(R.id.button4);

        mDeleteButton.setOnClickListener(this);
        mEditButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mDeleteButton) {
            dismiss();
            mPostActionDailogCallback.delete();
        } else if (v == mEditButton) {
            dismiss();
            mPostActionDailogCallback.edit();
        } else if (v == mShareButton) {
            dismiss();
            mPostActionDailogCallback.share();
        } else if (v == mCancelButton) {
            dismiss();
        }
    }

    interface PostActionDailogCallback {
        void delete();

        void share();

        void edit();

        void cancel();
    }
}
