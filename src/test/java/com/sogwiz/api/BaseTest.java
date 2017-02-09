package com.sogwiz.api;

import org.slf4j.MDC;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.lang.reflect.Method;

/**
 * Created by sogwiz on 2/8/17.
 */
public class BaseTest {

    public static String MDC_TEST_NAME = "testname";

    @BeforeMethod(alwaysRun=true)
    public void setup(Method testMethod) throws Exception {
        beginTestLogging(this.getClass().getName()+"."+testMethod.getName());
    }

    public void beginTestLogging(String testName) throws Exception {
        MDC.put(MDC_TEST_NAME, testName);
    }

    @AfterMethod(alwaysRun = true)
    public void testCleanUp() throws Exception {
        try {
            //TODO: clean up
        } finally {
            stopTestLogging();
        }
    }

    public void stopTestLogging() {
        String name = MDC.get(MDC_TEST_NAME);
        MDC.remove(MDC.get(MDC_TEST_NAME));
    }


}
