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

        // "first child"
        ConfigurableApplicationContext firstChildCtx = new SpringApplicationBuilder()
                .parent(sharedCtx)
                .sources(ChildConfig.class)
                .build()
                .run();
        assertEquals(sharedDisBean, firstChildCtx.getBean(SharedDisBean.class));
        firstChildCtx.getBean(ChildDisBean.class).doSomethingChildish();
        firstChildCtx.close();

        // "second child"
        ConfigurableApplicationContext secondChildCtx = new SpringApplicationBuilder()
                .parent(sharedCtx)
                .sources(ChildConfig.class)
                .build()
                .run();

        //still the same DisBean
        assertEquals("SharedDisBean in child context should be the same as in shared context", sharedDisBean, secondChildCtx.getBean(SharedDisBean.class));
        assertFalse(sharedDisBean.isDestroyed());
        secondChildCtx.getBean(ChildDisBean.class).doSomethingChildish();

        log.warn("Test finished, now all tests goes down");
    }
}

