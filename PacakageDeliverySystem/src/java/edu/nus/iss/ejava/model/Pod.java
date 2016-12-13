/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.ejava.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pod")
@NamedQueries({
    @NamedQuery(name = "Pod.findAllPods", query = "SELECT p FROM Pod p WHERE p.ackId = null AND p.image IS NOT NULL")
})
public class Pod implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "pod_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "note")
    private String note;
    
    @Column(name = "image")
    @Lob
    private byte[] image;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pkg_id", referencedColumnName = "pkg_id")
    private Delivery delivery;
    
    @NotNull
    @Column(name = "delivery_date")
    private Date deliveryDate;
    
    @Column(name = "ack_id")
    private String ackId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAckId() {
        return ackId;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }
}
