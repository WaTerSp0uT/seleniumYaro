package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class SecretConfigReader {

    public static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .systemProperties()   // Allows reading Maven -D flags
            .load();

    /**
     * Reads a secret from:
     * 1. .env
     * 2. JVM System Properties (-D)
     * 3. Environment Variables
     */
    public static String getSecret(String key) {

        String value = dotenv.get(key);

        // Fallback to JVM system property
        if (value == null) {
            value = System.getProperty(key);
        }

        // Final fallback to OS environment variable
        if (value == null) {
            value = System.getenv(key);
        }

        if (value == null) {
            throw new RuntimeException(
                    "Key [" + key + "] not found in .env, System Properties, or Environment Variables!"
            );
        }

        System.out.println("DEBUG: Login being used is: " + value);

        return value;
    }
}