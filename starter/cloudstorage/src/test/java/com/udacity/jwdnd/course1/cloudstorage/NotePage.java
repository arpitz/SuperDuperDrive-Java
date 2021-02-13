package com.udacity.jwdnd.course1.cloudstorage;

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

    public NotePage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void addNote(String title, String description){
       // this.addNewNoteButton.click();
        this.noteTitle.sendKeys(title);
        this.description.sendKeys(description);
        this.saveNote.click();
    }
}
