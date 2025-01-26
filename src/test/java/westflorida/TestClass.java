package westflorida;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.openqa.selenium.interactions.Actions;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TestClass {
	WebDriver driver;
	Actions actions;
	WebDriverWait wait;
	//	Scanner sc=new Scanner(System.in);

	@BeforeClass
	@Parameters("browser")
	public void start(@Optional("Chrome")String browser)
	{
		if (browser.equalsIgnoreCase("chrome")) {          
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else {
	        throw new IllegalArgumentException("Unsupported browser: " + browser);
	    }
		driver.get("https://westfloridaahec.org/");
		driver.manage().window().maximize();
		actions=new Actions(driver);	
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		System.out.println("from before class");
	}
	@AfterClass
	public void end() {
		driver.quit();
	}

	@Test(priority=1)
	public void NavigationHome() {
		//HOME(Link )
		WebElement homeLink = driver.findElement(By.linkText("HOME"));
		boolean homeVerification = homeLink.isDisplayed();
		Assert.assertTrue(homeVerification);
		homeLink.click();
		//Title
		wait.until(ExpectedConditions.titleContains("Home - West Florida Area Health Education Center, Inc"));
		Assert.assertEquals(driver.getTitle(), "Home - West Florida Area Health Education Center, Inc", "The HOME page should open correctly.");
		//LOGO
		WebElement logo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"wrapper\"]/header/div[1]/div[3]/div[1]/div/div/a/img[1]")));
		Assert.assertTrue(logo.isDisplayed(), "The logo is not displayed on the website!");
		//Searchbutton
		WebElement searchbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"wrapper\"]/header/div[1]/div[3]/div[1]/div/div/div/div/form/div/div[1]/label/input")));
		Assert.assertTrue(searchbox.isDisplayed(), "The searchbox is not displayed on the website!");
	}
	@Test(priority=2)
	public void privacy() throws InterruptedException {
		driver.get("https://westfloridaahec.org/#");
		WebElement privacyLink= wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"cookieconsent:desc\"]/a")));
		String parentWindowprivacy = driver.getWindowHandle();	
		privacyLink.click();
		Thread.sleep(2000);
		for (String handleLink : driver.getWindowHandles()) {
			if (!handleLink.equals(parentWindowprivacy)) {
				driver.switchTo().window(handleLink);
			}
		}		

		driver.close();  	    
		driver.switchTo().window(parentWindowprivacy);	
		System.out.println("privacy cookies Done");
	}
	@Test(priority=3)
	public void about()  {
		//ABOUT
		//	WebElement aboutLink = driver.findElement(By.xpath("//*[@id=\"menu-item-616\"]/a/span[1]"));
		WebElement aboutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"menu-item-616\"]/a/span[1]")));
		aboutLink.click();

		//aboutLink.click();
		List<WebElement> dropdownOptions = driver.findElements(By.xpath(" //*[@id='menu-item-616']/ul/li/a"));   
		for (WebElement aboutoptions : dropdownOptions) {
			String optionText=aboutoptions.getText();
			System.out.println("Navigating to: " +  optionText);
			wait.until(ExpectedConditions.elementToBeClickable(aboutoptions));
			aboutoptions.click();
			System.out.println("URL of navigated location:" + driver.getCurrentUrl());  
			driver.navigate().back();
			System.out.println("URL after navigation back:"+driver.getCurrentUrl());
			aboutLink = driver.findElement(By.xpath("//*[@id=\"menu-item-616\"]/a/span[1]"));
			aboutLink.click();
		}
	}

	@Test(priority=4)
	//,dependsOnMethods= {"about"}
	public void programs() throws IOException, InterruptedException  {
		//Programs
		driver.get("https://westfloridaahec.org/");
		WebElement programLink = driver.findElement(By.linkText("PROGRAMS"));
		programLink.click();
		List<WebElement> dropdownPrograms = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"menu-item-264\"]/ul/li/a")));
		for (WebElement programoptions: dropdownPrograms) {
			String optionProgram=programoptions.getText();
			System.out.println("Navigating to: " +  optionProgram);
			wait.until(ExpectedConditions.elementToBeClickable(programoptions));
			programoptions.click();
			System.out.println("URL of navigated location:" + driver.getCurrentUrl());   
			if (optionProgram.equals("Healthy Aging")) {
				WebElement formElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cog-input-auto-0\"]")));
				// Scroll the form into view if it's not already in the viewport
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].scrollIntoView(true);", formElement);
				wait.until(ExpectedConditions.visibilityOf(formElement));
				driver.findElement(By.xpath("//*[@id='cog-input-auto-0']")).sendKeys("sharu");
				driver.findElement(By.xpath("//*[@id='cog-input-auto-1']")).sendKeys("hi");
				driver.findElement(By.xpath("//*[@id='cog-1']")).sendKeys("(333) 333-3333");
				driver.findElement(By.xpath("//*[@id='cog-2']")).sendKeys("Test@123gmail.com");
				driver.findElement(By.xpath("//*[@id=\"post-500\"]/div/div[1]/div/div[1]/div/form/div/div/div[1]/div/div[4]/fieldset[2]/div[1]/div[1]/div/label[1]/span[1]/span")).click();
				driver.findElement(By.xpath("//*[@id=\"post-500\"]/div/div[1]/div/div[1]/div/form/div/div/div[1]/div/div[10]/button/span/span")).click();
				System.out.println("submitted");

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"post-500\"]/div/div[1]/div/div[1]/div/form/div/div/div[1]/div[2]/div[1]/div/div/h4/strong[2]")));

				// Capture screenshot for debugging
				File screenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshot1, new File("screenshotForm.png"));

			}          
			driver.navigate().back();
			System.out.println("URL after navigation back:"+driver.getCurrentUrl());
			programLink.click();	   
		}
	}

	@Test(priority=5)

	public void services()  {
		//SERVICES
		WebElement serviceLink =driver.findElement(By.linkText("SERVICES"));
		serviceLink.click();
		List<WebElement> dropdownServices = driver.findElements(By.xpath("//*[@id=\"menu-item-331\"]/ul/li/a"));   
		for (WebElement serviceoptions: dropdownServices) {
			String optionServices=serviceoptions.getText();
			System.out.println("Navigating to: " +  optionServices);
			wait.until(ExpectedConditions.elementToBeClickable(serviceoptions));
			serviceoptions.click();
			System.out.println("URL of navigated location:" + driver.getCurrentUrl());   
			driver.navigate().back();
			System.out.println("URL after navigation back:"+driver.getCurrentUrl());
			serviceLink.click();
		}
	}
	@Test(priority=6)
	public void cpr()  {
		//CPR
		WebElement cprLink =driver.findElement(By.linkText("CPR"));
		cprLink.click();
		List<WebElement> dropdownServices = driver.findElements(By.xpath("//*[@id=\"menu-item-467\"]/ul/li/a"));   
		for (WebElement cproptions: dropdownServices) {
			String optionServices=cproptions.getText();
			System.out.println("Navigating to: " +  optionServices);
			wait.until(ExpectedConditions.elementToBeClickable(cproptions));
			cproptions.click();
			System.out.println("URL of navigated location:" + driver.getCurrentUrl());   
			driver.navigate().back();
			System.out.println("URL after navigation back:"+driver.getCurrentUrl());
			cprLink.click();
		}
	}
	@Test(priority=7)
	public void ContactUs() throws InterruptedException {
		//CONTACT US
		WebElement contactLink = driver.findElement(By.linkText("CONTACT US"));
		Assert.assertTrue(contactLink.isDisplayed(), "HOME link should be visible.");
		contactLink.click();
		wait.until(ExpectedConditions.titleContains("CONTACT US - West Florida Area Health Education Center, Inc"));
		Assert.assertEquals(driver.getTitle(), "CONTACT US - West Florida Area Health Education Center, Inc", "The CONTACT US page should open correctly.");

		for(int i=1;i<=4;i++) {
			handleLinkClick("//*[@id='post-127']/div/div[1]/div/div/div/div[7]/div["+i+"]/div/div[3]/p/a");
		}
	}
	void handleLinkClick(String xpath) throws InterruptedException {
		String parentWindowHandle = driver.getWindowHandle();		    		    
		WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='post-127']/div/div[1]/div/div/div/div[7]/div[\"+i+\"]/div/div[3]/p/a")));
		link.click();

		//solution
		// Check if cookie consent banner exists and dismiss it
		try {
			WebElement dismissCookieButton = driver.findElement(By.cssSelector(".cc-btn.cc-dismiss"));
			if (dismissCookieButton.isDisplayed()) {
				wait.until(ExpectedConditions.elementToBeClickable(dismissCookieButton)).click();
			}
		} catch (NoSuchElementException e) {
			// No cookie banner found, continue
		}

		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(parentWindowHandle)) {
				driver.switchTo().window(handle);
			}
		}		    
		driver.close();  // Close the new window/tab		    
		driver.switchTo().window(parentWindowHandle);// Switch back to the original window/tab
		Thread.sleep(1000);
	}
	@Test(priority=8)
	public void News() {
		//NEWS
		WebElement NewsLink = driver.findElement(By.linkText("NEWS"));
		Assert.assertTrue(NewsLink.isDisplayed(), "News link should be visible.");
		NewsLink.click();
		wait.until(ExpectedConditions.titleContains("NEWS - West Florida Area Health Education Center, Inc"));
		Assert.assertEquals(driver.getTitle(), "NEWS - West Florida Area Health Education Center, Inc", "The NEWS page should open correctly.");

	}

	@Test(priority=9)
	public void search() {
		WebElement searchLink=driver.findElement(By.xpath("//*[@id=\"wrapper\"]/header/div[1]/div[3]/div[1]/div/div/div/div/form/div/div[1]/label/input"));
		searchLink.click();
		String keyword="services";
		//String keyword=sc.next();
		searchLink.sendKeys(keyword);
		WebElement icon=driver.findElement(By.xpath("//*[@id=\"wrapper\"]/header/div[1]/div[3]/div[1]/div/div/div/div/form/div/div[2]/input"));
		icon.click();
		String expectedUrl=("https://westfloridaahec.org/"+ "?s=" +keyword);
		String actualUrl=driver.getCurrentUrl();
		System.out.println(expectedUrl);
		System.out.println(actualUrl);
		Assert.assertEquals(actualUrl, expectedUrl);
		driver.navigate().to("https://westfloridaahec.org/");
	}

	@Test(priority=10)
	public void MyAccount() throws InterruptedException {
		//MyAccount
		WebElement MyacccountLink = driver.findElement(By.xpath("//*[@id=\"menu-main-menu\"]/li[8]/a/span[1]"));
		Assert.assertTrue(MyacccountLink.isDisplayed(), "My account link should be visible.");
		MyacccountLink.click();
		wait.until(ExpectedConditions.titleContains("My account - West Florida Area Health Education Center, Inc"));
		Assert.assertEquals(driver.getTitle(), "My account - West Florida Area Health Education Center, Inc", "The Myaccount page should open correctly.");
		//Registration
		WebElement accLink = driver.findElement(By.xpath("//*[@id=\"menu-main-menu\"]/li[8]/a/span[1]"));
		accLink.click();
		String Actual_url =driver.getCurrentUrl();
		String Expected_url="https://westfloridaahec.org/my-account/";
		if(Actual_url.contentEquals(Expected_url))
		{	
			WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reg_username")));
			username.sendKeys("Sharon");
			WebElement email = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='email']")));
			email.sendKeys("sharonmadhyahnapu1234@gmail.com");
			WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"reg_password\"]")));
			password.sendKeys("SHA@ron123");
			WebElement register = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"customer_login\"]/div[2]/form/p[4]/button")));
			register.click();

		}	    		

		//login
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys("sharonmadhyahnapu1234@gmail.com");
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("SHA@ron123");
		driver.findElement(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")).click();
		driver.navigate().to("https://westfloridaahec.org/");
	}
	@Test(priority=11)
	public void Resetpassword() throws IOException {

		WebElement accLink = driver.findElement(By.xpath("//*[@id=\"menu-main-menu\"]/li[8]/a/span[1]"));
		accLink.click();
		String Actual_url =driver.getCurrentUrl();
		String Expected_url="https://westfloridaahec.org/my-account/";
		if (Actual_url.contentEquals(Expected_url)) {

			WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"username\"]")));
			username.sendKeys("Sharon");

			WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"password\"]")));
			password.sendKeys("SHA@ron123");
			WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")));
			login.click();
			//problem cookies*********************************************8
			// Step 4: Handle cookies (if the "Agree" button appears)
			try {
				WebElement agreeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[7]/div/a")));
				agreeButton.click();
				System.out.println("Cookie consent clicked successfully.");
			} catch (TimeoutException e) {
				System.out.println("Cookie consent not found."+e.getMessage());
			}
			WebElement Account = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"post-381\"]/div/div/nav/ul/li[6]/a")));
			Account.click();
			driver.findElement(By.xpath("//*[@id=\"account_first_name\"]")).sendKeys("Sharon");
			driver.findElement(By.xpath("//*[@id=\"account_last_name\"]")).sendKeys("sharu");
			WebElement password2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"password_current\"]")));
			password2.sendKeys("SHA@ron123");
			WebElement passwordnew = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"password_1\"]")));
			passwordnew.sendKeys("Test@123");
			WebElement passwordconfirm = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"password_2\"]")));
			passwordconfirm.sendKeys("Test@123");
			WebElement saveButton = driver.findElement(By.xpath("//*[@id=\"post-381\"]/div/div/div[2]/form/p[5]/button"));


			try {
				saveButton.click();
				System.out.println("Button clicked successfully.");
			} catch (ElementClickInterceptedException e) {
				System.out.println("Button is not clickable due to an defect.");
			}

			// Capture screenshot for debugging
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File("screenshot.png"));

			try {
				WebElement successMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"post-381\"]/div/div/div[2]/div/div/div")));
				String successMessage = successMessageElement.getText();
				Assert.assertEquals(successMessage, "Account details changed successfully.");
				System.out.println("Test Passed: " + successMessage);
			} catch (Exception e) {
				System.out.println("Button did not work, skipping success message verification."+e.getMessage());
			}	         
			driver.navigate().back();
			driver.findElement(By.xpath("//*[@id=\"post-381\"]/div/div/div[2]/p[1]/a"));
		}
	}

	@Test(priority=12)
	public void facebookicon() throws InterruptedException {

		driver.navigate().to("https://westfloridaahec.org/");
		WebElement SD = driver.findElement(By.xpath("//*[@id=\"post-10\"]/div/div[2]/div/div[1]/div/div[1]/h2")); 
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", SD);
		Thread.sleep(2000);
		WebElement facebook=driver.findElement(By.xpath("//*[@id=\"post-10\"]/div/div[3]/div/div/div/div[1]/div[1]/div/div/a/div/span/i"));
		facebook.click();
		if (driver.getTitle().contains("Page not found")) {
			System.out.println("Test Failed: The Facebook icon leads to a 'Page Not Found' (404) error.");

			File screenshotFacebook = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(screenshotFacebook, new File("screenshotFB-404.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Test Passed: The Facebook icon is working correctly.");
		}
		Assert.assertFalse(driver.getTitle().contains("404"));
	}
}
