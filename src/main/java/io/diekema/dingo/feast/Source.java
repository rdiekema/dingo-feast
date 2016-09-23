package io.diekema.dingo.feast;

import java.io.IOException;
import java.util.List;

/**
 * Created by rdiekema on 9/14/16.
 */
public interface Source {

    Exchange collect() throws IOException;

}
