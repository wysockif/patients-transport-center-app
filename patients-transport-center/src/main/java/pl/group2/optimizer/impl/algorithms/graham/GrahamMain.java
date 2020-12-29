package pl.group2.optimizer.impl.algorithms.graham;

import pl.group2.optimizer.impl.items.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObject;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

import javax.swing.*;
import java.awt.*;
import java.util.List;


// KLASA DO SKASOWANIA - pokazuje działanie


public class GrahamMain {
    public static void main(String[] args) {
        Graham graham = new Graham();

        Hospital hospital1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 1000, 100);
        Hospital hospital2 = new Hospital(2, "Krakowski Szpital Kliniczny", 100, 120, 999, 99);
        Hospital hospital3 = new Hospital(3, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0);
        Hospital hospital4 = new Hospital(4, "Drugi Szpital im. Naczelnika RP", 10, 140, 70, 1);
        Hospital hospital5 = new Hospital(5, "Trzeci Szpital im. Króla RP", 140, 10, 996, 0);
//        Hospital hospital5 = new Hospital(5, "Trzeci Szpital im. Króla RP", 40, 10, 996, 0);


        Hospitals hospitals = new Hospitals();
        hospitals.add(hospital1);
        hospitals.add(hospital2);
        hospitals.add(hospital3);
        hospitals.add(hospital4);
        hospitals.add(hospital5);

        SpecialObject specialObject1 = new SpecialObject(1, "Pomnik Wikipedii", -1, 50);
        SpecialObject specialObject2 = new SpecialObject(2, "Pomnik Fryderyka Chopina", 110, 55);
        SpecialObject specialObject3 = new SpecialObject(3, "Pomnik Anonimowego Przechodnia", 40, 70);
        SpecialObjects specialObjects = new SpecialObjects();
        specialObjects.add(specialObject1);
        specialObjects.add(specialObject2);
        specialObjects.add(specialObject3);

        graham.loadPoints(hospitals, specialObjects);

        List<Point> convexHull = graham.findConvexHull();

        JFrame jFrame = new JFrame("Graham");
        int size = 700;
        jFrame.setSize(size + 50, size + 50);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawLine(100, 0, 100, 1000);
                g.drawLine(0, size - 100, 1000, size - 100);
                g.setColor(Color.blue);
                for (Hospital h : hospitals.getCollection()) {
                    g.fillOval(h.getXCoordinate() * 3 + 100 - 5, size - (h.getYCoordinate() * 3 + 100 + 5), 10, 10);
                    g.drawString("(" + h.getXCoordinate() + "," + h.getYCoordinate() + ")", h.getXCoordinate() * 3 + 100 - 20, size - (h.getYCoordinate() * 3 + 100 + 5));
                }
                g.setColor(Color.green.darker().darker());
                for (SpecialObject s : specialObjects.getCollection()) {
                    g.fillOval(s.getXCoordinate() * 3 + 100 - 5, size - (s.getYCoordinate() * 3 + 100 + 5), 10, 10);
                    g.drawString("(" + s.getXCoordinate() + "," + s.getYCoordinate() + ")", s.getXCoordinate() * 3 + 100 - 20, size - (s.getYCoordinate() * 3 + 100 + 5));

                }

                if (convexHull.size() < 1)
                    throw new IllegalArgumentException("Tak nie można");

                g.setColor(Color.gray);
                for (int i = 1; i < convexHull.size(); i++) {
                    int x1 = convexHull.get(i - 1).getXCoordinate();
                    int y1 = convexHull.get(i - 1).getYCoordinate();
                    int x2 = convexHull.get(i).getXCoordinate();
                    int y2 = convexHull.get(i).getYCoordinate();
                    g.drawLine(x1 * 3 + 100, size - (y1 * 3 + 100), x2 * 3 + 100, size - (y2 * 3 + 100));
                }


                int x1 = convexHull.get(0).getXCoordinate();
                int y1 = convexHull.get(0).getYCoordinate();
                int x2 = convexHull.get(convexHull.size() - 1).getXCoordinate();
                int y2 = convexHull.get(convexHull.size() - 1).getYCoordinate();
                g.drawLine(x1 * 3 + 100, size - (y1 * 3 + 100), x2 * 3 + 100, size - (y2 * 3 + 100));
            }

        };
        jFrame.add(jPanel);
        jFrame.setVisible(true);


    }
}