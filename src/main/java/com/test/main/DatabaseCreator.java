package com.test.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseCreator {

	public static void main(String args[]) throws Exception {

		String url = "jdbc:mysql://localhost:3306/shard1";
		String dbusr = "root";
		String dbpwd = "password";
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, dbusr, dbpwd);
		Statement statement = connection.createStatement();
		String query;
		//query = "drop table user";
		//statement.executeUpdate(query);
		query = "create table user(user_id integer primary key, usr_name varchar(50),"
				+ "usr_gender varchar(10),usr_country varchar(20))";
		statement.executeUpdate(query);
		statement.close();
		connection.close();
		url = "jdbc:mysql://localhost:3306/shard2";
		connection = DriverManager.getConnection(url, dbusr, dbpwd);
		statement = connection.createStatement();
		//query = "drop table user";
		//statement.executeUpdate(query);
		query = "create table user(user_id integer primary key, usr_name varchar(50),"
				+ "usr_gender varchar(10),usr_country varchar(20))";
		statement.executeUpdate(query);
		statement.close();
		connection.close();
	}
}
