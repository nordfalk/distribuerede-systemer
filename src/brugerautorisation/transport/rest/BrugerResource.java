/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.transport.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 * http://localhost:8080/WebApplicationRestJakob/webresources/ba/jacob
 * @author j
 */
@Path("ba")
public class BrugerResource {

  @Context
  private UriInfo context;

  /**
   * Creates a new instance of GenericResource
   */
  public BrugerResource() {
  }

  /**
   * Retrieves representation of an instance of dist.GenericResource
   * @return an instance of java.lang.String
   */
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public String getXml() {
    //TODO return proper representation object
    return "<?xml><hej/>";
  }


  @Path("jacob")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String hvadsomhelst() {
    //TODO return proper representation object
    return "{\"hurra\":true}";
  }

  /**
   * PUT method for updating or creating an instance of GenericResource
   * @param content representation for the resource
   */
  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public void putXml(String content) {
  }
}
