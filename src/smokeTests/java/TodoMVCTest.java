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

        addTask("1");
        editTask("1", "1 edited").pressEnter();
        assertTasksAre("1 edited");
        toggleAllTasks();
        assertTasksAre("1 edited");

        filterActive();
        assertActiveListIsEmpty();
        addTask("2");
        editTask("2", "2 edit canceled").pressEscape();
        toggleTask("2");
        assertActiveListIsEmpty();

        filterCompleted();
        assertTasksAre("1 edited", "2");
        //Activate
        toggleTask("2");
        clearCompleted();
        assertCompletedListIsEmpty();

        filterAll();
        assertItemsLeftCounterEquals("1");
        delete("2");
        assertTasksListIsEmpty();
    }

    ElementsCollection tasks = $$("#todo-list>li");

    private void addTask(String... texts) {
        for (String text : texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void assertTasksAre(String... texts) {
        tasks.shouldHave(exactTexts(texts));
    }

    private void assertActiveListIsEmpty() {
        tasks.filterBy(visible).shouldBe(empty);
    }

    private void assertCompletedListIsEmpty() {
        tasks.filterBy(visible).shouldBe(empty);
    }

    private void assertTasksListIsEmpty() {
        tasks.shouldBe(empty);
    }

    private void assertItemsLeftCounterEquals(String counter) {
        $("#todo-count").shouldBe(text(counter + " item"));
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
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

    private SelenideElement editTask(String text, String newText) {
        tasks.find(exactText(text)).doubleClick();
        $("li.editing>input.edit").clear();
        return $("li.editing>input.edit").shouldBe(visible).setValue(newText);

    }
}
