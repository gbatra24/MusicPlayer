package com.example.administrator.musictest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Gagan on 11/21/2016.
 */

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {
    private String[] tabtitle=new String[]{"Song", "Albums", "Artists"};
    Context context;
    private int pagecount=3;

    public SimpleFragmentPageAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            SongFragment songFragment = new SongFragment();
            return songFragment;
        }
        else if (position == 1){
            AlbumFragment albumFragment = new AlbumFragment();
            return albumFragment;
        }
        else if (position == 2){
            ArtistFragment artistFragment = new ArtistFragment();
            return artistFragment;
        }
        else return null;

    }

    @Override
    public int getCount() {
        return pagecount;
    }

    @Override
    public CharSequence getPageTitle(int position)

    {
        return tabtitle[position];
    }
}
