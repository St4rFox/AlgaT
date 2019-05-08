package algat;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class MyController {

    public void oneStepBack(MouseEvent event) {
        System.out.println("oneStepBack clicked!");
    }

    public void oneStepForward(MouseEvent event) {
        System.out.println("oneStepForward clicked!");
    }

    public void selectedStepBack(MouseEvent event) {
        System.out.println("selectedStepBack clicked!");
    }

    public void selectedStepForward(MouseEvent event) {
        System.out.println("selectedStepForward clicked!");
    }

    public void removeKey(MouseEvent event) {
        System.out.println("removeKey clicked!");
    }

    public void insertKeyValue(MouseEvent event) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New Entry");
        dialog.setHeaderText("Insert a new entry into the table");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField keyField = new TextField();
        keyField.setPromptText("Enter key for entry");
        TextField valueField = new TextField();
        valueField.setPromptText("Enter value for entry");

        grid.add(new Label("Key:"), 0, 0);
        grid.add(keyField, 1, 0);
        grid.add(new Label("Value:"), 0, 1);
        grid.add(valueField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(keyField.getText(), valueField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            System.out.println("Username=" + pair.getKey() + ", Password=" + pair.getValue());
        });
    }

    public void findKey(MouseEvent event) {
        System.out.println("findKey clicked!");
    }

    public void upload(MouseEvent event) {
        System.out.println("upload clicked!");
    }

    public void sliderController(MouseEvent event) {
        System.out.println("Dragging");
    }
}
