package org.code.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class TextureLoader {

    private TextureLoader() {
        // Utility class
    }

    public static Texture loadTexture(String path) {
        ByteBuffer imageBuffer;
        int width;
        int height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            imageBuffer = STBImage.stbi_load(path, w, h, channels, 4);

            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture: " + path + " - " + STBImage.stbi_failure_reason());
            }
            width = w.get(0);
            height = h.get(0);
        }

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);

        STBImage.stbi_image_free(imageBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        System.out.println("Loaded texture: " + path + " (" + width + "x" + height + ")");

        return new Texture(textureId, width, height);
    }
}
