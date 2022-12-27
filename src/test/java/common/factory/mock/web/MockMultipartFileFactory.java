package common.factory.mock.web;

import org.springframework.mock.web.MockMultipartFile;

import java.util.Random;

public final class MockMultipartFileFactory {
    public static MockMultipartFile createDefaultPngMockMultipartFile(final int size) {
        return create("image", "image.png", "image/png", size);
    }

    public static MockMultipartFile createDefaultTxtMockMultipartFile(final int size) {
        return create("text", "text.txt", "text/plain", size);
    }

    public static MockMultipartFile create(final String name, final String originalFilename, final String contentType, final int size) {
        final Random random = new Random();

        final byte[] bytes = new byte[size];

        random.nextBytes(bytes);

        return new MockMultipartFile(name, originalFilename, contentType, bytes);
    }
}
