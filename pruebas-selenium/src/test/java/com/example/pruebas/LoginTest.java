package com.example.pruebas;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;
    private String screenshotsPath = "/app/screenshots";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        baseUrl = System.getenv("SITIO_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://localhost:8080";
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        new File(screenshotsPath).mkdirs();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Prueba de login exitoso")
    void testLoginExitoso() throws IOException {
        driver.get(baseUrl);
        takeScreenshot("01_pagina_inicial");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("admin");
        passwordField.sendKeys("password");
        takeScreenshot("02_formulario_completado");

        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        takeScreenshot("03_login_exitoso");

        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class, 'alert-success')]")));
        
        Assertions.assertTrue(successMessage.getText().contains("¡Login exitoso!"));
        System.out.println("✓ Login exitoso completado correctamente");
    }

    @Test
    @DisplayName("Prueba de login fallido - usuario incorrecto")
    void testLoginFallidoUsuario() throws IOException {
        driver.get(baseUrl);
        takeScreenshot("04_inicio_login_fallido_usuario");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("usuario_incorrecto");
        passwordField.sendKeys("password");
        takeScreenshot("05_datos_incorrectos_usuario");

        loginButton.click();

        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class, 'alert-danger')]")));
        takeScreenshot("06_error_usuario_incorrecto");

        Assertions.assertTrue(errorMessage.getText().contains("Usuario o contraseña incorrectos"));
        System.out.println("✓ Error de usuario incorrecto mostrado correctamente");
    }

    @Test
    @DisplayName("Prueba de login fallido - contraseña incorrecta")
    void testLoginFallidoPassword() throws IOException {
        driver.get(baseUrl);
        takeScreenshot("07_inicio_login_fallido_password");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("admin");
        passwordField.sendKeys("password_incorrecto");
        takeScreenshot("08_datos_incorrectos_password");

        loginButton.click();

        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class, 'alert-danger')]")));
        takeScreenshot("09_error_password_incorrecto");

        Assertions.assertTrue(errorMessage.getText().contains("Usuario o contraseña incorrectos"));
        System.out.println("✓ Error de contraseña incorrecta mostrado correctamente");
    }

    @Test
    @DisplayName("Prueba de campos vacíos")
    void testCamposVacios() throws IOException {
        driver.get(baseUrl);
        takeScreenshot("10_inicio_campos_vacios");

        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("button[type='submit']")));

        loginButton.click();
        takeScreenshot("11_intento_envio_campos_vacios");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));

        String usernameValidation = usernameField.getAttribute("validationMessage");
        String passwordValidation = passwordField.getAttribute("validationMessage");

        Assertions.assertTrue(!usernameValidation.isEmpty() || !passwordValidation.isEmpty());
        System.out.println("✓ Validación de campos requeridos funcionando correctamente");
    }

    private void takeScreenshot(String testName) throws IOException {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(screenshotsPath + "/" + testName + ".png");
        FileUtils.copyFile(sourceFile, destFile);
        System.out.println("Screenshot guardado: " + destFile.getAbsolutePath());
    }
}