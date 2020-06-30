package assignment.utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageUtility {
    private ImageUtility() {

    }

    public static BufferedImage copyImage(final BufferedImage source){
        BufferedImage dest = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

        Graphics g = dest.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        return dest;
    }

    public static BufferedImage rotateImageClockwise(final BufferedImage source, double degree) {
        final int width = source.getWidth();
        final int height = source.getHeight();

        BufferedImage dest = new BufferedImage(height, width, source.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI * degree / 180.0, height / 2, width / 2);
        graphics2D.drawRenderedImage(source, null);

        return dest;
    }
}
