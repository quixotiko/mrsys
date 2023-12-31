package com.quixotiko.mrsys.service.serviceImpl;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.repository.UserRatingDao;
import com.quixotiko.mrsys.service.UserRatingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserRatingServiceImpl implements UserRatingService {
    @Resource
    private UserRatingDao userRatingDao;
//    @Override
//    public void saveUserRatings(Rating rating) {
//        userRatingDao.saveUserRatings(rating);
//    }
}
