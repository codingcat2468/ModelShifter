package com.codingcat.modelshifter.client.api.config;

import java.io.File;
import java.io.IOException;

public interface JsonConfiguration {
    File getConfigFile() throws IOException;
}
