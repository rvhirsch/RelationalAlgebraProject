/**
 * Created by Josh on 2/19/2017.
 */

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class gui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("RAGUI.fxml"));
        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("Relational Algebra");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
