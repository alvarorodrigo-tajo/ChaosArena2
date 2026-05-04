package riberadeltajo.es.chaosarena2;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class BucleJuego extends Thread {

    public  static final int MAX_FPS            = 60;
    private static final int MAX_FRAMES_SALTADOS = 5;
    private static final int TIEMPO_FRAME        = 1000 / MAX_FPS;

    private final Juego         juego;
    private final SurfaceHolder surfaceHolder;

    public volatile boolean enEjecucion = true;

    public int  iteraciones;
    public long tiempoTotal;
    public int  maxX, maxY;

    private static final String TAG = BucleJuego.class.getSimpleName();

    BucleJuego(SurfaceHolder sh, Juego juego) {
        this.juego         = juego;
        this.surfaceHolder = sh;

        Canvas c = sh.lockCanvas();
        if (c != null) {
            maxX = c.getWidth();
            maxY = c.getHeight();
            sh.unlockCanvasAndPost(c);
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "Bucle de juego iniciado");

        long  tiempoComienzo;
        long  tiempoDiferencia;
        int   tiempoDormir;
        int   framesASaltar;

        while (enEjecucion) {
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                if (canvas == null) continue;

                synchronized (surfaceHolder) {
                    tiempoComienzo = System.currentTimeMillis();
                    framesASaltar  = 0;

                    // ── Delta time en segundos ────────────────────────────────
                    float delta = TIEMPO_FRAME / 1000f;

                    juego.actualizar(delta);
                    juego.renderizar(canvas);
                    iteraciones++;

                    tiempoDiferencia = System.currentTimeMillis() - tiempoComienzo;
                    tiempoDormir     = (int)(TIEMPO_FRAME - tiempoDiferencia);
                    tiempoTotal     += tiempoDiferencia + Math.max(0, tiempoDormir);

                    if (tiempoDormir > 0) {
                        try { Thread.sleep(tiempoDormir); }
                        catch (InterruptedException ignored) {}
                    }

                    // Ponerse al día si vamos lentos
                    while (tiempoDormir < 0 && framesASaltar < MAX_FRAMES_SALTADOS) {
                        juego.actualizar(delta);
                        tiempoDormir += TIEMPO_FRAME;
                        framesASaltar++;
                    }
                }
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        Log.d(TAG, "Bucle de juego terminado");
    }
}

