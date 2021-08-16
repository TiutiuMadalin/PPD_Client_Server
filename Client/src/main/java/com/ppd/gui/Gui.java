package com.ppd.gui;

import com.ppd.controller.Controller;
import com.ppd.service.IAppServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Gui {
    public static void run(String[] args) throws Exception {
//        launch(args);
        start();
    }

    public static void start() throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IAppServices server = (IAppServices) factory.getBean("appService");

        Controller controller = new Controller();
        controller.setService(server);
    }
}
