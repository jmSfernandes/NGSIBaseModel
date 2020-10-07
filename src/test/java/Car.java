import annotations.NGSIEncoded;
import annotations.NGSIIgnore;

import java.util.Date;
import java.util.List;

public class Car extends NGSIBaseModel{
    String id;
    String model;
    String color;

    List<String> variations;

    @NGSIIgnore
    String ignoreMe;

    @NGSIEncoded
    String encodeMe;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getVariations() {
        return variations;
    }

    public void setVariations(List<String> variations) {
        this.variations = variations;
    }

    public String getIgnoreMe() {
        return ignoreMe;
    }

    public void setIgnoreMe(String ignoreMe) {
        this.ignoreMe = ignoreMe;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEncodeMe() {
        return encodeMe;
    }

    public void setEncodeMe(String encodeMe) {
        this.encodeMe = encodeMe;
    }


}
