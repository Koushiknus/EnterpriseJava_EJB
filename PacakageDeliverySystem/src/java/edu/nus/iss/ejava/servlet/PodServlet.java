package edu.nus.iss.ejava.servlet;

import edu.nus.iss.ejava.business.PackageService;
import edu.nus.iss.ejava.model.Pod;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

@WebServlet("/upload")
@MultipartConfig
public class PodServlet extends HttpServlet {
    
    @EJB private PackageService pService;
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        byte[] image = readPart(req.getPart("image"));
        Integer podId = new Integer(req.getParameter("podId"));
        String note = req.getParameter("note");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(req.getParameter("time")));
        Date time = cal.getTime();
        
        Pod pod = pService.findPodById(podId);
        if (pod != null) {
            pod.setDeliveryDate(time);
            pod.setImage(image);
            pod.setNote(note);
            pService.updatePod(pod);
            
            
            Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            WebTarget webTarget = client.target("http://10.10.0.50:8080/epod/upload");
        
        
            File imageFile = new File("image.jpg");
            FileOutputStream fileOuputStream = new FileOutputStream(imageFile); 
            fileOuputStream.write(image);
            fileOuputStream.close();
        
            
            
            FileDataBodyPart imgPart = new FileDataBodyPart("image", imageFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            imgPart.setContentDisposition(FormDataContentDisposition.name("image").fileName(imageFile.getName()).build());

            MultiPart formData = new FormDataMultiPart()
				.field("teamId", "874f8a2d", MediaType.TEXT_PLAIN_TYPE)
				.field("note", note, MediaType.TEXT_PLAIN_TYPE)
				.field("podId", Integer.toString(podId), MediaType.TEXT_PLAIN_TYPE)
                                .field("callback", "http://10.10.24.84:8080/epod/callback", MediaType.TEXT_PLAIN_TYPE)
				.bodyPart(imgPart);
            formData.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
            
            
            Response response = webTarget.request().post(Entity.entity(formData, formData.getMediaType()));

            System.out.println(response.getStatus() + " " + response.getStatusInfo() + " " + response);
        }
    }
    
    private byte[] readPart(Part p) throws IOException {
        byte[] buffer = new byte[1024 * 8];
        int sz = 0;
        try (InputStream is = p.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                while (-1 != (sz = bis.read(buffer))) {
                    baos.write(buffer, 0, sz);
                }
                buffer = baos.toByteArray();
            }
        }
        return (buffer);
    }
}
