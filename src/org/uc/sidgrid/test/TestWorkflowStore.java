package org.uc.sidgrid.test;

import org.uc.sidgrid.app.*;
import org.uc.sidgrid.dao.*;
import org.uc.sidgrid.services.WorkflowFactory;

public class TestWorkflowStore {
  public static void createWorkFlow()throws Exception{
	   DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		// get the user
		UserDAO userdao = daoFactory.getUserDAO();
		org.uc.sidgrid.app.User user = userdao.getUserByName("wwjag");
		System.out.println(user.getPasswd());
		WorkflowFactory factory = WorkflowFactory.getInstance();
	    Workflow wf = new Workflow();
	    wf.setWorkflowID("testit");
	    wf.setStatus("test");
	    wf.setUser(user);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		wfdao.create(wf);
		java.util.List<Workflow>tmpres = wfdao.restoreList("from Workflow where Status='created'");
		for(Workflow tmp : tmpres){
			System.out.println("worklofw id "+tmp.getWorkflowID()+"=>"+tmp.getScript()+"\n");
		}
  }
  public static void createApplication() throws Exception{
	  DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
	  ApplicationDAO appDao = daoFactory.getAppDAO();
	  Application app = new Application();
	  app.setAppName("test");
	  app.setTitle("my test");
	  app.setStatus(Application.created);
	  appDao.create(app);
  }
  public static void main(String[] args) throws Exception{
	  createApplication();
  }
}
