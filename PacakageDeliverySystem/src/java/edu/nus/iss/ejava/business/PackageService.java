/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.business;

import edu.nus.iss.ejava.model.Delivery;
import edu.nus.iss.ejava.model.Pod;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class PackageService {
    
    @PersistenceContext private EntityManager em;

    public void addPackage(final Delivery delivery) {
        em.persist(delivery);
        em.flush();
    }
    
    public void addPod(Integer did) {
        em.createNativeQuery("INSERT INTO pod(pkg_id) VALUES("+did+")").executeUpdate();
        em.flush();
    }
    
    public List<Delivery> findAllDeliveries() {
        TypedQuery<Delivery> query = em.createNamedQuery("Delivery.findAllDeliveries", Delivery.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	return query.getResultList();
    }
    
    public Pod findPodById(Integer podId) {
        return em.find(Pod.class, podId);
    }
    
    public void updatePod(Pod pod) {
        em.merge(pod);
    }
    
    public List<Pod> findAllPods() {
        TypedQuery<Pod> query = em.createNamedQuery("Pod.findAllPods", Pod.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	return query.getResultList();
    }
}
