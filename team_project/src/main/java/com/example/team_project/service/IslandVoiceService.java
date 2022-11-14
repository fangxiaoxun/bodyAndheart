package com.example.team_project.service;

import com.example.team_project.pojo.IslandVoice;

import java.util.List;

public interface IslandVoiceService {
    /**
     * 上传音频
     * @param url 音频保存的路径
     * @param belong 音频属于哪个岛
     * @return 上传成功的条数
     */
    int upload(String url, long belong);

    /**
     * 随机获取岛内的音频
     * @param belong 要获取哪个岛的音频
     * @param size 要获取的数量
     * @return 音频信息的集合
     */
    List<IslandVoice> getVoiceRandom(long belong, int size);
}
