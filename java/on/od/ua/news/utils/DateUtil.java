package on.od.ua.news.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lubomyr Shershun on 29.07.2015.
 * l.sherhsun@gmail.com
 */
public class DateUtil {
    List<Item> list;
    int days=-5;

    public DateUtil(List<Item> list) {
        this.list = list;
    }

    private void addDays(Date d, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        d.setTime( c.getTime().getTime() );
    }
    private int compareDate(String sDate){
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

        Date dbDate = null;
        Date now = new Date();
        df.format(now);
        try {
            dbDate = df.parse(sDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
       addDays(now,days);
        Log.i("DateUtil",now.toString());
       if (dbDate.before(now)){
           return -1;
       }
        return 0;
    }
    public List<Item> getDeletableList(){
        List<Item>deleteList=new ArrayList<>();
        deleteList.addAll(list);
        for (Item item:list){
            if (compareDate(item.getPubDate())!=-1){
                deleteList.remove(item);
            }
        }
        return deleteList;
    }
}
