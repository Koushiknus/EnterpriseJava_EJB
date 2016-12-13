/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.JsonObject;
import javax.json.Json;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "notes")
@NamedQueries({
    @NamedQuery(name = "Note.getAllNotesByUserId", query = "SELECT n FROM Note n WHERE n.user.userId = :userId ORDER BY n.postedDate DESC"),
    @NamedQuery(name = "Note.getAllNotes", query = "SELECT n FROM Note n ORDER BY n.postedDate DESC")
})
public class Note implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id = 1;
    
    @NotNull
    @JoinColumn(name="userid")
    @ManyToOne
    private User user;
    
    @NotNull
    @JoinColumn(name="catid")
    @ManyToOne
    private Category category;
    
    @NotNull
    @Column(name = "title")
    @Size(max = 255)
    private String title;

    @NotNull
    @Column(name = "content")
    @Size(max = 255)
    private String content;
    
    @NotNull
    @Column(name = "postedDate")
    private Date postedDate;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public JsonObject toJSON() {
        return (Json.createObjectBuilder()
                .add("id", id)
		.add("title", title)
                .add("content", content)
                .add("posted_date", dateToString(postedDate, "dd/MM/yyyy HH:mm:ss"))
		.add("category_name", category.getCatName())
                .add("user_name", user.getUserId())
		.build());
    }
    
    public static String dateToString(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String ret="";
        try {
            ret =sdf.format(date); 
        } catch (Exception e) {
            ret="";
        }
        return ret;
    }
}
