package com.hunt.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行sql语句  没有用到
 * @author williambai
 *
 */
public class ExecuteSqlUtil {

	 private static Logger log = LoggerFactory.getLogger(ExecuteSqlUtil.class);

	 /**
	     * 执行xx库下的表备份脚本
	     *
	     * @param tableName
	     */
	    public static void runSqlInStat(String tableName,SqlSession sqlSession) {
	    	 
//	    	 Configuration configuration = sqlSession.getConfiguration();
//	        String className = Configuration.INSTANCE.get("jdbc.xx.driver");
//	        String dbUrl = Configurations.INSTANCE.get("jdbc.xx.url");
//	        String dbUsername = Configurations.INSTANCE.get("jdbc.xx.username");
//	        String dbPassword = Configurations.INSTANCE.get("jdbc.xx.password");
//
//	        try {
//	            Class.forName(className);
//	            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//	            ScriptRunner runner = new ScriptRunner(conn);
//	            runner.setAutoCommit(true);
//
//	            String fileName = String.format("src/main/resources/db/%s.sql", tableName);
//	            File file = new File(fileName);
//
//	            try {
//	                if (file.getName().endsWith(".sql")) {
//	                    runner.setFullLineDelimiter(false);
//	                    runner.setDelimiter(";");//语句结束符号设置
//	                    runner.setLogWriter(null);//日志数据输出，这样就不会输出过程
//	                    runner.setSendFullScript(false);
//	                    runner.setAutoCommit(true);
//	                    runner.setStopOnError(true);
//	                    runner.runScript(new InputStreamReader(new FileInputStream(fileName), "utf8"));
//	                    log.info(String.format("【%s】回滚成功", tableName));
//	                }
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//
//	            conn.close();
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        } catch (ClassNotFoundException e) {
//
//	            e.printStackTrace();
//	        }

	    }
	    /**
	     *
	     * 功能描述:
	     * 对符合 mybatis.dtd形式的sql进行动态sql解析并执行，返回Map结构的数据集
	     * @param: executeSql 要执行的sql ， sqlSession 数据库会话，namespace 命名空间，mapper_id mapper的ID，bounds 分页参数
	     * @return:
	     * @auther:
	     * @date: 2018/11/9 16:17
	     */
	    public static List<?> executeDataBaseSql(String executeSql, SqlSession sqlSession, String namespace, String mapper_id, RowBounds bounds,
	                                               Class<?> clazz,Object param){
	        // 1. 对executeSql 加上script标签
	        StringBuffer sb = new StringBuffer();
	        sb.append("<script>");
	        sb.append(executeSql);
	        sb.append("</script>");
	        log.info(sb.toString());
	        Configuration configuration = sqlSession.getConfiguration();
	        
	        LanguageDriver languageDriver = configuration.getDefaultScriptingLanuageInstance();//getDefaultScriptingLanguageInstance();  // 2. languageDriver 是帮助我们实现dynamicSQL的关键
	        SqlSource sqlSource = languageDriver.createSqlSource(configuration,sb.toString(),clazz);  //  泛型化入参
	        newSelectMappedStatement(configuration,namespace+"."+mapper_id,sqlSource,clazz);
	        List<?> list = sqlSession.selectList(namespace+"."+mapper_id,param,bounds);
	        return list;
	    }

	    //
	    private  static MappedStatement newSelectMappedStatement(Configuration configuration,String msId, SqlSource sqlSource, final Class<?> resultType) {
	        // 加强逻辑 ： 一定要防止 MappedStatement 重复问题
	        MappedStatement msTest = null;
	        try{
	            synchronized (configuration) {   // 防止并发插入多次
	                msTest = configuration.getMappedStatement(msId);
	                if (msTest != null) {
	                    configuration.getMappedStatementNames().remove(msTest.getId());
	                }

	            }
	        }catch (IllegalArgumentException e){
	            log.info("没有此mappedStatment,可以注入此mappedStatement到configuration当中");
	        }
	        // 构建一个 select 类型的ms ，通过制定SqlCommandType.SELECT
	        MappedStatement ms = new MappedStatement.Builder(   // 注意！！-》 这里可以指定你想要的任何配置，比如cache,CALLABLE,
	                configuration, msId, sqlSource, SqlCommandType.SELECT)   // -》 注意，这里是SELECT,其他的UPDATE\INSERT 还需要指定成别的
	                .resultMaps(new ArrayList<ResultMap>() {
	                    {
	                        add(new ResultMap.Builder(configuration,
	                                "defaultResultMap",
	                                resultType,
	                                new ArrayList<ResultMapping>(0)).build());
	                    }
	                })
	                .build();
	        synchronized (configuration){
	            configuration.addMappedStatement(ms); // 加入到此中去
	        }
	        return ms;
	    }
	
	
}
