package com.quixotiko.mrsys.service.serviceImpl;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.repository.RatingDao;
import com.quixotiko.mrsys.service.RatingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    @Resource
    private RatingDao ratingDao;

    @Override
    public List<Rating> getRatingsService() {
        return ratingDao.getRatingsService();
    }
}
