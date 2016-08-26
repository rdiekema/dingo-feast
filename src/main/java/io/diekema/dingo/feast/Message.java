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
