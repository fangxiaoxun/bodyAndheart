package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.SportQuestion;

import java.sql.SQLException;
import java.util.List;

public interface SportQuestionDao extends BaseMapper {

    List<SportQuestion> getQuestionsRandom(int size);

    int releaseQuestion(SportQuestion question) throws SQLException;

    int delQuestion(String uuid) throws SQLException;

}
