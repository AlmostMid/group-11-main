package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.Observer;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.ImageUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


public class Pixels extends Application implements Observer {

  private final static Rect START_SIZE = new Rect(1000, 700);
  private final static Rect CANVAS_SIZE = new Rect(600, 600);
  private final Controller ctrl;
  private Canvas canvas;

  public Pixels() {
    ctrl = new Controller()
        .setColor(Color.fromARGB(0xff000000))
        .setTool(new Pen())
        .setImage(new Image(new Rect(32, 32)));
  }

  public static void main(String[] args) {
    // Calls the Javafx launch method, which in turn will call start()
    launch(args);
  }


  /**
   * The start method, which sets up the scene.
   *
   * @param stage the primary stage for this application, onto which the application scene can be
   *              set. Applications may create other stages, if needed, but they will not be primary
   *              stages.
   */
  @Override
  public void start(Stage stage) {
    final BorderPane layout = new BorderPane();

    layout.setTop(MenuBarFactory.create(stage, ctrl));

    canvas = createCanvas(CANVAS_SIZE);
    layout.setCenter(canvas);

    // Setup toolbar
    layout.setBottom(ToolBarFactory.create(ctrl));

    Scene scene = new Scene(layout, START_SIZE.width(), START_SIZE.height(),
        javafx.scene.paint.Color.GRAY);

    stage.setTitle("Pixels");
    stage.setScene(scene);
    stage.show();

    ctrl.addObserver(this);
  }


  private Canvas createCanvas(Rect size) {
    Canvas view = new Canvas(size.width(), size.height());
    final var context = view.getGraphicsContext2D();
    context.setImageSmoothing(false);

    view.setOnMousePressed(e -> {
      if (e.getButton() == MouseButton.PRIMARY) {
        ctrl.press(getPointFromEvent(size, e));
      } else {
        ctrl.abandon();
      }
    });

    view.setOnMouseReleased(e -> {
      if (e.isPrimaryButtonDown() || e.isSecondaryButtonDown()) {
        return;
      }
      ctrl.release(getPointFromEvent(size, e));
    });

    view.setOnMouseDragged(e -> ctrl.update(getPointFromEvent(size, e)));
    view.setOnMouseMoved(e -> ctrl.update(getPointFromEvent(size, e)));

    return view;
  }

  private Point getPointFromEvent(Rect size, MouseEvent e) {
    Rect bufferSize = ctrl.getImage().getSize();
    double x = e.getX();
    double y = e.getY();
    int px = (int) Math.floor(x / size.width() * bufferSize.width());
    int py = (int) Math.floor(y / size.height() * bufferSize.height());
    return new Point(px, py);
  }

  public void redraw() {
    GraphicsContext ctx = canvas.getGraphicsContext2D();
    ctx.setFill(Paint.valueOf("white"));
    ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    javafx.scene.image.Image img = ImageUtils.asJavaFXImage(ctrl.getImage());
    ctx.drawImage(img,
        0, 0, img.getWidth(), img.getHeight(),
        0, 0, canvas.getWidth(), canvas.getHeight());
    javafx.scene.image.Image himg = ImageUtils.asJavaFXImage(ctrl.getScratch());
    ctx.drawImage(himg,
        0, 0, img.getWidth(), img.getHeight(),
        0, 0, canvas.getWidth(), canvas.getHeight());
  }

  @Override
  public void onChange() {
    redraw();
  }
}
