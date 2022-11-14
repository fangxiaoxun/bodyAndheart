package com.example.team_project.service;

import com.example.team_project.pojo.Icon;

import java.util.List;

public interface IconService {
    /**
     * 根据iconMark获取用户头像
     * @param iconMark
     * @return 用户头像信息
     */
    Icon getUserIconByMark(int iconMark);

    /**
     * 分页获取所有的用户icon
     * @param currentPage 当前页码
     * @param count 一次展示的页数
     * @return 头像信息的集合，非null
     */
    List<Icon> allIcons(int currentPage, int count);

    /**
     * 获得用户头像的最大个数
     * @return 个数
     */
    long getCount();

    /**
     * 用户设置头像
     * @param userId 设置头像的用户
     * @param iconMark 头像的id
     * @return 设置是否成功
     */
    boolean setIcon(long userId, int iconMark);


}
