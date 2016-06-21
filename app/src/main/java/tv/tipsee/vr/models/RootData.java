package tv.tipsee.vr.models;

/**
 * Created by liqy on 2016/6/20.
 */
public class RootData<T> {

    public int code;
    public String message;
    public T value;
    public double time;

    public boolean isOk() {
        return code == 0;
    }

    public boolean isValueOk() {
        return code == 0 && value != null;
    }
}
