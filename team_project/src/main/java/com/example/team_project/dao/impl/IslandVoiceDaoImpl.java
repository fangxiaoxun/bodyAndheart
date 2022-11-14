package com.example.team_project.dao.impl;

import com.example.team_project.dao.IslandVoiceDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.IslandVoice;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Bean("islandVoiceDao")
public class IslandVoiceDaoImpl extends AbsBaseMapper implements IslandVoiceDao {
    @Override
    public int upload(String url, long island) throws SQLException {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        IslandVoice voice = new IslandVoice(uuid, url, island);
        return insert(voice);
    }

    @Override
    public List<IslandVoice> gerIslandVoice(long belong) {
        IslandVoice voice = new IslandVoice();
        voice.setBelong(belong);
        return select(voice);
    }

    @Override
    public String getTableName() {
        return "island_voice";
    }
}
