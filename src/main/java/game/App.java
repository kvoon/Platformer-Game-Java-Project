package game;

import game.model.GameEngineImplementation;
import javafx.application.Application;
import javafx.stage.Stage;
import game.model.GameEngine;
import game.model.GameEngineImplementation;
import game.view.GameWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class App extends Application {

    ArrayList<String> pathList = new ArrayList<String>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<String, String> params = getParameters().getNamed();

        String s = "Java 11 sanity check";
        if (s.isBlank()) {
            throw new IllegalStateException("You must be running Java 11+. You won't ever see this exception though" +
                    " as your code will fail to compile on Java 10 and below.");
        }

        pathList.add("level_1.json");
        pathList.add("TEST_1.json");
        pathList.add("TEST_2.json");
        pathList.add("TEST_3.json");// game terminator.
        GameEngine model = new GameEngineImplementation(pathList, 600);
        GameWindow window = new GameWindow(model, 960, 600);
        window.run();

        primaryStage.setTitle("Stickman");
        primaryStage.setScene(window.getScene());
        primaryStage.show();
    }
}
