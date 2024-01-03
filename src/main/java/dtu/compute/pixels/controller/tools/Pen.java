package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;

public class Pen implements Tool {

  private boolean pressed = false;

  @Override
  public void press(Controller ctrl, Point point) {
    pressed = true;
    update(ctrl, point);
  }

  @Override
  public void update(Controller ctrl, Point point) {
    if (!pressed) ctrl.resetScratch(false);
    ctrl.setScratchPixel(point, ctrl.getColor());
  }

  @Override
  public void release(Controller ctrl, Point point) {
    update(ctrl, point);
    pressed = false;
    ctrl.commitScratch();
  }

  @Override
  public void abandon(Controller ctrl) {
    pressed = false;
    ctrl.resetScratch(true);
  }
}
