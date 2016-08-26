package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by rdiekema on 8/23/16.
 */
public class AssetMessage implements Message<Asset> {
    List<Asset> body = new LinkedList<>();
    Map<String, String> headers = new HashMap<>();

    @Override
    public void setBody(List<Asset> body) {
        this.body = body;
    }

    @Override
    public List<Asset> getBody() {
        return body;
    }

    @Override
    public String getHeader(String header) {
        return null;
    }

    @Override
    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
