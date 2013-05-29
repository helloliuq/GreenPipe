package org.uc.sidgrid.mobyle.core;

import org.python.util.PythonInterpreter;
import org.python.core.PyObject;
import org.python.core.PyInteger;

public class Evaluator {
  private PythonInterpreter interp;
  private long tmpVarCnt;
  public Evaluator(){
	  interp = new PythonInterpreter();
	  tmpVarCnt = 0;
  }
  public void setVar(String var, Object value){
	  interp.set(var,value);
  }
  public Object getVar(String var){
	  return interp.get(var);
  }
  public void exec(String expr){
	  interp.exec(expr);
  }
  public PyObject eval(String expr){
	  String newExpr = "eval_"+tmpVarCnt+"="+expr;
	  /** interp.exec(newExpr);
	  PyObject result = interp.get("eval_"+tmpVarCnt);
	  return result; **/
	  return interp.eval(expr);
  }
  public boolean evalcond(String expr){
	  PyInteger res = (PyInteger)this.eval(expr);
	  if (res.getValue() > 0 )
		  return true;
	  else
		  return false;
  }
  public boolean isDefined(String varName){
	  if (interp.get(varName)!= null)
		  return true;
	  else
		  return false;
  }
}
