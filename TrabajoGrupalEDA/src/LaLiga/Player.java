package LaLiga;

import java.io.*;

public class Player {
    private int age;
    private int cost;
    private String name;
    private String position;

    public Player(String name, int age, String position, int cost) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.cost = cost;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public void growOld() {
        age++;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}