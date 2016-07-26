package lessons.tests.vladimir.todomvc.smoke;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import org.apache.commons.collections.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Created by Vladimir on 20.07.2016.
 */
public class TodoMVCTest {

    @Before
    public void testSetup() {
        Configuration.browser="chrome";
 //       Configuration.holdBrowserOpen=true;
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @Test
    public void testTasksLifeCycle() {

        addTasks("1","2","3","4");
        assertTasksAre("1","2","3","4");

        deleteTask("2");
        assertTasksAre("1","3","4");

        toggleTask("4");
        clearCompleted();
        assertTasksAre("1","3");

        toggleAllTasks();
        clearCompleted();
        assertTasksListIsEmpty();

    }

    ElementsCollection tasks = $$("#todo-list>li");

    private void addTasks(String... texts) {
        for (String text : texts) {
            if (!$("#main").isDisplayed()) {
                final String text1 = text;
                WebDriverWait wait = new WebDriverWait(getWebDriver(), 500000);
                ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        $("#new-todo").setValue(text1).pressEnter();
                        return $("#main").isDisplayed();
                    }
                };
                wait.until(condition);
            } else {
                $("#new-todo").setValue(text).pressEnter();
            }
        }
    }

    private void assertTasksAre(String... texts){
        tasks.shouldHave(exactTexts(texts));
    }

    private void clearCompleted(){
        $("#clear-completed").click();
    }

    private void deleteTask(String text){
        tasks.find(exactText(text)).hover().$(".destroy").click();
    }

    private void toggleTask(String text){
        tasks.find(exactText(text)).$(".toggle").click();
    }

    private void toggleAllTasks(){
        $("#toggle-all").click();
    }

    private void assertTasksListIsEmpty(){
        tasks.shouldBe(empty);
    }
}