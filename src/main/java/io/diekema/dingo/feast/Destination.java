package io.diekema.dingo.feast;

import java.io.IOException;

/**
 * Created by rdiekema on 9/14/16.
 */
public interface Destination {

    boolean deliver(Exchange exchange) throws IOException;

}
