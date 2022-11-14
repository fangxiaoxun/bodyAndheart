package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Interested;

import java.sql.SQLException;
import java.util.List;

public interface InterestedDao extends BaseMapper {
    /**
     * 获取用户所有拥有的兴趣标签
     * @param userId 要获取的用户的id
     * @return 所拥有的兴趣标签集合
     */
    List<Interested> userHas(long userId);

    /**
     * 同userHas,但分页
     */
    List<Interested> userHas(long userId, int current, int count);

    /**
     * 用户填加兴趣爱好
     * @param userId 要添加兴趣的用户
     * @param hobbies 要添加的兴趣
     * @return 添加成功或失败,失败为0
     */
    int addInterests(long userId, int[] hobbies) throws SQLException;
    /**
     * 用户删除兴趣爱好
     * @param userId 要删除兴趣的用户
     * @param deletes 要删除的兴趣
     * @return 添加成功或失败，失败为0
     */
    int delInterests(long userId, int[] deletes) throws SQLException;
}
