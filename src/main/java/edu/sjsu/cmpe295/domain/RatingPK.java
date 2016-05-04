package edu.sjsu.cmpe295.domain;

import java.io.Serializable;

/**
 * Created by BladeInShine on 16/5/4.
 */
public class RatingPK implements Serializable {

    private long userId;
    private long itemId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingPK ratingPK = (RatingPK) o;

        if (userId != ratingPK.userId) return false;
        return itemId == ratingPK.itemId;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (itemId ^ (itemId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "RatingPK{" +
            "userId=" + userId +
            ", itemId=" + itemId +
            '}';
    }
}
