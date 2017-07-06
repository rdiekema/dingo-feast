/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

/**
 * Created by rdiekema on 8/23/16.
 */
interface Message<T> {

    var body: List<T>

    fun getHeader(header: String): String

    fun setHeader(header: String, value: String)

    var headers: Map<String, String>
}
