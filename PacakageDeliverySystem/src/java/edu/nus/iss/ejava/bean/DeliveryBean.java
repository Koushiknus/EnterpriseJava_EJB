package edu.nus.iss.ejava.bean;

import edu.nus.iss.ejava.business.PackageService;
import edu.nus.iss.ejava.model.Delivery;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@RequestScoped
@ManagedBean
public class DeliveryBean implements Serializable {
    
    @EJB private PackageService pService;
    
    private String name, address, phone;
    
    public void addPackage() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            Delivery delivery = new Delivery();
            delivery.setAddress(address);
            delivery.setCreateDate(new Date());
            delivery.setName(name);
            delivery.setPhone(phone);
            pService.addPackage(delivery);
            pService.addPod(delivery.getId());
            ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PackageService getpService() {
        return pService;
    }

    public void setpService(PackageService pService) {
        this.pService = pService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
