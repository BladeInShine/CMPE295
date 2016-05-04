package edu.sjsu.cmpe295.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by BladeInShine on 16/5/4.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rating implements Serializable{

    @Column(name = "user_id")
    private long userId;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "rating")
    private float rating;

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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating1 = (Rating) o;

        if (userId != rating1.userId) return false;
        if (itemId != rating1.itemId) return false;
        return Float.compare(rating1.rating, rating) == 0;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (itemId ^ (itemId >>> 32));
        result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rating{" +
            "userId=" + userId +
            ", itemId=" + itemId +
            ", rating=" + rating +
            '}';
    }
}
