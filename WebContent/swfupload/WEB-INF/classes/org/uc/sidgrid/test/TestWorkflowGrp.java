package org.uc.sidgrid.test;

import java.util.List;
import java.util.Set;

import org.uc.sidgrid.app.User;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.dao.GenericDAO;
import org.uc.sidgrid.dao.WorkflowDAO;
import org.uc.sidgrid.social.Group;
import org.uc.sidgrid.data.*;
import org.uc.sidgrid.app.*;

public class TestWorkflowGrp {
  // find the groups by following the experiment of the workflow
  //TODO: users -> group -> friends -> their projects (?)
  /**
   /workflow/workflowid
   /workflow/userid/
   /groupid/experimentid/workflowid
   */
	
	public static void main(String[] args) throws Exception{
		  GenericDAO dao = new GenericDAO();
		  // test 1: search group by user
		  String queryStr = "from User where name='wwjag'";
		  List<User> result = dao.restoreList(queryStr);
		  for(User user : result){
			  Set<Group> grps = user.getGroups();
			  for (Group tmp : grps){
				  System.out.println("the user group is "+tmp.getName());
				  // for each group, let's find their experiments
				  Set<Experiment> exps = tmp.getExps();
				  for (Experiment exptmp : exps){
					  System.out.println("exp name is "+exptmp.getName()+":"+exptmp.getId());
					  // check whether this experiment has new workflows
					  DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
					  WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
					  List<Workflow> wfs = wfdao.getWorkflow(exptmp.getId());
					  if (wfs != null){
					    for (Workflow wftmp : wfs){
						   System.out.println("workflow is "+wftmp.getWorkflowID()+" for exp "+exptmp.getName());
					    }
					  }
				  }
			  }
		  }
	  }
}
