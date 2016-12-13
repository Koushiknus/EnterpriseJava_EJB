package edu.nus.iss.ejava.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.websocket.Session;

@ApplicationScoped
public class NoteWSCatBean implements Serializable {
    private final Lock lock = new ReentrantLock();
    private Map<String, List<Session>> cats = new HashMap<String, List<Session>>();
    
    public void add(String cat, Session session) {
        List<Session> allSession = cats.computeIfAbsent(cat, s -> new LinkedList<>());
        allSession.add(session);
    }
    
    public void broadcast(String cat, String text) {
        final String message = Json.createObjectBuilder().add("message", text).build().toString();
        cats.get(cat).stream().forEach(s -> {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException ex) {
            ex.printStackTrace();
            }
        });
    }
    
    public void remove(String cat, Session session) {
        
    }
    public void lock(Runnable block) {
        lock.lock();
        try {
            block.run();
        } finally {
            lock.unlock();
        }
    }
}
