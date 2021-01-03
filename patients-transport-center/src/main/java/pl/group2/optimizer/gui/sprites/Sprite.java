package pl.group2.optimizer.gui.sprites;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import static java.awt.image.AffineTransformOp.TYPE_BILINEAR;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public interface Sprite {
    default BufferedImage scaleCell(BufferedImage imageToScale, double scale) {
        int w = imageToScale.getWidth();
        int h = imageToScale.getHeight();
        BufferedImage after = new BufferedImage((int) Math.round(scale * w), (int) Math.round(scale * h), TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, TYPE_BILINEAR);
        return scaleOp.filter(imageToScale, after);
    }
}
