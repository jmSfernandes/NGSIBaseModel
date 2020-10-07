import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static JsonElement decodeAttribute(JsonElement value) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(value.getAsJsonPrimitive().getAsString(), "UTF-8");
        return new JsonPrimitive(decoded);
    }

    public static String encodeAttribute(JsonElement value) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(value.getAsJsonPrimitive().getAsString(), "UTF-8");
        encoded = encoded.replace("(", "%28").replace(")", "%29").replace("+", "%20");
        return encoded;
    }
    public static String encodeAttribute(String value) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(value, "UTF-8");
        encoded = encoded.replace("(", "%28").replace(")", "%29").replace("+", "%20");
        return encoded;
    }
    public static String formatDate(Date date){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formated = "";
        if(date!=null)
            formated = sdf.format(date);
        return formated;
    }

    public static Object getAsNumber(JsonElement value, Field field) {
        Class<?> type = field.getType();
        if (float.class.equals(type) || Float.class.equals(type)) {
            return value.getAsFloat();
        } else if (int.class.equals(type) || Integer.class.equals(type)) {
            return value.getAsInt();
        } else if (double.class.equals(type) || Double.class.equals(type)) {
            return value.getAsDouble();
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            return value.getAsLong();
        }
        return null;
    }

    public static Date getAsDate(String value) {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
        }
        return date;

    }

    public static boolean isDate(String value) {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            sdf.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;

    }
}
