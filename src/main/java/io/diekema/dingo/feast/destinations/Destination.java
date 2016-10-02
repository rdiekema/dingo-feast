/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.destinations;

import io.diekema.dingo.feast.Exchange;

import java.io.IOException;

/**
 * Created by rdiekema on 9/14/16.
 */
public interface Destination {

    boolean deliver(Exchange exchange) throws IOException;

}
