
package com.wangoose.ojt_whc_java;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SearchUsersResult implements Serializable {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<UserItem> items = null;

    private boolean isBookmark = false;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<UserItem> getItems() {
        return items;
    }

    public void setItems(List<UserItem> items) {
        this.items = items;
    }

    public void createEmptyItems() { items = new ArrayList<UserItem>(); }

    public void addItems(UserItem target) { items.add(target); }

    public void removeItems(int index) { items.remove(index); }

    public void setBookmarkList(boolean signal) { isBookmark = signal; }

    public boolean isBookmarkList() { return isBookmark; }
}