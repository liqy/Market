package tv.tipsee.vr.views.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.ecmobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tv.tipsee.vr.models.VRVideo;

/**
 * Created by liqy on 2016/6/20.
 */
public class VRVideoAdapter extends RecyclerView.Adapter<VRVideoAdapter.ViewHolder> {

    public ArrayList<VRVideo> videos;
    public FragmentActivity activity;

    public VRVideoAdapter(FragmentActivity activity) {
        this.activity = activity;
        this.videos = new ArrayList<>();
    }

    public void addVrVideoList(List<VRVideo> list) {
        if (list != null && !list.isEmpty()) {
            this.videos.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vr_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        VRVideo video = this.videos.get(position);

        Picasso.with(activity)
                .load(video.pic)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.vr_image_thumb);

//        holder.vr_video_title.setText(video.title);
//        holder.vr_video_desc.setText(video.desc);

    }

    public VRVideo getItem(int position) {
        return this.videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.videos.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView vr_image_thumb;
//        public TextView vr_video_title;
//        public TextView vr_video_desc;

        public ViewHolder(View view) {
            super(view);
            vr_image_thumb = (ImageView) view.findViewById(R.id.vr_image_thumb);
//            vr_video_desc = (TextView) view.findViewById(R.id.vr_video_desc);
//            vr_video_title = (TextView) view.findViewById(R.id.vr_video_title);
        }
    }
}
