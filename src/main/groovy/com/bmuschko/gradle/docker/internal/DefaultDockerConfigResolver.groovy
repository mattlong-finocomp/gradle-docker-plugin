package com.bmuschko.gradle.docker.internal

import groovy.transform.CompileStatic
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import javax.annotation.Nullable

import static com.bmuschko.gradle.docker.internal.OsUtils.isWindows

@CompileStatic
class DefaultDockerConfigResolver {

    private static final Logger logger = Logging.getLogger(DefaultDockerConfigResolver)

    String getDefaultDockerUrl() {
        String dockerUrl = getEnv("DOCKER_HOST")
        if (!dockerUrl) {
            boolean isWindows = isWindows()

            if (isWindows) {
                if (isFileExists('\\\\.\\pipe\\docker_engine')) {
                    dockerUrl = 'npipe:////./pipe/docker_engine'
                }
            } else {
                // macOS or Linux
                if (isFileExists('/var/run/docker.sock')) {
                    dockerUrl = 'unix:///var/run/docker.sock'
                } else if (isFileExists("${System.getProperty("user.home")}/.docker/run/docker.sock")) {
                    dockerUrl = "unix://${System.getProperty('user.home')}/.docker/run/docker.sock"
                }
            }

            if (!dockerUrl) {
                dockerUrl = 'tcp://127.0.0.1:2375'
            }
        }
        logger.info("Default docker.url set to $dockerUrl")
        dockerUrl
    }

    @Nullable
    File getDefaultDockerCert() {
        String dockerCertPath = getEnv("DOCKER_CERT_PATH")
        if (dockerCertPath) {
            File certFile = new File(dockerCertPath)
            if (certFile.exists()) {
                return certFile
            }
        }
        return null
    }

    @Nullable
    protected String getEnv(String name) {
        System.getenv(name)
    }

    protected boolean isFileExists(String path) {
        new File(path).exists()
    }

}
