# NGSIBaseModel
This is a multiporposal parser for the NGSI10 standard.
The NGSI10 standard is a the default standard used by [ORION](https://fiware-orion.readthedocs.io/en/master/) the main Componnent of the [FIWARE project](https://www.fiware.org/).

This parser is able to simely convert the Json objects received in the NGSI format (both standard and keyValues) to Java Classes. 
And also convert Java classes to the equivalent and compliant NGSI10 entities.

### The Classes should extend the NGSIBaseModel. And fullfill the following constraints:

  - The class name should be the same as the entity type which we are parsing;
  
  - the fields of the class must not be private;
  
  - You must use the JsonObject from the [Gson library](https://github.com/google/gson). You can easly convert from and to other Json libraries by calling the method `toString()` and parsing the string.
  
  - The attributes of the class should be named the same as the entity attributes (ignoring case);
  - If the class is to be sent to an orion instance the id field should be defined, otherwise the id and type attributes will not be defined by the base model (You can still defined them manually after retriving the json);
  - The class can have list field but they should either be `List<String>` or a <code>List</code> of another class that also extends the *NGSIBaseModel* 
  - If you want to parse date strings directly the class field should be of type date. The model only supports the format `"yyyy-MM-dd'T'HH:mm:ss'Z'"`(ISO8601). 
For other formats create String fields and parse them accordingly after the model creates the objects.
  - If you have a field that supports any of the [forbidden characters of ORION](https://fiware-orion.readthedocs.io/en/master/user/forbidden_characters/index.html) use the annotation `@NGSIEncode`. 
  The parser will perform urlEncoding and decoding on fields marked with that annotation; 
  - It is also possible to ignore field that are only client specific, with the annotation `@NGSIIgnore`. Those fields will not be added to the NGSI json Object and will not be parsed from the NGSI objects as well.

## Example of a Class:
```java 
    
public class Car extends NGSIBaseModel{
    String id;
    String model;
    String color;
    
    //this field will be converted into a JsonArray 
    List<String> variations;
    
    //this field will be ignored
    @NGSIIgnore
    String ignoreMe;
    
    //this field will be parsed 
    @NGSIEncoded
    String encodeMe;

    //this field will be parsed from and to the format "yyyy-MM-dd'T'HH:mm:ss'Z'"
    Date timestamp;
    ...
    
    }



```


  

### From NGSI:
To convert json to a compliant class is just necessary to call the method <code>fromNgsi()</code> 
```java
    JsonObject json= new JsonObject(jsonString);
    Car car= new Car().fromNGSI(json);

```

### To NGSI:
To convert json to a compliant class is just necessary to call the method <code>fromNgsi()</code> 
```java
     Car test = new Car();
     test.setId("car_1");
     test.setColor("red");
     test.setModel("corvette");

     test.setEncodeMe("This car is a simple car==!\"(with a red hood)");
     test.setIgnoreMe("I was ignored!");

     DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
     Date timestamp = df.parse("2020-10-07T09:50:00Z");
     test.setTimestamp(timestamp);
     
     List<String> variations = new ArrayList<String>();
     variations.add("Street");
     variations.add("Lounge");
     variations.add("Easy");
     variations.add("SportsWagon");
     test.setVariations(variations);
     
     JsonObject json= test.toNGSI();
     
```






```
import through maven and graddle:
```xml
<dependency>
    <groupId>com.github.jmSfernandes</groupId>
    <artifactId>NGSIBaseModel</artifactId>
    <version>1.0.0</version>
</dependency>
```
```json
compile 'com.github.jmSfernandes:NGSIBaseModel:1.0.0'
```

Copyright [2020]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
