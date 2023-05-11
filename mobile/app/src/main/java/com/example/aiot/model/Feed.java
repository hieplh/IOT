package com.example.aiot.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@Entity(tableName = "feed")
public class Feed {

    @PrimaryKey
    @NonNull
    @JsonProperty("id")
    private String id;

    @JsonProperty("value")
    @ColumnInfo(name = "value")
    private String value;

    @JsonProperty("feed_id")
    @ColumnInfo(name = "feed_id")
    private long feedId;

    @JsonProperty("feed_key")
    @ColumnInfo(name = "feed_key")
    private String feed_key;

    @JsonProperty("created_at")
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @JsonProperty("created_epoch")
    @ColumnInfo(name = "created_epoch")
    private long createdEpoch;

    @JsonProperty("expiration")
    @ColumnInfo(name = "expiration")
    private String expiration;

    public Feed() {}

    @Ignore
    public Feed(String id, String value, long feedId, String feed_key, Date createdAt, long createdEpoch, String expiration) {
        this.id = id;
        this.value = value;
        this.feedId = feedId;
        this.feed_key = feed_key;
        this.createdAt = createdAt;
        this.createdEpoch = createdEpoch;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getFeed_key() {
        return feed_key;
    }

    public void setFeed_key(String feed_key) {
        this.feed_key = feed_key;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedEpoch() {
        return createdEpoch;
    }

    public void setCreatedEpoch(long createdEpoch) {
        this.createdEpoch = createdEpoch;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", feedId=" + feedId +
                ", feed_key='" + feed_key + '\'' +
                '}';
    }
}
