package org.mysql.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by jpjensen on 5/17/17.
 */
public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream("Application.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            Class.forName(properties.getProperty("driver-class-name"));

            connection = DriverManager.getConnection(properties.getProperty("url") + "?serverTimezone=UTC", properties.getProperty("user"),
                    properties.getProperty("password"));

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM account");

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                Date createdAt = resultSet.getDate("created_at");
                Date updateAt = resultSet.getDate("update_at");

                System.out.println(id + "|" + createdAt + "|" + updateAt);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

    }
}
