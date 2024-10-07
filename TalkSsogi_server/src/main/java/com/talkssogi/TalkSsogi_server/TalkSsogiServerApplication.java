package com.talkssogi.TalkSsogi_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
public class TalkSsogiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkSsogiServerApplication.class, args);
	}

}
