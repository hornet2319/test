package on.od.ua.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import on.od.ua.news.fragments.AboutFragment;
import on.od.ua.news.fragments.MainFragment;
import on.od.ua.news.fragments.NavigationDrawerFragment;
import on.od.ua.news.utils.CacheCleaner;
import on.od.ua.news.utils.DOMParser;
import on.od.ua.news.utils.InternetUtil;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    static final String ARG_SECTION_NUMBER = "section_number";
    private Fragment fragment;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        DOMParser parser=new DOMParser(this,true);
        InternetUtil internet=new InternetUtil(this);

        if (internet.isConnected()) parser.parseXML();
        CacheCleaner cleaner= new CacheCleaner(this);
        cleaner.execute();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        if (mNavigationDrawerFragment.isDrawerOpen())mNavigationDrawerFragment.closeDrawer();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position + 1))
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    private Fragment getFragment(int i) {
        fragment = null;
        Bundle args = new Bundle();
        switch (i) {
            case 1:

                fragment = new MainFragment();

                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;
            case 2:

                fragment = new AboutFragment();
                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;

            default:
                fragment = new MainFragment();
                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;
        }
        return fragment;
    }
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.app_name);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;

        }
    }
    public void restoreActionBar() {

        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
