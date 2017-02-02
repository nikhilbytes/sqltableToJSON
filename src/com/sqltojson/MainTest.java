package com.sqltojson;
import com.sqltojson.SqltableToJSON

Class MainTest
{
public static void main(String args[])
{
public String getJSONString(int idRoot){
return SqltableToJSON.(idRoot);
}
 
public Object getJsonEquivJavaObject(int idRoot){
return SqltableToJSON.getJsonObject(idRoot);
} 
System.out.println(getJSONString(idRoot));

}
}
