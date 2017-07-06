/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

/**
 * Created by rdiekema on 8/23/16.
 */
interface Features {

    interface File {

        interface Types {
            companion object {
                val `in` = "in"
                val out = "out"
            }
        }

        companion object {
            val `in` = "inputRoot"
            val out = "outputRoot"
            val stream = "stream"
            val originalName = "originalName"
        }
    }


    interface Message {
        interface Fields {
            companion object {
                val content = "content"
                val headers = "headers"
            }
        }

    }

    interface Format {
        companion object {
            val js = "js"
            val sass = "sass"
            val less = "less"
            val css = "css"
            val html = "html"
            val text = "text"
        }
    }

    interface Operations {
        companion object {
            val noop = "noop"
            val concat = "concat"
            val minify = "min"
            val compile = "compile"
            val replace = "replace"
        }
    }

    companion object {

        val DOT = "."
    }

}
