package com.example.sahilsa.gallerycamerademo;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Album;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.model.SelectedItemCollection;
import com.zhihu.matisse.internal.ui.MediaSelectionFragment;
import com.zhihu.matisse.internal.ui.adapter.AlbumMediaAdapter;
import com.zhihu.matisse.ui.MatisseFragment;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GalleryActivity extends AppCompatActivity implements AlbumMediaAdapter.CheckStateListener, AlbumMediaAdapter.OnMediaClickListener,
        MediaSelectionFragment.SelectionProvider {

    private ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            setUpViewPager();
                        } else {
                            finish();
                            Toast.makeText(GalleryActivity.this, "Permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void setUpViewPager() {
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        setUpMatisse();
        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFrag(MatisseFragment.newInstance(), "Gallery");
        adapter.addFrag(OneFragment.newInstance(), "Photo");
        adapter.addFrag(OneFragment.newInstance(), "Video");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
    }

    private void setUpMatisse() {
        Matisse.from(GalleryActivity.this)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.example.sahilsa.gallerycamerademo.fileprovider"))
                .maxSelectable(5)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine());
    }

    @Override
    public void onUpdate() {
        ((MatisseFragment) adapter.getItem(0)).updateBottomToolbar();
    }

    @Override
    public void onMediaClick(Album album, Item item, int adapterPosition) {
        ((MatisseFragment) adapter.getItem(0)).onMediaClick(album, item, adapterPosition);
    }

    @Override
    public SelectedItemCollection provideSelectedItemCollection() {
        return ((MatisseFragment) adapter.getItem(0)).getSelectedItemCollection();
    }
}
