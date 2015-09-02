package on.od.ua.news;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import on.od.ua.news.R;
import on.od.ua.news.utils.Item;
import on.od.ua.news.utils.MyDataBase;


public class DetailActivity extends ActionBarActivity {
    private String ITEM_TITLE;
    private Item item=null;
    private MyDataBase db=null;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i=getIntent();
        db=new MyDataBase(this);
        ITEM_TITLE=i.getStringExtra("title");
        this.item=getItem(ITEM_TITLE);

        mToolbar=(Toolbar)findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inatializing views
        ImageView main_img=(ImageView)findViewById(R.id.det_main_img);
        TextView header=(TextView)findViewById(R.id.det_header);
        TextView category=(TextView)findViewById(R.id.det_cath);
        TextView time=(TextView)findViewById(R.id.det_time);
        TextView description=(TextView)findViewById(R.id.det_description);
        //

        ImageLoader loader=ImageLoader.getInstance();

        ViewGroup.LayoutParams params= main_img.getLayoutParams();
        String path=loader.getDiskCache().get(item.getImage_uri()).getAbsolutePath();
        params.height=getHeight(path);
        main_img.setLayoutParams(params);

        loader.displayImage(item.getImage_uri(), main_img);
        header.setText(Html.fromHtml("<strong>" + item.getTitle() + "</strong>"));
        category.setText(Html.fromHtml("<strong>" + item.getCategory() + "</strong>"));
        time.setText(item.getPubDate_converted());
        description.setText(Html.fromHtml(item.getDescription_cropped()));

    }
    private int getHeight(String path){
        int result=0;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int img_width=bitmap.getWidth();
        int img_height=bitmap.getHeight();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_width = size.x;
        result=screen_width*img_height/img_width;
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
    private Item getItem(String title){
        Item item=null;
        item=db.getItem(title);
        return item;
    }
}
