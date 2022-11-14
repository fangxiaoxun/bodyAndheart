package com.example.team_project.framkwork.jdbc;


import com.alibaba.druid.pool.DruidDataSource;
import com.example.team_project.framkwork.jdbc.annotation.Param;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 提供方法,简化JDBC操作
 *
 * @author xiaoming
 */
public class JDBCUtils {
    private final DataSource source;
    private ConcurrentLinkedQueue<Connection> connQueue = new ConcurrentLinkedQueue<>();
    private static final Logger LOGGER = Logger.getLogger(JDBCUtils.class);
    public JDBCUtils(DataSource source) {
        this.source = source;
    }


    /**
     * 查询,并封装成map集合,必须是查询内容只有一条的情况下,如果不是只有一条,则只会封装第一条
     *
     * @param args sql中待填充的参数
     * @return map
     */
    public Map<String, Object> queryForMap(String sql, Object... args) {
        //在外边定义,便于释放资源
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet query = null;
        Map<String, Object> map = new HashMap<>();
        try {
            connection = source.getConnection();
            statement = getStatement(connection, sql, args);
            query = statement.executeQuery();
            //封装内容到map集合
            if (query.next()) {
                ResultSetMetaData rd = query.getMetaData();
                for (int i = 0; i < rd.getColumnCount(); i++) {
                    String columnName = rd.getColumnName((i + 1));
                    map.put(columnName, query.getObject((i + 1)));
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            //关闭资源
            closeResource(query, statement, connection);
        }

        return map;
    }

    /**
     * 查询对象,并封装成target（目标类）对象,把所有这些对象添加到ArrayList,只适用于单表查询
     * @param sql
     * @param target
     * @param args
     * @return List 存放着目标类
     */
    public <T> List<T> query(String sql, Class<T> target, Object... args) {
        List<T> queryList = new ArrayList<>(16);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = source.getConnection();
            statement = getStatement(conn, sql, args);
            resultSet = statement.executeQuery();
            //获取构造器来创建对象使用
            Constructor<?> constructor = target.getConstructor();
            //获取公开的方法,用于获取set方法
            Method[] methods = target.getMethods();
            //获取成员变量
            Field[] fields = target.getDeclaredFields();
            boolean flag = false;
            //要使用到的变量和方法
            List<Method> usedMethod = new ArrayList<>();
            List<Field> usedField = new ArrayList<>();
            //把set方法添加到集合中,便于使用,且刚好可以让成员变量和成员方法的索引一致
            for (int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                if (name.contains("set")) {
                    String sub = name.substring(3);
                    for (Field field : fields) {
                        if (sub.equalsIgnoreCase(field.getName())) {
                            usedMethod.add(methods[i]);
                            usedField.add(field);
                            break;
                        }
                    }
                }
            }
            while (resultSet.next()) {
                T obj = (T) constructor.newInstance();
                for (int i = 0; i < usedMethod.size(); i++) {
                    Field field = usedField.get(i);
                    Object object = null;
                    if (field.isAnnotationPresent(Param.class)) {
                        Param annotation = field.getAnnotation(Param.class);
                        if (!annotation.ignore()) {
                            object = resultSet.getObject(annotation.value());
                        }
                    } else {
                        object = resultSet.getObject((field.getName()));
                    }
                    if (object != null) {
                        usedMethod.get(i).invoke(obj, object);
                    }
                }
                queryList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeResource(resultSet, statement, conn);
        }
        return queryList;
    }

    /**
     * @param sql
     * @param args
     * @return 返回ture或false的情况与PreparedStatement实例的execute方法一样
     */
    public boolean execute(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement statement = null;
        boolean execute = false;
        try {
            conn = startTranslation();
            statement = getStatement(conn, sql, args);
            execute = statement.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }finally {
            try {
                closeResource(statement, conn);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return execute;
    }

    /**
     * @param sql
     * @param args
     * @return 一个Map可以看作是一条记录, 这个方法用来解决queryForMap方法只能获取第一条的弊端, 并且也能解决query方法只能单表的问题...
     * 就是稍微比较麻烦
     */
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        List<Map<String, Object>> query = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = source.getConnection();
            statement = getStatement(connection, sql, args);
            resultSet = statement.executeQuery();
            //与上面的queryForList相似
            ResultSetMetaData metaData = null;
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 0; i < count; i++) {
                    String columnName = metaData.getColumnName((i + 1));
                    map.put(columnName, resultSet.getObject((i + 1)));
                }
                query.add(map);
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeResource(resultSet, statement, connection);
        }
        return query;
    }

    public int update(String sql, Object... args) throws SQLException {
        int affect = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            DruidDataSource dataSource = (DruidDataSource) source;
            connection = startTranslation();
            int activeCount = dataSource.getActiveCount();
            LOGGER.debug("druid线程活动数量:" + activeCount);
            if (activeCount > 7) {
                LOGGER.warn("jdbc连接数量超过7个，有未释放连接的风险,若活动数量没有下降,则必须排查释放的问题!");
            }
            statement = getStatement(connection, sql, args);
            affect = statement.executeUpdate();
        }finally {
            closeResource(statement, connection);
        }

        return affect;
    }

    public int delete(String sql, Object... args) throws SQLException {
        return update(sql, args);
    }

    public int insert(String sql, Object... args) throws SQLException {
        return update(sql, args);
    }


    /**
     * 当需要关闭ResultSet,Statement与Connection资源，调用该方法
     *
     * @param statement
     * @param connection
     * @param resultSet
     */
    protected void closeResource(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    protected void closeResource(Statement statement, Connection connection) throws SQLException {
        if (connection.getAutoCommit()) {
            closeResource(null, statement, connection);
        } else {
            closeResource(null, statement, null);
            connQueue.add(connection);
        }
    }

    /**
     * 设置statement参数
     *
     * @param conn
     * @param sql
     * @param args
     * @throws SQLException
     */
    private PreparedStatement getStatement(Connection conn, String sql, Object[] args) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject((i + 1), args[i]);
            }
        }
        return statement;
    }

    private Connection startTranslation() throws SQLException {
        Connection connection = source.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    public void commit() throws SQLException {
        while (!connQueue.isEmpty()) {
            Connection poll = connQueue.poll();
            poll.commit();
            LOGGER.debug("释放："+poll);
            poll.close();
        }
    }

    public void rollback() throws SQLException {
        while (!connQueue.isEmpty()) {
            Connection poll = connQueue.poll();
            poll.rollback();
            LOGGER.debug("释放："+poll);
            poll.close();
        }
    }

}
