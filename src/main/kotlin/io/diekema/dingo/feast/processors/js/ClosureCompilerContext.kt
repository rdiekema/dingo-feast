/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.js

import com.google.javascript.jscomp.*
import com.google.javascript.jscomp.Compiler

import java.util.Collections

/**
 * Created by rdiekema on 8/23/16.
 */
class ClosureCompilerContext {
    private val compiler: Compiler

    init {
        this.compiler = Compiler()
    }

    fun minify(sourceFiles: List<SourceFile>): String? {
        val options = CompilerOptions()

        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options)

        val result = compiler.compile<SourceFile, SourceFile>(mutableListOf(), sourceFiles, options)

        if (result.success) {
            return compiler.toSource()
        }

        return null
    }
}
