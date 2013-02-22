package com.tsmsogn.stackmob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {
    private List<Post> posts;

    public User() {
        super(User.class);
    }
    
    protected User(String username, String password) {
        super(User.class, username, password);
    }

    public List<Post> getPosts() {
        if (posts == null) {
            posts = new ArrayList<Post>();
        }
        Collections.sort(posts, new Comparator<Post>() {

            @Override
            public int compare(Post lhs, Post rhs) {
                // TODO Auto-generated method stub
                return 0;
            }

        });
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getID().equals(post.getID())) {
                posts.set(i, post);
                return;
            }
        }
        posts.add(post);
    }

}
