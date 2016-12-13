package edu.nus.iss.ejava.websocket;

import edu.nus.iss.ejava.bean.NoteWSCatBean;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.PathParam;

@RequestScoped
@ServerEndpoint("/notews/{cats}")
public class NoteWebSocket {
    
    @Resource(lookup = "concurrent/notews")
    private ManagedExecutorService service;
    @PathParam("cats")
    private String catName;
    @Inject
    private NoteWSCatBean noteCats;
    
    @OnOpen
    public void open(Session session) {
        noteCats.lock(() -> {noteCats.add(catName, session);});
    }
    
    @OnMessage
    public void message(Session session, String msg) {
        service.submit(() -> {
            noteCats.lock(() -> {noteCats.broadcast(catName, msg);});
        });
    }
    
    @OnClose
    public void close(Session session) {
//        if (noteCats != null) {
//            noteCats.lock(() -> {
//                noteCats.remove(catName, session);
//            });  
//        }
    }
}
