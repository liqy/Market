package tv.tipsee.vr.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import tv.tipsee.vr.models.RootData;
import tv.tipsee.vr.models.VRVideo;

/**
 * Created by liqy on 2016/6/20.
 */
public interface TipSeeService {


    @GET("video/v1.0.0/index.list/json")
    Call<RootData<List<VRVideo>>> getVideoList();

}
