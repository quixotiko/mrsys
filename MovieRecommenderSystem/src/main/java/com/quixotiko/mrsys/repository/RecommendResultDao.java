package com.quixotiko.mrsys.repository;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.domain.RatingId;
import com.quixotiko.mrsys.domain.RecommendResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendResultDao extends JpaRepository<RecommendResult, Integer> {

    List<RecommendResult> findByIdUserid(int userid);
}
