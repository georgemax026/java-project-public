package main.java.myApp.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// utility methods to resize and compress the jpg coming from adding movies/series
public class ImageUtils {
    public static void resizeAndSaveJpg(File input, File output, int maxWidth, int maxHeight, float quality) throws IOException {
        BufferedImage original = ImageIO.read(input);

        // scale factor
        double scale = Math.min((double) maxWidth / original.getWidth(), (double) maxHeight / original.getHeight());
        int newW = (int) (original.getWidth() * scale);
        int newH = (int) (original.getHeight() * scale);

        // draw scaled image
        Image scaled = original.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        // save compressed JPG
        ImageIO.write(resized, "jpg", output);
    }
}
