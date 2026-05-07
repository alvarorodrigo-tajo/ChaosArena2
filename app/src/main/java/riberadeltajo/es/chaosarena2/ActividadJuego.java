package riberadeltajo.es.chaosarena2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import riberadeltajo.es.chaosarena2.engine.Juego;
import riberadeltajo.es.chaosarena2.engine.ResourceManager;

public class ActividadJuego extends AppCompatActivity {

    // ── Constantes de juego ──────────────────────────────
    public static final int MAX_LEVELS = 3;
    public static final int MAX_SLOTS  = 3;

    public static final float[] LEVEL_ENEMY_HEALTH = { 250f, 250f, 250f };
    public static final float[] LEVEL_ENEMY_SPEED  = { 300f, 400f, 500f };
    public static final float[] LEVEL_ENEMY_DAMAGE = {   5f,   5f,   5f };
    public static final float[] LEVEL_ATTACK_RATE  = { 1.6f, 1.0f, 0.6f };

    public static final String[] LEVEL_ENEMY_NAMES = { "Goro", "Sub Zero", "Liu Kang" };
    public static final String[] CHAR_NAMES         = { "Liu Kang", "Goro", "Ronin" };

    public static final String[] LEVEL_LORE = {
            "Nivel 1: El Torneo Comienza\n\nHas llegado a las puertas del Chaos Arena. " +
                    "Tu primer rival es un guerrero sin nombre que busca robar tu honor. ¡Derrótalo!",
            "Nivel 2: El Honor del Ronin\n\nBajo el sol abrasador, un maestro de la espada " +
                    "te espera. Dice que tu estilo es débil... demuéstrale que se equivoca.",
            "Nivel 3: El Maestro de las Sombras\n\nLa ciudad cyberpunk es el fin del camino. " +
                    "El Shadow Master te observa desde lo alto. Si vences, serás leyenda."
    };

    // ── Campos ────────────────────────────────────────────────────────────────
    private Juego           juego;
    private ResourceManager resources;
    private SharedPreferences prefs;

    // ── Ciclo de vida ─────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pantalla completa, sin barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        prefs     = getSharedPreferences("ChaosArenaPrefs", MODE_PRIVATE);
        resources = new ResourceManager(this);
        juego     = new Juego(this, resources, prefs);

        setContentView(juego);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (juego != null) juego.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (juego != null) juego.resumeMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (juego  != null) juego.releaseMusic();
        if (resources != null) resources.recycle();
    }
}

