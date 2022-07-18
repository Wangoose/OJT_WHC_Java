package com.wangoose.ojt_whc_java;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SearchUsersResult implements Parcelable {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<UserItem> items = new ArrayList<>();

    private boolean isBookmark = false;

    protected SearchUsersResult(List<UserItem> items) {
        this.items = items;
    }

    protected SearchUsersResult(Parcel source) {
        source.readTypedList(items, UserItem.CREATOR);
        isBookmark = source.readBoolean();
    }

    public static final Creator<SearchUsersResult> CREATOR = new Creator<SearchUsersResult>() {
        @Override
        public SearchUsersResult createFromParcel(Parcel source) {
            return new SearchUsersResult(source);
        }

        @Override
        public SearchUsersResult[] newArray(int size) {
            return new SearchUsersResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeBoolean(isBookmark);
    }

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