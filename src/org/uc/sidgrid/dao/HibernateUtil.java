package org.uc.sidgrid.dao;

//import org.hibernate.*;
//import org.hibernate.cfg.*;
import java.io.File;
import javax.servlet.http.HttpSession;
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.AxisHttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static Log log = LogFactory.getLog(HibernateUtil.class);
  private static final SessionFactory sessionFactory;

  static {
      try {
          sessionFactory = new Configuration().configure().buildSessionFactory();
      } catch (Throwable ex) {
          // Make sure you log the exception, as it might be swallowed
          System.err.println("Initial SessionFactory creation failed." + ex);
          ex.printStackTrace();
          throw new ExceptionInInitializerError(ex);
      }
  }

  public static SessionFactory getSessionFactory() {
      return sessionFactory;
  }

}
