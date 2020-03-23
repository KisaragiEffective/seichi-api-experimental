/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.kisaragieffective.experimental.seichiapi;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {
    @Value("${spring.datasource.url}")
    @SuppressWarnings({"unused"})
    private String dbUrl;

    @Autowired
    @SuppressWarnings({"unused"})
    private DataSource dataSource;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @RequestMapping("/")
    @SuppressWarnings({"unused"})
    String index() {
        return "index";
    }

    @RequestMapping("/db")
    @SuppressWarnings({"unused"})
    String db(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            try(ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks")) {
                List<String> output = new ArrayList<>();
                while (rs.next()) {
                    output.add("Read from DB: " + rs.getTimestamp("tick"));
                }
                model.put("records", output);
                return "db";
            }
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping("/api/v1/transaction/increase")
    String increase(Map<String, Object> template) {
        template.put("message", "INCREASE PAGE, BUT IS NOT IMPLEMENTED");
        return "error";
    }

    @RequestMapping("/api/v1/transaction/decrease")
    String decrease(Map<String, Object> template) {
        template.put("message", "DECREASE PAGE, BUT IS NOT IMPLEMENTED");
        return "error";
    }

    @RequestMapping("/api/v1/transaction/logs")
    String showLog(Map<String, Object> template) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            try(ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks")) {
                List<String> output = new ArrayList<>();
                while (rs.next()) {
                    output.add("Read from DB: " + rs.getTimestamp("tick"));
                }
                template.put("json", output);
                return "v1_log";
            }
        } catch (Exception e) {
            template.put("message", e.getMessage());
            return "error";
        }
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }
    }
}