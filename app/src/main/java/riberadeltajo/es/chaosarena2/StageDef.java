package riberadeltajo.es.chaosarena2;

import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.Map;

public class StageDef {
    public String name;
    public Bitmap bgBitmap;
    public float floorVisualOffset;
    public float groundY;
    private Map<String, Float> charYOffsets = new HashMap<>();

    public StageDef(String name, Bitmap bgBitmap, float floorVisualOffset, float groundY) {
        this.name = name;
        this.bgBitmap = bgBitmap;
        this.floorVisualOffset = floorVisualOffset;
        this.groundY = groundY;
    }

    public StageDef addOffset(String charName, float offset) {
        charYOffsets.put(charName, offset);
        return this;
    }

    public float getOffsetFor(String charName) {
        Float v = charYOffsets.get(charName);
        return v != null ? v : 0f;
    }

    public void recycle() {
        if (bgBitmap != null && !bgBitmap.isRecycled()) bgBitmap.recycle();
    }
}
