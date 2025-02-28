package com.iut.banque.test.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.util.HashMap;
import java.util.Map;

public class TestUserConnected {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testUserIsConnected() {
        driver.get("http://localhost:8081/_00_ASBank2023/");

        driver.findElement(By.linkText("Page de Login")).click();
        driver.findElement(By.id("controller_Connect_login_action_userCde")).click();
        driver.findElement(By.id("controller_Connect_login_action_userCde")).sendKeys("client1");
        driver.findElement(By.id("controller_Connect_login_action_userPwd")).click();
        driver.findElement(By.id("controller_Connect_login_action_userPwd")).sendKeys("clientpass1");
        driver.findElement(By.id("controller_Connect_login_action_submit")).click();

        {
            String expectedValue = "Logout";
            String value = driver.findElement(By.id("logout_Retour")).getAttribute("value");
            Assert.assertEquals(value, expectedValue);
        }

    }
}
