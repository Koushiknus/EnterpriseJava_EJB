/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.business;

import edu.nus.iss.ejava.model.People;
import edu.nus.iss.ejava.model.Appointment;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class PeopleManagementBean {
    
    @PersistenceContext private EntityManager em;

    public void addPeople(final People people) {
        em.persist(people);
    }
    
    public People getPeopleByEmail(final String email) {
        TypedQuery<People> query = em.createNamedQuery("People.findPeopleByEmail", People.class);
        query.setParameter("email", email);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<People> lstPeople = query.getResultList();
        if (lstPeople!=null && !lstPeople.isEmpty()) {
            return lstPeople.get(0);
        }
	return null;
    }
    
    public boolean isPeopleExist(final String email) {
        People people = getPeopleByEmail(email);
	return (people!=null);
    }
    
    public void addAppointment(final Appointment appointment) {
        em.persist(appointment);
    }
    
    public List<Appointment> findAppointmentByPeople(String email) {
        People people = getPeopleByEmail(email);
        if (people != null) {
            return people.getLstAppointment();
        }
        
        return null;
    }
}
