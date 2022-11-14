package com.example.team_project.service.impl;

import com.example.team_project.dao.IslandVoiceDao;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.pojo.IslandVoice;
import com.example.team_project.service.IslandVoiceService;

import java.sql.SQLException;
import java.util.*;

@Bean("islandVoiceService")
public class IslandVoiceServiceImpl implements IslandVoiceService {
    @AutoWire
    private IslandVoiceDao voiceDao;

    @Override
    public int upload(String url, long belong) {
        int result = 0;
        try {
            result = voiceDao.upload(url, belong);
        } catch (SQLException e) {
            voiceDao.rollback();
            e.printStackTrace();
        } finally {
            voiceDao.commit();
        }
        return result;
    }

    @Override
    public List<IslandVoice> getVoiceRandom(long belong, int size) {
        if (belong > 0 && size > 0) {
            //获取所有的语音信息
            List<IslandVoice> voices = voiceDao.gerIslandVoice(belong);
            List<IslandVoice> result;
            int length = voices.size();
            if (length > size) {
                Set<Integer> indexes = new HashSet<>(size);
                //获取size个不重复数
                while (indexes.size() < size) {
                    int index = (int) (Math.random() * length);
                    indexes.add(index);
                }
                //存放结果值
                result = new ArrayList<>(size);
                for (int index : indexes) {
                    result.add(voices.get(index));
                }
            }else {
                //若length <= size则返回全部的语音信息
                result = voices;
            }
            return result;
        }else if (belong > 0 && size == 0) {
            return new LinkedList<>();
        }
        return null;
    }
}
