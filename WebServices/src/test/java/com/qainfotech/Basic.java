package com.qainfotech;

import static io.restassured.RestAssured.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



public class Basic {

	private String response;
	private  Board board;
	private int playerId;
	private RestCode restCode;
	private String BASEURI = "http://10.0.1.86/snl";
	private String BASEPATH = "/rest/v1/";

	@BeforeClass
	public void setURL() {
		restCode = new RestCode(BASEURI, BASEPATH);
		board = restCode.initializeBoard();
		System.out.println(board);
       
		
	}
     @Test
	 public void getBoardDetailsFromService(){
		System.out.println(restCode.getBoardDetailsFromService(8483l));
	 }
	@Test
	public void testIsPlayerAdd() {
		System.out.println("board : "+board);
     int initalPlayerNo=restCode.getPlayerNumbers(board.getId());
  
  	 JSONObject jsObj=new JSONObject();
   	 jsObj.put("board", board.getId());
   	 JSONObject player=new JSONObject();
   	 player.put("name", "player");
   	 jsObj.put("player",player );
		
   	 restCode.addPlayer(jsObj);
		
     Assert.assertEquals(restCode.getPlayerNumbers(board.getId()), initalPlayerNo+1);
	}
  	
  	@Test
  	public void testMaxPlayer(){
  		for (int playerIndex = 0; playerIndex <4; playerIndex++) {
			 JSONObject jsObj=new JSONObject();
	    	 jsObj.put("board", board.getId());
	    	 JSONObject player=new JSONObject();
	    	 player.put("name", "player" + playerIndex);
	    	 jsObj.put("player",player );
			 
	    	 restCode.addPlayer(jsObj);

		}
  		
  		System.out.println();
  	}
  	@Test
  	public void testIsPlayerDeleted(){
  		testMaxPlayer();
  		JSONArray players=restCode.getPlayers();
  		System.out.println("sdkfejkfwjekfj");
  		for(int playerNo=0;playerNo<players.length();playerNo++){

  			JSONObject player=players.getJSONObject(playerNo);
  			//System.out.println("id :"+player.getLong("id"));
  			restCode.deletePlayer(player.getLong("id"));
  		}
  		Assert.assertEquals(restCode.getPlayerNumbers(board.getId()), 0);
  	}
  	@AfterClass
  	public void deleteBoard(){
  		
  		restCode.deleteBoard(board.getId());
  		board=null;
  		
  	}
  	
  
}
