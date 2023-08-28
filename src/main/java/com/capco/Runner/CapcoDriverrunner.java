package com.capco.Runner;
import com.capco.base.*;
import com.capco.driver.*;
import io.qameta.allure.Allure;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



public class CapcoDriverrunner {
	public Base base;
	public Properties prop;
	

	
    @BeforeTest
    public void beforetest()  {
    	
    }
    
    
   @Test
    public void test() throws Exception {
        CapcoDriver capcoDriver = new CapcoDriver();
        capcoDriver.test();
    }

}