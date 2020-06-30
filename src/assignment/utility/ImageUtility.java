package assignment.utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageUtility {
    private ImageUtility() {

    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage rotateImageClockwise(BufferedImage src, double degree) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI * degree / 180.0, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }
}
