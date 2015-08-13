package com.test.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ShardedIDGenrator implements IdentifierGenerator {

	static Integer id;
	static {
		id = 0;
		String url = "jdbc:mysql://localhost:3306/shard1";
		String dbusr = "root";
		String dbpwd = "password";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, dbusr, dbpwd);
			Statement stmt = con.createStatement();
			String query = "select max(user_id) from user";
			ResultSet rset = stmt.executeQuery(query);
			if (rset.next()) {
				int tempid = rset.getInt(1);
				if (id < tempid)
					id = tempid;
			}
			stmt.close();
			con.close();
			url = "jdbc:mysql://localhost:3306/shard2";
			con = DriverManager.getConnection(url, dbusr, dbpwd);
			stmt = con.createStatement();
			query = "select max(user_id) from user";
			rset = stmt.executeQuery(query);
			if (rset.next()) {
				int tempid = rset.getInt(1);
				if (id < tempid)
					id = tempid;
			}
			System.out.println(id);
			stmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1)
			throws HibernateException {
		id = id + 1;
		return id;
	}

}
