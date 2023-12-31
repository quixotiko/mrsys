package com.quixotiko.mrsys.service;

import com.quixotiko.mrsys.domain.RecommendResult;

import java.util.List;

public interface RecommendResultService {
    List<RecommendResult> getResult(int userid);
}
