/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

import java.util.List;
import java.util.Map;

/**
 * Created by rdiekema on 8/23/16.
 */
public interface Message<T> {

    public void setBody(List<T> content);

    public List<T> getBody();

    public String getHeader(String header);

    public void setHeader(String header, String value);

    public Map<String, String> getHeaders();

    public void setHeaders(Map<String, String> headers);
}
