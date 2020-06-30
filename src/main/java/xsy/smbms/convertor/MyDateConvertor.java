package xsy.smbms.convertor;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2020/6/21.
 */
public class MyDateConvertor implements Converter<String,Date> {
    @Override
    public Date convert(String s) {
        try {
            if (s.length() == 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(s);
            } else if (s.length() == 19) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.parse(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
