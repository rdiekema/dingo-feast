/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.mojo

import io.diekema.dingo.feast.Build
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import kotlin.reflect.full.createInstance

/**
 * Created by rdiekema on 8/26/16.
 */

@Mojo(name = "feast")
class FeastMojo : AbstractMojo() {

    @Parameter private val className: String? = null

    @Throws(MojoExecutionException::class, MojoFailureException::class)
    override fun execute() {
        val kClass = Class.forName(className).kotlin
        val build = kClass.createInstance() as Build

        build.run()
    }
}
