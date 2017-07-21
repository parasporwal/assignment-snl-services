package com.qainfotech;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import Exceptions.GeneralException;
import Exceptions.PlayerHasSameNameException;
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

	public RestCode(String baseUri, String basePath) {
		RestAssured.baseURI = baseUri;
		RestAssured.basePath = basePath;
	}

	public Board initializeBoard() throws GeneralException {
		response = given().when().get("/board/new.json").then().assertThat().statusCode(200).extract().body()
				.asString();

		if (getStatusCode(response) == 1) {

			int id = JsonPath.read(response, "$.response.board.id");
			board = new Board();
			board.setId(id);
			System.out.println(board);
			return board;
		} else {
			throw new GeneralException("OOPS ! Something Went Wrong");
		}

	}

	public int getPlayerNumbers(long boardId) {

		this.response = get("/board/" + board.getId() + ".json").asString();
		System.out.println(response);

		int playerSize = JsonPath.read(response, "$.response.board.players.length()");
		return playerSize;
	}

	public void addPlayer(JSONObject json) throws MaxPlayerReachedException, PlayerHasSameNameException {

		String givenPlayerName = json.getJSONObject("player").getString("name");
		this.response = get("/board/" + board.getId() + ".json").asString();
		System.out.println(response);

		int playerSize = getPlayerNumbers(board.getId());
		if (playerSize >= 4) {
			throw new MaxPlayerReachedException("Can't add players, four players are already registered to this board");
		}
		/*
		 * code to prevent user from hitting service if he tries to register
		 * player with same names
		 */
		/*
		 * JSONArray playerNames=getPlayerNames(); List<Object>
		 * Names=playerNames.toList(); ListIterator<Object>
		 * listItr=Names.listIterator(); while(listItr.hasNext()){ String
		 * name=listItr.next().toString();
		 * if(name.equalsIgnoreCase(givenPlayerName)){ throw new
		 * PlayerHasSameNameException("One Player has already same name"); }
		 * 
		 * }
		 */
		status = given().contentType(ContentType.JSON).body(json.toString()).when().post("/player.json").then()
				.extract().body().asString();

		if (getStatusCode(status) == -1) {
			throw new PlayerHasSameNameException("One Player has already same name");
		}
		else{
			System.out.println("New Player \""+givenPlayerName+" \"has been added successfully");
		}

	}

	public String getBoardDetailsFromService(Long boardId) {
		response = get("/board/" + board.getId() + ".json").asString();
		return response;
	}

	public void deleteBoard(long id) {
		// TODO Auto-generated method stub
		status = given().when().delete("board/" + id + ".json").then().extract().body().asString();
		if(getStatusCode(status)==1){
			System.out.println("board has been deleted successfully");
		}
		else{
			System.out.println(" board hasn't been cleared");
		}
	}

	public int getStatusCode(String status) {
		return Integer.parseInt((JsonPath.read(status, "$.response.status")).toString());
	}

	public JSONArray getPlayers() {
		// TODO Auto-generated method stub

		response = getBoardDetailsFromService(board.getId());
		JSONArray players = new JSONArray(JsonPath.read(response, "$.response.board.players").toString());
		return players;
	}

	public JSONArray getPlayerNames() {
		
		JSONArray playerNames = new JSONArray(JsonPath.read(response, "$.response.board.players[*].name").toString());
		return playerNames;

	}

	public void deletePlayer(long id) {
		// TODO Auto-generated method stub
		status = given().when().delete("/player/" + id + ".json").then().assertThat().statusCode(200).extract().body()
				.asString();
		response = getBoardDetailsFromService(board.getId());
		System.out.println("Board Details After Deleting Player" + id + response);
	}

}
