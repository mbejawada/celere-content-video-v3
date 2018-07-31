package com.sorc.content.elasticsearch.core.searchmodels;

import java.io.Serializable;

public class BaseSearchResultType implements Serializable{
	
	private static final long serialVersionUID = 6237283894018481386L;
	private String type;
	private float score;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}
