package dtu.compute.pixels.controller;

import dtu.compute.pixels.controller.tools.Tool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import java.util.ArrayList;
import java.util.List;

public class Controller {

  private final List<Observer> observers;
  private Image image;
  private Image scratch;
  private Tool tool;
  private Color color;
  private boolean abandonActive = false;

  public Controller() {
    observers = new ArrayList<>();
  }


  public Image getImage() {
    return image;
  }

  public Controller setImage(Image image) {
    this.image = image;
    scratch = new Image(image.getSize());
    notifyChange();
    return this;
  }

  public Color getColor() {
    return color;
  }

  public Controller setColor(Color color) {
    this.color = color;
    notifyChange();
    return this;
  }

  public Controller press(Point point) {
    tool.press(this, point);
    return this;
  }

  public Controller abandon() {
    tool.abandon(this);
    return this;
  }

  public Controller release(Point point) {
    tool.release(this, point);
    return this;
  }

  public Controller update(Point point) {
    tool.update(this, point);
    return this;
  }

  public Controller click(Point point) {
    tool.press(this, point);
    tool.release(this, point);
    return this;
  }
  /**
   * Paint the scratch over the current image.
   */
  public void commitScratch() {
    this.image.paintOverWith(this.scratch);
    this.resetScratch(true);
  }

  /**
   * Set the scratch image all transparent.
   *
   * @param notify notify observers that stuff has changed.
   */
  public void resetScratch(boolean notify) {
    this.scratch.reset(Color.TRANSPARENT);
    if (notify) {
      this.notifyChange();
    }
  }

  /**
   * Sets a pixel on the scratch image.
   *
   * @param point
   * @param color
   */
  public void setScratchPixel(Point point, Color color) {
    this.scratch.setPixel(point, color);
    this.notifyChange();
  }

  /**
   * Part of the observer pattern Adds an observer from the list of observers.
   *
   * @param observer, the observer to be notified on change.
   */
  public Controller addObserver(Observer observer) {
    observers.add(observer);
    return this;
  }

  /**
   * Part of the observer pattern. Removes an observer from the list of observers.
   *
   * @param observer, the observer to be no longer notified on change.
   * @return a boolean indicating if the observer was in the list.
   */
  public boolean removeObserver(Observer observer) {
    return observers.remove(observer);
  }

  /**
   * Part of the observer pattern. Notify all observers that the state was changed.
   */
  public void notifyChange() {
    for (Observer o : observers) {
      o.onChange();
    }
  }

  public Image getScratch() {
    return scratch;
  }

  public Tool getTool() {
    return tool;
  }

  public Controller setTool(Tool t) {
    this.tool = t;
    notifyChange();
    return this;
  }
}
