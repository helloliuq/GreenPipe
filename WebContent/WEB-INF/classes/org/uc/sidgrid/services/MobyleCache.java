package org.uc.sidgrid.services;

/**
 * this class caches the parsed Mobyle XML objects
 * @author wenjun wu
 *
 */
import java.util.Hashtable;
import org.uc.sidgrid.mobyle.ProgramDocument;

public class MobyleCache {
  private Hashtable<String, ProgramDocument>sidgridXMLs; // appName -> programDoc
  
}
