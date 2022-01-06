package com.example.demo.mybatiesIntecet;
 
import java.util.Properties;
 
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 //mysql的拦截器
@Intercepts({ @org.apache.ibatis.plugin.Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class SqlInterceptor implements Interceptor {

	public static final Logger log = LogManager.getLogger();
 
	public Object intercept(Invocation invocation) throws Throwable {
		// TODO Auto-generated method stub
 
		log.info("Interceptor......");
 
		// 获取原始sql语句
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		//方法的全路径
		mappedStatement.getId();
		//
		Object[] args = invocation.getArgs();
		Object arg = args[1];


		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		String oldsql = boundSql.getSql();
		log.info("old:"+oldsql);
 
		// 改变sql语句
		BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), oldsql + " where id=1",
				boundSql.getParameterMappings(), boundSql.getParameterObject());
		MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
		invocation.getArgs()[0] = newMs;
 
		// 继续执行
		Object result = invocation.proceed();
		return result;
	}
 
	public Object plugin(Object target) {
		// TODO Auto-generated method stub
		return Plugin.wrap(target, this);
	}
 
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
 
	}
 
	// 复制原始MappedStatement
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null) {
			for (String keyProperty : ms.getKeyProperties()) {
				builder.keyProperty(keyProperty);
			}
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		builder.useCache(ms.isUseCache());
		return builder.build();
	}
 
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;
 
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
 
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
 
}