package in.handmademess.popularmovies;

/**
 * Created by Anup on 24-09-2017.
 */


public class TrailerInfo {
    public String id,key,name;

    public TrailerInfo() {
    }

    public TrailerInfo(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }
}
