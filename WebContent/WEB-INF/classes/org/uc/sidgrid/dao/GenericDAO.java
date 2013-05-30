package org.uc.sidgrid.dao;

//import org.hibernate.*;
//import org.hibernate.criterion.*;

import java.util.*;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.Configuration;

/**
* this class is a thin wrapper for Hibernate service
* It have to run in a multiple thread environment!!
* @author wenjun wu
*
*/
public class GenericDAO {
	private SessionFactory factory = null;
	protected Session session;
	
	private final static int CMD_DELETE = 1;
  private final static int CMD_DELETE_LIST = 2;
  private final static int CMD_RESTORE = 3;
  private final static int CMD_RESTORE_LIST = 4;
  private final static int CMD_UPDATE = 5;
  private final static int CMD_CREATE = 6;
  private final static int CMD_SAVEORUPDATE = 7;
  
  private static Log log = LogFactory.getLog(GenericDAO.class);
	/** public void setSession(Session s) {
      this.session = s;
  }
	public Session getSession() {
      if (session == null)
          session = HibernateUtil.getSessionFactory().getCurrentSession();
      return session;
  } **/
  public void create(Object object) throws Exception {
      doTransaction(object, "", CMD_CREATE);
  }
	public void update(Object object) throws Exception {
          doTransaction(object, "", CMD_UPDATE);
  }

  public void saveOrUpdate(Object object) throws Exception {
          doTransaction(object, "", CMD_SAVEORUPDATE);
  }
  
  public Object restore(String query) throws Exception {
      List list = restoreList(query);
      if (list.size() == 0) {
          return null;
//          throw new PersistenceManagerException("No object found with given query.");
      }
      return restoreList(query).get(0);
  }
  
  public List restoreList(String query) throws Exception {
          return (List) doTransaction(null, query, CMD_RESTORE_LIST);
  }

  public void delete(Object object) throws Exception {
          doTransaction(object, "", CMD_DELETE);
  }

  public void deleteList(String query) throws Exception {
          doTransaction(null, query, CMD_DELETE_LIST);
  }
	private Object doTransaction(Object object, String query, int command) throws Exception {
      Session session = null;
      Transaction tx = null;
      Object result = null;
      Query q = null;

      try {
      	factory = HibernateUtil.getSessionFactory();
          session = factory.openSession();
          // Open a new Session, if this thread has none yet
          tx = null;
          tx = session.beginTransaction();
          switch (command) {
              case CMD_CREATE:
                  session.save(object);
                  break;
              case CMD_DELETE:
                  session.delete(object);
                  break;
              case CMD_DELETE_LIST:
                  session.delete(query);
                  break;
              case CMD_UPDATE:
                  session.update(object);
                  break;
              case CMD_SAVEORUPDATE:
                  session.saveOrUpdate(object);
                  break;
              case CMD_RESTORE_LIST:
                  q = session.createQuery(query);
                  result = q.list();
                  break;
              case CMD_RESTORE:
                  q = session.createQuery(query);
                  result = q.list().get(0);
                  break;
          }
          tx.commit();
          session.close();
      } catch (HibernateException e) {
          if (tx != null) {
              tx.rollback();
          }
          log.error("Could not complete transaction", e);
          e.printStackTrace();
          throw e;
      } finally {
          session.close();
      }
      return result;
  }
	
}
