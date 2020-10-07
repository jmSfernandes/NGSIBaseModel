import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NGSIBaseModelTester {
    private final String path = ".\\src\\test\\java\\";

    @Test
    public void testNGSIIgnore() throws Exception {
        Car test = initCar();

        JsonObject test_json = test.toNGSI();
        Assert.assertFalse(test_json.has("ignoreme"));
    }

    @Test
    public void testNGSIEncode() throws Exception {
        Car test = initCar();
        String expected = "This%20car%20is%20a%20simple%20car%3D%3D%21%22%28with%20a%20red%20hood%29";
        JsonObject test_json = test.toNGSI();
        String actual = test_json.get("encodeme").getAsJsonObject().get("value").getAsString();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testToNGSI() throws Exception {
        Car test = initCar();

        JsonObject car_json = (JsonObject) readJSON("car.json");
        JsonObject test_json = test.toNGSI();

        Assert.assertEquals(test_json, car_json);
    }


    @Test
    public void testNGSIDecode() throws Exception {
        Car test = initCar();
        String expected = "This car is a simple car==!\"(with a red hood)";
        JsonObject car_json = (JsonObject) readJSON("car.json");

        Car test_car = (Car) new Car().fromNGSI(car_json);

        Assert.assertEquals(test_car.getEncodeMe(), expected);
    }

    @Test
    public void testFromNGSI() throws Exception {
        Car test = initCar();

        JsonObject car_json = (JsonObject) readJSON("car.json");
        Car test_car = (Car) new Car().fromNGSI(car_json);

        Assert.assertEquals(test, test_car);
    }

    @Test
    public void testFromNGSIComplexList() throws Exception {
        Sensor expected = initSensor();

        JsonObject json = (JsonObject) readJSON("sensor.json");
        Sensor actual = (Sensor) new Sensor().fromNGSI(json);

        Assert.assertEquals(actual, expected);
    }


    private Car initCar() throws ParseException {
        Car test = new Car();
        test.setId("car_1");
        test.setColor("red");
        test.setModel("corvette");

        test.setEncodeMe("This car is a simple car==!\"(with a red hood)");
        test.setIgnoreMe("I was ignored!");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date timestamp = df.parse("2020-10-07T09:50:00Z");
        test.setTimestamp(timestamp);
        List<String> variations = new ArrayList<String>();
        variations.add("Street");
        variations.add("Lounge");
        variations.add("Easy");
        variations.add("SportsWagon");
        test.setVariations(variations);

        return test;
    }

    private Sensor initSensor() throws ParseException {
        Sensor test = new Sensor();
        test.setId("sensor_1");

        test.setModel("arduino_dth_11");


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date timestamp = df.parse("2020-10-07T09:50:00Z");
        test.setTimestamp(timestamp);
        List<Accelerometer> accelerometers = new ArrayList<Accelerometer>();
        accelerometers.add(new Accelerometer(-0.384399414, 2.519180298, 9.288574219, df.parse("2020-10-06T18:42:14Z")));
        accelerometers.add(new Accelerometer(-0.357467651, 2.469497681, 9.574081421, df.parse("2020-10-06T18:42:14Z")));
        accelerometers.add(new Accelerometer(-0.462814331, 2.379714966, 9.450775146, df.parse("2020-10-06T18:42:14Z")));
        accelerometers.add(new Accelerometer(-0.405960083, 2.387496948, 9.595626831, df.parse("2020-10-06T18:42:14Z")));
        test.setAccelerometerList(accelerometers);

        return test;
    }

    private JsonElement readJSON(String filename) throws FileNotFoundException {
        String uri = path + filename;
        Gson gson = new Gson();
        Object object = gson.fromJson(new FileReader(uri), Object.class);
        return gson.toJsonTree(object);

    }
}
