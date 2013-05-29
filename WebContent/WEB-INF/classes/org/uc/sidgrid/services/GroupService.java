package org.uc.sidgrid.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import org.uc.sidgrid.social.Group;
import org.uc.sidgrid.social.GroupJson;
import org.uc.sidgrid.dao.*;
import org.uc.sidgrid.data.Experiment;
import org.uc.sidgrid.app.Workflow;
import org.uc.sidgrid.app.WorkflowJson;

public class GroupService {
	// get the groups owned by me
	public static List getAllGroups(String user){
		DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		GroupDAO grpdao = daoFactory.getGroupDAO();
		List<Group> groups = grpdao.getGroupByUser(user);
		// convert the group into a json-friendly one
		List grpJsons = new ArrayList();
		for(Group tmp : groups){
			grpJsons.add(new GroupJson(tmp));
		}
		return grpJsons;
	}
	
	// get the workflows created for the group
	public static List<Workflow> getWfsByGroup(String grpname){
		DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		GroupDAO grpdao = daoFactory.getGroupDAO();
		Group grp = grpdao.getGroupByName(grpname);
		Set<Experiment> exps = grp.getExps();
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		List<Workflow> result = new ArrayList<Workflow>();
		for(Experiment exp : exps){
			long expid = exp.getId();
			List<Workflow> tmp_exps = wfdao.getWorkflow(expid);
			List wfJsons = new ArrayList();
			for (Workflow wf : tmp_exps){
				wfJsons.add(new WorkflowJson(wf));
			}
			result.addAll(wfJsons);
		}
		return result;
	}
}
