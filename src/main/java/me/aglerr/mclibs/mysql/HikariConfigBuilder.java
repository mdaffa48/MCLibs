package me.aglerr.mclibs.mysql;

import com.zaxxer.hikari.HikariConfig;

import java.util.Objects;

public class HikariConfigBuilder {

    private String host, database, user, password, poolName, path;
    private Integer port;
    private int waitTimeout = 600000;
    private int maxLifetime = 1800000;
    private int poolSize = 50;
    private int connectionTimeout = 10000;
    private boolean useSSL = false;
    private boolean publicKeyRetrieval = true;

    public HikariConfigBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public HikariConfigBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public HikariConfigBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public HikariConfigBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public HikariConfigBuilder setPoolName(String poolName) {
        this.poolName = poolName;
        return this;
    }

    public HikariConfigBuilder setPath(String path){
        this.path = path;
        return this;
    }

    public HikariConfigBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public HikariConfigBuilder setWaitTimeout(int waitTimeout) {
        this.waitTimeout = waitTimeout;
        return this;
    }

    public HikariConfigBuilder setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
        return this;
    }

    public HikariConfigBuilder setPoolSize(int poolSize) {
        this.poolSize = poolSize;
        return this;
    }

    public HikariConfigBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public HikariConfigBuilder setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }

    public HikariConfigBuilder setPublicKeyRetrieval(boolean publicKeyRetrieval) {
        this.publicKeyRetrieval = publicKeyRetrieval;
        return this;
    }

    public HikariConfig buildMysql() {
        Objects.requireNonNull(host, "Host/Address cannot be null");
        Objects.requireNonNull(database, "Database name cannot be null");
        Objects.requireNonNull(user, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        Objects.requireNonNull(poolName, "Pool name cannot be null");
        Objects.requireNonNull(port, "Port cannot be null");

        HikariConfig config = new HikariConfig();
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName(poolName);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=%b&allowPublicKeyRetrieval=%b",
                host, port, database, useSSL, publicKeyRetrieval));
        config.setUsername(user);
        config.setPassword(password);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(poolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(waitTimeout);
        config.setMaxLifetime(maxLifetime);
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("useUnicode", true);

        return config;
    }

    public HikariConfig buildSQLite(){
        Objects.requireNonNull(path, "Path cannot be null");
        Objects.requireNonNull(poolName, "Pool name cannot be null");

        HikariConfig config = new HikariConfig();
        config.setConnectionTestQuery("SELECT 1");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + path);
        config.setPoolName(poolName);
        config.setMaximumPoolSize(1);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(waitTimeout);
        config.setLeakDetectionThreshold(10000);

        return config;
    }

}
