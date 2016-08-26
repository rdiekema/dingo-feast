package io.diekema.dingo.feast;

/**
 * Created by rdiekema on 8/23/16.
 */
public interface DSL {

    String DOT = ".";

    interface File {
        String in = "inputRoot";
        String out = "outputRoot";
        String stream = "stream";
        String originalName = "originalName";

        interface Types {
            String in = "in";
            String out = "out";
        }
    }



    interface Message {
        interface Fields {
            String content = "content";
            String headers = "headers";
        }

    }

    interface Format {
        String js = "js";
        String sass = "sass";
        String less = "less";
        String css = "css";
        String html = "html";
        String text = "text";
    }

    interface Operations {
        String noop = "noop";
        String concat = "concat";
        String minify = "min";
        String compile = "compile";
        String replace = "replace";
    }

}
