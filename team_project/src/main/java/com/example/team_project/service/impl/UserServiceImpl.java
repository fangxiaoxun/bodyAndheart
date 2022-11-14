package com.example.team_project.service.impl;

import com.example.team_project.dao.UserDao;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.pojo.User;
import com.example.team_project.service.UserService;

@Bean("userService")
public class UserServiceImpl implements UserService {
    @AutoWire
    private UserDao userDao;

    @Override
    public User login(long id, String password) {
        return userDao.login(id,password,false);
    }

    @Override
    public User loginByCookie(long id, String password) {
        return userDao.login(id,password,true);
    }

    @Override
    public Long signIn(String password, int iconMark) {
        Long id = null;
        try {
            int result = userDao.signIn(password, iconMark);
            if (result != 0) {
                userDao.commit();
                id = userDao.getIdAfterSignIn(result);
            }
        }catch (Exception e){
            userDao.rollback();
        }
        return id;
    }

    @Override
    public boolean changePassword(long id, String password) {
        boolean result = false;
        try {
            result = userDao.changePassword(id, password);
        } catch (Exception e) {
            userDao.rollback();
            e.printStackTrace();
        }finally {
            userDao.commit();
        }
        return result;
    }

    @Override
    public User loginBySH1Password(long uid, String password) {
        if (uid > 0 && password != null) {
            User user = new User();
            user.setId(uid);
            user.setPassword(password);

            return userDao.selectOne(user);
        }
        return null;
    }

    @Override
    public User getUserInfo(long uid) {
        if (uid > 0) {
            User user = new User();
            user.setId(uid);
            return userDao.selectOne(user);
        }
        return null;
    }


}
