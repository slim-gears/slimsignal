// Copyright 2015 Denis Itskovich
// Refer to LICENSE.txt for license details
package com.slimgears.slimsignal.apt;

import com.slimgears.slimapt.AnnotationProcessorBase;

import java.io.IOException;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

/**
 * Created by ditskovi on 12/27/2015.
 */
@SupportedAnnotationTypes("com.slimgears.slimsignal.core.annotations.Entity")
public class EntityAnnotationProcessor extends AnnotationProcessorBase {
    @Override
    protected boolean processType(TypeElement typeElement) throws IOException {
        new EntityMetaGenerator(processingEnv, typeElement).build();
        return true;
    }
}
