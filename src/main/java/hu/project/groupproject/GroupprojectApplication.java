package hu.project.groupproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @ComponentScan(basePackages = "hu.project.groupproject")
@EnableJpaRepositories(basePackages = "hu.project.groupproject")
public class GroupprojectApplication {
	

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GroupprojectApplication.class);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);
	}


}




// try(AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext()){
// // new AnnotationConfigWebApplicationContext(GroupprojectApplication.class,RepoConfig.class)){
// 	// SpringApplication.run(GroupprojectApplication.class, args);
// 	ctx.scan("hu.project.groupproject");
// 	ctx.register(RepoConfig.class);
// 	ctx.refresh();
// 	MyService s = ctx.getBean(MyService.class);
// 	s.doWork();
// }
// AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(GroupprojectApplication.class, RepoConfig.class);
// // SpringApplication.run(GroupprojectApplication.class, args);
// MyService s = ctx.getBean(MyService.class);
// s.doWork();