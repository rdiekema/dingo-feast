package io.diekema.dingo.feast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rdiekema on 8/24/16.
 */
public class Exchange {

    private LinkedList<Asset> assets = new LinkedList<>();

    public Exchange() {

        this.assets = new LinkedList<Asset>();
    }

    public void enrich(List<Asset> assets) {
        this.assets.addAll(assets);
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = new LinkedList<>(assets);
    }

}
