package dtu.compute.pixels.util;

import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class ImageUtils {

  public static void writePNG(File file, Image image) throws IOException {
    RenderedImage render = asBufferedImage(image);
    ImageIO.write(render, "png", file);
  }

  public static Image readPNG(File file) throws IOException {
    BufferedImage a = ImageIO.read(file);
    var width = a.getWidth();
    var height = a.getHeight();
    Image image = new Image(new Rect(width, height));
    a.getRGB(0, 0, width, height, image.getBuffer(), 0, width);
    return image;
  }

  public static BufferedImage asBufferedImage(Image image) {
    final int width = image.getSize().width();
    final int height = image.getSize().height();
    BufferedImage render = new BufferedImage(image.getSize().width(), image.getSize().height(), BufferedImage.TYPE_INT_ARGB);
    render.setRGB(0, 0, width, height, image.getBuffer(), 0, width);
    return render;
  }

  public static javafx.scene.image.Image asJavaFXImage(Image image) {
    final int width = image.getSize().width();
    final int height = image.getSize().height();
    WritableImage out = new WritableImage(width, height);
    var pw = out.getPixelWriter();
    pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), image.getBuffer(), 0, width);
    return out;
  }

}
