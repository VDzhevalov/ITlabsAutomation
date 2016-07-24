package lessons.tests.vladimir.todomvc;

import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
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

        $("#new-todo").setValue("task1").pressEnter();
        $("#new-todo").setValue("task2").pressEnter();
        $("#new-todo").setValue("task3").pressEnter();
        $("#new-todo").setValue("task4").pressEnter();
        $$("#todo-list li").shouldHave(exactTexts("task1","task2","task3","task4"));

        //Delete
        $$("#todo-list li").find(exactText("task2")).hover().$(".destroy").click();
        $$("#todo-list li").shouldHave(exactTexts("task1","task3","task4"));

        //Сomplete
        $$("#todo-list li").find(exactText("task4")).$(".toggle").click();

        $("#clear-completed").click();
        $$("#todo-list li").shouldHave(exactTexts("task1","task3"));

        //complete all
        $("#toggle-all").click();
        $$("#todo-list li.completed").shouldHave(exactTexts("task1","task3"));

        $("#clear-completed").click();
        $$("#todo-list li").shouldBe(empty);

    }
}
