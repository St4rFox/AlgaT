package algat.controller;

import algat.model.Lesson;
import algat.model.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class LessonsController implements Initializable {
    //Dichiarazione oggetti parte della view
    //Come private
    //Davanti serve un'annotazione perché il compilatore lo colleghi con fxml
    @FXML private VBox leftbar;
    @FXML private WebView webView;      //Collegato all'id apposito in fxml; SB li trae dal controller
    @FXML private ScrollPane questionsPane;
    @FXML private Button gotopg;

    //associo la scene al controller e poi i controller ai singoli elementi
    //aggiungo il controller nell'.fxml per poi associare in sb gli elemeti agli id della classe controller
    //NB in fxml il controller è associato solo alla "radice" (borderpane, in qs caso)


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webView.visibleProperty().addListener((observableValue, oldValue, newValue) -> questionsPane.setVisible(!newValue));

        //NB la funzione Constructor(ciccio.class) permette di lavorare con la classe ciccio senza conoscerne
        //preventivamente i campi
        Yaml yaml = new Yaml(new Constructor(Lesson.class));
        //La riga successiva prende il testo dal file di cui il path a dx
        InputStream inputStream = this.getClass().getResourceAsStream("/algat/data.yml");

        //loadAll MAPPA i campi di ogni dato di tipo lessons dopo che IS ha "letto"
        int i=1;
        for (Object object: yaml.loadAll(inputStream)) {
            if (object instanceof Lesson) {                     //SE il dato lessons ha i campi del TIPO Lesson va avanti
                Lesson lesson = (Lesson) object;                //Cast
                //Creo un bottone
                Button button = new Button(lesson.getTitle());  //NB NON uso this perchè porta a LessonController, dove sono ora
                //Aggiungo il bottone alla lista di figli di leftbar
                button.setOnAction(actionEvent -> {             //NB Lambda functions;passare f come parametri; actionevent ha una classe sua;
                    //Corpo della funzione associata all'ActionEvent
                    try {
                        String filePath = getClass().getResource("/algat/lessons/" + lesson.getFileName()).getPath().substring(1).replaceAll("%20", " ");
                        String content = new String(Files.readAllBytes(new File(filePath).toPath()));
                        webView.getEngine().loadContent(content, "text/html");
                        webView.setVisible(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

                Button button2 = new Button("Domande " + i);
                button2.setOnAction(actionEvent -> {
                    webView.setVisible(false);
                    VBox questionsVBox = new VBox();
                    for (Question question : lesson.getQuestions()) {
                        VBox qsContainer = new VBox();
                        qsContainer.getChildren().add(new Text(question.getText()));

                        ToggleGroup answersGroup = new ToggleGroup();
                        for (String answer : question.getAnswers()) {
                            RadioButton radioButton = new RadioButton(answer);
                            radioButton.setToggleGroup(answersGroup);
                            qsContainer.getChildren().add(radioButton);
                        }

                        questionsVBox.getChildren().add(qsContainer);
                    }

                    questionsPane.setContent(questionsVBox);
                });
                leftbar.getChildren().addAll(button, button2); //Aggiunge un figlio al nodo leftbar dell'albero SceneTree
            }
            i++;


        }

        Button firstButton = (Button) leftbar.getChildren().get(0); //Io bottone per Ia lez
        firstButton.fire(); //Evento su bottone
    }
    //Chiamato in auto da jfx quando è caricato il controller; a differenza del costruttore, dà già valori ai campi
    //Non si incorre in nullpointer
}
