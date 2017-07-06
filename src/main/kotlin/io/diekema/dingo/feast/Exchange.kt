/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

import java.util.LinkedList

/**
 * Created by rdiekema on 8/24/16.
 */
class Exchange {

    var assets = mutableListOf<Asset>()

    fun enrich(assets: List<Asset>) {
        this.assets.addAll(assets)
    }

}
