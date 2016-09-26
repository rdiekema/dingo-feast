/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

/**
 * Created by rdiekema on 8/23/16.
 */
public interface Features {

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
