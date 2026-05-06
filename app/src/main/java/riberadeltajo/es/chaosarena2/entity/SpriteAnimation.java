package riberadeltajo.es.chaosarena2.entity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reemplaza TextureAtlas + Animation de libGDX.
 * Carga frames individuales desde assets/sprites/<prefijo>/<nombre>_001.png ...
 * y los reproduce como animación con control de duración y modo de bucle.
 */
public class SpriteAnimation {

    public enum PlayMode { LOOP, NORMAL }

    private final Bitmap[] frames;
    private final float frameDuration;  // segundos por frame
    private final PlayMode playMode;
    private final float totalDuration;

    public SpriteAnimation(Bitmap[] frames, float frameDuration, PlayMode playMode) {
        this.frames        = frames;
        this.frameDuration = frameDuration;
        this.playMode      = playMode;
        this.totalDuration = frameDuration * frames.length;
    }

    /** Devuelve el frame correspondiente al tiempo dado. */
    public Bitmap getKeyFrame(float stateTime) {
        if (frames.length == 0) return null;
        int idx;
        if (playMode == PlayMode.LOOP) {
            idx = (int)(stateTime / frameDuration) % frames.length;
        } else {
            idx = Math.min((int)(stateTime / frameDuration), frames.length - 1);
        }
        return frames[idx];
    }

    public boolean isFinished(float stateTime) {
        return playMode == PlayMode.NORMAL && stateTime >= totalDuration;
    }

    public float getDuration() { return totalDuration; }

    public int getFrameWidth()  { return frames.length > 0 ? frames[0].getWidth()  : 1; }
    public int getFrameHeight() { return frames.length > 0 ? frames[0].getHeight() : 1; }

    public void recycle() {
        for (Bitmap b : frames) if (b != null && !b.isRecycled()) b.recycle();
    }

    // ── Carga desde assets ────────────────────────────────────────────────────

    /**
     * Carga una animación buscando en assets todos los archivos que empiecen
     * por `prefix` dentro de la carpeta `folder`, ordenados por nombre.
     * Ejemplo: folder="sprites/martial1", prefix="martial1_idle"
     */
    public static SpriteAnimation load(AssetManager assets, String folder,
                                       String prefix, float frameDuration, PlayMode mode) {
        List<String> names = new ArrayList<>();
        try {
            String[] files = assets.list(folder);
            if (files != null) {
                for (String f : files) {
                    if (f.startsWith(prefix) && (f.endsWith(".png") || f.endsWith(".webp"))) {
                        names.add(folder + "/" + f);
                    }
                }
            }
        } catch (IOException e) { /* carpeta no encontrada */ }

        Collections.sort(names);

        if (names.isEmpty()) return fallback(frameDuration, mode);

        List<Bitmap> bitmaps = new ArrayList<>();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        for (String path : names) {
            try (InputStream is = assets.open(path)) {
                Bitmap bmp = BitmapFactory.decodeStream(is, null, opts);
                if (bmp != null) bitmaps.add(bmp);
            } catch (IOException e) { /* frame corrupto, se salta */ }
        }

        if (bitmaps.isEmpty()) return fallback(frameDuration, mode);
        return new SpriteAnimation(bitmaps.toArray(new Bitmap[0]), frameDuration, mode);
    }

    /** Animación de un solo pixel blanco, para cuando no hay sprites. */
    public static SpriteAnimation fallback(float frameDuration, PlayMode mode) {
        Bitmap b = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        b.eraseColor(0xFFFFFFFF);
        return new SpriteAnimation(new Bitmap[]{b}, frameDuration, mode);
    }
}
