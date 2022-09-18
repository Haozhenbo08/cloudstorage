package driver.project;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Homepage {
    private WebDriver driver;

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "save-note")
    private WebElement saveNote;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id = "new-note")
    private WebElement newNoteButton;

    @FindBy(id = "edit-note")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note")
    private WebElement deleteNoteButton;

    @FindBy(id = "credential-url")
    private WebElement credUrl;

    @FindBy(id = "credential-username")
    private WebElement credUsername;

    @FindBy(id = "credential-password")
    private WebElement credPassword;

    @FindBy(id = "save-cred")
    private WebElement saveCred;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credTab;

    @FindBy(id = "new-cred")
    private WebElement newCredButton;

    @FindBy(id = "cred-encryptedPassword")
    private WebElement encryptedPassword;

    @FindBy(id = "delete-cred")
    private WebElement deleteCredButton;

    public Homepage(WebDriver webDriver) {
        driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.logoutButton.click();
    }

    public void createNote(String noteTitle, String noteDescription) {
        WebDriverWait wait = new WebDriverWait (driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        this.noteTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note")));
        this.newNoteButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        this.noteTitle.sendKeys(noteTitle);
        this.noteDescription.sendKeys(noteDescription);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note")));
        this.saveNote.click();
    }

    public void editNote(String newTitle, String newDescription) {
        WebDriverWait wait = new WebDriverWait (driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        this.noteTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note")));
        this.editNoteButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        this.noteTitle.clear();
        this.noteTitle.sendKeys(newTitle);
        this.noteDescription.clear();
        this.noteDescription.sendKeys(newDescription);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note")));
        this.saveNote.click();
    }

    public void deleteNote() {
        WebDriverWait wait = new WebDriverWait (driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        this.noteTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note")));
        this.deleteNoteButton.click();
    }

    public void createCred(String url, String username, String password) {
        WebDriverWait wait = new WebDriverWait (driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        this.credTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-cred")));
        this.newCredButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        this.credUrl.sendKeys(url);
        this.credUsername.sendKeys(username);
        this.credPassword.sendKeys(password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-cred")));
        this.saveCred.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        this.credTab.click();
    }

    public void deleteCred() {
        WebDriverWait wait = new WebDriverWait (driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        this.credTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-cred")));
        this.deleteCredButton.click();
    }
}
