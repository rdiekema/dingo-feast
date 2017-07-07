package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Exchange
import org.slf4j.LoggerFactory

/**
 * Created by rdiekema-idexx on 7/7/17.
 */
class LoggingProcessor : Processor {

    val LOG = LoggerFactory.getLogger(LoggingProcessor::class.java.name);

    override fun process(exchange: Exchange) {
        LOG.info(exchange.assets.get(0).name)
    }
}