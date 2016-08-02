package lessons.tests.vladimir.todomvc.smoke.v3;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Vladimir on 20.07.2016.
 */
public class TodoMVCTest {

    @Before
    public void testSetup() {
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
    }

    @Test
    public void testTasksLifeCycle() {

        addTasks("a");
        assertTasksAre("a");
        assertItemsLeft(1);
        toggleAllTasks();
        assertTasksAre("a");

        filterActive();
        assertVisibleTasksListIsEmpty();
        addTasks("b");
        toggleTask("b");
        assertVisibleTasksListIsEmpty();

        filterCompleted();
        assertVisibleTasksAre("a","b");
        //Activate
        toggleTask("a");
        assertVisibleTasksAre("b");
        clearCompleted();
        assertVisibleTasksListIsEmpty();

        filterAll();
        assertVisibleTasksAre("a");
    }

    @Test
    public void testTaskDelete() {
        addTasks("a","b");
        filterActive();

        deleteTask("a");
        assertTasksAre("b");
        assertItemsLeft(1);
    }

    @Test
    public void testCancelTaskUpdate(){
        addTasks("a");
        filterActive();

        startTaskUpdate("a", "a edit canceled").pressEscape();
        assertTasksAre("a");
        assertItemsLeft(1);
    }

    @Test
    public void testTaskUpdate(){
        addTasks("a");
        filterActive();

        startTaskUpdate("a", "a edit").pressEnter();
        assertTasksAre("a edit");
        assertItemsLeft(1);
    }

    ElementsCollection tasks = $$("#todo-list>li");

    private void addTasks(String... texts) {
        for (String text : texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void assertTasksAre(String... texts) {
        tasks.shouldHave(exactTexts(texts));
    }

    private void assertVisibleTasksAre(String... texts) {
        tasks.filterBy(visible).shouldHave(exactTexts(texts));
    }

    private void assertVisibleTasksListIsEmpty() {
        tasks.filterBy(visible).shouldBe(empty);
    }

    private void assertTasksListIsEmpty() {
        tasks.shouldBe(empty);
    }

    private void assertItemsLeft(Integer counter) {
        $("#todo-count").$("strong").shouldHave(exactText(counter+""));
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void deleteTask(String text) {
        tasks.find(exactText(text)).hover().$(".destroy").click();
    }

    private void toggleTask(String text) {
        tasks.find(exactText(text)).$(".toggle").click();
    }

    private void toggleAllTasks() {
        $("#toggle-all").click();
    }

    private void filterAll() {
        $(By.linkText("All")).click();
    }

    private void filterActive() {
        $(By.linkText("Active")).click();
    }

    private void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    private SelenideElement startTaskUpdate(String oldText, String newText) {
        tasks.find(exactText(oldText)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newText);
    }
}