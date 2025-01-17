package com.bmuschko.gradle.docker.internal

import groovy.transform.CompileStatic
import org.gradle.internal.logging.progress.ProgressLogger
import org.gradle.internal.logging.progress.ProgressLoggerFactory
import org.gradle.internal.service.ServiceRegistry

@CompileStatic
final class IOUtils {

    private IOUtils() { }

    static void closeQuietly(Closeable toClose) {
        try {
            if (toClose != null) {
                toClose.close()
            }
        } catch (IOException ignored) {
            // ignore
        }
    }

    /**
     * Create a progress logger for an arbitrary project and class.
     *
     * @param services the service registry.
     * @param clazz optional class to pair the ProgressLogger to. Defaults to _this_ class if null.
     * @return instance of ProgressLogger.
     */
    static ProgressLogger getProgressLogger(final ServiceRegistry registry, final Class clazz) {
        ProgressLoggerFactory factory = registry.get(ProgressLoggerFactory)
        ProgressLogger progressLogger = factory.newOperation(Objects.requireNonNull(clazz))
        progressLogger.setDescription("ProgressLogger for ${clazz.getSimpleName()}")
    }
}
