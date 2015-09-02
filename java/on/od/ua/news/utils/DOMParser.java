package on.od.ua.news.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import on.od.ua.news.R;


/**
 * Created by Lubomyr Shershun on 24.07.2015.
 * l.sherhsun@gmail.com
 */
public class DOMParser {
    private final String LOG_TAG = "DOMPARSER";
    private final String FEED_URL = "http://on.od.ua/feed/";
    private List<Item> item_list=new ArrayList<>();

    private Context context;
    // Create a new URL
    private URL url = null;
    private MyDataBase dataBase;
    private boolean bool=false;
    private ImageLoaderConfiguration config;
    private ImageLoader imgLoader;

    // Find the new URL from the given URL
    public DOMParser(Context context, boolean bool) {
        this.bool=bool;
        this.context=context;
        dataBase=new MyDataBase(context);
        try {
            // Find the new URL from the given URL
            url = new URL(FEED_URL);
        } catch (MalformedURLException e) {
            // Throw an exception
            e.printStackTrace();
        }
    }
    public DOMParser(Context context) {
        this.bool=false;
        this.context=context;
        dataBase=new MyDataBase(context);
        try {
            // Find the new URL from the given URL
            url = new URL(FEED_URL);
        } catch (MalformedURLException e) {
            // Throw an exception
            e.printStackTrace();
        }
    }
    private class HttpTask extends AsyncTask<Void, Void, List<Item>> {
        private ProgressDialog refreshDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a new dialog
            if(bool) {
                refreshDialog = ProgressDialog.show(context, null, null);

                // Spin the wheel whilst the dialog exists
                refreshDialog.setContentView(R.layout.progress_dialog_loader);
                // Don't exit the dialog when the screen is touched
                refreshDialog.setCanceledOnTouchOutside(false);
                // Don't exit the dialog when back is pressed
                refreshDialog.setCancelable(true);
            }

        }

        @Override
        protected List<Item> doInBackground(Void... params) {
            Item item=new Item();

            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                // Parse the XML
                Document doc = builder.parse(new InputSource(url.openStream()));
                // Normalize the data
                doc.getDocumentElement().normalize();
                // Get all <item> tags.
                NodeList nList = doc.getElementsByTagName("item");

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node node = nList.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        //Print each detail
                        Element eElement = (Element) node;
                        Element image=(Element) eElement.getElementsByTagName("enclosure").item(0);
                        item.setImage_uri(image.getAttribute("url"));
                        Log.i(LOG_TAG, image.getAttribute("url"));
                        //caching image

                        Log.i(LOG_TAG, "title " + eElement.getElementsByTagName("title").item(0).getTextContent());
                        item.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
                        item.setDescription(eElement.getElementsByTagName("content:encoded").item(0).getTextContent());
                        item.setCategory(eElement.getElementsByTagName("category").item(0).getTextContent());
                        item.setPubDate(eElement.getElementsByTagName("pubDate").item(0).getTextContent().replace(" +0000", ""));
                        item.setCreator(eElement.getElementsByTagName("dc:creator").item(0).getTextContent());
                        item.setURL(eElement.getElementsByTagName("link").item(0).getTextContent());

                        if(dataBase.isEmpty()){
                         /*   imgLoader.loadImage(item.getImage_uri(),new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    super.onLoadingStarted(imageUri, view);
                                    Log.i("IMG downloading started", item.getImage_uri());
                                    Log.i("IMG downloading started", imgLoader.getDiskCache().get(item.getImage_uri()).getAbsolutePath());
                                    item.setImage_path(imgLoader.getDiskCache().get(item.getImage_uri()).getAbsolutePath());
                                }
                            });*/
                            dataBase.setItem(item);
                        }
                        else {
                            if(dataBase.getItem(item.getTitle())==null){
                            /*    imgLoader.loadImage(item.getImage_uri(),new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
                                        super.onLoadingStarted(imageUri, view);
                                        Log.i("IMG downloading started", imgLoader.getDiskCache().get(imageUri).getAbsolutePath());
                                        item.setImage_path(imgLoader.getDiskCache().get(imageUri).getAbsolutePath());

                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        // Do whatever you want with Bitmap
                                    }
                                });*/

                                dataBase.setItem(item);
                            }
                            else{
                                Log.e("DATABASE", "item collision" + dataBase.getItem(item.getTitle()));
                                break;
                            }



                        }
                        item_list.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return item_list;


        }

        @Override
        protected void onPostExecute(List<Item> items) {
           if (bool)refreshDialog.dismiss();
            super.onPostExecute(items);
        }
    }

    public void parseXML() {
       HttpTask task=new HttpTask();
        task.execute();
    // Return the feed
    }


}

