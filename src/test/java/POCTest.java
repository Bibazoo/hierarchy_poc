import shared.SharedDisBean;
import shared.SharedConfig;
import child.ChildConfig;
import child.ChildDisBean;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Log4j2
public class POCTest {

    @Test
    public void should_shareDisposableBean_and_shouldNot_close_it() {
        ConfigurableApplicationContext sharedCtx = new SpringApplicationBuilder()
                .sources(SharedConfig.class)
                .build()
                .run();
        SharedDisBean sharedDisBean = sharedCtx.getBean(SharedDisBean.class);
        sharedDisBean.doSomething();

        // first child ctx
        ConfigurableApplicationContext firstChildCtx = new SpringApplicationBuilder()
                .parent(sharedCtx)
                .sources(ChildConfig.class)
                .build()
                .run();

        assertEquals("SharedDisBean in child context should be the same as in shared context", sharedDisBean, firstChildCtx.getBean(SharedDisBean.class));
        ChildDisBean firstChildDisBean = firstChildCtx.getBean(ChildDisBean.class);
        firstChildDisBean.doSomethingChildish();

        firstChildCtx.close();
        assertTrue("Child ctx closed, so all disposable beans in this context should be closed", firstChildDisBean.isDestroyed());

        // second child ctx
        ConfigurableApplicationContext secondChildCtx = new SpringApplicationBuilder()
                .parent(sharedCtx)
                .sources(ChildConfig.class)
                .build()
                .run();

        //still the same DisBean
        assertEquals("SharedDisBean in child context should be the same as in shared context", sharedDisBean, secondChildCtx.getBean(SharedDisBean.class));
        assertFalse(sharedDisBean.isDestroyed());
        secondChildCtx.getBean(ChildDisBean.class).doSomethingChildish();

        log.warn("Test finished, now all contexts goes down");
    }
}

