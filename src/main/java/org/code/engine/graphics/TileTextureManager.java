package org.code.engine.graphics;

import org.code.engine.graphics.world.TileType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class TileTextureManager {

    private static final Set<String> ACCEPTED_FILE_TYPES = Set.of(".png", ".jpg");

    private final Map<TileType, List<Texture>> tileTextures;
    private final Random random;
    private final String texturesPath;

    public TileTextureManager(String texturesPath) {
        this.texturesPath = texturesPath;
        this.tileTextures = new EnumMap<>(TileType.class);
        this.random = new Random();

        for (TileType tileType : TileType.values()) {
            tileTextures.put(tileType, new ArrayList<>());
        }
    }

    public void loadTileTextures() {
        File texturesDir = new File(texturesPath);

        if (!texturesDir.exists() || !texturesDir.isDirectory()) {
            System.err.println("Textures directory not found: " + texturesPath);
            return;
        }

        File[] files = texturesDir.listFiles(((dir, name) ->
                ACCEPTED_FILE_TYPES.contains(name.toLowerCase())
        ));

        if (files == null || files.length == 0) {
            System.err.println("No texture files found in: " + texturesPath);
            return;
        }

        for (TileType tileType : TileType.values()) {
            loadVariationsForType(tileType, files);
        }

        tileTextures.forEach((tile, textures) -> {
            if (!textures.isEmpty()) {
                System.out.println("Loaded " + textures.size() + " texture(s) for " + tile.getTextureName());
            }
        });
    }

    private void loadVariationsForType(TileType tileType, File[] files) {
        String prefix = tileType.getTextureName() + "_";

        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.toLowerCase().startsWith(prefix.toLowerCase())) {
                continue;
            }
            try {
                Texture texture = TextureLoader.loadTexture(file.getAbsolutePath());
                tileTextures.get(tileType).add(texture);
            } catch (Exception e) {
                System.err.println("Failed to load texture: " + fileName + " - " + e.getMessage());
            }
        }
    }

    public Texture getRandomTexture(TileType type) {
        List<Texture> variations = tileTextures.get(type);

        if (variations.isEmpty()) {
            return null;
        }

        return variations.get(random.nextInt(variations.size()));
    }

    public Texture getTexture(TileType type, int variationIndex) {
        List<Texture> variations = tileTextures.get(type);

        if (variations.isEmpty()) {
            return null;
        }

        return variations.get(variationIndex % variations.size());
    }

    public int getVariationCount(TileType type) {
        return tileTextures.get(type).size();
    }

    public boolean hasTextures(TileType type) {
        return !tileTextures.get(type).isEmpty();
    }

    public boolean hasAnyTextures() {
        for (List<Texture> textures : tileTextures.values()) {
            if (!textures.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void cleanup() {
        for (List<Texture> textures : tileTextures.values()) {
            for (Texture texture : textures) {
                texture.cleanup();
            }
        }
        tileTextures.clear();
    }
}
