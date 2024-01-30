package hu.project.groupproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "hu.project.groupproject")
public class GroupprojectApplication {
	

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GroupprojectApplication.class);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);
	}


}


