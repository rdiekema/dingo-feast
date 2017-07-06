/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by rdiekema on 8/23/16.
 */
class Asset {
    var name: String? = null
    var content: String? = null
    var originalPath: Path? = null
    var currentPath: Path? = null
    var extension: String? = null
    var revision: String? = null

    constructor(name: String?, content: String?, originalPath: String?, extension: String?) {
        this.name = name
        this.content = content

        originalPath?.let {
            this.originalPath = Paths.get(originalPath);
        }

        this.extension = extension
    }

    constructor(name: String?, content: String, originalPath: String?) {
        this.content = content

        originalPath?.let {
            this.originalPath = Paths.get(originalPath).toAbsolutePath()
        }

        if (name != null && name.contains(".")) {
            val nameWithExtension = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (nameWithExtension.size > 1) {
                extension = nameWithExtension[nameWithExtension.size - 1]
                this.name = name.substring(0, name.indexOf(extension!!) - 1)
            }
        } else {
            this.name = name
        }
    }

    constructor(name: String?, content: String?, originalPath: Path?, currentPath: Path?, extension: String?, revision: String?) {
        this.name = name
        this.content = content
        this.originalPath = originalPath
        this.currentPath = currentPath
        this.extension = extension
        this.revision = revision
    }

    override fun toString(): String {
        return "Asset{" +
                "name='" + name + "'" +
                ", originalPath='" + originalPath + "'" +
                ", currentPath='" + currentPath + "'" +
                ", extension='" + extension + "'" +
                '}'
    }
}
