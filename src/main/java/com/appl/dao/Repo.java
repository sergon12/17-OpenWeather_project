package com.appl.dao;

import com.appl.model.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Repository
public class Repo implements Dao<Day>  {

//    private DataSource dataSource;
//    private JdbcTemplate jdbc;        //*1

//    @Autowired
//    public Repo(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    @Autowired
    private Environment env;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.hikari.schema}")
    private String datasourceSchema;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

//    @PostConstruct                    //*1
//    private void postConstruct() {
//        jdbc = new JdbcTemplate(dataSource);
//    }





    @Override
    public List<Day> findAll() {
        List<Day> listDays = new ArrayList<>();
        try {
            int tableSize = 0;

            //System.out.println("-->>> " + env.getProperty("spring.datasource.currentSchema"));

            //Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/db1?currentSchema=schema1", "postgres", "pass4p");
            Connection connection = DriverManager.getConnection(
                    datasourceUrl + "?currentSchema=" + datasourceSchema, username, password );

            String query = "select time, sunrise_time, sunset_time, day_length from weather order by id asc";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String time = rs.getString(1);
                time = time.split(" ")[0];
                String sunriseTime = rs.getString(2);
                String sunsetTime = rs.getString(3);
                String dayLength = rs.getString(4);
                tableSize++;
                Day day = new Day(time, sunriseTime, sunsetTime, dayLength, tableSize);
                listDays.add(day);
            }

            System.out.println("ResultSet.size = " + tableSize);

        } catch (SQLException sqle) {
            System.out.println("logWeatherToDB SQL exception: " + sqle);
        }
        return listDays;
    }

    public String addS() {
        return "eeeee!";
    }

}
