package on.od.ua.news.utils;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Lubomyr Shershun on 24.07.2015.
 * l.sherhsun@gmail.com
 */
public class Item implements Serializable,Comparable<Item> {
    // Create the strings we need to store
    private String title, description, pubDate, creator, category, URL, image_uri, image_path;
    //TODO drawable here
    // Serializable ID
    private static final long serialVersionUID = 1L;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getPubDate_converted() {
        final String NEWFORMAT="dd-MM-yyyy HH:mm";
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Greenwich"));
        SimpleDateFormat newDateFormat=new SimpleDateFormat(NEWFORMAT,Locale.getDefault());
        Date d = null;
        try {
            d = df.parse(this.getPubDate());
        }catch (ParseException e) {
            e.printStackTrace();
        }
        df.applyPattern(NEWFORMAT);
        newDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));


      /*  Log.d("TimeZone", "Final value " + df.format(d));
        Log.d("TimeZone", "NewFinal value " + newDateFormat.format(d));*/

        return newDateFormat.format(d);

    }
    @Override
   // "EEE, d MMM yyyy HH:mm:ss"
    public int compareTo(Item another) {

        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(this.getPubDate());
            d2 = df.parse(another.getPubDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d1.equals(d2))
            return 0;
        else if (d1.before(d2))
            return 1;
        else if (d1.after(d2))
            return -1;

        return 0;

    }
    public String getDescription_cropped(){
        String result;
        final String start="<p><img src=";
        final String start2="<img src=";
        final String end="></p>";
        final String end2=">";

        result=getDescription().replaceAll(start+".*"+end, "").replaceAll(start+".*"+end2, "").replaceAll(start2+".*"+end2,"");

        return result;
    }
}
