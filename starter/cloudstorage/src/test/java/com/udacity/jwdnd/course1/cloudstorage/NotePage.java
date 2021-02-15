package com.udacity.jwdnd.course1.cloudstorage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {
    @FindBy(css="#add-new-note")
    private WebElement addNewNoteButton;

    @FindBy(id="note-title")
    private WebElement noteTitle;

    @FindBy(id="note-description")
    private WebElement description;

    @FindBy(id="save-note")
    private WebElement saveNote;

    private final WebDriver driver;

    public NotePage(WebDriver webDriver){
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }
    public void addNote(String title, String description){
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", noteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", this.description);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveNote);
    }
}
