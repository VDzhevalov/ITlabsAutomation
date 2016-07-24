package lessons.tests.vladimir.todomvc.smoke;

        import com.codeborne.selenide.Configuration;
        import com.codeborne.selenide.ElementsCollection;
        import com.codeborne.selenide.WebDriverRunner;
        import io.github.bonigarcia.wdm.EdgeDriverManager;
        import org.junit.Before;
        import org.junit.Test;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.edge.EdgeDriver;

        import static com.codeborne.selenide.CollectionCondition.empty;
        import static com.codeborne.selenide.CollectionCondition.exactTexts;
        import static com.codeborne.selenide.Condition.enabled;
        import static com.codeborne.selenide.Condition.exactText;
        import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Vladimir on 20.07.2016.
 */
public class TodoMVCTest {

    @Before
    public void testSetup() {
        Configuration.browser="chrome";
        //Configuration.holdBrowserOpen=true;
        open("https://todomvc4tasj.herokuapp.com/");

    }

    @Test
    public void testTasksLifeCycle() {

        add("1","2","3","4");
        assertTasksAre("1","2","3","4");

        delete("2");
        assertTasksAre("1","3","4");

        toggleTask("4");
        clearCompleted();
        assertTasksAre("1","3");

        toggleAllTasks();
        clearCompleted();
        assertTasksListIsEmpty();

    }

    ElementsCollection tasks = $$("#todo-list>li");

    private void add (String... taskTexts){
        for (String text : taskTexts){
            try {Thread.sleep(3000);}
            catch (Exception e){}
            //$("#new-todo").setValue(text).pressEnter();
            $("#new-todo").shouldBe(enabled).setValue(text).pressEnter();
        }
    }

    private void assertTasksAre(String... taskTexts){
        tasks.shouldHave(exactTexts(taskTexts));
    }

    private void clearCompleted(){
        $("#clear-completed").click();
    }

    private void delete(String taskText){
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    private void toggleTask(String tastText){
        tasks.find(exactText(tastText)).$(".toggle").click();
    }

    private void toggleAllTasks(){
        $("#toggle-all").click();
    }

    private void assertTasksListIsEmpty(){
        tasks.shouldBe(empty);
    }
}
