package tv.tipsee.vr.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liqy on 2016/6/20.
 */
public class VRVideo implements Parcelable {

    public String file;
    public String pic;
    public String title;
    public String desc;

    public VRVideo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.file);
        dest.writeString(this.pic);
        dest.writeString(this.title);
        dest.writeString(this.desc);
    }

    protected VRVideo(Parcel in) {
        this.file = in.readString();
        this.pic = in.readString();
        this.title = in.readString();
        this.desc = in.readString();
    }

    public static final Creator<VRVideo> CREATOR = new Creator<VRVideo>() {
        @Override
        public VRVideo createFromParcel(Parcel source) {
            return new VRVideo(source);
        }

        @Override
        public VRVideo[] newArray(int size) {
            return new VRVideo[size];
        }
    };
}
