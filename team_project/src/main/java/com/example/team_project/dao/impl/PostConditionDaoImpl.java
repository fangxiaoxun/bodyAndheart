package com.example.team_project.dao.impl;

import com.example.team_project.dao.PostConditionDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.PostCondition;

import java.util.List;

public class PostConditionDaoImpl extends AbsBaseMapper implements PostConditionDao {


    @Override
    public List<PostCondition> all() {
        return select(new PostCondition());
    }

    @Override
    public PostCondition getPostCondition(int condition) {
        PostCondition selectCondition = new PostCondition();
        selectCondition.setCondition(condition);
        //获取单个情绪状态
        return selectOne(selectCondition);
    }

    @Override
    public long maxId() {
        return count(new PostCondition());
    }

    @Override
    public String getTableName() {
        return "post_condition";
    }
}
