package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

/**
 * Created by rdiekema on 10/2/16.
 */
public class Version implements Processor {

    @Override
    public void process(Exchange exchange) throws IOException {
        for (Asset asset : exchange.getAssets()) {
            asset.setRevision("_" + DigestUtils.md5Hex(asset.getContent()));
        }
    }
}
