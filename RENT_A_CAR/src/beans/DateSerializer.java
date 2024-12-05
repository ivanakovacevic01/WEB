package beans;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {

    private final SimpleDateFormat dateFormat;

    public DateSerializer() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format datuma koji želite da koristite
    }

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        String formattedDate = dateFormat.format(date);
        return new JsonPrimitive(formattedDate);
    }
}