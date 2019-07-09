package algat.controller;

import algat.model.Lesson;
import algat.model.Question;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class LessonsController implements Initializable {
    @FXML private VBox leftBar;
    @FXML private WebView webView;
    @FXML private ScrollPane questionsPane;

    private Stage stage;
    private List<Lesson> lessons = new LinkedList<>();
    private int lessonCursor = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionsPane.setVisible(false);
        webView.visibleProperty().addListener((observableValue, oldValue, newValue) -> questionsPane.setVisible(!newValue));

        Yaml yaml = new Yaml(new Constructor(Lesson.class));
        InputStream inputStream = this.getClass().getResourceAsStream("/algat/lessons.yml");

        int lessonIndex = 0;
        for (Object object: yaml.loadAll(inputStream)) {
            Lesson lesson = (Lesson) object;
            Button lessonButton = new Button(lesson.getTitle());

            lessonButton.setOnAction(actionEvent -> {
                String uri = getClass().getResource("/algat/resources/lessons/" + lesson.getFileName()).toExternalForm();
                webView.getEngine().load(uri);
                webView.setVisible(true);
            });

            Button questionsButton = createQuestionsButton(lesson, lessonIndex);

            VBox buttons = new VBox();
            buttons.getChildren().addAll(lessonButton, questionsButton);
            if (lessonIndex == 0)
                lessonButton.fire();
            else
                buttons.setVisible(false);

            leftBar.getChildren().add(buttons);
            lessons.add(lesson);
            lessonIndex++;
        }
    }


    private Button createQuestionsButton(Lesson lesson, int lessonIndex) {
        Button questionsButton = new Button("Domande " + (lessonIndex + 1));

        questionsButton.setOnAction(actionEvent -> {
            webView.setVisible(false);
            VBox questionsBox = new VBox();
            List<Question> questions = lesson.getQuestions();

            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                VBox questionBox = new VBox();
                questionBox.getStyleClass().add("question");
                Text questionText = new Text(question.getText());
                questionText.getStyleClass().add("question-text");
                questionBox.getChildren().add(questionText);
                if (i != 0)
                    questionBox.setVisible(false);

                ToggleGroup answersGroup = new ToggleGroup();
                for (String answer : question.getAnswers()) {
                    RadioButton radioButton = new RadioButton(answer);
                    radioButton.setToggleGroup(answersGroup);
                    radioButton.selectedProperty().addListener((observableValue, oldValue, selected) -> {
                        String correctAnswer = question.getAnswers().get(question.getAnswer());

                        if (!selected)
                            radioButton.getStyleClass().removeAll("green-radio-button", "red-radio-button");
                        else if (correctAnswer.equals(radioButton.getText())) {
                            radioButton.getStyleClass().add("green-radio-button");
                            this.unlockNextQuestion(questionBox);
                        } else
                            radioButton.getStyleClass().add("red-radio-button");
                    });
                    questionBox.getChildren().add(radioButton);
                }

                questionsBox.getChildren().add(questionBox);
            }

            questionsPane.setContent(questionsBox);
        });

        return questionsButton;
    }

    public void goToPlayground(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/Playground.fxml"));
            Parent playground = loader.load();
            PlaygroundController controller = loader.getController();
            controller.setStage(stage);
            controller.setLessonProgress(lessonCursor);
            stage.getScene().setRoot(playground);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setLessonCursor(int lessonCursor) {
        this.lessonCursor = lessonCursor;

        ObservableList<Node> children = leftBar.getChildren();
        for (int i = 0; i < children.size(); i++) {
            if (i <= lessonCursor)
                children.get(i).setVisible(true);
        }
    }

    private void unlockNextQuestion(VBox questionBox) {
        VBox questionsBox = (VBox) questionsPane.getContent();
        ObservableList<Node> questions = questionsBox.getChildren();
        int currentQuestionIndex = questions.indexOf(questionBox);

        if (currentQuestionIndex + 1 < questions.size())
            questions.get(currentQuestionIndex + 1).setVisible(true);
        else {
            Text congratsText = new Text("Congratulazioni! Hai risposto correttamente a tutte le domande!");
            congratsText.getStyleClass().add("congrats-text");
            questions.add(congratsText);
            lessonCursor++;
            if (lessonCursor < lessons.size()) {
                leftBar.getChildren().get(lessonCursor).setVisible(true);
            }
        }
    }
}
