package cs.bham.ac.uk.assignment3.models;

public class Food {
    Integer id;
    String name;
    String meal;
    Integer time;

    public Food(Integer id, String name, String meal, Integer time) {
        this.id = id;
        this.name = name;
        this.meal = meal;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeal() {
        return meal;
    }

    public Integer getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name;
    }
}
