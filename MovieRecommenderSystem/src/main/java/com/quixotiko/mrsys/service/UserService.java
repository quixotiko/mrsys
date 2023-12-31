package com.quixotiko.mrsys.service;

import com.quixotiko.mrsys.domain.User;

public interface UserService {
    User loginService(String username, String password);
    User registerService(User user);
}
