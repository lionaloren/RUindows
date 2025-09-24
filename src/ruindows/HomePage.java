package ruindows;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage {

    Scene scene;
    Stage mainStage;
    ImageView background, trashIcon, notepadIcon, chRUmeIcon, windowIcon;
    Label trash, notepad, chRUme;
    Button windowMenu;
    Button notepadMenu;
    MenuItem logoutItem, shutdownItem;
    ContextMenu windowMenuItem;
    BorderPane bp;
    StackPane sp;
    HBox shortcutColumns;

    public HomePage(Stage primaryStage, LoginPage loginPage) {
        this.mainStage = primaryStage;

        background = new ImageView(new Image(getClass().getResource("homepage.jpg").toExternalForm()));
        background.setFitWidth(1200);
        background.setFitHeight(600);

        trashIcon = createShortcutIcon("trash-icon.png");
        trash = createShortcutLabel("Trash");

        notepadIcon = createShortcutIcon("notepad-icon.png");
        notepad = createShortcutLabel("Notepad");

        chRUmeIcon = createShortcutIcon("chrome.png");
        chRUme = createShortcutLabel("ChRUme");

        VBox trashBox = createShortcutBox(trashIcon, trash);
        VBox notepadBox = createShortcutBox(notepadIcon, notepad);
        VBox chRUmeBox = createShortcutBox(chRUmeIcon, chRUme);

        List<VBox> shortcutList = Arrays.asList(trashBox, notepadBox, chRUmeBox);

        shortcutColumns = new HBox(50);
        shortcutColumns.setPadding(new Insets(30));
        shortcutColumns.setAlignment(Pos.TOP_LEFT);

        VBox currentColumn = new VBox(30);
        int count = 0;

        for (VBox shortcut : shortcutList) {
            if (count == 5) {
                shortcutColumns.getChildren().add(currentColumn);
                currentColumn = new VBox(30);
                count = 0;
            }
            currentColumn.getChildren().add(shortcut);
            count++;
        }
        if (!currentColumn.getChildren().isEmpty()) {
            shortcutColumns.getChildren().add(currentColumn);
        }

        windowMenu = new Button();
        windowIcon = new ImageView(new Image(getClass().getResource("window-icon.png").toExternalForm()));
        windowIcon.setFitWidth(24);
        windowIcon.setFitHeight(24);
        windowMenu.setGraphic(windowIcon);
        windowMenu.setStyle("-fx-background-color: transparent;");
        windowMenu.setFocusTraversable(false);

        windowMenuItem = new ContextMenu();
        logoutItem = new MenuItem("Log Out", createIcon("logout3-icon.png", 16));
        logoutItem.setStyle("-fx-text-fill: white;");
        
        shutdownItem = new MenuItem("Shutdown", createIcon("shutdown4-icon.png", 16));
        shutdownItem.setStyle("-fx-text-fill: white;");
        
        windowMenuItem.getItems().addAll(logoutItem, shutdownItem);
        windowMenuItem.setStyle("-fx-background-color: #222222;");

        windowMenu.setOnMouseEntered(e -> {
            windowMenu.setStyle("-fx-background-color: rgba(136,136,136,0.4); -fx-cursor: default; -fx-background-radius: 0;");
        });
        windowMenu.setOnMouseExited(e -> {
            windowMenu.setStyle("-fx-background-color: transparent; -fx-cursor: default; -fx-background-radius: 0;");
        });
        windowMenu.setOnAction(e -> {
            if (windowMenuItem.isShowing()) {
                windowMenuItem.hide();
            } else {
                Bounds bounds = windowMenu.localToScreen(windowMenu.getBoundsInLocal());
                double x = bounds.getMinX();
                double estimatedMenuHeight = windowMenuItem.getItems().size() * 30 + 10;
                double y = bounds.getMinY() - estimatedMenuHeight;
                windowMenuItem.show(windowMenu, x, y);
            }
        });

        notepadMenu = new Button();
        ImageView notepadIconBar = new ImageView(new Image(getClass().getResource("notepad-icon.png").toExternalForm()));
        notepadIconBar.setFitWidth(24);
        notepadIconBar.setFitHeight(24);
        notepadMenu.setGraphic(notepadIconBar);
        notepadMenu.setFocusTraversable(false);
        notepadMenu.setStyle("-fx-background-color: transparent;");
        notepadMenu.setOnMouseEntered(e -> {
            notepadMenu.setStyle("-fx-background-color: rgba(136,136,136,0.4); -fx-cursor: default; -fx-background-radius: 0;");
        });
        notepadMenu.setOnMouseExited(e -> {
            notepadMenu.setStyle("-fx-background-color: transparent; -fx-cursor: default; -fx-background-radius: 0;");
        });
        notepadMenu.setOnAction(e -> openNotepad());

        bp = new BorderPane();
        bp.setPrefSize(1200, 600);

        sp = new StackPane(background);
        sp.getChildren().add(shortcutColumns);
        StackPane.setAlignment(shortcutColumns, Pos.TOP_LEFT);

        bp.setCenter(sp);

        HBox taskBarContainer = new HBox();
        taskBarContainer.setStyle("-fx-background-color: #222222;");
        taskBarContainer.setSpacing(10);
        taskBarContainer.setPadding(new Insets(2, 5, 2, 5));
        taskBarContainer.setAlignment(Pos.CENTER_LEFT);

        HBox windowMenuBar = new HBox(windowMenu);
        windowMenuBar.setStyle("-fx-background-color: transparent;");
        windowMenuBar.setPrefHeight(30);

        taskBarContainer.getChildren().addAll(windowMenuBar, notepadMenu);
        bp.setBottom(taskBarContainer);

        scene = new Scene(bp);
        mainStage.setScene(scene);

        trashIcon.setOnMouseClicked(e -> openTrash());
        notepadIcon.setOnMouseClicked(e -> {
            new NotepadApp(sp);
        });
        chRUmeIcon.setOnMouseClicked(e -> openChRUme());

        logoutItem.setOnAction(e -> {
            try {
                loginPage.start(mainStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        shutdownItem.setOnAction(e -> mainStage.close());
    }

    private ImageView createShortcutIcon(String imageName) {
        ImageView icon = new ImageView(new Image(getClass().getResource(imageName).toExternalForm()));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        icon.setStyle("-fx-cursor: hand;");
        return icon;
    }

    private Label createShortcutLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: black;");
        return label;
    }

    private VBox createShortcutBox(ImageView icon, Label label) {
        label.setPrefWidth(icon.getFitWidth());
        label.setAlignment(Pos.CENTER);
        VBox box = new VBox(5, icon, label);
        return box;
    }

    private ImageView createIcon(String imageName, double size) {
        ImageView icon = new ImageView(new Image(getClass().getResource(imageName).toExternalForm()));
        icon.setFitWidth(size);
        icon.setFitHeight(size);
        return icon;
    }

    private void openTrash() {
        showAlert("Trash", "Trash application opened!");
    }

    private void openNotepad() {
        new NotepadApp(sp);
    }

    private void openChRUme() {
        new ChRUmeApp(sp);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() {
        mainStage.show();
    }
}
