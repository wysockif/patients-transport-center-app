package pl.group2.optimizer.impl.items;

import java.awt.*;
import java.util.zip.DataFormatException;

public interface Items {
    Object[] convertAttributes(String[] attributes) throws DataFormatException;

    void validateAttributes(Object[] attributes) throws DataFormatException;

    void addNewElement(Object[] attributes);

    void draw(Graphics g, double scalaX, double scalaY, int minX, int minY);
}