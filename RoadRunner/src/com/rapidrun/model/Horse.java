package com.rapidrun.model;

import javafx.beans.property.*;

public class Horse {
    private final StringProperty id = new SimpleStringProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final StringProperty jockeyName = new SimpleStringProperty(this, "jockeyName");
    private final IntegerProperty age = new SimpleIntegerProperty(this, "age");
    private final StringProperty breed = new SimpleStringProperty(this, "breed");
    private final StringProperty raceRecord = new SimpleStringProperty(this, "raceRecord");
    private final StringProperty group = new SimpleStringProperty(this, "group");
    private final StringProperty imagePath = new SimpleStringProperty(this, "imagePath");

    public Horse(String id, String name, String jockeyName, int age, String breed, String raceRecord, String group, String imagePath) {
        this.id.set(id);
        this.name.set(name);
        this.jockeyName.set(jockeyName);
        this.age.set(age);
        this.breed.set(breed);
        this.raceRecord.set(raceRecord);
        this.group.set(group);
        this.imagePath.set(imagePath);
    }

    // Property accessors
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty jockeyNameProperty() { return jockeyName; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty breedProperty() { return breed; }
    public StringProperty raceRecordProperty() { return raceRecord; }
    public StringProperty groupProperty() { return group; }
    public StringProperty imagePathProperty() { return imagePath; }

    // Standard getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getJockeyName() { return jockeyName.get(); }
    public void setJockeyName(String jockeyName) { this.jockeyName.set(jockeyName); }

    public int getAge() { return age.get(); }
    public void setAge(int age) { this.age.set(age); }

    public String getBreed() { return breed.get(); }
    public void setBreed(String breed) { this.breed.set(breed); }

    public String getRaceRecord() { return raceRecord.get(); }
    public void setRaceRecord(String raceRecord) { this.raceRecord.set(raceRecord); }

    public String getGroup() { return group.get(); }
    public void setGroup(String group) { this.group.set(group); }

    public String getImagePath() { return imagePath.get(); }
    public void setImagePath(String imagePath) { this.imagePath.set(imagePath); }

    public String toFileString() {
        return String.join(",",
                getId(),
                getName(),
                getJockeyName(),
                String.valueOf(getAge()),
                getBreed(),
                getRaceRecord(),
                getGroup(),
                getImagePath());
    }




    //==================================================================================
    //After race is run

    private DoubleProperty raceTime = new SimpleDoubleProperty();

    // Getter and Setter for raceTime
    public double getRaceTime() {
        return raceTime.get();
    }

    public void setRaceTime(double raceTime) {
        this.raceTime.set(raceTime);
    }

    public DoubleProperty raceTimeProperty() {
        return raceTime;
    }

}
