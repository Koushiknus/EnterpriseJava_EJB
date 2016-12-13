package edu.nus.iss.ejava.rest;

import edu.nus.iss.ejava.business.PackageService;
import edu.nus.iss.ejava.model.Delivery;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PodResource {
    @EJB private PackageService pService;
    
    @GET
    public Response getDeliveries(){
        Response.ResponseBuilder builder;
        List<Delivery> lstDelivery = pService.findAllDeliveries();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Delivery delivery: lstDelivery) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("teamId", "874f8a2d");
            objectBuilder.add("podId", delivery.getPod().getId());
            objectBuilder.add("name", delivery.getName());
            objectBuilder.add("address", delivery.getAddress());
            objectBuilder.add("phone", delivery.getPhone());
            arrayBuilder.add(objectBuilder.build());
        }
        builder = (Response.status(Response.Status.OK).entity(arrayBuilder.build()));
	return builder.build();
    }
}
