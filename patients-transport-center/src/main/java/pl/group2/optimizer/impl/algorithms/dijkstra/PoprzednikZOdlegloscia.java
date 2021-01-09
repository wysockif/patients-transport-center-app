package pl.group2.optimizer.impl.algorithms.dijkstra;

public class PoprzednikZOdlegloscia {
    int id;
    int odleglosc;

    public int getId() {
        return id;
    }

    public int getOdleglosc() {
        return odleglosc;
    }

    public PoprzednikZOdlegloscia(int id, int odleglosc) {
        this.id = id;
        this.odleglosc = odleglosc;
    }
}
