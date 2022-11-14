package com.example.team_project.service.impl;

import com.example.team_project.dao.IconDao;
import com.example.team_project.dao.impl.IconDaoImpl;

import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.pojo.Icon;
import com.example.team_project.service.IconService;

import java.util.List;

@Bean("iconService")
public class IconServiceImpl implements IconService {
    private final IconDao ICON_DAO = new IconDaoImpl();

    @Override
    public Icon getUserIconByMark(int iconMark) {
        Icon icon = new Icon();
        icon.setIconMark(iconMark);
        return ICON_DAO.selectOne(icon);
    }

    @Override
    public List<Icon> allIcons(int currentPage, int count) {
        return ICON_DAO.getAll(currentPage - 1, count);
    }

    @Override
    public long getCount() {
        return ICON_DAO.getCount();
    }

    @Override
    public boolean setIcon(long userId, int iconMark) {
        boolean flag = false;
        //成功才提交，异常要进行回退
        try {
            int result = ICON_DAO.setIcon(userId, iconMark);
            if (result != 0) {
                flag = true;
            }
        } catch (Exception e) {
            ICON_DAO.rollback();
            e.printStackTrace();
        } finally {
            ICON_DAO.commit();
        }
        return flag;
    }

}
