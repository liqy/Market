package tv.tipsee.vr.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.tipsee.vr.utils.Constants;

/**
 * Created by liqy on 2016/6/20.
 */
public class RestClient {
    private static TipSeeService tipSeeService;

    public static TipSeeService getClient() {
        try {
            if (tipSeeService == null) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                return chain.proceed(chain.request());
                            }
                        }).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.API_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                tipSeeService = retrofit.create(TipSeeService.class);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return tipSeeService;
    }
}
