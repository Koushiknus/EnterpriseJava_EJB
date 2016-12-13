package edu.nus.iss.ejava.bean;

import edu.nus.iss.ejava.business.NoteManagementtBean;
import edu.nus.iss.ejava.model.Category;
import edu.nus.iss.ejava.model.Note;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
@ManagedBean
public class NoteBean implements Serializable {
    
    @EJB private NoteManagementtBean noteBean;
    private NoteEventSocketClient client;
    
    private String title, content;
    private Long categoryId;
    
    private List<Category> categoryList;
    private List<JsonObject> noteList;
    private List<JsonObject> allNotes;
    
    public void createNote() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            Note note = new Note();
            note.setCategory(noteBean.findCategoryById(categoryId));
            note.setContent(content);
            note.setPostedDate(new Date());
            note.setTitle(title);
            note.setUser(noteBean.findUserById(ec.getRemoteUser()));
            noteBean.createNote(note);
            sendMessageOverSocket(note.toJSON().toString());
            ec.redirect(ec.getRequestContextPath() + "/faces/manage/postednotes.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(NoteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendMessageOverSocket(String message) {
        if (client == null) {
            try {
                initializeWebSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.sendMessage(message);
    }
    
    private void initializeWebSocket() throws Exception {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        client = new NoteEventSocketClient(new URI("ws://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/notews/all"));
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public NoteManagementtBean getNoteBean() {
        return noteBean;
    }

    public void setNoteBean(NoteManagementtBean noteBean) {
        this.noteBean = noteBean;
    }

    public List<Category> getCategoryList() {
        return noteBean.getAllCategories();
    }

    public List<JsonObject> getNoteList() {
        List<Note> lstNote = noteBean.getAllNotesByUserId(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        List<JsonObject> lst = new ArrayList<>();
        for (Note note: lstNote) {
            lst.add(note.toJSON());
        }
        return lst;
    }
    public List<JsonObject> getAllNotes() {
        List<Note> lstNote = noteBean.getAllNotes();
        List<JsonObject> lst = new ArrayList<>();
        for (Note note: lstNote) {
            lst.add(note.toJSON());
        }
        return lst;
    }
    
}
