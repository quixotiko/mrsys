package com.quixotiko.mrsys.domain;
import jakarta.persistence.*;

import java.io.Serializable;



@Table(name = "ratings")
@Entity
public class Rating {
    @EmbeddedId
    private RatingId id;

    private int rating;
    private long timestamp;

    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
