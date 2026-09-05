package com.abikananda.ipo;

import com.abikananda.ipo.config.EnvContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication @EnableScheduling @EnableAsync @EnableCaching
public class IpoAnalysisApplication {
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(IpoAnalysisApplication.class);
    app.addInitializers(new EnvContextInitializer());
    app.run(args);
  }
}
