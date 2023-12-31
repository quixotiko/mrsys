package com.quixotiko.mrsys.service.serviceImpl;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.domain.RecommendResult;
import com.quixotiko.mrsys.repository.RecommendResultDao;
import com.quixotiko.mrsys.repository.UserRatingDao;
import com.quixotiko.mrsys.service.RecommendResultService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendResultServiceImpl implements RecommendResultService {
    @Resource
    private RecommendResultDao recommendResultDao;
    @Override
    public List<RecommendResult> getResult(int userid) {
        return recommendResultDao.findByIdUserid(userid);
    }
}
