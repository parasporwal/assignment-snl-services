package com.qainfotech;

public class Board {
	
	private long Id;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	@Override
	public String toString() {
		return "Board [Id=" + Id + "]";
	}
	

}
