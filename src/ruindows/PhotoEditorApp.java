package ruindows;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class PhotoEditorApp {

	Scene scene;
    Stage mainStage;
    ImageView imageView;
    double zoomFactor = 1.0;
    int rotation = 0;
    
    static final Map<String, String> fileMapping = new HashMap<>();
    static final Map<String, Stage> openWindows = new HashMap<>();

    final String filename;

    public PhotoEditorApp(String filename, String originalImageFile) {
        fileMapping.put(filename, originalImageFile);
        this.filename = filename;
        openEditor();
    }

    public static void renameVirtualFile(String oldName, String newName) {
        if (fileMapping.containsKey(oldName)) {
            String original = fileMapping.remove(oldName);
            fileMapping.put(newName, original);
        }

        if (openWindows.containsKey(oldName)) {
            Stage s = openWindows.remove(oldName);
            openWindows.put(newName, s);
        }
    }

    public static void updateOpenWindowFilename(String oldName, String newName) {
        if (openWindows.containsKey(oldName)) {
            Stage s = openWindows.remove(oldName);
            openWindows.put(newName, s);
        }
    }

    private void openEditor() {
        if (openWindows.containsKey(filename)) {
            openWindows.get(filename).toFront();
            return;
        }

        String resourceFile = fileMapping.get(filename);
        if (resourceFile == null) {
            showError("Missing image source for " + filename);
            return;
        }

        Image image;
        try {
            image = new Image(PhotoEditorApp.class.getResource(resourceFile).toExternalForm());
        } catch (Exception e) {
            showError("Failed to load image: " + resourceFile);
            return;
        }

        imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(350);
        imageView.setFitHeight(350);

        StackPane centerPane = new StackPane(imageView);
        centerPane.setMinSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        centerPane.setPrefSize(350, 350);
        centerPane.setAlignment(Pos.CENTER);

        Group imageGroup = new Group(centerPane);

        ScrollPane scrollPane = new ScrollPane(imageGroup);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);

        Slider zoomSlider = new Slider(1.0, 3.0, 1.0);
        zoomSlider.setOrientation(Orientation.HORIZONTAL);
        zoomSlider.setBlockIncrement(0.1);

        Label zoomLabel = new Label("Zoom");

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            zoomFactor = newVal.doubleValue();
            centerPane.setScaleX(zoomFactor);
            centerPane.setScaleY(zoomFactor);
        });

        Button rotateButton = new Button("Rotate");
        rotateButton.setOnAction(e -> {
            rotation = (rotation + 90) % 360;
            centerPane.getTransforms().clear();
            double pivotX = centerPane.getWidth() / 2;
            double pivotY = centerPane.getHeight() / 2;
            centerPane.getTransforms().add(new Rotate(rotation, pivotX, pivotY));
        });

        HBox controlBar = new HBox(10, zoomSlider, zoomLabel, rotateButton);
        controlBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setTop(controlBar);
        root.setCenter(scrollPane);

        scene = new Scene(root, 700, 400);

        mainStage = new Stage();
        mainStage.setTitle(filename);
        mainStage.setScene(scene);
        mainStage.setOnCloseRequest(e -> openWindows.remove(filename));
        mainStage.show();

        Platform.runLater(() -> {
            scrollPane.setHvalue(scrollPane.getHmax() / 2);
            scrollPane.setVvalue(scrollPane.getVmax() / 2);
        });

        openWindows.put(filename, mainStage);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
