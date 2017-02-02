
package com.sqltojson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.chart.dao.impl.ChartDaoimpl;


@Component
public class SqltableToJSON {

		
public static Map getChartObject(int idMstreport,JdbcTemplate jdbcTempObj){

	@SuppressWarnings("unchecked")
	List<Map<String,String>> resultHead=new ChartDaoimpl().getChartData(idMstreport,jdbcTempObj);
	Map<Object,Map> yetToresolvParent=new LinkedHashMap<Object,Map>();
	Map<Object,List> tree=new HashMap<Object,List>();
	for(Map<String,String> innermap: resultHead){
		if(innermap.get("idChartobjectrelationParent")==null){
			innermap.put("idChartobjectrelationParent","ROOT");
		}
		//Check parent in final
		if(!tree.containsKey(innermap.get("idChartobjectrelationParent"))){
			List temp=	new ArrayList<Object>();
					temp.add(innermap.get("idChartProperty"));
			tree.put(innermap.get("idChartobjectrelationParent"),temp);
		}else{
			List temp=tree.get(innermap.get("idChartobjectrelationParent"));
			temp.add(innermap.get("idChartProperty"));
			tree.put(innermap.get("idChartobjectrelationParent"),temp);
		}
		
		//Parent Not found in final
		if(yetToresolvParent!=null && yetToresolvParent.containsKey(innermap.get("idChartobjectrelationParent"))){
			Map temp=yetToresolvParent.get(innermap.get("idChartobjectrelationParent"));
			temp.put(innermap.get("propertyName"), resolveTypeConstructObject(innermap.get("propertyType"), innermap.get("propertyName"), innermap.get("value")));
			yetToresolvParent.put(innermap.get("idChartobjectrelationParent"),temp );
			
		}else{
			Object temp=resolveTypeConstructObject(innermap.get("propertyType"), innermap.get("propertyName"), innermap.get("value"));
			Map ch=new LinkedHashMap<String,Object>();  ch.put(innermap.get("propertyName"),temp );
	    	yetToresolvParent.put(innermap.get("idChartobjectrelationParent"),ch );
	    }
		
			
			
			
	}
	//Comparator comp=new ChartdecenComp();
	Map<Long, Object> sortit=new TreeMap<Long,Object>(Collections.reverseOrder());
	for (Entry<Object, List> entry : tree.entrySet())	{
	long temp=	traverseTreeCalculateOrder(tree,entry.getKey(),1,1,"ROOT");
	if(sortit.containsKey(temp)){
		while(sortit.containsKey(temp)){
			temp=temp+1;
		}
		sortit.put(temp,entry.getKey());
	}else{
		sortit.put(temp,entry.getKey());
	}
	
	}
	
	

	for(Entry<Long, Object> entr: sortit.entrySet()){
	
	List temp=tree.get(entr.getValue()); 
	Map<Object, Object> temp2=yetToresolvParent.get(entr.getValue());  //Inner map of yetToresolvParent		
			  int index=0;
			  if(temp2==null){
				  continue;
			  }
			  for (Entry<Object, Object> entryChild : temp2.entrySet())	{
				Object tem=  temp.get(index);  
				Map toResolvDependency= yetToresolvParent.get(tem) ;   //Search in yetToresolvParent
				if(toResolvDependency!=null){
				if(entryChild.getValue() instanceof List ){
					Iterator it = toResolvDependency.entrySet().iterator();
					List tempList=	(List) temp2.get(entryChild.getKey());	
					while(it.hasNext()){
			          Entry entry=(Entry) it.next();
			          tempList.add(entry.getValue());
					    }
				  
				temp2.put(entryChild.getKey(), tempList);
				}else {
				temp2.put(entryChild.getKey(), toResolvDependency);
				}
				yetToresolvParent.put(entr.getValue(), temp2);
				tree.put(entr.getValue(), tree.get(tem));
				yetToresolvParent.remove(tem);
				 
				}	
				
				index++;
				
		}	
		
	}
	
	return yetToresolvParent.get("ROOT");
}

private static  Object resolveTypeConstructObject(String type,String propName,String value){
    if(type.equals("object")){
    	Map<String,Object> obj=new HashMap<String,Object>();
    	//obj.put(propName, value);
    	return obj;
    }else if(type.equals("array") && (!value.isEmpty())){
    	List<String> obj = Arrays.asList(value.split("\\s*,\\s*"));
    	return obj;
    	
   }else if(type.equals("array") && (value.isEmpty())){
   	List<Object> obj =new ArrayList<Object>();
   	return obj;
   	
  }else if(type.equals("string")){
	   return value;
   }else if(type.equals("int")){
	//   return new Integer(value); // NOPMD by nikhil.t on 12/2/16 3:39 PM
	   return Integer.valueOf(value);
   }
   else if(type.equals("boolean")){
	   return Boolean.valueOf(value); // NOPMD by nikhil.t on 12/2/16 3:39 PM
   }
   else if(type.equals("float")){
	   return Float.valueOf(value);
   }
    return null;
}



private static long  traverseTreeCalculateOrder(Map<Object,List> tree,Object node,long rootInitialHieght,long calculator,Object root){
	if(node.equals("ROOT")){
		return Long.MIN_VALUE;
	}
	rootInitialHieght=rootInitialHieght+calculator;
	List rootList=tree.get(root);
	if(rootList.contains(node)){
		return rootInitialHieght;
	}else{
		for(Object listDeppNode :rootList){
			calculator++;
			if(tree.get(listDeppNode)==null){
				continue;
			}
			rootInitialHieght=	traverseTreeCalculateOrder(tree,node,rootInitialHieght,calculator,listDeppNode);
			
		}
	}
	return rootInitialHieght;
}
	
}


