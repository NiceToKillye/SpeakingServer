package loader.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "application")
public class ConfigProperties {

    private String storage;
    private String examStorage;
    private String variantStorage;

    private String adminEmail;
    private String adminUsername;
    private String adminPassword;
}