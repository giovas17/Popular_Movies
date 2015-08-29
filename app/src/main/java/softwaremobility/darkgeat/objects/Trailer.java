package softwaremobility.darkgeat.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darkgeat on 8/25/15.
 */
public class Trailer implements Parcelable{
    private String key, name, site, type;
    private int size;

    public Trailer() {
        this.key = "";
        this.name = "";
        this.site = "";
        this.type = "";
        this.size = 0;
    }

    public Trailer(String key, String name, String site, String type, int size) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            Trailer aux = new Trailer();
            aux.setSize(source.readInt());
            aux.setName(source.readString());
            aux.setType(source.readString());
            aux.setKey(source.readString());
            aux.setSite(source.readString());
            return aux;
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(key);
        dest.writeString(site);
    }
}
