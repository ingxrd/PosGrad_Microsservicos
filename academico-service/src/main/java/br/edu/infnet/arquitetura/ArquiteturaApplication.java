package br.edu.infnet.arquitetura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ArquiteturaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArquiteturaApplication.class, args);
    }
}