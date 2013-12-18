package com.tsmsogn.stackmob;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.exception.StackMobException;

public class AddPostActivity extends ActionBarActivity implements
        OnClickListener {

    private View mSaveButton;
    private EditText mPostEditText;
    private EditText mPostMetaEditText;
    private Post mPost = new Post();

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("post")) {
            try {
                mPost.fillFromJson(getIntent().getExtras().getString("post"));
            } catch (StackMobException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        mSaveButton = (Button) findViewById(R.id.button1);
        mPostEditText = (EditText) findViewById(R.id.editText1);
        mPostMetaEditText = (EditText) findViewById(R.id.editText2);
        if (mPost.getBody() != null) {
            mPostEditText.setText(mPost.getBody());
        }
        if (mPost.getSpecificPostMeta(Constants.SPEAKER) != null) {
            mPostMetaEditText.setText(mPost.getSpecificPostMeta(Constants.SPEAKER)
                    .getValue());
        }
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == mSaveButton) {
            Intent intent = getIntent();
            intent.putExtra("post",
                    getPost().toJson(StackMobOptions.depthOf(2)));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private Post getPost() {
        mPost.setBody(mPostEditText.getText().toString());
        PostMeta postMeta = mPost.getSpecificPostMeta(Constants.SPEAKER);
        if (postMeta != null) {
            mPost.getPostMetas().set(
                    mPost.getPostMetas().indexOf(postMeta),
                    new PostMeta(Constants.SPEAKER, mPostMetaEditText.getText()
                            .toString()));
        } else {
            mPost.getPostMetas().add(
                    new PostMeta(Constants.SPEAKER, mPostMetaEditText.getText()
                            .toString()));
        }
        return mPost;
    }

}
