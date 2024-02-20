package com.sanhil.model;

public class jwtResponse {
	private String jwtToken;
	private Integer userId;

	jwtResponse(){}

	jwtResponse(String jwtToken,Integer userId){
		this.jwtToken = jwtToken;
		this.userId = userId;
	}

	public String getJwtToken(){
		return this.jwtToken;
	}

	public void setJwtToken(String jwtToken){
		this.jwtToken = jwtToken;
	}

	public Integer getUserId(){
		return this.userId;
	}

	public void setUserId(Integer userId){
		this.userId = userId;
	}
}
