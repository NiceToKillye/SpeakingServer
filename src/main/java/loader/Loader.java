package loader;

import loader.custom.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class Loader {
    public static void main(String[] args) {
        SpringApplication.run(Loader.class, args);
    }
}