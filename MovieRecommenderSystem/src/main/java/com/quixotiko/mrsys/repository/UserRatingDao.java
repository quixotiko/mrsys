package com.quixotiko.mrsys.repository;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.domain.RatingId;
import com.quixotiko.mrsys.domain.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRatingDao extends JpaRepository<UserRating, RatingId> {

//    void saveUserRatings(Rating rating);

}
