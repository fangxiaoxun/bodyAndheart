package com.example.team_project.framkwork.jdbc.repository;

import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.jdbc.JDBCUtils;
import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.example.team_project.framkwork.utils.MapUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * absBaseMapper类中的方法，大部分仅能用于包装类，剩下小部分也只能在参数填满后，才能使用
 */
public abstract class AbsBaseMapper implements BaseMapper{
    protected JDBCUtils jdbcUtils = new JDBCUtils(BeanFactory.getBean("dataSource", DataSource.class));

    public abstract String getTableName();

    /**
     * 自动生成查询条件,进行了重写
     * @param po 包括查询参数的类对象
     * @param <T> 使用泛型，使得方法更加通用
     * @return 查询条件组成的map集合
     */
    public final <T> Map<String,Object> getConditions(T po) {
        Class<?> clazz = po.getClass();
        //暴力获取全部成员变量
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> conditions = new HashMap<>();
        Class<Param> paramAnnotation = Param.class;
        try {
            for (Field field : fields) {
                //暴力获取成员变量值
                field.setAccessible(true);
                Object value = field.get(po);
                //首先该成员变量必须不为null，为null的话即表示没有该条件
                if (value != null) {
                    //如果该成员变量上有注解，则先查看该参数是否要被忽略，若不为忽略的参数，则把这个属性的名字，映射为@Param的值
                    if (field.isAnnotationPresent(paramAnnotation)) {
                        Param param = field.getAnnotation(paramAnnotation);
                        if (!param.ignore()) {
                            //如果不是忽略的属性，则把这个注解的值，以及该成员变量的值存入集合中
                            conditions.put(param.value(), value);
                        }
                    } else {
                        //如果没有加上注解，则把这个字段，按照成员变量名和成员变量值以键值对存入map集合
                        conditions.put(field.getName(), value);
                    }
                }
            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return conditions;
    }

    @Override
    public <T> T selectOne(T obj) {
        String base = "select * from {0} where 1 = 1";
        Map<String, Object> conditions = getConditions(obj);
        base = MessageFormat.format(base,getTableName());
        String sql = splice(base,conditions);
        Map<String, Object> params = jdbcUtils.queryForMap(sql, conditions.values().toArray());
        T object = (T) MapUtils.instanceByMap(obj.getClass(), params);
        return object;
    }

    @Override
    public <T> int update(T changedPo, T conditionPo) throws SQLException {
        //update 语句比较特殊，所以特别对它写了个方法，感觉有点繁琐，应该能够优化
        String base = "update {0} set ";
        base = MessageFormat.format(base,getTableName());
        StringBuilder sql = new StringBuilder(base);
        //存值（包括更改后的值和条件，按照?的顺序）
        ArrayList<Object> values = new ArrayList<>();
        //拼接更改的内容
        Map<String, Object> changed = getConditions(changedPo);
        for (Map.Entry<String, Object> entry : changed.entrySet()) {
            sql.append(entry.getKey()).append(" = ").append('?').append(',');
            values.add(entry.getValue());
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where 1 = 1");
        //拼接条件
        Map<String, Object> conditions = getConditions(conditionPo);
        values.addAll(conditions.values());

        String finalSql = splice(sql.toString(),conditions);
        return jdbcUtils.update(finalSql,values.toArray());
    }

    @Override
    public <T> List<T> select(T obj) {
        String base = "select *from {0} where 1 = 1";
        base = MessageFormat.format(base,getTableName());
        Map<String, Object> conditions = getConditions(obj);
        String sql = splice(base,conditions);
        return jdbcUtils.query(sql,(Class<T>) obj.getClass(),conditions.values().toArray());
    }

    @Override
    public <T> List<T> select(T obj, int currentPage, int count) {
        String base = "select *from {0} where 1 = 1";
        base = MessageFormat.format(base,getTableName());
        Map<String, Object> conditions = getConditions(obj);
        String sql = splice(base,conditions);
        sql = sql + " limit " + currentPage + "," + count;
        return jdbcUtils.query(sql,(Class<T>) obj.getClass(),conditions.values().toArray());
    }

    @Override
    public <T> int delete(T obj) throws SQLException {
        String base = "delete from {0} where 1 = 1";
        base = MessageFormat.format(base,getTableName());
        Map<String, Object> conditions = getConditions(obj);
        String sql = splice(base,conditions);
        return jdbcUtils.delete(sql,conditions.values().toArray());
    }

    @Override
    public <T> int insert(T obj) throws SQLException {
        //insert 语句拼接与其他有所不同，单独做sql拼接
        String base = "insert into {0}(";
        base = MessageFormat.format(base,getTableName());
        Map<String, Object> conditions = getConditions(obj);
        StringBuilder sql = new StringBuilder(base);
        Set<String> columns = conditions.keySet();
        for (String column : columns) {
            //加着重号避免字段名与mysql关键字冲突
            sql.append('`').append(column).append('`').append(',');
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") values(");
        for (String column : columns) {
            sql.append('?').append(',');
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(')');

        return jdbcUtils.insert(sql.toString(),conditions.values().toArray());
    }

    @Override
    public <T> long count(T obj) {
        long count = 0;
        String base = "select count(*) `count` from {0} where 1 = 1";
        base = MessageFormat.format(base,getTableName());
        Map<String, Object> conditions = getConditions(obj);
        String sql = splice(base, conditions);
        Map<String, Object> result = jdbcUtils.queryForMap(sql, conditions.values().toArray());
        count = (long) result.get("count");
        return count;
    }

    /**
     * 根据conditions的数量，来拼接sql（带?）
     * @param base
     * @param conditions
     * @return
     */
    private String splice(String base,Map<String,Object> conditions){
        StringBuffer sql = new StringBuffer(base);
        Set<String> columns = conditions.keySet();
        for (String column : columns) {
            //加着重号保证与mysql关键字不冲突
            sql.append(" and ").append('`').append(column).append('`').append(" = ").append("?");
        }
        return sql.toString();
    }

    public void commit() {
        try {
            jdbcUtils.commit();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void rollback() {
        try {
            jdbcUtils.rollback();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
