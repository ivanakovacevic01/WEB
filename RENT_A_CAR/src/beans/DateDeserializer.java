package beans;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
public class DateDeserializer implements JsonDeserializer<Date>{

	@Override
	public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		// TODO Auto-generated method stub
		String date=element.getAsString();
		Date d=new Date();
		SimpleDateFormat simpleFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		try {
			d=simpleFormat.parse(date);
		}catch(ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

}
