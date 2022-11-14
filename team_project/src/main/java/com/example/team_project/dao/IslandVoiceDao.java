package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.IslandVoice;

import java.sql.SQLException;
import java.util.List;

public interface IslandVoiceDao extends BaseMapper {
    int upload(String url, long island) throws SQLException;

    List<IslandVoice> gerIslandVoice(long belong);


}
