// Copyright 2015 Denis Itskovich
// Refer to LICENSE.txt for license details
package com.slimgears.slimorm.core.interfaces;

import java.io.Closeable;
import java.io.IOException;

/**
* Created by Denis on 09-Apr-15
* <File Description>
*/
public interface RepositorySession extends Closeable {
    void saveChanges() throws IOException;
    void discardChanges();
}