package com.iut.banque.test.selenium;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.HashMap;
import java.util.Map;

public class TestVerificationAffichageLogo {
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
    public void testLogoPresence() {
        driver.get("http://localhost:8081/_00_ASBank2023/");

        WebElement logo = driver.findElement(By.xpath("//img[@alt='logo']"));
        assertNotNull("Le logo n'est pas trouvé dans le DOM !", logo);
        System.out.println("L'image du logo est bien trouvée dans le DOM.");

        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", logo);
        assertTrue("L'image ne s'affiche pas !", naturalWidth > 0);
        System.out.println("L'image du logo est bien affichée !");
    }
}