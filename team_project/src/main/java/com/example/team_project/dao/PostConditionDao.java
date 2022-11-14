package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.PostCondition;

import java.util.List;
import java.util.Map;

public interface PostConditionDao extends BaseMapper {
    List<PostCondition> all();

    PostCondition getPostCondition(int condition);

    /**
     * 目前由于情绪状态的数量与id最大值相同，故使用了数量来代替最大值
     * @return 最大值
     */
    long maxId();
}
