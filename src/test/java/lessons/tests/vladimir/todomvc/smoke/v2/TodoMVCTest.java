package lessons.tests.vladimir.todomvc.smoke.v2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
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

    @Test
    public void testTasksLifeCycle() {

        addTasks("1");
        startEditTask("1", "1 edited").pressEnter();
        assertTasksAre("1 edited");
        toggleAllTasks();
        assertTasksAre("1 edited");

        filterActive();
        assertVisibleTasksListIsEmpty();
        addTasks("2");
        startEditTask("2", "2 edit canceled").pressEscape();
        toggleTask("2");
        assertVisibleTasksListIsEmpty();

        filterCompleted();
        assertVisibleTasksAre("1 edited", "2");
        //Activate
        toggleTask("2");
        assertVisibleTasksAre("1 edited");
        clearCompleted();
        assertVisibleTasksListIsEmpty();

        filterAll();
        assertItemsLeft(1);
        deleteTask("2");
        assertTasksListIsEmpty();
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

    private SelenideElement startEditTask(String oldText, String newText) {
        tasks.find(exactText(oldText)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newText);
    }
}
