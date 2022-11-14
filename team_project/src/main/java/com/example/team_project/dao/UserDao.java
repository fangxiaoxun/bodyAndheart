package com.example.team_project.dao;


import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.User;

import java.sql.SQLException;

public interface UserDao extends BaseMapper {
    /**
     * 登录功能
     * @param id 登录的账户
     * @param password 登录的密码
     * @return 若登录失败，返回null，成功则返回用户数据
     */
    User login(long id, String password, boolean byCookie);

    /**
     * 注册账户
     * @param password 密码
     * @param iconMark 头像
     * @return 注册成功后的账号,失败后为null
     */
    int signIn(String password, int iconMark) throws SQLException;

    /**
     * 获取注册后，得到的id，供用户登录使用
     * @param signInResult 注册后的结果（0是注册失败，1是注册成功）
     * @return 用户的id
     */
    Long getIdAfterSignIn(int signInResult);

    /**
     * 更改密码
     * @param id 更改密码的用户
     * @param password 更改后的密码
     * @return 更改的结果（成功或失败）
     */
    boolean changePassword(long id, String password) throws SQLException;

}
