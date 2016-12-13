/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.rest;

import edu.nus.iss.ejava.business.PeopleManagementBean;
import edu.nus.iss.ejava.model.Appointment;
import edu.nus.iss.ejava.model.People;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagePeopleResource {
    @EJB private PeopleManagementBean peopleBean;
    
    @POST
    @Path("/add")
    public Response addPeople(final JsonObject peopleForm) {
        Response.ResponseBuilder builder;
        if (peopleBean.isPeopleExist(peopleForm.getString("email"))) {
            JsonObject json = Json.createObjectBuilder()
                            .add("msg", "The email "+peopleForm.getString("email")+" already registered.")
                            .build();
            builder = Response.status(Response.Status.CONFLICT).entity(json);
        } else {
            People people = new People();
            String pId = UUID.randomUUID().toString();
            people.setpId(pId.substring(0, pId.indexOf("-")));
            people.setEmail(peopleForm.getString("email"));
            people.setName(peopleForm.getString("name"));
            peopleBean.addPeople(people);
            builder = Response.status(Response.Status.CREATED);
        }
	return builder.build();
    }
    
    @POST
    @Path("/appointment/add")
    public Response addAppointment(final JsonObject apptForm) {
	Response.ResponseBuilder builder;
        if (!peopleBean.isPeopleExist(apptForm.getString("email"))) {
            JsonObject json = Json.createObjectBuilder()
                            .add("msg", "The user with email "+apptForm.getString("email")+" haven't registered.")
                            .build();
            builder = Response.status(Response.Status.NOT_FOUND).entity(json);
        } else {
            People people = peopleBean.getPeopleByEmail(apptForm.getString("email"));
            Appointment app = new Appointment();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(apptForm.getString("appt-date"));
                app.setApptDate(date);
            } catch(Exception e){e.printStackTrace();}
            app.setDescription(apptForm.getString("description"));
            app.setPeople(people);
            peopleBean.addAppointment(app);
            builder = Response.status(Response.Status.CREATED);
        }
	return builder.build();
    }
    
    @POST
    @Path("/appointment")
    public Response getAllAppointments(final JsonObject form) {
	Response.ResponseBuilder builder;
        if (!peopleBean.isPeopleExist(form.getString("email"))) {
            JsonObject json = Json.createObjectBuilder()
                            .add("msg", "The user with email "+form.getString("email")+" haven't registered.")
                            .build();
            builder = Response.status(Response.Status.NOT_FOUND).entity(json);
        } else {
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            List<Appointment> listAppt = peopleBean.findAppointmentByPeople(form.getString("email"));
            listAppt.forEach((appt) -> {
                arrBuilder.add(appt.toJSON());
            });
            builder = (Response.status(Response.Status.OK).entity(arrBuilder.build()));
        }
        return builder.build();
    }
}
