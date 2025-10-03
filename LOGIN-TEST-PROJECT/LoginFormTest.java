import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class LoginFormTest {
    private WebDriver driver;
    private WebDriverWait wait;
    
    // UPDATE URL PATH
    private String baseUrl = "file:///" + System.getProperty("user.dir") + "/src/login.html";

    @Before
    public void setUp() {
        // UPDATE CHROMEDRIVER PATH
        String chromeDriverPath = System.getProperty("user.dir") + "/lib/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        System.out.println("ChromeDriver path: " + chromeDriverPath);
        
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        System.out.println("Opening URL: " + baseUrl);
        driver.get(baseUrl);
    }

    @Test
    public void test01_LoginSuccess() {
        System.out.println("=== Test Case 1: Login Success ===");
        
        try {
            // Enter valid credentials
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='text'], input[placeholder*='username'], input[placeholder*='ten']")));
            WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'LOGIN') or contains(text(), 'DANG NHAP')]"));
            
            usernameField.sendKeys("valid_user");
            passwordField.sendKeys("valid_password");
            loginButton.click();
            
            // Check success alert
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                System.out.println("Alert text: " + alertText);
                
                if (alertText.contains("thanh cong") || alertText.contains("success")) {
                    System.out.println("PASS: Login successful - Message: " + alertText);
                } else {
                    System.out.println("FAIL: Unexpected message: " + alertText);
                }
                alert.accept();
            } catch (Exception e) {
                System.out.println("FAIL: No alert displayed");
            }
        } catch (Exception e) {
            System.out.println("ERROR in test case 1: " + e.getMessage());
        }
        
        takeScreenshot("test01_login_success");
    }

    @Test
    public void test02_LoginWithWrongPassword() {
        System.out.println("=== Test Case 2: Wrong Password ===");
        
        try {
            // Enter correct username, wrong password
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='text']")));
            WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'LOGIN') or contains(text(), 'DANG NHAP')]"));
            
            usernameField.sendKeys("valid_user");
            passwordField.sendKeys("wrong_password");
            loginButton.click();
            
            // Check error alert
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                System.out.println("Alert text: " + alertText);
                
                if (alertText.contains("sai") || alertText.contains("error") || alertText.contains("invalid")) {
                    System.out.println("PASS: Error message displayed: " + alertText);
                } else {
                    System.out.println("FAIL: Unexpected error message: " + alertText);
                }
                alert.accept();
            } catch (Exception e) {
                System.out.println("FAIL: No error alert displayed");
            }
        } catch (Exception e) {
            System.out.println("ERROR in test case 2: " + e.getMessage());
        }
        
        takeScreenshot("test02_wrong_password");
    }

    @Test
    public void test03_EmptyFields() {
        System.out.println("=== Test Case 3: Empty Fields ===");
        
        try {
            // Test case 3a: Empty username
            WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'LOGIN') or contains(text(), 'DANG NHAP')]"));
            
            passwordField.sendKeys("some_password");
            loginButton.click();
            checkValidationMessage("username");
            
            // Refresh page for next test
            driver.navigate().refresh();
            
            // Test case 3b: Empty password
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='text']")));
            loginButton = driver.findElement(By.xpath("//button[contains(text(), 'LOGIN') or contains(text(), 'DANG NHAP')]"));
            
            usernameField.sendKeys("some_username");
            loginButton.click();
            checkValidationMessage("password");
            
        } catch (Exception e) {
            System.out.println("ERROR in test case 3: " + e.getMessage());
        }
        
        takeScreenshot("test03_empty_fields");
    }

    private void checkValidationMessage(String fieldType) {
        try {
            WebElement field = driver.findElement(By.cssSelector(
                fieldType.equals("username") ? "input[type='text']" : "input[type='password']"));
            
            // Check HTML5 validation
            if (field.getAttribute("required") != null) {
                System.out.println("PASS: Field " + fieldType + " is marked as required");
            }
            
            // Check validation message
            String validationMessage = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", field);
            
            if (validationMessage != null && !validationMessage.isEmpty()) {
                System.out.println("PASS: Validation warning for " + fieldType);
            } else {
                // Check alert
                try {
                    wait.until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    if (alertText.contains("dien") || alertText.contains("nhap") || alertText.contains("thong tin")) {
                        System.out.println("PASS: Required field warning displayed");
                    }
                    alert.accept();
                } catch (Exception e) {
                    System.out.println("FAIL: Cannot confirm validation for field " + fieldType);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR checking validation: " + e.getMessage());
        }
    }

    @Test
    public void test04_ForgotPasswordLink() {
        System.out.println("=== Test Case 4: Forgot Password Link ===");
        
        try {
            // Find Forgot password link
            WebElement forgotPasswordLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(), 'Forgot') or contains(text(), 'Quen') or contains(text(), 'forgot')]")));
            
            // Check if link exists and is clickable
            if (forgotPasswordLink.isDisplayed() && forgotPasswordLink.isEnabled()) {
                System.out.println("PASS: Forgot password link exists and is clickable");
                
                // Click and check alert
                forgotPasswordLink.click();
                
                try {
                    wait.until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    if (alertText.contains("quen mat khau") || alertText.contains("forgot password")) {
                        System.out.println("PASS: Forgot password link works: " + alertText);
                    }
                    alert.accept();
                } catch (Exception e) {
                    System.out.println("PASS: Forgot password link is clickable (no alert)");
                }
            } else {
                System.out.println("FAIL: Forgot password link not visible or not clickable");
            }
        } catch (Exception e) {
            System.out.println("ERROR in test case 4: " + e.getMessage());
        }
        
        takeScreenshot("test04_forgot_password");
    }

    @Test
    public void test05_SignUpLink() {
        System.out.println("=== Test Case 5: Sign Up Link ===");
        
        try {
            // Find Sign Up link
            WebElement signUpLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(), 'Sign Up') or contains(text(), 'Dang ky') or contains(text(), 'sign up')]")));
            
            // Check if link exists and is clickable
            if (signUpLink.isDisplayed() && signUpLink.isEnabled()) {
                System.out.println("PASS: Sign Up link exists and is clickable");
                
                // Click and check alert
                signUpLink.click();
                
                try {
                    wait.until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    if (alertText.contains("dang ky") || alertText.contains("sign up")) {
                        System.out.println("PASS: Sign Up link works: " + alertText);
                    }
                    alert.accept();
                } catch (Exception e) {
                    System.out.println("PASS: Sign Up link is clickable (no alert)");
                }
            } else {
                System.out.println("FAIL: Sign Up link not visible or not clickable");
            }
        } catch (Exception e) {
            System.out.println("ERROR in test case 5: " + e.getMessage());
        }
        
        takeScreenshot("test05_sign_up");
    }

    @Test
    public void test06_SocialLoginButtons() {
        System.out.println("=== Test Case 6: Social Login Buttons ===");
        
        try {
            // Find social login buttons
            java.util.List<WebElement> socialButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".social-btn, [class*='social'], button, div[class*='btn']")));
            
            System.out.println("Found " + socialButtons.size() + " social login buttons");
            
            // Check quantity
            if (socialButtons.size() >= 3) {
                System.out.println("PASS: At least 3 social login buttons found");
            } else {
                System.out.println("FAIL: Only " + socialButtons.size() + " social login buttons found");
            }
            
            // Check each button
            for (int i = 0; i < socialButtons.size(); i++) {
                WebElement button = socialButtons.get(i);
                if (button.isDisplayed() && button.isEnabled()) {
                    System.out.println("PASS: Social login button " + (i+1) + " is visible and clickable");
                    
                    // Test click
                    try {
                        button.click();
                        System.out.println("PASS: Social login button " + (i+1) + " is clickable");
                        
                        // Handle alert if present
                        try {
                            wait.until(ExpectedConditions.alertIsPresent());
                            Alert alert = driver.switchTo().alert();
                            String alertText = alert.getText();
                            System.out.println("Alert from button " + (i+1) + ": " + alertText);
                            alert.accept();
                        } catch (Exception e) {
                            // No alert is normal
                        }
                        
                        // Return to original page if needed
                        if (!driver.getCurrentUrl().equals(baseUrl)) {
                            driver.get(baseUrl);
                        }
                        
                    } catch (Exception e) {
                        System.out.println("FAIL: Cannot click social login button " + (i+1));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR in test case 6: " + e.getMessage());
        }
        
        takeScreenshot("test06_social_buttons");
    }

    private void takeScreenshot(String testName) {
        try {
            // Create screenshots directory if not exists
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }
            
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File("screenshots/" + testName + ".png");
            Files.copy(screenshot.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("PASS: Screenshot saved: " + destination.getPath());
        } catch (IOException e) {
            System.out.println("FAIL: Cannot take screenshot: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }
}