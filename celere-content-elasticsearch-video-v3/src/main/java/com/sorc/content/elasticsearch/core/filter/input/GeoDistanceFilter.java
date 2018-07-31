package com.sorc.content.elasticsearch.core.filter.input;

public class GeoDistanceFilter implements IElasticSearchFilter {

	private String field;
	private Double lat;
	private Double lon;
	private String distance;
	
	public GeoDistanceFilter(String field, Double lat, Double lon, String distance) {
		this.field = field;
		this.lat = lat;
		this.lon = lon;
		this.distance = distance;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
}
