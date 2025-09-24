package ruindows;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TextFileApp {

	Scene scene;
    Stage mainStage;
    String filename;
    TextArea textArea;

    static HashMap<String, String> virtualFileDatabase = new HashMap<>();
    static HashMap<String, TextFileApp> openWindows = new HashMap<>();
    
    public TextFileApp(String filename) {
        this.filename = filename;

        mainStage = new Stage();
        mainStage.setTitle(filename);

        textArea = new TextArea();
        textArea.setFont(Font.font("calibri", 14));
        textArea.setWrapText(true);
        textArea.setText(virtualFileDatabase.getOrDefault(filename, ""));

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> saveFile());
        fileMenu.getItems().add(saveItem);
        menuBar.getMenus().add(fileMenu);

        VBox root = new VBox(menuBar, textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);

        scene = new Scene(root, 600, 400);
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
        mainStage.show();

        openWindows.put(filename, this);
    }

    private void saveFile() {
        String content = textArea.getText();
        virtualFileDatabase.put(filename, content);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText(null);
        alert.setContentText("Changes to '" + filename + "' have been saved.");
        alert.showAndWait();
    }

    public static void renameVirtualFile(String oldName, String newName) {
        if (virtualFileDatabase.containsKey(oldName)) {
            String content = virtualFileDatabase.remove(oldName);
            virtualFileDatabase.put(newName, content);
        }
    }

    public static void updateOpenWindowFilename(String oldName, String newName) {
        TextFileApp app = openWindows.remove(oldName);
        if (app != null) {
            app.filename = newName;
            app.mainStage.setTitle(newName);
            openWindows.put(newName, app);
        }
    }

    public static void setVirtualFile(String filename, String content) {
        virtualFileDatabase.put(filename, content);
    }

    public static boolean hasVirtualFile(String filename) {
        return virtualFileDatabase.containsKey(filename);
    }
}
