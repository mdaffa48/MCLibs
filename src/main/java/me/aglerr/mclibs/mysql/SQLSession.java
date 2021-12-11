package me.aglerr.mclibs.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.libs.Common;

import java.io.File;
import java.sql.*;
import java.util.function.Consumer;

public class SQLSession {

    private HikariDataSource dataSource;
    private Connection connection;
    private boolean mysql;

    private final String path;
    private final HikariConfig config;

    public SQLSession(String path, HikariConfig config) {
        this.path = path;
        this.config = config;
    }

    public boolean createConnection(boolean mysql) {
        this.mysql = mysql;
        if(mysql){
            Common.log("Trying to connect to MySQL database...");
            try {
                dataSource = new HikariDataSource(config);
                Common.log("Successfully established connection with MySQL database!");
                return true;
            } catch (Exception ignored) { }
        } else {
            Common.log("Trying to connect to SQLite database...");
            try{
                File file = new File(path);
                if(!file.exists()){
                    file.createNewFile();
                }
                dataSource = new HikariDataSource(config);
                Common.log("Successfully established connection with SQLite database!");
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public void executeUpdate(String statement) {
        executeUpdate(statement, error -> {
            Common.log("&cAn error occurred while running statement: " + statement);
            error.printStackTrace();
        });
    }

    public void executeUpdate(String statement, Consumer<SQLException> onFailure) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            onFailure.accept(ex);
        }
    }

    public void executeQuery(String statement, QueryConsumer<ResultSet> callback) {
        executeQuery(statement, callback, error -> {
            Common.log("&cAn error occurred while running query: " + statement);
            error.printStackTrace();
        });
    }

    public void executeQuery(String statement, QueryConsumer<ResultSet> callback, Consumer<SQLException> onFailure) {
        try{
            Connection connection = this.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();

            callback.accept(resultSet);

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex){
            onFailure.accept(ex);
        }
    }

    public void buildStatement(String query, QueryConsumer<PreparedStatement> consumer) {
        buildStatement(query, consumer, error -> {
            Common.log("&cAn error occurred while building statement: " + query);
            error.printStackTrace();
        });
    }

    public void buildStatement(String query, QueryConsumer<PreparedStatement> consumer, Consumer<SQLException> onFailure) {
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement);
        } catch (SQLException ex) {
            onFailure.accept(ex);
        }
    }

    public Connection getConnection() throws SQLException {
        if(mysql){
            return dataSource.getConnection();
        } else {
            if(connection == null || connection.isClosed()){
                connection = dataSource.getConnection();
            }
            return connection;
        }
    }

    public void close() {
        dataSource.close();
    }

    public interface QueryConsumer<T> {

        void accept(T value) throws SQLException;

    }

}
