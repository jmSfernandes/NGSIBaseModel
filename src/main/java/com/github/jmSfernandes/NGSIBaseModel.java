package com.github.jmSfernandes;

import com.github.jmSfernandes.annotations.NGSIEncoded;
import com.github.jmSfernandes.annotations.NGSIIgnore;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NGSIBaseModel {


    public NGSIBaseModel fromNGSI(JsonObject entity) throws Exception {
        Class mClass = this.getClass();
        if (entity.has("type")) {
            if (!mClass.getSimpleName().toLowerCase().equals(entity.get("type").getAsString())) {
                throw new Exception("The type of NGSI Entity must be the same as the class");
            }
        }

        Object obj = mClass.newInstance();

        for (Field field : mClass.getDeclaredFields()) {

            boolean isIgnore = field.getAnnotation(NGSIIgnore.class) != null;
            boolean isEncoded = field.getAnnotation(NGSIEncoded.class) != null;


            JsonElement attribute = entity.get(field.getName().toLowerCase());
            if (attribute != null && !isIgnore) {
                if (attribute.isJsonObject() && attribute.getAsJsonObject().has("value")) {
                    JsonElement value = attribute.getAsJsonObject().get("value");
                    obj = setValue(obj, value, field, isEncoded);
                } else {
                    obj = setValue(obj, attribute, field, isEncoded);
                }
            }
        }

        return (NGSIBaseModel) obj;
    }

    private Object setValue(Object obj, JsonElement value, Field field, boolean isEncoded) throws Exception {
        if (value != null) {
            if (value.isJsonPrimitive()) {
                if (value.getAsJsonPrimitive().isString()) {
                    if (isEncoded)
                        value = NGSIUtils.decodeAttribute(value);
                    if (field.getType().equals(Date.class)) {
                        if (NGSIUtils.isDate(value.getAsString()))
                            field.set(obj, NGSIUtils.getAsDate(value.getAsString()));
                    } else {
                        field.set(obj, value.getAsString());
                    }
                } else if (value.getAsJsonPrimitive().isNumber()) {

                    field.set(obj, NGSIUtils.getAsNumber(value, field));
                }
            } else if (value.isJsonArray()) {
                if (field.getType().equals(List.class) || field.getType().equals(ArrayList.class)) {
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> itemType = (Class<?>) listType.getActualTypeArguments()[0];

                    List array = new ArrayList();
                    for (JsonElement element : value.getAsJsonArray()) {

                        if (isEncoded)
                            element = NGSIUtils.decodeAttribute(element);

                        if (itemType.equals(String.class)) {
                            array.add(element.getAsString());
                        } else {
                            Object obj_sub = itemType.newInstance();
                            if (obj_sub.getClass().getSuperclass() != null && obj_sub.getClass().getSuperclass().equals(NGSIBaseModel.class) &&
                                    element.isJsonObject()) {
                                obj_sub = ((NGSIBaseModel) obj_sub).fromNGSI(element.getAsJsonObject());
                                array.add(obj_sub);
                            }
                        }
                    }

                    field.set(obj, array);
                }
            } else if (value.isJsonObject()) {
                if (isEncoded)
                    value = NGSIUtils.decodeAttribute(value);

                Object obj_sub = field.getType().newInstance();
                if (field.getClass().getSuperclass() != null && field.getClass().getSuperclass().equals(NGSIBaseModel.class)) {
                    obj_sub = ((NGSIBaseModel) obj_sub).fromNGSI(value.getAsJsonObject());
                    field.set(obj, obj_sub);
                }
            }
        }

        return obj;
    }


    public JsonObject toNGSI() throws Exception {

        Class<?> mClass = this.getClass();
        Object obj=this;

        JsonObject json = new JsonObject();
        if (mClass.getDeclaredField("id") != null) {
            String _id= (String) mClass.getDeclaredField("id").get(obj);
            json.addProperty("id", _id);
            json.addProperty("type", mClass.getSimpleName().toLowerCase());

        }


        for(Field field: mClass.getDeclaredFields())
        {
            boolean isIgnore = field.getAnnotation(NGSIIgnore.class) != null;
            boolean isEncoded = field.getAnnotation(NGSIEncoded.class) != null;
            if (!isIgnore && !field.getName().equals("id")) {
                Object fieldValue = field.get(obj);
                Class<?> fieldType = field.getType();
                if (fieldValue != null) {
                    if (fieldType.equals(List.class) || fieldType.equals(ArrayList.class)) {
                        JsonObject attrJSON = (JsonObject) setJsonAttributeComplex(fieldValue, field, "array", isEncoded);
                        json.add(field.getName().toLowerCase(), attrJSON);
                    }
                    else if (fieldType.getSuperclass().equals(NGSIBaseModel.class)) {
                        JsonObject attrJSON = (JsonObject) setJsonAttributeComplex(fieldValue, field, "object", isEncoded);
                        json.add(field.getName().toLowerCase(), attrJSON);
                    } else {
                        JsonObject attrJSON = (JsonObject) setJsonAttributeSimple(fieldValue, field, isEncoded);
                        json.add(field.getName().toLowerCase(), attrJSON);
                    }
                }
            }
        }

        return json;
    }


    private static JsonElement setJsonAttributeSimple(Object value, Field field, boolean isEncoded) throws UnsupportedEncodingException {
        JsonObject attrObj = new JsonObject();
        Class<?> value_class = value.getClass();
        if (String.class.equals(value_class)) {
            if (isEncoded)
                value = NGSIUtils.encodeAttribute((String) value);
            attrObj.addProperty("value", (String) value);
            attrObj.addProperty("type", "Text");
        } else if (Date.class.equals(value_class)) {
            attrObj.addProperty("value", NGSIUtils.formatDate((Date) value));
            attrObj.addProperty("type", "ISO8601");
        } else if (Integer.class.equals(value_class) || int.class.equals(value.getClass())) {
            attrObj.addProperty("value", (Number) value);
            attrObj.addProperty("type", "Integer");

        } else if (Float.class.equals(value.getClass()) || float.class.equals(value.getClass())) {
            attrObj.addProperty("value", (Number) value);
            attrObj.addProperty("type", "Float");
        }

        return attrObj;
    }

    private static JsonElement setJsonAttributeComplex(Object value, Field field, String type, boolean isEncoded) throws Exception {
        JsonObject attrObj = new JsonObject();

        switch (type) {
            case ("object"):
                attrObj.add("value", ((NGSIBaseModel)value).toNGSI());
                attrObj.addProperty("type", "Text");
                break;
            case ("array"):
                JsonArray array = new JsonArray();
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> type_generic = (Class<?>) listType.getActualTypeArguments()[0];

                for (Object v : (List)value) {
                    if(v.getClass().getSuperclass().equals(NGSIBaseModel.class))
                        array.add(((NGSIBaseModel)v).toNGSI());
                    else
                        array.add(String.valueOf(v));
                }

                attrObj.add("value", array);
                attrObj.addProperty("type", "Text");
                break;
            default:
                break;
        }

        return attrObj;
    }

    @Override
    public boolean equals(Object obj) {
        if(!this.getClass().equals(obj.getClass()))
            return false;
        Field[] fields=this.getClass().getDeclaredFields();
        for(Field field: fields){
            try {
                boolean isIgnore = field.getAnnotation(NGSIIgnore.class) != null;
                if(!field.get(this).equals(field.get(obj))&& !isIgnore)
                    return false;

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }


}