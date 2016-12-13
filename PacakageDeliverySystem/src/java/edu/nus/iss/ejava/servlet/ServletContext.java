package edu.nus.iss.ejava.servlet;

import edu.nus.iss.ejava.bean.HQService;
import edu.nus.iss.ejava.business.PackageService;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContext implements ServletContextListener{
    @Resource(lookup = "concurrent/ca3") private ManagedScheduledExecutorService executor;
    @EJB private PackageService pService;
    @Override 
    public void contextInitialized(ServletContextEvent sce) {    
        HQService hqService = new HQService(pService.findAllPods());
        executor.scheduleWithFixedDelay(hqService, 0l, 4l, TimeUnit.SECONDS);
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executor.shutdownNow();
    }
}
