package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.util.ColorUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;

public class ToolBarFactory {

  public static ToolBar create(Controller ctrl) {
    ToolBar tb = new ToolBar();
    ColorPicker picker = createColorPicker(ctrl);
    tb.getItems().addAll(picker);

    // The effect to indicate if something is chosen.
    Effect chosen = new Bloom();

    // Add tools here
    Button pen = new Button("Pen");
    pen.setOnAction(e -> {
      ctrl.setTool(new Pen());
    });

    // And here.
    tb.getItems().addAll(pen);

    // indicate which tool is activated
    ctrl.addObserver(() -> {
      pen.setEffect(null);
      if (ctrl.getTool() instanceof Pen) {
        pen.setEffect(chosen);
      }
    });


    return tb;
  }

  private static ColorPicker createColorPicker(Controller ctrl) {
    ColorPicker picker = new ColorPicker();
    picker.getStyleClass().add("button");
    picker.setValue(ColorUtils.fromColor(ctrl.getColor()));
    picker.setOnAction(e -> {
      ctrl.setColor(ColorUtils.toColor(picker.getValue()));
    });

    // If the color changes; then change the value of the picker.
    ctrl.addObserver(() -> {
      picker.setValue(ColorUtils.fromColor(ctrl.getColor()));
    });

    return picker;
  }


}
