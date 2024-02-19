package Growup.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling // 스케줄링 기능을 enable 함
@SpringBootApplication
@EnableJpaAuditing
public class GrowupApplication {
	public static void main(String[] args) {
		SpringApplication.run(GrowupApplication.class, args);
	}

}
