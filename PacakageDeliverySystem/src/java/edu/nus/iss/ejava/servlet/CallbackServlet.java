package edu.nus.iss.ejava.servlet;

import edu.nus.iss.ejava.business.PackageService;
import edu.nus.iss.ejava.model.Pod;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/callback")
public class CallbackServlet extends HttpServlet {
    
    @EJB private PackageService pService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Integer podId = new Integer(req.getParameter("podId"));
        String ackId = req.getParameter("ackId");
        
        Pod pod = pService.findPodById(podId);
        if (pod != null) {
            pod.setAckId(ackId);
            pService.updatePod(pod);
        }
    }
}
