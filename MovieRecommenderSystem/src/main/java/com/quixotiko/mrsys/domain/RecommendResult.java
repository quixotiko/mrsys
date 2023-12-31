package com.quixotiko.mrsys.domain;
import jakarta.persistence.*;

import java.io.Serializable;



@Table(name = "recommendresult")
@Entity
public class RecommendResult {
    @Id
    private RatingId id;
    private float rating;
    private long timestamp;

    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
