package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

    @FindBy(id="credential-url")
    private WebElement url;

    @FindBy(id="credential-username")
    private WebElement username;

    @FindBy(id="credential-password")
    private WebElement password;

    @FindBy(id="save-credential")
    private WebElement saveCredentialButton;

    public CredentialPage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void addCredential(String url, String username, String password){
        this.url.sendKeys(url);
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        this.saveCredentialButton.click();
    }
}
