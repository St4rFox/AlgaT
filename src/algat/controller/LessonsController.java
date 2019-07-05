package algat.controller;

import algat.model.Lesson;
import algat.model.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Stage stage;

    //associo la scene al controller e poi i controller ai singoli elementi
    //aggiungo il controller nell'.fxml per poi associare in sb gli elemeti agli id della classe controller
    //NB in fxml il controller è associato solo alla "radice" (borderpane, in qs caso)


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionsPane.setVisible(false);
        webView.visibleProperty().addListener((observableValue, oldValue, newValue) -> questionsPane.setVisible(!newValue));

        //NB la funzione Constructor(ciccio.class) permette di lavorare con la classe ciccio senza conoscerne
        //preventivamente i campi
        Yaml yaml = new Yaml(new Constructor(Lesson.class));
        //La riga successiva prende il testo dal file di cui il path a dx
        InputStream inputStream = this.getClass().getResourceAsStream("/algat/data.yml");

        //loadAll MAPPA i campi di ogni dato di tipo lessons dopo che IS ha "letto"
        int i=0;
        for (Object object: yaml.loadAll(inputStream)) {
            if (object instanceof Lesson) {                     //SE il dato lessons ha i campi del TIPO Lesson va avanti
                Lesson lesson = (Lesson) object;                //Cast
                //Creo un bottone
                Button button = new Button(lesson.getTitle());  //NB NON uso this perchè porta a LessonController, dove sono ora
                //Aggiungo il bottone alla lista di figli di leftbar
                if (i != 0) {
                    button.setVisible(false);
                    button.setDisable(true);
                }
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

                Button button2 = SetQuestions(lesson, i);
                leftbar.getChildren().addAll(button, button2); //Aggiunge un figlio al nodo leftbar dell'albero SceneTree
            }
            i = i+2;


        }

        Button firstButton = (Button) leftbar.getChildren().get(0); //Io bottone per Ia lez

        firstButton.fire(); //Evento su bottone

    }
    //Chiamato in auto da jfx quando è caricato il controller; a differenza del costruttore, dà già valori ai campi
    //Non si incorre in nullpointer


    private Button SetQuestions (Lesson lesson, int i) {
        Button button2 = new Button("Domande " + (i/2 + 1));
        if (i != 0) {
            button2.setVisible(false);
            button2.setDisable(true);
        }
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
            Button checkAnswers = new Button("Correzione");

            VBox boxDomandeEBottone = new VBox();
            boxDomandeEBottone.getChildren().add(questionsVBox);
            boxDomandeEBottone.getChildren().add(checkAnswers);
            questionsPane.setContent(boxDomandeEBottone);

            checkAnswers.setOnAction(actionEvent1 -> {
                int c = 0;
                boolean allGood = true;
                for(Node node:questionsVBox.getChildren()) {
                    VBox vbNode = (VBox) node;
                    RadioButton firstAnswer = (RadioButton) vbNode.getChildren().get(1);
                    int indiceRispostaCorretta = lesson.getQuestions().get(c).getAnswer();
                    System.out.println(indiceRispostaCorretta);
                    if (firstAnswer.getToggleGroup().getToggles().get(indiceRispostaCorretta).isSelected()){
                        //Reazione se la risposta è corretta: colora di verde il pulsanteù
                        RadioButton rispostaScelta = (RadioButton) firstAnswer.getToggleGroup().getSelectedToggle();
                        rispostaScelta.getStyleClass().add("green-radio-button");


                    }
                    else {
                        //Reazione se la risposta è sbagliata: colora di rosso il pulsante
                        RadioButton rispostaScelta = (RadioButton) firstAnswer.getToggleGroup().getSelectedToggle();
                        allGood = false;
                        rispostaScelta.getStyleClass().add("red-radio-button");

                    }
                    c++;
                }
                if (allGood) {
                    //sblocca pulsanti successivi
                    leftbar.getChildren().get(i+2).setDisable(false);
                    leftbar.getChildren().get(i+3).setDisable(false);
                    leftbar.getChildren().get(i+2).setVisible(true);
                    leftbar.getChildren().get(i+3).setVisible(true);
                }
            });




        });
        return button2;

    }

    public void goToPlayground(ActionEvent evento) {
        try {
            Parent playground = FXMLLoader.load(getClass().getResource("/algat/view/Playground.fxml"));
            stage.setScene(new Scene(playground));
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setStage (Stage stage) {
        this.stage = stage;
    }
}
