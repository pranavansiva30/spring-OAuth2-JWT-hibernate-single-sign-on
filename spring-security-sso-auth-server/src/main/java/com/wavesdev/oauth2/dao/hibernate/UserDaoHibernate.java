package com.wavesdev.oauth2.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.wavesdev.oauth2.dao.UserDao;
import com.wavesdev.oauth2.model.User;



@Repository("userDao")
public class UserDaoHibernate extends GenericDaoHibernate implements UserDao {

	public User findByUserName(String username) {
		final Criteria criteria = getSession().createCriteria(User.class).add(
				Restrictions.eq("username", username));
		User u = (User) criteria.uniqueResult();

		return u;
	}

	public int saveUser(User u) {
		int i = (Integer) getSession().save(u);
		return i;
	}

	public void updateUser(User u) {
		Session session = getSession();
		session.update(u);

	}

	public int deleteUser(Long userid) {
		Session session = getSession();
		Query q = session.createQuery("delete from User where id=:i");
		q.setParameter("i", userid);
		int x = q.executeUpdate();
		return x;
	}

	public User getById(Long id) {
		final Criteria criteria = getSession().createCriteria(User.class).add(
				Restrictions.eq("id", id));
		User u = (User) criteria.uniqueResult();

		return u;
	}

	public List<User> getUsers() {
		List<User> list = new ArrayList<User>();
		final Criteria criteria = getSession().createCriteria(User.class);
		list = criteria.list();
		return list;
	}

}
