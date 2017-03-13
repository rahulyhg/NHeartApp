package com.cviac.nheart.nheartapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cviac.nheart.nheartapp.R;

import com.cviac.nheart.nheartapp.activities.Chat_Skezo;
import com.cviac.nheart.nheartapp.activities.Registration;
import com.cviac.nheart.nheartapp.activities.Skezo_Main;
import com.cviac.nheart.nheartapp.adapters.SkezoInfoAdapter;
import com.cviac.nheart.nheartapp.datamodel.MusicInfo;
import com.cviac.nheart.nheartapp.datamodel.SkezoInfo;

import java.util.ArrayList;
import java.util.List;


public class SkezoFragment extends Fragment {
    ProgressBar progress;
    private List<SkezoInfo> skezolist;
    public ListView lv2;
    ImageButton fab;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_skezo, container, false);

        lv2 = (ListView) view.findViewById(R.id.list_skezo);
       /*  progress = (ProgressBar)getActivity().findViewById(R.id.progressBarFetch);
        progress.setVisibility(View.VISIBLE);
        progress.setIndeterminate(true);*/

        values();
      /*  progress.setVisibility(View.GONE);*/

        SkezoInfoAdapter adapter = new SkezoInfoAdapter(getActivity(), skezolist);

        lv2.setAdapter(adapter);

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1, long pos2) {
                SkezoInfo emp = skezolist.get(pos1);

                if (pos1 == 0)
                {
                    Intent i = new Intent(getActivity().getApplicationContext(), Skezo_Main.class);
                    startActivity(i);
                }

                if (pos1 == 1)
                {
                    Toast.makeText(getContext(), "I'm Under Renovation", Toast.LENGTH_LONG).show();

                }
            }
        });
        return view;
    }

    private void values(){

        skezolist=new ArrayList<>();

        SkezoInfo si1=new SkezoInfo(R.mipmap.girlone," ","a moment ago","Libo");
        skezolist.add(si1);

        SkezoInfo si2=new SkezoInfo(R.mipmap.girltwo," ","a moment ago","Tibo");
        skezolist.add(si2);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_category).setVisible(false);
        menu.findItem(R.id.action_cart).setVisible(false);
        menu.findItem(R.id.action_call).setVisible(false);
        menu.findItem(R.id.loc).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}