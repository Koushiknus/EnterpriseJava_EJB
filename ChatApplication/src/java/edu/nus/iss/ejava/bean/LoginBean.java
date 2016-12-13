package edu.nus.iss.ejava.bean;

import edu.nus.iss.ejava.business.NoteManagementtBean;
import edu.nus.iss.ejava.model.User;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
@ManagedBean
public class LoginBean implements Serializable {
    
    @EJB private NoteManagementtBean noteBean;
    
    public void logout() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ((HttpServletRequest) ec.getRequest()).logout();
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
        } catch (IOException | ServletException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String username, password;
    public void register() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (noteBean.findUserById(username) == null) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes("UTF-8"));
                byte[] digest = md.digest();
                BigInteger bigInt = new BigInteger(1, digest);
                password = bigInt.toString(16);
                
                User user = new User();
                user.setUserId(username);
                user.setPassword(password);
                noteBean.createUser(user);
                ExternalContext ec = fc.getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
            } else {
                fc.addMessage(null, new FacesMessage("User already existed!"));
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
