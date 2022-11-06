package com.droptheclothes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DroptheClothesApplication {

  public static void main(String[] args) {
    SpringApplication.run(DroptheClothesApplication.class, args);
  }

}
