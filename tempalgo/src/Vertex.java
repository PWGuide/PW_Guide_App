public class Vertex {
    final private String name;
    final private double x;
    final private double y;
    final private int floor;


    public Vertex( String name, double x, double y, int floor) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.floor = floor;
    }


    public String getName() {
        return name;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return name;
    }

}