package on.od.ua.news.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lubomyr Shershun on 29.07.2015.
 * l.sherhsun@gmail.com
 */
public class CacheCleaner extends AsyncTask<Void,Void,Void> {
    private List<Item> list;
    private Context context;
    private ImageLoader imageLoader;

    public CacheCleaner(Context context) {
        this.context = context;
        list= new ArrayList<>();
        imageLoader=ImageLoader.getInstance();

    }

    @Override
    protected Void doInBackground(Void... params) {
        MyDataBase db=new MyDataBase(context);

        if (!db.isEmpty()) {

            DateUtil util = new DateUtil(db.getItems());
            list.clear();
            list.addAll(util.getDeletableList());

            for (Item item : list) {
                MemoryCacheUtils.removeFromCache(item.getImage_uri(), imageLoader.getMemoryCache());
                DiskCacheUtils.removeFromCache(item.getImage_uri(), imageLoader.getDiskCache());
                db.deleteItem(item);
            }
        }

        return null;
    }
}
