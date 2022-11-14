package com.example.team_project.service;


import com.example.team_project.pojo.User;

/**
 * User业务功能
 */
public interface UserService {
    /**
     * 登录服务
     * @param id 用户名
     * @param password 密码
     * @return 登录成功返回User对象，失败则为null
     */
    User login(long id, String password);

    User loginByCookie(long id, String password);

    /**
     * 注册账户
     * @param password 密码
     * @param iconMark 头像
     * @return 注册成功后的账号,失败后为null
     */
    Long signIn(String password, int iconMark);

    /**
     * 更改密码
     * @param id 更改的用户id
     * @param password 更改后的密码
     * @return 是否更改成功
     */
    boolean changePassword(long id, String password);

    /**
     * 登录(获取用户数据)，通过SH1加密后的数据进行登录
     * @param uid 用户的id
     * @param password 用户的密码
     * @return 返回用户数据
     */
    User loginBySH1Password(long uid, String password);

    /**
     * 用uid获取某个用户的信息
     * @return 用户的信息
     */
    User getUserInfo(long uid);
}
