package nz.co.rubz.kiwi;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextStartedEvent;

import nz.co.rubz.kiwi.notify.KiwiNotificationService;
import nz.co.rubz.kiwi.server.KiwiChannelManager;
import nz.co.rubz.kiwi.server.KiwiWebSocketServer;

public class KiwiServer {
    private static Logger log = Logger.getLogger(KiwiServer.class);
    
    public ApplicationContext context;
    
    public KiwiWebSocketServer server;
    
    public KiwiChannelManager cManager;
    
    public KiwiNotificationService notificationService;
    

    
    public void init() {
        context = InitEnvironment.initAppclicatContext();
        InitEnvironment.initLog4j();
//        context.getBean(InitEnvironment.class).initSchedule();
//        server = context.getBean(ClassuWebSocketServer.class);
//        cManager = context.getBean(ClassuChannelManager.class);
//        notificationService = context.getBean(ClassuNotificationService.class);
        context.publishEvent(new ContextStartedEvent(context));
    }
    

    
    
    public static void main(String args[]) {
        KiwiServer service = new KiwiServer();
            service.init();
            log.info("inited environment successed!");
            try {
//            	service.cManager.checkClassuChannel();
//            	service.notificationService.start();
//				service.server.start();
			} catch (Exception e) {
				log.error("start where server error ." ,e);
			} 
    }
    
}
