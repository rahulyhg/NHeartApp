package com.cviac.nheart.nheartapp.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.cviac.nheart.nheartapp.Prefs;
import com.cviac.nheart.nheartapp.R;
import com.cviac.nheart.nheartapp.datamodel.CategoriesResponse;
import com.cviac.nheart.nheartapp.datamodel.GetCartItemsResponse;
import com.cviac.nheart.nheartapp.datamodel.LoginResponse;
import com.cviac.nheart.nheartapp.fragments.ChatFragment;


import com.cviac.nheart.nheartapp.fragments.GiftFragment;
import com.cviac.nheart.nheartapp.fragments.HugFragment;
import com.cviac.nheart.nheartapp.fragments.MusicFragment;
import com.cviac.nheart.nheartapp.fragments.SkezoFragment;
import com.cviac.nheart.nheartapp.restapi.OpenCartAPI;
import com.cviac.nheart.nheartapp.utilities.BadgeDrawable;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.cviac.nheart.nheartapp.activities.ProductdetailsActivity.count;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    static TabLayout tabLayout;
    private LayerDrawable mcartMenuIcon;
    private int mCartCount = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Toolbar toolbar;
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(false, true);
*/

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setTag("Mirror");
        tab.setIcon(R.mipmap.ic_message_text_white_24dp);
        tab = tabLayout.getTabAt(1);
        tab.setTag("Lovewrap");
        tab.setIcon(R.mipmap.ic_gift_black_24dp);
        tab = tabLayout.getTabAt(2);
        tab.setTag("Co Listen");
        tab.setIcon(R.mipmap.ic_music_circle_black_24dp);
        tab = tabLayout.getTabAt(3);
        tab.setTag("Skezo");
        tab.setIcon(R.mipmap.robo_black);
        tab = tabLayout.getTabAt(4);
        tab.setTag("Hug");


        tab.setIcon(R.mipmap.ic_people_black_24dp);
        setTitle("Mirror");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTitle(tab.getTag().toString());
                setTabIcon((TabLayout.Tab) tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabIcon((TabLayout.Tab) tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getSetToken();

    }

    private void setTabIcon(TabLayout.Tab tab, boolean isSelected) {
        String tabTitle = tab.getTag().toString();
        switch (tabTitle) {

            case "Mirror":
                int ic = (isSelected) ? R.mipmap.ic_message_text_white_24dp : R.mipmap.ic_message_text_black_24dp;
                tab.setIcon(ic);
                break;

            case "Lovewrap":
                int ic1 = (isSelected) ? R.mipmap.ic_gift_white_24dp : R.mipmap.ic_gift_black_24dp;
                tab.setIcon(ic1);
                break;

            case "Co Listen":
                int ic2 = (isSelected) ? R.mipmap.ic_music_circle_white_24dp : R.mipmap.ic_music_circle_black_24dp;
                tab.setIcon(ic2);
                break;

            case "Skezo":
                int ic3 = (isSelected) ? R.mipmap.robo_white : R.mipmap.robo_black;
                tab.setIcon(ic3);
                break;

            case "Hug":
                int ic4 = (isSelected) ? R.mipmap.ic_people_white_24dp : R.mipmap.ic_people_black_24dp;
                tab.setIcon(ic4);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mcartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        setBadgeCount(this, mcartMenuIcon, "");
        getAndSetCartCount();
        return true;
    }



    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_cart_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_cart_badge, badge);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1000) {
            if (data != null) {
                String catId = data.getStringExtra("categoryid");

                if (catId != null)

                {
                    GiftFragment gfrag = (GiftFragment) getSupportFragmentManager().getFragments().get(1);
                    gfrag.refresh(catId);


                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_category:
                Intent i = new Intent(MainActivity.this, CategorylistActivity.class);
                startActivityForResult(i, 1000);
                break;
            // action with ID action_settings was selected
            case R.id.action_refresh:

                break;
            case R.id.action_cart:
//                if(mCartCount==0) {
//                    Intent h = new Intent(MainActivity.this, EmptyCartListActivity.class);
//                    startActivity(h);
//                }
//                if(mCartCount!=0){
                    Intent h = new Intent(MainActivity.this, CartItemListActivity.class);
                    startActivity(h);
//                }
                break;


            default:
                break;
        }

        return true;


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //return true;
    }


    //return super.onOptionsItemSelected(item);


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        //   private final List<String> mFragmentTitleList = new ArrayList<>();
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Context context;

        public PlaceholderFragment() {
        }


//
//
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);

            switch (position + 1) {
                case 1:
                    return new ChatFragment();
                case 2:
                    Fragment frag = new GiftFragment();
                    return new GiftFragment();
                case 3:
                    Fragment frag1 = new MusicFragment();
                    return new MusicFragment();
                case 4:
                    Fragment frag2 = new SkezoFragment();
                    return new SkezoFragment();
                case 5:
                    Fragment frag3 = new HugFragment();
                    return new HugFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {

           /* switch (position) {
                case 0:
                    return "Mirror";

                case 1:
                    return "Love Wrap";
                case 2:
                    return "Co-Listen";
                case 3:
                    return "Skezo";
                case 4:
                    return "Hug";
            }*/
            return null;
        }
    }

    private void getSetToken() {
        String token = Prefs.getString("token", null);
        if (token == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://nheart.cviac.com/index.php?route=api/login")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenCartAPI api = retrofit.create(OpenCartAPI.class);

            String apiKey = "um5xn7zaF0RfeAzhN5vG3xsqeeFjupkgOjvtqSubhcR68yw1yg5l1nu4z0EIaYx2HLqRwlvkLGCnFL8EIG0T61L3AtD1v5HNCTPYKdksMXZrCGWGdFX1p5z8KKGQz7lBQzczWxopiQcsUXKr6B7vNasiWEpZ5pNWTjhZgMMOUILMKmnj335u67xLaO334LRmgDiEA6IDyR4Hmilqp3xjce2SvPJeRDwPuINSmSFLFxJO8qUSiF6xObvNhqZZAkey";
            final Call<LoginResponse> call = api.login(apiKey);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                    int code = response.code();
                    LoginResponse rsp = response.body();
                    Prefs.putString("token", rsp.getToken());
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getAndSetCartCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nheart.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenCartAPI api = retrofit.create(OpenCartAPI.class);

        String token = Prefs.getString("token", null);
        Call<GetCartItemsResponse> call = api.getCartItems(token);
        call.enqueue(new Callback<GetCartItemsResponse>() {

            public void onResponse(Response<GetCartItemsResponse> response, Retrofit retrofit) {
                GetCartItemsResponse rsp = response.body();
                if (rsp != null) {
                    mCartCount = rsp.getProds().size();
                    setBadgeCount(MainActivity.this, mcartMenuIcon, mCartCount+"");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayer mp = MusicFragment.mp;
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }
}
