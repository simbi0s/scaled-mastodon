package org.test.mastodon.mconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MconnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MconnectorApplication.class, args);
	}

}
