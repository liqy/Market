package tv.tipsee.vr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.EcmobileMainActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.tipsee.vr.models.RootData;
import tv.tipsee.vr.models.VRVideo;
import tv.tipsee.vr.network.RestClient;
import tv.tipsee.vr.utils.RecyclerUtils;
import tv.tipsee.vr.views.adapters.VRVideoAdapter;

public class VRVideoListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private VRVideoAdapter vrVideoAdapter;

    private ImageView go_market;

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

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .oval(false)
                .build();

        vrVideoAdapter = new VRVideoAdapter(this, transformation);
        recyclerView.setAdapter(vrVideoAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerUtils.RecyclerItemClickListener(this, new RecyclerUtils.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VRVideo vrVideo = vrVideoAdapter.getItem(position);
                VRVideoInfoActivity.openVRVideoInfoActivity(VRVideoListActivity.this, vrVideo);
            }
        }));

        go_market = (ImageView) findViewById(R.id.go_market);

        go_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(VRVideoListActivity.this, EcmobileMainActivity.class);
                startActivity(it);
            }
        });
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
