package riberadeltajo.es.chaosarena2.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import java.io.IOException;
import java.io.InputStream;

import riberadeltajo.es.chaosarena2.stage.StageDef;

/**
 * Carga y centraliza todos los assets visuales del juego.
 * Equivalente al ResourceManager de libGDX pero usando Android APIs.
 */
public class ResourceManager {

    // ── Fuentes ───────────────────────────────────────────────────────────────
    public final Paint fontSmall;   // ~36sp — textos de HUD
    public final Paint fontBig;     // ~72sp — títulos y resultados
    public final Paint fontMedium;  // ~52sp — botones

    // ── Fondos ────────────────────────────────────────────────────────────────
    public final Bitmap bgTitle;

    // ── Joystick ──────────────────────────────────────────────────────────────
    public final Bitmap joystickBg;
    public final Bitmap joystickKnob;

    // ── Iconos ────────────────────────────────────────────────────────────────
    public final Bitmap soundOn;
    public final Bitmap soundOff;

    // ── Escenarios ────────────────────────────────────────────────────────────
    public final StageDef[] stages;

    // ── AssetManager (necesario para cargar animaciones de personajes) ────────
    public final AssetManager assets;

    // ── Constructor ───────────────────────────────────────────────────────────

    public ResourceManager(Context context) {
        assets = context.getAssets();

        // Fuentes
        fontSmall  = makePaint(36f,  0xFFFFFFFF);
        fontMedium = makePaint(52f,  0xFFFFFFFF);
        fontBig    = makePaint(72f,  0xFFFFFFFF);

        // Fondos e iconos
        bgTitle     = loadBitmap("backgrounds/title_bg.png");
        joystickBg   = loadBitmap("joystick/AIR_joystick_bg600.png");
        joystickKnob = loadBitmap("joystick/AIR_joystick_stick600.png");
        soundOn      = loadBitmap("ui/sound_on.png");
        soundOff     = loadBitmap("ui/sound_off.png");

        // Escenarios
        Bitmap bgForest  = loadBitmap("backgrounds/forest_bg.png");
        Bitmap bgDesert  = loadBitmap("backgrounds/desert_bg.png");
        Bitmap bgCity    = loadBitmap("backgrounds/city_bg.png");

        stages = new StageDef[]{
                new StageDef("Bosque",   bgForest, -40f,  80f),
                new StageDef("Desierto", bgDesert,   0f,  60f),
                new StageDef("Ciudad",   bgCity,    40f,  90f),
        };
    }

    // ── Helpers de personaje ──────────────────────────────────────────────────

    /** Carpeta de assets para cada personaje. */
    public String getFolderForChar(String name) {
        if (name.contains("Goro"))     return "sprites/goro";
        if (name.contains("Sub Zero")) return "sprites/subzero";
        return "sprites/liu_kang";
    }

    /** Prefijo de frames para cada personaje. */
    public String getPrefixForChar(String name) {
        if (name.contains("Goro"))     return "goro";
        if (name.contains("Sub Zero")) return "subzero";
        return "liu_kang";
    }

    // ── Helpers internos ─────────────────────────────────────────────────────

    public Bitmap loadBitmap(String assetPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        try (InputStream is = assets.open(assetPath)) {
            return BitmapFactory.decodeStream(is, null, opts);
        } catch (IOException e) {
            // Devuelve bitmap de un pixel si el asset no existe
            Bitmap b = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFF00FF); // magenta para detectar assets faltantes
            return b;
        }
    }

    private static Paint makePaint(float sp, int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setTextSize(sp);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        return p;
    }

    // ── Dispose ───────────────────────────────────────────────────────────────

    public void recycle() {
        recycleBitmap(bgTitle);
        recycleBitmap(joystickBg);
        recycleBitmap(joystickKnob);
        recycleBitmap(soundOn);
        recycleBitmap(soundOff);
        for (StageDef s : stages) s.recycle();
    }

    private static void recycleBitmap(Bitmap b) {
        if (b != null && !b.isRecycled()) b.recycle();
    }
}
