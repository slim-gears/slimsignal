package com.slimgears.slimsignal.apt;
// Copyright 2015 Denis Itskovich
// Refer to LICENSE.txt for license details

import com.slimgears.slimapt.AnnotationProcessorBase;

import java.io.IOException;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

/**
 * Created by Denis on 02-Apr-15
 * <File Description>
 */
@SupportedAnnotationTypes("com.slimgears.slimsignal.core.annotations.GenerateEntity")
public class GenerateEntityAnnotationProcessor extends AnnotationProcessorBase {

    @Override
    protected boolean processType(TypeElement typeElement) throws IOException {
        new EntityGenerator(processingEnv)
                .superClass(typeElement)
                .build();

        return true;
    }
}
