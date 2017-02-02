# sqltableToJSON

sqltabletoJson is a API written in java,which provides a architecture which enables you to convert relation table data(Mysql/orcale,Sql server etc) to JSON formate.

## USECASE 
Assume, you have a scenerio such that you have to frequently configure third party JS object without changing the code in live server as for example creating several Highchart object for populating chart without stopping the server and  without predefining any json for it.other good example can be any adhoc json which is difficult to keep a equivelent Java POJO class.  

## EXAMPLE

Take this json object

{
 "a":"hii",
 "b":"I am here",
 "c":2,
 "d":["to","convert","adhoc","json"]
 }
 ## Equivelant POJO Java class
 ```java
public Class MypojoForJson
{
  private String a;

  public String getA() { return this.a; }

  public void setA(String a) { this.a = a; }

  private String b;

  public String getB() { return this.b; }

  public void setB(String b) { this.b = b; }

  private int c;

  public int getC() { return this.c; }

  public void setC(int c) { this.c = c; }

  private ArrayList<String> d;

  public ArrayList<String> getD() { return this.d; }

  public void setD(ArrayList<String> d) { this.d = d; }
}
```
## IF it json keep changing,how will you define it ?

sqltableToJSON API provides a way to do it.
you have to create two table in your current relational DB and define your object in any insertion order.
## Table 1-  `jsonproperty`
It defines your several JSON properties  as object,array,integer,float,string,boolean
id     propertyName     propertytype
1      chartObjet       object
2      type             string
3      categories       array
4      arrayofobject    array
5      simpleobject     object      
## Table 2-  `jsonInternalrelation` 
It defines the parent to child relationship among properties defined in table `jsonproperty`. column `value` will be null ,in case of object.if you have array of string or integer,value will be defined as comma seperated values as 'see','the','color',2 will be converted as ['see','the','color',2] but in case of array of object define array as parent of each object with null value.no need to maintain any insertion order.   


|id | idJsonproperty(foriegn key) | idJsonpropertyParent(foriegn key) | value|
|---| ----------------------------|-----------------------------------|-----|
|1   |    1                 |              null           |              null (Root object starts here)|
|2   |    2                 |              1              |              'i am here'  |
|3   |    3                 |               1             |             'see','the','color',2 |
|4   |    5                 |               1             |               null   |
|5   |   4                  |              5              |              null    |

## HOW TO USE
1.import SqltableToJSON class in your class
2. create tables and `getJson(INT)` procedure.
3. call static method SqltableToJSON.getJSON(1);  (Parameter is id of Root object in jsonproperty table ).
 IT'S DONE
