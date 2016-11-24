/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rdiekema on 8/24/16.
 */
public class Exchange {

    private LinkedList<Asset> assets = new LinkedList<>();

    public Exchange() {

        this.assets = new LinkedList<>();
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
