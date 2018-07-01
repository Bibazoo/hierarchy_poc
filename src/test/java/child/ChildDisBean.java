package child;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import shared.SharedDisBean;

/**
 * Implements DisposableBean to check cascade dependency destroying
 */
@Log4j2
public class ChildDisBean implements DisposableBean  {

    private SharedDisBean impl;

    @Getter
    boolean isDestroyed = false;


    public ChildDisBean(SharedDisBean impl) {
        this.impl = impl;
    }

    public void doSomethingChildish(){
        int sharedBeanResult = impl.doSomething();
        log.info("Hi! Look at me! I'm using shared bean: {}. Result: {}", impl, sharedBeanResult);
    }

    @Override
    public void destroy() throws Exception {
        if(isDestroyed)
            throw new IllegalStateException("Sorry, I'm destroyed already");
        else
        {
            log.info("{} destroy() invoked", this.getClass());
            isDestroyed = true;
        }
    }

}
