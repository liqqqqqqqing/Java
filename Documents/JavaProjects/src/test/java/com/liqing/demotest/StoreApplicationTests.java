package com.liqing.demotest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class StoreApplicationTests {

        @Autowired
        private DataSource dataSource;

        @Test
        void contextLoads() {

        }

        @Test
        public void getConnection() throws SQLException {
            System.out.println("数据库"+dataSource.getConnection());
        }



}
