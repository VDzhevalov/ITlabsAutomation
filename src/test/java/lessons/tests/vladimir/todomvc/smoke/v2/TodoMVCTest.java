package lessons.tests.vladimir.todomvc.smoke.v2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
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
    public void testBasicUserActivity() {

        add("1", "2", "3");
        toggleAll();

        showCompleted();
        editTask("1", "1e");
        toggleTask("1e");
        delete("2");
        clearComplited();
        tasks.filterBy(cssClass("completed")).shouldBe(empty);

        showActive();
        editTask("1e", "1");
        delete("1");
        add("2", "3", "4");
        toggleTask("2");
        toggleAll();
        tasks.filterBy(cssClass("active")).shouldBe(empty);

        showAll();
        assertTasksAre("2", "3", "4");
        clearComplited();
        tasks.shouldBe(empty);
    }

    ElementsCollection tasks = $$("#todo-list li");
    ElementsCollection filters = $$("#filters a");

    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void assertTasksAre(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    private void clearComplited() {
        $("#clear-completed").click();
    }

    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    private void toggleTask(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void showAll() {
        filters.find(exactText("All")).click();
    }

    private void showActive() {
        filters.find(exactText("Active")).click();
    }

    private void showCompleted() {
        filters.find(exactText("Completed")).click();
    }

    private void editTask(String taskText, String newText) {
        tasks.find(exactText(taskText)).doubleClick();
        $("input.edit").clear();
        $("input.edit").setValue(newText).pressEnter();
    }
}
