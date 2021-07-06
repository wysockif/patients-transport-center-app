package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;

import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.Color.BLACK;
import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.GRAY;
import static java.awt.Color.WHITE;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class MouseClickListener implements MouseListener {
    private final Optimizer optimizer;

    public MouseClickListener(Optimizer optimizer) {
        this.optimizer = optimizer;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && optimizer.isRunning()) {
            double scaleX = optimizer.getScaleX();
            double scaleY = optimizer.getScaleY();
            int minX = optimizer.getMinX();
            int minY = optimizer.getMinY();
            int xShift = 0;
            int yShift = 0;
            int x = (int) Math.round((e.getX() - PADDING - MARGIN + xShift + minX * scaleX) / scaleX);
            int y = (int) Math.round((PADDING + HEIGHT - MARGIN - yShift + minY * scaleY - e.getY()) / scaleY);
            optimizer.getPatientsManagement().getXField().setText(String.valueOf(x));
            optimizer.getPatientsManagement().getYField().setText(String.valueOf(y));
            lightUp();
        }
    }

    private void lightUp() {
        optimizer.getPatientsManagement().getXField().setBackground(GRAY);
        optimizer.getPatientsManagement().getXField().setForeground(BLACK);
        optimizer.getPatientsManagement().getYField().setBackground(GRAY);
        optimizer.getPatientsManagement().getYField().setForeground(BLACK);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && optimizer.isRunning()) {
            resetBackground();
        }
    }

    private void resetBackground() {
        optimizer.getPatientsManagement().getXField().setBackground(DARK_GRAY.darker());
        optimizer.getPatientsManagement().getXField().setForeground(WHITE);
        optimizer.getPatientsManagement().getYField().setBackground(DARK_GRAY.darker());
        optimizer.getPatientsManagement().getYField().setForeground(WHITE);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
