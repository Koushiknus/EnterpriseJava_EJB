/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.business;

import edu.nus.iss.ejava.model.Category;
import edu.nus.iss.ejava.model.Group;
import edu.nus.iss.ejava.model.Note;
import edu.nus.iss.ejava.model.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class NoteManagementtBean {
    
    private static final String DEFAULT_ROLE = "manage";
    
    @PersistenceContext private EntityManager em;

    public void createUser(final User user) {
        em.persist(user);
        Group group = new Group();
        group.setGroupId(DEFAULT_ROLE);
        group.setUserId(user.getUserId());
        em.persist(group);
    }
    
    public User findUserById(String id) {
        return em.find(User.class, id);
    }
    
    public List<Category> getAllCategories() {
        TypedQuery<Category> query = em.createNamedQuery("Category.getAllCategories", Category.class);
	return query.getResultList();
    }
    
    public Category findCategoryById(Long id) {
        return em.find(Category.class, id);
    }
    
    public void createNote(Note note) {
        em.persist(note);
    }
    
    public List<Note> getAllNotesByUserId(String userId) {
        TypedQuery<Note> query = em.createNamedQuery("Note.getAllNotesByUserId", Note.class);
        query.setParameter("userId", userId);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	return query.getResultList();
    }
    
    public List<Note> getAllNotes() {
        TypedQuery<Note> query = em.createNamedQuery("Note.getAllNotes", Note.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	return query.getResultList();
    }
}
