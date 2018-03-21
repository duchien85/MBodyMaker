package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RuntimeTextureLoader {

    Map<String, TextureRegion> assetMap = new HashMap<String, TextureRegion>();
    Map<String, TextureRegion> customLoadMap = new HashMap<String, TextureRegion>();

    @Nullable
    public TextureRegion loadFromAsset(String path){
        TextureRegion region = assetMap.get(path);
        if (region == null){
            final Texture texture;
            try {
                texture = new Texture(path);
            } catch (Exception e) {
                return null;
            }
            region = new TextureRegion(texture);
            assetMap.put(path, region);
        }
        return region;
    }

    public TextureRegion loadFromFile(FileHandle fileHandle) {
        TextureRegion region = customLoadMap.get(fileHandle.path());
        if (region == null){
            final Texture texture;
            try {
                texture = new Texture(fileHandle);
            } catch (Exception e) {
                return null;
            }
            region = new TextureRegion(texture);
            customLoadMap.put(fileHandle.path(), region);
        }
        return region;
    }
}
