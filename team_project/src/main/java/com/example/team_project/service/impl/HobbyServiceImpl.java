package com.example.team_project.service.impl;

import com.example.team_project.dao.HobbyDao;
import com.example.team_project.dao.InterestedDao;
import com.example.team_project.dao.impl.HobbyDaoImpl;
import com.example.team_project.dao.impl.InterestedDaoImpl;
import com.example.team_project.pojo.Hobby;
import com.example.team_project.pojo.Interested;
import com.example.team_project.service.HobbyService;

import java.util.List;

public class HobbyServiceImpl implements HobbyService {
    private final HobbyDao HOBBY_DAO = new HobbyDaoImpl();
    private final InterestedDao INTERESTED_DAO = new InterestedDaoImpl();
    @Override
    public List<Hobby> all() {
        return HOBBY_DAO.all();
    }

    @Override
    public List<Hobby> all(int currentPage, int count) {
        return HOBBY_DAO.all(currentPage - 1, count);
    }

    @Override
    public boolean addHobby(long userId, int[] adds) {
        boolean result = false;
        try {
            int i = INTERESTED_DAO.addInterests(userId, adds);
            if (i != 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //若都成功则提交，都失败则回退
            if (result){
                INTERESTED_DAO.commit();
            }else {
                INTERESTED_DAO.rollback();
            }
        }
        return result;
    }

    @Override
    public boolean deleteHobby(long userId, int[] deletes) {
        boolean result = false;
        try {
            int i = INTERESTED_DAO.delInterests(userId, deletes);
            if (i != 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //若都成功则提交，都失败则回退
            if (result){
                INTERESTED_DAO.commit();
            }else {
                INTERESTED_DAO.rollback();
            }
        }
        return result;
    }

    @Override
    public List<Interested> userHas(long userId) {
        return INTERESTED_DAO.userHas(userId);
    }

    @Override
    public List<Interested> userHas(long userId, int currentPage, int count) {
        return INTERESTED_DAO.userHas(userId,currentPage - 1, count);
    }
}
