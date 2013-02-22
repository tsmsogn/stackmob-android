package com.tsmsogn.stackmob;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> mObjects;
    private TextView mPostBodyTextView;
    private ImageView mPostActionImageView;
    private PostArrayAdapterCallback mPostArrayAdapterCallback;

    public PostArrayAdapter(Context context, int textViewResourceId,
            List<Post> objects,
            PostArrayAdapterCallback postArrayAdapterCallback) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mObjects = objects;
        mPostArrayAdapterCallback = postArrayAdapterCallback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final int p = position;

        // if (view == null) {
        // LayoutInflater inflater = getLayoutInflater();
        // view = inflater.inflate(R.layout.multiple_choice, null);
        // }

        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_post, null);

        final Post post = mObjects.get(position);

        mPostBodyTextView = (TextView) view.findViewById(R.id.textView1);
        mPostActionImageView = (ImageView) view.findViewById(R.id.imageView1);

        mPostBodyTextView.setText(post.getBody());
        mPostActionImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPostArrayAdapterCallback.success(p);
            }
        });

        return view;
    }

    interface PostArrayAdapterCallback {
        void success(int p);

        void failure();
    }
}
