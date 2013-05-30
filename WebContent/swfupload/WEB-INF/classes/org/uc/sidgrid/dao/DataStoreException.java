package org.uc.sidgrid.dao;

public class DataStoreException extends Exception
{
  public DataStoreException()
  {
    super();
  }

  public DataStoreException(String message)
  {
    super(message);
  }

  public DataStoreException(Exception e)
  {
    super(e);
  }

  public DataStoreException(String message, Exception e)
  {
    super(message, e);
  }

}