/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.model;

import java.io.Serializable;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Id
    @Column(name = "appt_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer apptId = 1;
    
    @NotNull
    @Column(name = "description")
    private String description;
    
    @Column(name = "appt_date")
    private Date apptDate;
    
    @NotNull
    @JoinColumn(name="pid")
    @ManyToOne
    private People people;
    
    public Integer getApptId() {
        return apptId;
    }

    public void setApptId(Integer apptId) {
        this.apptId = apptId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getApptDate() {
        return apptDate;
    }

    public void setApptDate(Date apptDate) {
        this.apptDate = apptDate;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }
    
    public JsonObject toJSON() {
        return (Json.createObjectBuilder()
                .add("appointmentId", apptId)
		.add("dateTime", apptDate.getTime())
		.add("description", description)
                .add("personId", people.getpId())
		.build());
    }
}
