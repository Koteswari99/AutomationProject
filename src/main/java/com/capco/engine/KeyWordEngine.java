package com.capco.engine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.sql.*;
import org.testng.Assert;


import com.capco.base.*;
import com.capco.Runner.*;

public class KeyWordEngine {

	public WebDriver driver;
	public Properties prop;

	public static Workbook book;
	public static Sheet sheet;

	public Base base;
	public CapcoDriverrunner runner;
	public WebElement element;
	public ResultSet resultSet;

	public void startExecution(String Excelfilename, String sheetName) throws InvalidFormatException {

		FileInputStream file = null;
		try {
			file = new FileInputStream(Excelfilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			book = WorkbookFactory.create(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sheet = book.getSheet(sheetName);
		int k = 0;
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			try {

				String locatorType = sheet.getRow(i + 1).getCell(k + 1).toString().trim();
				String locatorValue = sheet.getRow(i + 1).getCell(k + 2).toString().trim();
				String[] locatorValueArray = locatorValue.split(";");
				String action = sheet.getRow(i + 1).getCell(k + 3).toString().trim();
				String value = sheet.getRow(i + 1).getCell(k + 4).toString().trim();

				switch (action) {
				
				case "open browser":
					//runner = new CapcoDriverrunner();
					//prop = runner.beforetest();
					base = new Base();
					prop = base.init_properties();
					if (value.isEmpty() || value.equals("NA")) {
						driver = base.init_driver(prop.getProperty("browser"));
					} else {
						driver = base.init_driver(value);
					}
					break;

				case "enter url":
					if (value.isEmpty() || value.equals("NA")) {
						driver.get(prop.getProperty("url"));
					} else {
						driver.get(value);
					}
					break;

				case "quit":
					driver.quit();
					break;
				case "SelectDefaultFrame":
					driver.switchTo().defaultContent();
					break;
				case "AlertBox":
					Alert alert = driver.switchTo().alert();

					if (value.equalsIgnoreCase("OK")) {
						alert.accept();
					} else if (value.equalsIgnoreCase("Cancel")) {
						alert.dismiss();
					}
					break;
				case "Windowswitch":
					Set<String> WindowIds = driver.getWindowHandles();
					Iterator<String> itr = WindowIds.iterator();
					String mainwindow = itr.next();
					String Tabwindow = itr.next();
					if (value.equalsIgnoreCase("tabwindow")) {
						driver.switchTo().window(Tabwindow);
					} else if (value.equalsIgnoreCase("mainwindow")) {
						driver.switchTo().window(mainwindow);
					}
					break;
				case "function":
					StringTokenizer tokenizer = new StringTokenizer(value, "(,)");
					String string1 = tokenizer.nextToken();
					String string2 = tokenizer.nextToken();
					String string3 = tokenizer.nextToken();
					Mainfunction mainfunction = new Mainfunction();
					if (Mainfunction.isInt(string2) == true && Mainfunction.isInt(string3) == true) {
						int int1 = Integer.parseInt(string2);
						int int2 = Integer.parseInt(string3);

						mainfunction.function1(string1, int1, int2);
					} else if (Mainfunction.isString(string2) == true && Mainfunction.isString(string3) == true) {
						mainfunction.function2(string1, string2, string3);
					}
					break;
				case "assertions-title":
					String actualTitle = driver.getTitle();
					Assert.assertEquals(actualTitle, value);
					System.out.println("Assertion for title is successful");

				case "databaseconnection":
					Class.forName("com.mysql.jdbc.Driver");
					StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
					String value1 = stringTokenizer.nextToken();
					String value2 = stringTokenizer.nextToken();
					String value3 = stringTokenizer.nextToken();
					String value4 = stringTokenizer.nextToken();
					Connection connection = DriverManager.getConnection(value1, value2, value3);
					// example:
					// Connection connection =
					// DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root",
					// "password");
					Statement statement = connection.createStatement();
					resultSet = statement.executeQuery(value4);
					// example:
					// ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
					while (resultSet.next()) {
						String username = resultSet.getString("username");
						String password = resultSet.getString("password");
						System.out.println("Username: " + username);
						System.out.println("Password: " + password);
					}
					break;
				case "database-compare-value":
					StringTokenizer stringTokenizer1 = new StringTokenizer(value, ",");
					String comparevalue1 = stringTokenizer1.nextToken();
					String comparevalue2 = stringTokenizer1.nextToken();
					while (resultSet.next()) {
						Assert.assertEquals(comparevalue1, resultSet.getString(comparevalue2));
					}
					
				default:
					break;

				}

				switch (locatorType) {
				case "id":
					element = driver.findElement(By.id(locatorValue));
					if (action.equalsIgnoreCase("sendkeys")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					} else if (action.equalsIgnoreCase("isDisplayed")) {
						element.isDisplayed();
					} else if (action.equalsIgnoreCase("getText")) {
						String elementText = element.getText();
						System.out.println("text from element : " + elementText);
					} else if (action.equalsIgnoreCase("SelectDropdown")) {
						List<WebElement> dropdownValues = element.findElements(By.tagName("option"));
						for (int j = 0; j < dropdownValues.size(); j++) {
							if (dropdownValues.get(j).getText().equals(value)) {
								element.sendKeys(value);
								break;
							}
						}
					}

					locatorType = null;
					break;

				case "name":
					element = driver.findElement(By.name(locatorValue));
					if (action.equalsIgnoreCase("sendkeys")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					} else if (action.equalsIgnoreCase("isDisplayed")) {
						element.isDisplayed();
					} else if (action.equalsIgnoreCase("getText")) {
						String elementText = element.getText();
						System.out.println("text from element : " + elementText);
					} else if (action.equalsIgnoreCase("SelectDropdown")) {
						List<WebElement> dropdownValues = element.findElements(By.tagName("option"));
						for (int j = 0; j < dropdownValues.size(); j++) {
							if (dropdownValues.get(j).getText().equals(value)) {
								element.sendKeys(value);
								break;
							}
						}
					}

					locatorType = null;
					break;

				case "xpath":
					element = driver.findElement(By.xpath(locatorValueArray[0]));
					System.out.println(locatorValueArray[0]);
					System.out.println(locatorValueArray[1]);
					if (action.equalsIgnoreCase("sendkeys")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					} else if (action.equalsIgnoreCase("isDisplayed")) {
						element.isDisplayed();
					} else if (action.equalsIgnoreCase("getText")) {
						String elementText = element.getText();
						System.out.println("text from element : " + elementText);
					} else if (action.equalsIgnoreCase("SelectDropdown")) {
						List<WebElement> dropdownValues = element.findElements(By.tagName(locatorValueArray[1]));

						for (int j = 0; j < dropdownValues.size(); j++) {
							if (dropdownValues.get(j).getText().equals(value)) {
								element.sendKeys(value);
								break;
							}
						}
					} else if (action.equalsIgnoreCase("SelectFrame")) {

						driver.switchTo().frame(element);

					} else if (action.equalsIgnoreCase("DD")) {

						WebElement dest = driver.findElement(By.xpath(locatorValueArray[1]));
						driver.manage().window().maximize();
						Actions act = new Actions(driver);
						act.dragAndDrop(element, dest).build().perform();
					} else if (action.equalsIgnoreCase("assertions-element")) {

						Assert.assertTrue(element.isDisplayed());
						System.out.println("Assertion for element is successful");

					} else if (action.equalsIgnoreCase("database-compare-webpage")) {
						String elementText = element.getText();
						while (resultSet.next()) {
							Assert.assertEquals(elementText, resultSet.getString(value));
						}
					} 
					locatorType = null;
					break;

				case "cssSelector":
					element = driver.findElement(By.cssSelector(locatorValue));
					if (action.equalsIgnoreCase("sendkeys")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					} else if (action.equalsIgnoreCase("isDisplayed")) {
						element.isDisplayed();
					} else if (action.equalsIgnoreCase("getText")) {
						String elementText = element.getText();
						System.out.println("text from element : " + elementText);
					} else if (action.equalsIgnoreCase("SelectDropdown")) {
						List<WebElement> dropdownValues = element.findElements(By.tagName("option"));

						for (int j = 0; j < dropdownValues.size(); j++) {
							if (dropdownValues.get(j).getText().equals(value)) {
								element.sendKeys(value);
								break;
							}
						}
					}

					locatorType = null;
					break;

				case "className":
					element = driver.findElement(By.className(locatorValue));
					if (action.equalsIgnoreCase("sendkeys")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					} else if (action.equalsIgnoreCase("isDisplayed")) {
						element.isDisplayed();
					} else if (action.equalsIgnoreCase("getText")) {
						String elementText = element.getText();
						System.out.println("text from element : " + elementText);
					} else if (action.equalsIgnoreCase("SelectDropdown")) {
						List<WebElement> dropdownValues = element.findElements(By.tagName("option"));
						for (int j = 0; j < dropdownValues.size(); j++) {
							if (dropdownValues.get(j).getText().equals(value)) {
								element.sendKeys(value);
								break;
							}
						}
					}

					locatorType = null;
					break;

				case "linkText":
					element = driver.findElement(By.linkText(locatorValue));
					element.click();
					locatorType = null;
					break;

				case "partialLinkText":
					element = driver.findElement(By.partialLinkText(locatorValue));
					element.click();
					locatorType = null;
					break;

				default:
					break;
				}

			} catch (Exception e) {

			}

		}

	}
}
