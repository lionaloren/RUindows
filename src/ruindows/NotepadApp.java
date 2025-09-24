package ruindows;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NotepadApp {

	Scene scene;
    Stage mainStage;
    TextArea textArea;
    StackPane homeStack;
    String currentFileName = null;
    Label label;

    static int fileCount = 0;
    static ArrayList<String> virtualFiles = new ArrayList<>();
    
    public NotepadApp() {
        this(null);
    }

    public NotepadApp(StackPane homeStack) {
        this.homeStack = homeStack;

        mainStage = new Stage();
        mainStage.setTitle("Notepad");

        textArea = new TextArea();
        textArea.setFont(Font.font("calibri", 14));
        textArea.setWrapText(true);

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> saveFile());

        fileMenu.getItems().addAll(saveItem);
        menuBar.getMenus().add(fileMenu);

        VBox root = new VBox();
        root.getChildren().addAll(menuBar, textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        root.setPadding(Insets.EMPTY);
        root.setSpacing(0);  

        scene = new Scene(root, 700, 400);
        scene.getRoot().setStyle("-fx-padding: 0; -fx-background-insets: 0;");
        
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
        mainStage.show();
    }

    private void saveFile() {
        if (currentFileName == null) {
            String defaultName = (fileCount == 0) ? "text.txt" : "text" + fileCount + ".txt";

            TextInputDialog renameDialog = new TextInputDialog(defaultName);
        	renameDialog.setTitle("Save file");
        	renameDialog.setHeaderText("Save file");
        	renameDialog.setContentText("Rename file:");

            renameDialog.showAndWait().ifPresent(newName -> {
                if (!newName.toLowerCase().endsWith(".txt")) {
                    newName += ".txt";
                }

                String baseName = newName.substring(0, newName.length() - 4);
                if (!isAlphanumeric(baseName)) {
                    showError("File name is invalid", "File name must be alphanumeric!");
                    return;
                }

                for (String name : virtualFiles) {
                    if (name.equalsIgnoreCase(newName)) {
                        showError("File with that name already exists!", "A file with that name has already been made");
                        return;
                    }
                }

                virtualFiles.add(newName);
                currentFileName = newName;
                if (label == null) {
                    label = new Label(newName);
                } else {
                    label.setText(newName);
                }

                mainStage.setTitle(newName);

                if (homeStack != null) {
                    addShortcutToDesktop(newName);
                }

                fileCount++;
                
                TextFileApp.setVirtualFile(currentFileName, textArea.getText());
            });
        } else {
            mainStage.setTitle(currentFileName);
            
            TextFileApp.setVirtualFile(currentFileName, textArea.getText());
        }
    }

    private void addShortcutToDesktop(String filename) {
        if (homeStack == null) return;

        ImageView icon = new ImageView(new Image(getClass().getResource("notepad-icon.png").toExternalForm()));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        icon.setStyle("-fx-cursor: hand;");
        icon.setOnMouseClicked(e -> new TextFileApp(filename));

        label = new Label(filename);
        label.setPrefWidth(icon.getFitWidth());
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill: black;");

        label.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
            	TextInputDialog renameDialog = new TextInputDialog(label.getText());
            	renameDialog.setTitle("Save file");
            	renameDialog.setHeaderText("Save file");
            	renameDialog.setContentText("Rename file:");

                renameDialog.showAndWait().ifPresent(newName -> {
                    if (!newName.toLowerCase().endsWith(".txt")) {
                        newName += ".txt";
                    }

                    String baseName = newName.substring(0, newName.length() - 4);
                    if (!isAlphanumeric(baseName)) {
                        showError("File name is invalid", "File name must be alphanumeric!");
                        return;
                    }

                    for (String name : virtualFiles) {
                        if (name.equalsIgnoreCase(newName)) {
                            showError("File with that name already exists!", "A file with that name has already been made");
                            return;
                        }
                    }

                    virtualFiles.remove(currentFileName);
                    virtualFiles.add(newName);

                    TextFileApp.renameVirtualFile(currentFileName, newName);
                    TextFileApp.updateOpenWindowFilename(currentFileName, newName);

                    currentFileName = newName;
                    label.setText(newName);
                    mainStage.setTitle(newName);
                    
                    String finalNewName = newName;
                    icon.setOnMouseClicked(event -> new TextFileApp(finalNewName));
                });
            }
        });

        VBox shortcut = new VBox(5, icon, label);
        shortcut.setUserData("notepad-shortcut");
        shortcut.setAlignment(Pos.TOP_LEFT);

        icon.setOnMouseClicked(e -> new TextFileApp(filename));

        HBox shortcutColumns = null;
        for (Node node : homeStack.getChildren()) {
            if (node instanceof HBox) {
                shortcutColumns = (HBox) node;
                break;
            }
        }

        if (shortcutColumns == null) return;

        VBox targetColumn = null;
        for (Node col : shortcutColumns.getChildren()) {
            if (col instanceof VBox) {
                VBox column = (VBox) col;
                if (column.getChildren().size() < 5) {
                    targetColumn = column;
                    break;
                }
            }
        }

        if (targetColumn == null) {
            targetColumn = new VBox(30);
            shortcutColumns.getChildren().add(targetColumn);
        }

        targetColumn.getChildren().add(shortcut);
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!(ch >= 'A' && ch <= 'Z') && !(ch >= 'a' && ch <= 'z') && !(ch >= '0' && ch <= '9')) {
                return false;
            }
        }
        return true;
    }
}