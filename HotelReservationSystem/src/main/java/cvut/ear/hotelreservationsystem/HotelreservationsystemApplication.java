package cvut.ear.hotelreservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class HotelreservationsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelreservationsystemApplication.class, args);
    }

}
