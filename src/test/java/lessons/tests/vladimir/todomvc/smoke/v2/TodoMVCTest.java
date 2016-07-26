package lessons.tests.vladimir.todomvc.smoke.v2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
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

        add("1");
        editTask("1", "1e").pressEnter();
        assertTasksAre("1e");
        toggleAll();


        showActive();
        assertActiveListIsEmpty();
        add("2");
        editTask("2", "2e").pressEscape();
        toggleTask("2");
        assertActiveListIsEmpty();

        showCompleted();
        assertTasksAre("1e","2");
        toggleTask("1e");
        clearComplited();
        assertCompletedListIsEmpty();

        showAll();
        delete("1e");
        assertTasksListIsEmpty();;
    }

    ElementsCollection tasks = $$("#todo-list>li");
    ElementsCollection filters = $$("#filters a");

    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void assertTasksAre(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    private void  assertActiveListIsEmpty(){
        tasks.filterBy(cssClass("active")).shouldBe(empty);
    }

    private void  assertCompletedListIsEmpty(){
        tasks.filterBy(cssClass("completed")).shouldBe(empty);
    }

    private void assertTasksListIsEmpty(){
        tasks.shouldBe(empty);
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

    private SelenideElement editTask(String taskText, String newText) {
        tasks.find(exactText(taskText)).doubleClick();
        $("li.editing>input.edit").clear();
        return $("li.editing>input.edit").shouldBe(visible).setValue(newText);

    }
}
