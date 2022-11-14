package com.example.team_project.framkwork.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.example.team_project.framkwork.core.annotation.ComponentScan;
import com.example.team_project.framkwork.core.annotation.Configuration;
import com.example.team_project.framkwork.core.annotation.MethodReturn;
import com.example.team_project.framkwork.jdbc.JDBCUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(packetPath = "com/example/team_project")
public class Config {

    @MethodReturn(value = "dataSource",methodOrder = 1)
    public DataSource dataSource(){
        Properties pro = new Properties();
        try {
            pro.load(this.getClass().getClassLoader().getResourceAsStream("conf/druid.properties"));
            return DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @MethodReturn(value = "JdbcUtils",methodOrder = 2)
    public JDBCUtils jdbcUtils(DataSource source) {
        return new JDBCUtils(source);
    }

    @MethodReturn(value = "mapper",methodOrder = 3)
    public ObjectMapper getMapper() {
        return new ObjectMapper();
    }

}
