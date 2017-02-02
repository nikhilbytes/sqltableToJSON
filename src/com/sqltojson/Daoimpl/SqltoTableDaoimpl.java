package com.sqltojson.Daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 
*
* @author nikhil tripathi
*/
public class SqltoTableDaoimpl  {  // NOPMD by nikhil.t on 12/2/16 3:02 PM

	
/**
 * This code is for dao object .	
 */
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://url:3306/schema?allowMultiQueries=true";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "admin";
	   
static public Connection getConnection(){
	//STEP 2: Register JDBC driver
    Class.forName(JDBC_DRIVER);

   //STEP 3: Open a connection
    System.out.println("Connecting to database...");
    return DriverManager.getConnection(DB_URL,USER,PASS);

}
public List<Map<String,String>> getSqlData( final int idroot) { 
	  Connection conn = null;	  
 List<Map> finalObj=new ArrayList<Map>();
 int j=1;
 try{
    
    //STEP 4: Execute a query
     System.out.println("Creating statement...");    
	 String sql = "Call getJson(?)";
	 conn=getConnection();
	 PreparedStatement stmt = conn.prepareStatement(sql);
	 stmt.setInt(1,idroot );
     ResultSet rs = stmt.executeQuery(sql);
	  ResultSetMetaData md = rs.getMetaData();
	  int columns = md.getColumnCount();
	  while (rs.next()){
		  Map<String,String> temp=new HashMap<String,String>();
		  for(j=1;j<columns;j++){
			  temp.put(md.getColumnLabel(j),rs.getObject(j).toString()) ;
		  j++;
	  }
		  finalObj.add(temp);  
	  }
	
	  
}catch(SQLException se){
//Handle errors for JDBC
se.printStackTrace();
}
 return finalObj;
 
}
	
}
