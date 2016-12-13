/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.model;

import java.io.Serializable;
import java.util.List;
import javax.json.JsonObject;
import javax.json.Json;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "people")
@NamedQueries({
    @NamedQuery(name = "People.findPeopleByEmail", query = "SELECT p FROM People p WHERE p.email = :email")
})
public class People implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Id
    @Basic(optional = false)
    @Column(name = "pid")
    private String pId;
    
    @NotNull
    @Column(name = "name")
    @Size(max = 128)
    private String name;
           
    @NotNull
    @Column(name = "email")
    @Size(max = 128)
    private String email;

    @OneToMany(mappedBy = "people")
    private List<Appointment> lstAppointment;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Appointment> getLstAppointment() {
        return lstAppointment;
    }

    public void setLstAppointment(List<Appointment> lstAppointment) {
        this.lstAppointment = lstAppointment;
    }
    
    public JsonObject toJSON() {
        return (Json.createObjectBuilder()
                .add("pId", pId)
		.add("name", name)
		.add("email", email)
		.build());
    }
}
