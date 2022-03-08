package loader.custom;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {

    private final ConfigProperties properties;

    public ResourceConfiguration(ConfigProperties properties){
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userDir = System.getProperty("user.dir");
        registry.addResourceHandler(userDir + "/" + properties.getStorage() + "/**")
                .addResourceLocations("file:" + userDir + "/" + properties.getStorage()  + "/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
