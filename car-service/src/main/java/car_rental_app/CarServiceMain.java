package car_rental_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class CarServiceMain {

    /**
     * Bootstrap the CarServiceMain Spring Boot application.
     *
     * @param args command-line arguments passed through to the Spring application
     */
    public static void main(String [] args){

        SpringApplication.run(CarServiceMain.class, args);
    }
}
