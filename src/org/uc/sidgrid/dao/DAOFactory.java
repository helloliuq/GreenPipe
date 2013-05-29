package org.uc.sidgrid.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A DAOFactory for hibernate persistence db
 * @author wenjun wu
 *
 */
public class DAOFactory {
	private static Log log = LogFactory.getLog(DAOFactory.class);
	public static DAOFactory instance(Class factory) {
        try {
            log.debug("Creating concrete DAO factory: " + factory);
            return (DAOFactory)factory.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create DAOFactory: " + factory);
        }
    }
	public ApplicationDAO getAppDAO() {
        return (ApplicationDAO)instantiateDAO(ApplicationDAO.class);
    }
	public WorkflowDAO getWorkflowDAO(){
		return (WorkflowDAO)instantiateDAO(WorkflowDAO.class);
	}
	public UserDAO getUserDAO(){
		return (UserDAO)instantiateDAO(UserDAO.class);
	}
	public GroupDAO getGroupDAO(){
		return (GroupDAO)instantiateDAO(GroupDAO.class);
	}
	public ExperimentDAO getExperimentDAO(){
		return (ExperimentDAO)instantiateDAO(ExperimentDAO.class);
	}
	private GenericDAO instantiateDAO(Class daoClass) {
        try {
            log.debug("Instantiating DAO: " + daoClass);
            return (GenericDAO)daoClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }
}
