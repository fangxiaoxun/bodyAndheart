package com.example.team_project.service.impl;

import com.example.team_project.service.StimulateService;

import java.util.*;

public class StimulateServiceImpl implements StimulateService {
    private static final String[] STIMULATES = {"aaaaa","bbbbb"};

    @Override
    public List<String> randomStimulate(int size) {
        if (STIMULATES.length < size) {
            return Arrays.asList(STIMULATES);
        }
        Set<Integer> indexes = new HashSet<>((int)(size / 0.75) + 1);
        return null;
    }
}
