package com.example.demo.mybatiesIntecet;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Test2 {
    public static final Logger log = LogManager.getLogger();
    public static void main(String[] args) {
        try {
            String resource = "mybaties.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = sqlSessionFactory.openSession();
            try {
                TestMapper mapper=session.getMapper(TestMapper.class);
                List<Test> tests=mapper.test();
                session.commit();
                log.info(JSON.toJSONString(tests));

            } finally {
                session.close();
            }

        } catch (
                IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
