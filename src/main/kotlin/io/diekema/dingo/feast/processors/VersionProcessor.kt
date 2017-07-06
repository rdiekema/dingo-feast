package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import org.apache.commons.codec.digest.DigestUtils

import java.io.IOException

/**
 * Created by rdiekema on 10/2/16.
 */
class VersionProcessor : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        for (asset in exchange.assets) {
            asset.revision = "_" + DigestUtils.md5Hex(asset.content)
        }
    }
}
