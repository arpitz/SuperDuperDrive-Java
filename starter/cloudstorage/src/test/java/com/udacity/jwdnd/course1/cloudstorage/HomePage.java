package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id="nav-files-tab")
    private WebElement fileTab;

    @FindBy(id="nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialTab;

    public HomePage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void setTab(String tabName){
        if(tabName.equals("files"))
            fileTab.click();
        else if(tabName.equals("notes"))
            noteTab.click();
        else
            credentialTab.click();
    }
}
