package Model;

import Controller.ControladorProducto;
import View.View;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SiMvJdbcApplication {

	@Autowired
	RepositorioProducto repositorio;

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SiMvJdbcApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			final Log logger = LogFactory.getLog(getClass());
			ControladorProducto controlador = new ControladorProducto(repositorio, new View());
		};
	}

}
