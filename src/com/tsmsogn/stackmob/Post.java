package com.tsmsogn.stackmob;

import java.util.ArrayList;
import java.util.List;

import com.stackmob.sdk.model.StackMobModel;

public class Post extends StackMobModel {
    private String parent_id;
    private String title;
    private String body;
    private String excerpt;
    private int status;
    private int left;
    private int right;
    private List<PostMeta> postMetas;
    
    public Post() {
        super(Post.class);
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public List<PostMeta> getPostMetas() {
        if (postMetas == null) {
            postMetas = new ArrayList<PostMeta>();
        }
        return postMetas;
    }

    public void setPostMetas(List<PostMeta> postMetas) {
        this.postMetas = postMetas;
    }
    
    public PostMeta getSpecificPostMeta(String key) {
        postMetas = getPostMetas();
        for (PostMeta postMeta : postMetas) {
            if (postMeta.getKey().equals(key)) {
                return postMeta;
            }
        }
        // TODO
        return null;
    }

}
