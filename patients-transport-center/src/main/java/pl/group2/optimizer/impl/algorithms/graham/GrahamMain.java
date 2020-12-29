package pl.group2.optimizer.impl.algorithms.graham;

import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObject;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

import java.util.Map;


// KLASA DO SKASOWANIA - pokazuje działanie


public class GrahamMain {
    public static void main(String[] args) {
        Graham graham = new Graham();

        Hospital hospital1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 1000, 100);
        Hospital hospital2 = new Hospital(2, "Krakowski Szpital Kliniczny", 100, 120, 999, 99);
        Hospital hospital3 = new Hospital(3, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0);
        Hospital hospital4 = new Hospital(4, "Drugi Szpital im. Naczelnika RP", 10, 140, 70, 1);
        Hospital hospital5 = new Hospital(5, "Trzeci Szpital im. Króla RP", 140, 10, 996, 0);
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
        graham.findConvexHull();
    }
}
