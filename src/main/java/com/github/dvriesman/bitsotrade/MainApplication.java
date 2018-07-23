package com.github.dvriesman.bitsotrade;

import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import com.github.dvriesman.bitsotrade.service.websocket.WebsocketEndpoint;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
public class MainApplication extends Application {

    private ConfigurableApplicationContext context;
    private Parent rootNode;

    @Bean
    public RestClientFacade restClientFacade() {
        return new RestClientFacade();
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dvriesman/bitsotrade/view/Application.fxml"));
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        //WebsocketEndpoint.startWebSocket();

        primaryStage.setTitle("Bitsontrade");
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) { launch(args); }
}
