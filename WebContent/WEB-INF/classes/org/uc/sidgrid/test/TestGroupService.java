package org.uc.sidgrid.test;

import java.util.List;
import org.uc.sidgrid.social.Group;
import org.uc.sidgrid.app.Workflow;
import org.uc.sidgrid.services.GroupService;

public class TestGroupService {
	public static void main(String[] args) throws Exception{
		List<Group> grps = GroupService.getAllGroups("wwjag");
		 for (Group tmp : grps){
			  System.out.println("the user group is "+tmp.getName());
			 /** List<Workflow> wfs = GroupService.getWfsByGroup(tmp.getName());
			  if (wfs != null){
				    for (Workflow wftmp : wfs){
					   System.out.println("workflow is "+wftmp.getWorkflowID());
				    }
			  } **/
			  
		 }
	}
}
