package com.test.main;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.test.domain.User;
import com.test.util.HibernateShardUtil;

public class Main {
	
	@SuppressWarnings("unchecked")
	public void list() {
		SessionFactory sessionFactory = HibernateShardUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		List<User> userList = session.createQuery("from User").list();
		Iterator<User> iterator = userList.iterator();
		while (iterator.hasNext()) {
			User user = (User) iterator.next();
			System.out.println(user);
		}
		session.close();
	}

	public void input() {
		
		SessionFactory sessionFactory = HibernateShardUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		try {
			
			transaction = session.beginTransaction();
			
			User user = new User();
			user.setName("Max Payne");
			user.setCountry("U.S.A");
			user.setGender("Male");
			session.save(user);
			
			user = new User();
			user.setName("Virat Kohli");
			user.setCountry("India");
			user.setGender("Male");
			session.save(user);
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		session.close();
	}

	public static void main(String[] args) {
		
		Main obj = new Main();
		obj.list();
		obj.input();
		obj.list();
		
	}
}