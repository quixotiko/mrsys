package com.quixotiko.mrsys.service.serviceImpl;

import com.quixotiko.mrsys.domain.User;
import com.quixotiko.mrsys.repository.UserDao;
import com.quixotiko.mrsys.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServicelmpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User loginService(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, password);
        if (user != null) {
            user.setPassword("");;
        }
        return user;
    }

    @Override
    public User registerService(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            return null;
        }else {
            User newUser = userDao.save(user);
            if (newUser != null) {
                newUser.setPassword("");
            }
            return newUser;
        }
    }
}
