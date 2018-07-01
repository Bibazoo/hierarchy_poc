package shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedConfig {
    @Bean
    public SharedDisBean disposableBeanImpl() {
        return new SharedDisBean();
    }
}
