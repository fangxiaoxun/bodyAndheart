package com.example.team_project.service;

import com.example.team_project.pojo.Hobby;
import com.example.team_project.pojo.Interested;

import java.util.List;

public interface HobbyService {
    /**
     * 获取所有的hobby标签
     * @return 所有标签的集合
     */
    List<Hobby> all();

    /**
     * 同all
     * @param currentPage 当前页面（从1开始）
     * @param count 一页展示的数量
     * @return 某页标签的集合
     */
    List<Hobby> all(int currentPage, int count);

    /**
     *  用户添加hobby标签
     * @param userId 添加hobby的用户
     * @param adds 要添加的hobbyId
     * @return 是否添加成功
     */
    boolean addHobby(long userId, int[] adds);
    /**
     *  用户删除hobby标签
     * @param userId 删除hobby的用户
     * @param deletes 要删除的hobbyId
     * @return 是否删除成功
     */
    boolean deleteHobby(long userId, int[] deletes);

    /**
     * 用户拥有的所有hobby
     * @param userId
     * @return hobby的集合，不为null
     */
    List<Interested> userHas(long userId);

    List<Interested> userHas(long userId,int currentPage, int count);
}
