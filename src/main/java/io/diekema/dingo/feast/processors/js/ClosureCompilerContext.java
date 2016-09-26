/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.js;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;

import java.util.Collections;
import java.util.List;

/**
 * Created by rdiekema on 8/23/16.
 */
public class ClosureCompilerContext {
    private Compiler compiler;

    public ClosureCompilerContext() {
        this.compiler = new Compiler();
    }

    public String minify(List<SourceFile> sourceFiles) {
        CompilerOptions options = new CompilerOptions();

        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        Result result = compiler.compile(Collections.EMPTY_LIST, sourceFiles, options);

        if(result.success){
            return compiler.toSource();
        }

        return null;
    }
}
