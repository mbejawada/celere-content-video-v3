/**
 * 
 */
package com.sorc.content.services.video.response.status;

import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Response.Status.Family;

/**
 * @author rakesh.moradiya
 *
 */
public class RestrictedResource implements StatusType{
	
	public static final int CODE = 403;
	private String description;
	
	public RestrictedResource(String description){
		this.description = description;
	}
	
	@Override
	public Family getFamily() {
		return Family.CLIENT_ERROR;
	}

	@Override
	public String getReasonPhrase() {
		return description;
	}

	@Override
	public int getStatusCode() {
		return CODE;
	}

}
