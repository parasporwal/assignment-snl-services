package com.qainfotech;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class RestCode {
	
     private String response;
     private static Board board;
     private String players;
     private ValidatableResponse res;
     private String boardDetail;
     private String status;
     
	
	 public RestCode(String baseUri, String basePath){
		RestAssured.baseURI=baseUri;
		RestAssured.basePath=basePath;
	 }
	 
	 
	 
	 public Board initializeBoard(){
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
 	  System.out.println(board);
 	  return board; 
	 }
	
	 
	 public int getPlayerNumbers(long boardId){

			this.response=get("/board/"+board.getId()+".json").asString();
			System.out.println(response);
			
			int playerSize=JsonPath.read(response,"$.response.board.players.length()" ); 
			return playerSize;
	 }
	 public void addPlayer(JSONObject json)throws MaxPlayerReachedException{
		
		
		this.response=get("/board/"+board.getId()+".json").asString();
		System.out.println(response);
		
		int playerSize=JsonPath.read(response,"$.response.board.players.length()" ); 
		if(playerSize>=4){
			throw new MaxPlayerReachedException("Can't add players, four players are already registered to this board");
		}
		response= given().
    			 contentType(ContentType.JSON).
    			 body(json.toString()).
   			    when().
   			       post("/player.json").
   			    then(). 
   			        assertThat().statusCode(200). 
   			    extract(). 
   			     body().asString();
    	
	}
	 
	 public String getBoardDetailsFromService(Long boardId){
		 response=get("/board/"+board.getId()+".json").asString();
		 return response;
	 }



	public void deleteBoard(long id) {
		// TODO Auto-generated method stub
		res=given(). 
				when(). 
				delete("board/"+id+".json"). 
				then(). 
				assertThat().statusCode(200);
		
		
	}



	public JSONArray getPlayers() {
		// TODO Auto-generated method stub
		
		response=getBoardDetailsFromService(board.getId());
		System.out.println("KMKJK<<>"+response);
		JSONArray players=new JSONArray(JsonPath.read(response, "$.response.board.players").toString());
		return players;
	}



	public void deletePlayer(long id) {
		// TODO Auto-generated method stub
		status=given(). 
		when(). 
		delete("/player/"+id+".json"). 
		then(). 
		assertThat().statusCode(200). 
		extract().body().asString();
		response=getBoardDetailsFromService(board.getId());
		System.out.println("Board Details After Deleting Player"+id+response);
	}
	
	

}
