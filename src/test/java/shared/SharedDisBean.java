package shared;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Log4j2
public class SharedDisBean implements DisposableBean, InitializingBean {

    @Getter
    boolean isDestroyed = false;

    /**
     * Some internal state to make it more interesting
     */
    private int internalState = 0;

    public synchronized int doSomething() {
        if(!isDestroyed){
            int result = ++internalState;
            log.info("I'm [{}] so shared and was used {} times", this, result);
            return result;
        }
        else
            throw new IllegalStateException("Sorry, I'm destroyed already");
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

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("{} initialized", this.getClass());
    }
}
