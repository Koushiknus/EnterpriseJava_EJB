package edu.nus.iss.ejava.bean;

import edu.nus.iss.ejava.business.PackageService;
import edu.nus.iss.ejava.model.Pod;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class HQService implements Runnable{

    private List<Pod> lstPod;
    
    public HQService(List<Pod> lstPod) {
        this.lstPod = lstPod;
    }
    
    @Override
    public void run() {
//        while (true) {
            System.out.println("Running...");
            
            System.out.println("Size..." + lstPod.size());
            for (Pod pod: lstPod) {
                System.out.println("Processing: " + pod.getId());
                Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
                WebTarget webTarget = client.target("http://10.10.0.50:8080/epod/upload");
        
        
                File imageFile = new File("image.jpg");
                FileOutputStream fileOuputStream; 
                    try {
                        fileOuputStream = new FileOutputStream(imageFile);
                        fileOuputStream.write(pod.getImage());
                        fileOuputStream.close();
                    } catch (Exception ex) {
                        Logger.getLogger(HQService.class.getName()).log(Level.SEVERE, null, ex);
                    }


                FileDataBodyPart imgPart = new FileDataBodyPart("image", imageFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
                imgPart.setContentDisposition(FormDataContentDisposition.name("image").fileName(imageFile.getName()).build());

                MultiPart formData = new FormDataMultiPart()
                                    .field("teamId", "874f8a2d", MediaType.TEXT_PLAIN_TYPE)
                                    .field("note", pod.getNote(), MediaType.TEXT_PLAIN_TYPE)
                                    .field("podId", Integer.toString(pod.getId()), MediaType.TEXT_PLAIN_TYPE)
                                    .field("callback", "http://10.10.24.84:8080/epod/callback", MediaType.TEXT_PLAIN_TYPE)
                                    .bodyPart(imgPart);
                formData.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);


                Response response = webTarget.request().post(Entity.entity(formData, formData.getMediaType()));

                System.out.println(response.getStatus() + " " + response.getStatusInfo() + " " + response);
            }
            
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(HQService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
        
    }
}
