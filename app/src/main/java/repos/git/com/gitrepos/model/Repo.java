package repos.git.com.gitrepos.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by toshiba on 26/01/2015.
 */
public class Repo implements Parcelable {

    private String name;
    private int forks;
    private int watchers;
    private String contributorsUrl;

    public Repo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public Repo(Parcel in) {
        name = in.readString();
        forks = in.readInt();
        watchers = in.readInt();
        contributorsUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(forks);
        dest.writeInt(watchers);
        dest.writeString(contributorsUrl);
    }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {
        @Override
        public Repo createFromParcel(Parcel source) {
            return new Repo(source);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[0];
        }
    };

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }
}
