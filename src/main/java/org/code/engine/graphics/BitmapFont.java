package org.code.engine.graphics;

import org.code.utils.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Loads a TTF font file and bakes it into an OpenGL texture atlas using STB TrueType.
 * One instance per font size. TextRenderer owns these.
 */
public final class BitmapFont {

    // Atlas dimensions — 512x512 is plenty for ASCII at most sizes
    private static final int ATLAS_WIDTH  = 512;
    private static final int ATLAS_HEIGHT = 512;

    // ASCII range we care about: space (32) through tilde (126)
    private static final int FIRST_CHAR = 32;
    private static final int CHAR_COUNT = 96;

    private final STBTTBakedChar.Buffer charData;
    private final int textureId;
    private final float fontSize;

    /**
     * @param fontPath  path to .ttf file, e.g. "assets/fonts/font.ttf"
     * @param fontSize  point size to bake at, e.g. 16f
     */
    public BitmapFont(String fontPath, float fontSize) {
        this.fontSize = fontSize;
        this.charData = STBTTBakedChar.malloc(CHAR_COUNT);

        // Load TTF file into a ByteBuffer
        ByteBuffer ttfBuffer = loadFile(fontPath);

        // Bake font into a greyscale bitmap
        ByteBuffer bitmap = MemoryUtil.memAlloc(ATLAS_WIDTH * ATLAS_HEIGHT);
        STBTruetype.stbtt_BakeFontBitmap(ttfBuffer, fontSize, bitmap, ATLAS_WIDTH, ATLAS_HEIGHT, FIRST_CHAR, charData);

        // Upload bitmap to OpenGL as an alpha-only texture
        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, ATLAS_WIDTH, ATLAS_HEIGHT,
                0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        MemoryUtil.memFree(bitmap);

        Logger.debug(BitmapFont.class, "BitmapFont: baked '" + fontPath + "' at " + fontSize + "px");
    }

    /**
     * Render a single character quad at (x, y) in screen space.
     * Advances x by the character width and returns the new x position.
     * Must be called between glBegin(GL_QUADS) / glEnd().
     */
    public float renderChar(char c, float x, float y) {
        int idx = c - FIRST_CHAR;
        if (idx < 0 || idx >= CHAR_COUNT) {
            return x; // unsupported character — skip
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xBuf = stack.floats(x);
            FloatBuffer yBuf = stack.floats(y);

            STBTTBakedChar bakedChar = charData.get(idx);

            float x0 = x + bakedChar.xoff();
            float y0 = y + bakedChar.yoff();
            float x1 = x0 + (bakedChar.x1() - bakedChar.x0());
            float y1 = y0 + (bakedChar.y1() - bakedChar.y0());

            float u0 = (float) bakedChar.x0() / ATLAS_WIDTH;
            float v0 = (float) bakedChar.y0() / ATLAS_HEIGHT;
            float u1 = (float) bakedChar.x1() / ATLAS_WIDTH;
            float v1 = (float) bakedChar.y1() / ATLAS_HEIGHT;

            GL11.glTexCoord2f(u0, v0); GL11.glVertex2f(x0, y0);
            GL11.glTexCoord2f(u1, v0); GL11.glVertex2f(x1, y0);
            GL11.glTexCoord2f(u1, v1); GL11.glVertex2f(x1, y1);
            GL11.glTexCoord2f(u0, v1); GL11.glVertex2f(x0, y1);

            return x + bakedChar.xadvance();
        }
    }

    /**
     * Returns the total pixel width of a string at this font's size.
     */
    public float measureWidth(String text) {
        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            int idx = text.charAt(i) - FIRST_CHAR;
            if (idx >= 0 && idx < CHAR_COUNT) {
                width += charData.get(idx).xadvance();
            }
        }
        return width;
    }

    /**
     * Approximate line height — STB doesn't expose this directly, so we use fontSize as a
     * close-enough value. Multiply by 1.2 for comfortable line spacing.
     */
    public float getLineHeight() {
        return fontSize;
    }

    public int getTextureId() {
        return textureId;
    }

    public void cleanup() {
        GL11.glDeleteTextures(textureId);
        charData.free();
    }

    private static ByteBuffer loadFile(String path) {
        try (InputStream is = BitmapFont.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                // Try as a filesystem path
                java.io.File file = new java.io.File(path);
                if (!file.exists()) {
                    throw new RuntimeException("Font file not found: " + path);
                }
                byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                ByteBuffer buf = MemoryUtil.memAlloc(bytes.length);
                buf.put(bytes);
                buf.flip();
                return buf;
            }
            byte[] bytes = is.readAllBytes();
            ByteBuffer buf = MemoryUtil.memAlloc(bytes.length);
            buf.put(bytes);
            buf.flip();
            return buf;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font: " + path, e);
        }
    }
}
