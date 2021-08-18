package com.wizeline.commons;

import com.wizeline.domain.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    static final String SELECT_USER_BY_USERNAME = "select * from users where username =  '%s' ";

    static {
        config.setJdbcUrl( "jdbc:mysql://bootcamp-tht.sre.wize.mx:3306/bootcamp_tht??characterEncoding=utf8&autoReconnect=true&useSSL=false" );
        config.setUsername( "secret" );
        config.setPassword( "noPow3r" );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static User getUser(String username) throws SQLException {
        User user =  null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement pst = con.prepareStatement( String.format(SELECT_USER_BY_USERNAME,username) );
             ResultSet rs = pst.executeQuery();) {
            while ( rs.next() ) {
                user = new User();
                user.setUsername( rs.getString( "username" ) );
                user.setPassword( rs.getString( "password" ) );
                user.setSalt(rs.getString( "salt" ) );
                user.setRole(rs.getString( "role" ) );
            }
        }
        return user;
    }
}