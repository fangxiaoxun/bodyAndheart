package com.example.team_project.web.listener;

import com.example.team_project.dao.impl.PostLikeDaoImpl;
import com.example.team_project.dao.impl.SportAnswerLikeDaoImpl;
import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.utils.ServiceConfUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //加载对象容器
        LoadConfig.load(Config.class);
        String contextPath = sce.getServletContext().getContextPath();
        //把项目路径加入到config中
        ServiceConfUtils.addConfig(ServiceConfUtils.CONTEXT_PATH, contextPath);
        //加载mvc容器(目前没有controller)
//        LoadConfig.load(MvcConfig.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //当webapp被关闭时，需要把缓存中的数据进行持久化
        new PostLikeDaoImpl().forcePersistence();
        BeanFactory.getBean(SportAnswerLikeDaoImpl.class).cacheManage();
    }
}
