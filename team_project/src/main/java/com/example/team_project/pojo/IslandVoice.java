package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

public class IslandVoice {
    private String uuid;
    @Param("voice_url")
    private String voiceUrl;
    private Long belong;

    public IslandVoice() {
    }

    public IslandVoice(String uuid, String voiceUrl, Long belong) {
        this.uuid = uuid;
        this.voiceUrl = voiceUrl;
        this.belong = belong;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public Long getBelong() {
        return belong;
    }

    public void setBelong(Long belong) {
        this.belong = belong;
    }

    @Override
    public String toString() {
        return "IslandVoice{" +
                "uuid='" + uuid + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                ", belong=" + belong +
                '}';
    }
}
