package com.github.lassana.offlineroutingsample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import com.github.lassana.offlineroutingsample.App;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.downloader.AbstractMap;
import com.github.lassana.offlineroutingsample.util.event.MapSuccessfulDownloadedEvent;
import com.squareup.otto.Subscribe;

public class MainActivity extends ActionBarActivity {

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            updateContent(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getApplication(this).registerOttoBus(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getApplication(this).unregisterOttoBus(this);
    }

    @Subscribe
    public void onMapDownloaded(MapSuccessfulDownloadedEvent event) {
        findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                updateContent(true);
            }
        });
    }

    private void updateContent(boolean force) {
        if (mCurrentFragment == null || force) {
            mCurrentFragment = AbstractMap.getSelectedMap().exist(this)
                    ? new MapFragment()
                    : new MapDownloaderFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mCurrentFragment).commit();
        }
    }

}
