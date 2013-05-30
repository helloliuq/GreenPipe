package org.uc.sidgrid.dao;

import java.util.List;
import org.uc.sidgrid.app.Workflow;
import org.uc.sidgrid.app.RunArgument;

public class WorkflowDAO extends GenericDAO{
	public int getTotalWorkflows(String userName){
	 try {
	 //	 check whether lsgwuser set already has it
       String query = "from "+Workflow.class.getName() +" as workflow where workflow.user='"+userName+"'";
	   List res = this.restoreList(query);
	   int total = res.size();
	   return total;
     } catch (Exception e){
	   e.printStackTrace();
	   return -1;
     }
   }
	public int getTotalWorkflows(){
		 try {
		 //	 check whether lsgwuser set already has it
	       String query = "from "+Workflow.class.getName() +" as workflow";
		   List res = this.restoreList(query);
		   int total = res.size();
		   return total;
	     } catch (Exception e){
		   e.printStackTrace();
		   return -1;
	     }
	   }
	public List<Workflow> getAllWorkflows(String userName){
		try {
			 //	 check whether lsgwuser set already has it
		       String query = "from "+Workflow.class.getName() +" as workflow where workflow.user='"+userName+"' order by workflow.createTime";
			   List<Workflow> res = this.restoreList(query);
			   return res;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public List<Workflow> getAllWorkflows(){
		try {
			 //	 check whether lsgwuser set already has it
		       String query = "from "+Workflow.class.getName() +" as workflow order by workflow.createTime";
			   List<Workflow> res = this.restoreList(query);
			   return res;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public Workflow getWorkflow(String userName, String wfId){
		try {
			 //	 check whether lsgwuser set already has it
		       String query = "from "+Workflow.class.getName() +" as workflow where workflow.user='"+userName+
		                      "' and workflow.workflowID='"+wfId+"'";
			   List<Workflow> res = this.restoreList(query);
			   return res.get(0);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public Workflow getWorkflow(String wfId){
		try {
			 //	 check whether lsgwuser set already has it
		       String query = "from "+Workflow.class.getName() +" as workflow where workflow.workflowID='"+wfId+"'";
			   List<Workflow> res = this.restoreList(query);
			   return res.get(0);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public List<Workflow> getWorkflow(long expId){
		try {
			 //	 check whether lsgwuser set already has it
		       String query = "from "+Workflow.class.getName() +" as workflow where workflow.exp='"+expId+"'";
			   List<Workflow> res = this.restoreList(query);
			   return res;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public List<RunArgument> getArgs(String wfId){
		try {
	       String query = "from " + RunArgument.class.getName() + " as runarg where runarg.WorkflowId='"+wfId+"'";
		   List<RunArgument> res = this.restoreList(query);
		   return res;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
