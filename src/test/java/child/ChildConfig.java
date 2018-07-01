package child;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shared.SharedDisBean;

@Configuration
public class ChildConfig {

    @Bean
    @Autowired
    public ChildDisBean disposableBeanLover(SharedDisBean impl){
        return new ChildDisBean(impl);
    }
}
