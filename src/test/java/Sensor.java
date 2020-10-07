import annotations.NGSIEncoded;
import annotations.NGSIIgnore;

import java.util.Date;
import java.util.List;

public class Sensor extends NGSIBaseModel{
    String id;
    String model;

    List<Accelerometer> accelerometerList;

    Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Accelerometer> getAccelerometerList() {
        return accelerometerList;
    }
    public void setAccelerometerList(List<Accelerometer> accelerometerList) {
        this.accelerometerList = accelerometerList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }




}
