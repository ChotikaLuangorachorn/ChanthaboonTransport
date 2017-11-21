package models;

import java.io.Serializable;

public class JobType implements Serializable {
    private int id;
    private String description;

    public JobType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "JobType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
