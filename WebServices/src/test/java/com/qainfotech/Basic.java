package com.qainfotech;
import static io.restassured.RestAssured.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;


public class Basic {
      private String BASEURI="http://10.0.1.86/snl";
      private String BASEPATH="/rest/v1";
      private String response;
      private Board board;
     
      
      @BeforeClass
      public void setURL(){
    	  System.out.println("dkgetu4iofjsdfkl");
    	  RestAssured.baseURI=BASEURI;
    	  RestAssured.basePath=BASEPATH;
    	  response= given().
    			    when().
    			       get("/board/new.json").
    			    then(). 
    			        assertThat().statusCode(200). 
    			    extract(). 
    			     body().asString();
    	 
    	  int id=JsonPath.read(response, "$.response.board.id");
    	  board=new Board();
    	  board.setId(id);
    		System.out.println("id "+id);
      }
	
      @Test
	public void testForStatusCode(){
    	  response= given().
  			    when().
  			       get("/board/"+board.getId()+".json").
  			    then(). 
  			        assertThat().statusCode(200). 
  			    extract(). 
  			     body().asString();
		//System.out.println(response);
    	  
    	  System.out.println(" ideadkffkefek "+JsonPath.read(response, "$.response.board.id"));
		
	
	}
      @Test
     public void testIsPlayerAdd(){
    	 System.out.println("heheehehehe");
    	 System.out.println("3 :"+board.getId());
      }
}
