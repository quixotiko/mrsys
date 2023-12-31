package com.quixotiko.mrsys.repository;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.domain.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingDao extends JpaRepository<Rating, RatingId> {

    @Query(value="select * from ratings order by movieid desc limit 5", nativeQuery = true)
    List<Rating> getRatingsService();
}
