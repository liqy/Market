package tv.tipsee.vr;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.insthub.ecmobile.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.tipsee.vr.models.RootData;
import tv.tipsee.vr.models.VRVideo;
import tv.tipsee.vr.network.RestClient;
import tv.tipsee.vr.player.MD360PlayerActivity;
import tv.tipsee.vr.views.adapters.VRVideoAdapter;

public class VRVideoListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private VRVideoAdapter vrVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_video_list);
        initView();
        loadVrVideoList();
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        vrVideoAdapter = new VRVideoAdapter(this);
        recyclerView.setAdapter(vrVideoAdapter);

//                        VRVideo vrVideo = vrVideoAdapter.getItem(position);
//                        if (TextUtils.isEmpty(vrVideo.file)){
//
//                        }else {
//                            MD360PlayerActivity.startVideo(VRVideoListActivity.this, Uri.parse(vrVideo.file));
//                        }

    }

    private void loadVrVideoList() {

        Call<RootData<List<VRVideo>>> list = RestClient.getClient().getVideoList();

        list.enqueue(new Callback<RootData<List<VRVideo>>>() {
            @Override
            public void onResponse(Call<RootData<List<VRVideo>>> call, Response<RootData<List<VRVideo>>> response) {

                RootData<List<VRVideo>> data = response.body();

                if (data != null && data.isValueOk()) {
                    vrVideoAdapter.addVrVideoList(data.value);
                }
            }

            @Override
            public void onFailure(Call<RootData<List<VRVideo>>> call, Throwable t) {
            }
        });
    }

}
