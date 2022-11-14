package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Hobby;

import java.sql.SQLException;
import java.util.List;
/**
 * hobby的数据层
 */
public interface HobbyDao extends BaseMapper {
    /**
     * 获取所有的爱好标签
     * @return 爱好标签集合
     */
    List<Hobby> all();

    /**
     * 同all,但是是分页
     */
    List<Hobby> all(int current, int count);

    /**
     * 添加标签
     * @param hobby 要添加的标签
     * @return 添加成功为1，失败为0
     */
    int add(Hobby hobby) throws SQLException;

    /**
     * 删除标签
     * @param hobby 要删除的标签id
     * @return 删除成功为1，失败为0
     */
    int delete(int hobby) throws SQLException;
}
