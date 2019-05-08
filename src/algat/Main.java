package algat;

import algat.hashtable.ExternalHashTable;
import algat.hashtable.Hasher;
import algat.hashtable.InternalHashTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource("AlternativeUI.fxml"));
        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        ExternalHashTable table = new ExternalHashTable(10, Hasher.NAIVE);
        table.put("hello", "ciao");
        table.put("dog", "cane");
        table.put("cat", "gatto");
        table.put("horse", "cavallo");
        System.out.println(table);
        System.out.println(table.contains("horse"));
        table.remove("horse");
        System.out.println(table);
        System.out.println(table.contains("horse"));
        table.remove("horse");
        System.out.println(table);
        System.out.println(table.contains("horse"));

        launch(args);
    }
}
