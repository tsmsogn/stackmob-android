package com.tsmsogn.stackmob;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.tsmsogn.stackmob.PostActionDailog.PostActionDailogCallback;
import com.tsmsogn.stackmob.PostArrayAdapter.PostArrayAdapterCallback;

public class MainActivity extends SherlockActivity implements
        OnItemClickListener {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int MENU_ID_MENU1 = (Menu.FIRST + 1);
    private static final int MENU_ID_MENU2 = (Menu.FIRST + 2);
    private Context mContext;
    protected User mUser;
    private ListView mPostsLitView;
    private List<Post> mPosts;
    private PostArrayAdapter mPostArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Constants.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        StackMobAndroid.init(mContext, 0, Constants.STACKMOB_API_KEY);

        if (StackMob.getStackMob().isLoggedIn()) {
            // TODO
            User.getLoggedInUser(User.class, StackMobOptions.depthOf(2),
                    new StackMobQueryCallback<User>() {

                        @Override
                        public void failure(StackMobException arg0) {
                            doLogin();
                        }

                        @Override
                        public void success(List<User> arg0) {
                            mUser = arg0.get(0);
                            initializePostArrayAdapter();
                        }
                    });
        } else {
            doLogin();
        }

        mPostsLitView = (ListView) findViewById(R.id.listView1);
        mPostsLitView.setOnItemClickListener(this);
    }

    private void doLogin() {
        Intent intent = new Intent(getApplicationContext(),
                SignInActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_ID_MENU2, 0, "Add").setIcon(R.drawable.ic_content_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SubMenu subMenu1 = menu.addSubMenu("Action Item");
        subMenu1.add(0, MENU_ID_MENU1, 1, R.string.sign_out);

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.ic_action_overflow);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ID_MENU1:
            mUser.logout(new StackMobCallback() {

                @Override
                public void success(String arg0) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void failure(StackMobException arg0) {
                    // TODO Auto-generated method stub
                }
            });

            break;
        case MENU_ID_MENU2:
            startActivityForResult(new Intent(this, AddPostActivity.class), 0);
            break;
        default:
            break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 0 && resultCode == RESULT_OK) {
                Post post = new Post();
                post.fillFromJson(data.getStringExtra("post"));
                mUser.addPost(post);
                mUser.save(StackMobOptions.depthOf(2));
                mPostArrayAdapter.notifyDataSetChanged();
            } else if (requestCode == 1) {
                if (data.getExtras().containsKey(Constants.LOGGED_IN_USER)) {
                    mUser = User.newFromJson(User.class,
                            data.getStringExtra(Constants.LOGGED_IN_USER));
                    initializePostArrayAdapter();
                }
            }
        } catch (StackMobException e) {
        }
    }

    private void initializePostArrayAdapter() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mPostArrayAdapter = new PostArrayAdapter(mContext,
                        R.id.listView1, mUser.getPosts(),
                        new PostArrayAdapterCallback() {

                            @Override
                            public void success(final int p) {
                                PostActionDailog postActionDailog = new PostActionDailog(
                                        mContext,
                                        new PostActionDailogCallback() {

                                            @Override
                                            public void delete() {
                                                // TODO
                                                mUser.getPosts()
                                                        .get(p)
                                                        .destroy(
                                                                new StackMobCallback() {

                                                                    @Override
                                                                    public void success(
                                                                            String arg0) {
                                                                        mUser.getPosts()
                                                                                .remove(p);
                                                                        mUser.save(
                                                                                StackMobOptions
                                                                                        .depthOf(2),
                                                                                new StackMobCallback() {

                                                                                    @Override
                                                                                    public void success(
                                                                                            String arg0) {
                                                                                        runOnUiThread(new Runnable() {

                                                                                            @Override
                                                                                            public void run() {
                                                                                                mPostArrayAdapter
                                                                                                        .notifyDataSetChanged();
                                                                                            }
                                                                                        });

                                                                                    }

                                                                                    @Override
                                                                                    public void failure(
                                                                                            StackMobException arg0) {
                                                                                        // TODO
                                                                                        // Auto-generated
                                                                                        // method
                                                                                        // stub

                                                                                    }
                                                                                });

                                                                    }

                                                                    @Override
                                                                    public void failure(
                                                                            StackMobException arg0) {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // method
                                                                        // stub

                                                                    }
                                                                });

                                            }

                                            @Override
                                            public void share() {
                                                // TODO Auto-generated method
                                                // stub

                                            }

                                            @Override
                                            public void edit() {
                                                Intent intent = new Intent(
                                                        mContext,
                                                        AddPostActivity.class);
                                                intent.putExtra(
                                                        "post",
                                                        mUser.getPosts()
                                                                .get(p)
                                                                .toJson(StackMobOptions
                                                                        .depthOf(2)));
                                                startActivityForResult(intent,
                                                        0);
                                            }

                                            public void cancel() {
                                                // TODO Auto-generated method
                                                // stub

                                            }
                                        });
                                postActionDailog.show();
                            }

                            @Override
                            public void failure() {
                                // TODO Auto-generated method stub

                            }

                        });
                mPosts = mUser.getPosts();
                mPostsLitView.setAdapter(mPostArrayAdapter);
            }
        });

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
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

}
