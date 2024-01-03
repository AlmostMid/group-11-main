package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuBarFactory {

    public static MenuBar create(Stage stage, Controller ctrl) {
        final MenuBar bar = new MenuBar();

        MenuController mctrl = new MenuController(ctrl);

        Menu fileMenu = createFileMenu(stage, mctrl);
        Menu help = new Menu("Help");


        bar.getMenus().setAll(fileMenu, help);
        bar.setUseSystemMenuBar(true);

        return bar;
    }

    private static Menu createFileMenu(Stage stage, MenuController mctrl) {
        Menu file = new Menu("File");
        file.getItems().setAll(
            createNewImageMenuItem(mctrl),
            createLoadImageMenuItem(stage, mctrl));
        return file;
    }

    private static MenuItem createLoadImageMenuItem(Stage stage, MenuController mctrl) {
        MenuItem menuItem = new MenuItem("Load Image");
        menuItem.setOnAction(event -> mctrl.onLoadImage(stage, event));
        return menuItem;
    }

    private static MenuItem createNewImageMenuItem(MenuController mctrl) {
        MenuItem newImage = new MenuItem("New Image");
        newImage.setOnAction(event -> mctrl.onNewImage(event));
        return newImage;
    }


    private static class MenuController {
        final Controller ctrl;

        private MenuController(Controller ctrl) {
            this.ctrl = ctrl;
        }

        private void onNewImage(ActionEvent event) {
            var dialog = new Dialog<>();
            dialog.setTitle("New Image");
            dialog.setContentText("Create a new image.");
            dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.APPLY);
            dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.add(new Label("Width:"), 0, 0);
            TextField widthInput = new TextField("32");
            gridPane.add(widthInput, 1, 0);

            gridPane.add(new Label("Height:"), 0, 1);
            TextField heightInput = new TextField("32");
            gridPane.add(heightInput, 1, 1);
            dialog.getDialogPane()
                .setContent(gridPane);

            dialog.showAndWait()
                .ifPresent(response -> {
                    if (response instanceof ButtonType
                        && ((ButtonType) response).getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                        return;
                    }
                    try {
                        int width = Integer.parseInt(widthInput.getText());
                        int height = Integer.parseInt(heightInput.getText());
                        ctrl.setImage(new Image(new Rect(width, height)));
                    } catch (NumberFormatException e) {
                        error("Width and Height can only be integers");
                    }
                });
        }

        public void onLoadImage(Stage stage, ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Image");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                throw new UnsupportedOperationException("not yet implemented: How to load a file " + selectedFile);
            }
        }
    }

    private static void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(s);
        alert.showAndWait();
    }
}