package models;

import java.util.Date;

public class Schedule {
    public static final String RESERVE = "reserve";
    public static final String JOB = "job";
    private String id;
    private Date startDate;
    private Date endDate;
    private String note;
    private String type;

    public Schedule(String id, Date startDate, Date endDate, String note, String type) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
    }

    public Schedule(Date startDate, Date endDate, String note, String type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getNote() {
        return note;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", note='" + note + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
