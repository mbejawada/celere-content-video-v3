/**
 * 
 */
package com.sorc.content.services.video.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sorc.content.core.dao.IDataAccess;
import com.sorc.content.elasticsearch.core.dao.IElasticSearchDataAccess;

/**
 * @author rakesh.moradiya
 *
 */
@Path("/healthcheck")
@Service
public class VideoHealthCheck {

	@Autowired
	private IElasticSearchDataAccess dao;

	@SuppressWarnings("rawtypes")
	public IDataAccess getDao() {
		return dao;
	}
	
	@GET
	public  Response healthcheck(){
	  dao =  (IElasticSearchDataAccess) getDao();
	  return  dao.healthCheck() ? Response.ok().build() : Response.serverError().build();
	}
}
