package com.github.hcsp;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class MockDataGenerator {
    private static void mockData(SqlSessionFactory sqlSessionFactory, int howMany) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            List<News> currentNews = sqlSession.selectList("com.github.hcsp.MockMapper.selectNews");

            int count = howMany - currentNews.size();
            Random random = new Random();
            try {
                while (count-- > 0) {
                    int index = random.nextInt(currentNews.size());
                    News newsToBeInserted = new News(currentNews.get(index));

                    Instant currentTime = newsToBeInserted.getCreatedAt();
                    currentTime = currentTime.minusSeconds(random.nextInt(3600 * 24 * 365));
                    newsToBeInserted.setModifiedAt(currentTime);
                    newsToBeInserted.setCreatedAt(currentTime);

                    sqlSession.insert("com.github.hcsp.MockMapper.insertNews", newsToBeInserted);
                    System.out.println("Left:" + count);

                    if (count % 2000 == 0) {
                        sqlSession.flushStatements();
                    }
                }
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                throw new RuntimeException();
            }
        }
    }

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory;

        try {
            String resource = "db/mybatis/config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mockData(sqlSessionFactory, 100_0000);

    }
}

