package com.wavesdev.oauth2.dao.hibernate;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class GenericDaoHibernate {
	 @Resource
	    private SessionFactory sessionFactory;
	 
	 public SessionFactory getSessionFactory() {
	        return this.sessionFactory;
	    }

	    public Session getSession() throws HibernateException {
	        Session sess = getSessionFactory().getCurrentSession();
	        if (sess == null) {;
	            sess = getSessionFactory().openSession();
	        }
	        return sess;
	    }

	    @Autowired
	    @Required
	    public void setSessionFactory(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
}
