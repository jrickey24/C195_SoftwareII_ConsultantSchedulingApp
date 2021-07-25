package Model;

import java.time.LocalDateTime;


public class Appointment {


    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;

    public Appointment (int appointmentId, String title, String description,String location, String type,
                        LocalDateTime startTime, LocalDateTime endTime, int customerId, int userId,
                        int contactId,String contactName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    public Appointment(){}

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setContactId(int contactId) { this.contactId = contactId; }
    public void setContactName(String contactName) {this.contactName = contactName;}
    public int getAppointmentId() { return appointmentId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getCustomerId() { return customerId; }
    public int getUserId(){ return userId; }
    public int getContactId() { return contactId; }
    public String getContactName() {return contactName;}
}
