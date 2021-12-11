package me.aglerr.mclibs.mysql;

import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SQLHelper {

    private static SQLSession globalSession;

    public static boolean createConnection(boolean mysql, String path, HikariConfig config) {
        globalSession = new SQLSession(path, config);
        return globalSession.createConnection(mysql);
    }

    public static void executeUpdate(String statement) {
        globalSession.executeUpdate(statement);
    }

    public static void executeUpdate(String statement, Consumer<SQLException> onFailure) {
        globalSession.executeUpdate(statement, onFailure);
    }

    public static void executeQuery(String statement, QueryConsumer<ResultSet> callback) {
        globalSession.executeQuery(statement, callback::accept);
    }

    public static void executeQuery(String statement, QueryConsumer<ResultSet> callback, Consumer<SQLException> onFailure) {
        globalSession.executeQuery(statement, callback::accept, onFailure);
    }

    public static void buildStatement(String query, QueryConsumer<PreparedStatement> consumer, Consumer<SQLException> onFailure) {
        globalSession.buildStatement(query, consumer::accept, onFailure);
    }

    public static boolean doesConditionExist(String statement) {
        AtomicBoolean result = new AtomicBoolean(false);
        executeQuery(statement, resultSet -> result.set(resultSet.next()));
        return result.get();
    }

    public static Connection getConnection() throws SQLException {
        return globalSession.getConnection();
    }

    public static void close() {
        globalSession.close();
    }

    public interface QueryConsumer<T> {

        void accept(T value) throws SQLException;

    }

}
