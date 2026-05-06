package riberadeltajo.es.chaosarena2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.*;
import android.media.MediaPlayer;
import android.view.*;

public class Juego extends SurfaceView implements SurfaceHolder.Callback {

    // ── Constantes ────────────────────────────────────────────────────────────
    private static final float WORLD_W      = 1920f;
    private static final float WORLD_H      = 1080f;
    private static final float INITIAL_TIME = 90f;
    private static final float MOVE_SPEED   = 650f;
    private static final float MAP_MIN_X    = 150f;
    private static final float BAR_WIDTH    = 700f;
    private static final float BAR_HEIGHT   = 45f;
    private static final float BAR_COMBO_H  = 12f;

    // ── Motor ─────────────────────────────────────────────────────────────────
    private final Activity activity;
    private final ResourceManager   res;
    private final SharedPreferences prefs;
    private BucleJuego  bucle;
    private MediaPlayer music;
    private VirtualJoystick joystick;

    // ── Escalado mundo→pantalla ───────────────────────────────────────────────
    private final Matrix worldMatrix   = new Matrix();
    private final Matrix inverseMatrix = new Matrix();

    // ── Modo de juego ─────────────────────────────────────────────────────────
    private int activeSlot;       // >=0 historia | -1 duelo | -2 arcade
    private int currentLevel = 1;

    // ── Estado ────────────────────────────────────────────────────────────────
    private enum Screen { MAIN_MENU, CHAR_SELECT, LORE, FIGHTING, PAUSED, RESULT, BUFF_SELECT, EVENT }
    private Screen currentScreen = Screen.MAIN_MENU;

    // ── Jugadores e IA ────────────────────────────────────────────────────────
    private Player  player1, player2;
    private EnemyAI enemyAI;

    // ── Modo arcade ───────────────────────────────────────────────────────────
    private float      timeLeft          = INITIAL_TIME;
    private int        enemiesDefeated   = 0;
    private float      totalTimeSurvived = 0f;
    private RunManager runManager;

    // ── Resultado ─────────────────────────────────────────────────────────────
    private boolean lastBattleWon    = false;
    private boolean pendingResult    = false;
    private boolean pendingResultWon = false;
    private float   deathDelay       = 0f;
    private static final float DEATH_ANIM_WAIT = 1.5f;

    // ── Escenario ─────────────────────────────────────────────────────────────
    private StageDef currentStage;

    // ── Selección ─────────────────────────────────────────────────────────────
    private int selectedCharIdx  = 0;
    private int selectedStageIdx = 0;
    private int pendingSlot      = 0;

    // ── Buff / Event ──────────────────────────────────────────────────────────
    private java.util.List<Buff> buffChoices;
    private Event pendingEvent;

    // ── Botones — cada pantalla tiene los suyos, nunca se comparten ───────────

    // Menú principal
    private final RectF btnStory  = new RectF();
    private final RectF btnArcade = new RectF();
    private final RectF btnDuel   = new RectF();

    // Selección de personaje
    private final RectF[] btnChars      = { new RectF(), new RectF(), new RectF() };
    private final RectF   btnStageChange = new RectF();
    private final RectF   btnLuchar      = new RectF();

    // Lore
    private final RectF btnStartFight = new RectF();

    // Combate
    private final RectF btnPause   = new RectF();
    private final RectF btnPunch   = new RectF();
    private final RectF btnKick    = new RectF();
    private final RectF btnSpecial = new RectF();
    private final RectF btnJump    = new RectF();

    // Pausa
    private final RectF btnResume   = new RectF();
    private final RectF btnExitMenu = new RectF();

    // Resultado
    private final RectF btnResultAction = new RectF();
    private final RectF btnResultMenu   = new RectF();

    // Selección de buff
    private final RectF[] btnBuffs = { new RectF(), new RectF(), new RectF() };

    // Evento
    private final RectF btnEventOk = new RectF();

    // ── Paints ────────────────────────────────────────────────────────────────
    private final Paint paintOverlay = new Paint();
    private final Paint paintBlack   = new Paint();
    private final Paint paintBitmap  = new Paint(Paint.FILTER_BITMAP_FLAG);

    // ── Efecto de impacto ─────────────────────────────────────────────────────
    private float hitFlashTimer = 0f;
    private float hitFlashX     = 0f;
    private float hitFlashY     = 0f;
    private static final float HIT_FLASH_DURATION = 0.10f;
    private final Paint paintHitFlash = new Paint(Paint.ANTI_ALIAS_FLAG);

    // ── Constructor ───────────────────────────────────────────────────────────

    public Juego(Activity activity, ResourceManager res, SharedPreferences prefs) {
        super(activity);
        this.activity = activity;
        this.res      = res;
        this.prefs    = prefs;

        getHolder().addCallback(this);
        setFocusable(true);

        paintOverlay.setColor(0xCC000000);
        paintBlack.setColor(0xFF000000);

        joystick = new VirtualJoystick(280f, WORLD_H - 280f, 180f,
                res.joystickBg, res.joystickKnob);

        initAllButtonRects();
    }

    // ── Inicialización de botones (posiciones fijas en coordenadas mundo) ─────

    private void initAllButtonRects() {
        // Menú principal — centrados horizontalmente
        float bw = 700f, bh = 140f, cx = (WORLD_W - bw) / 2f;
        btnStory .set(cx, 350f, cx + bw, 490f);
        btnArcade.set(cx, 530f, cx + bw, 670f);
        btnDuel  .set(cx, 710f, cx + bw, 850f);

        // Selección de personaje
        float cbw = 500f, cbh = 130f, cgap = 80f;
        float totalW = 3 * cbw + 2 * cgap;
        float startX = (WORLD_W - totalW) / 2f;
        for (int i = 0; i < 3; i++)
            btnChars[i].set(startX + i * (cbw + cgap), 300f,
                    startX + i * (cbw + cgap) + cbw, 300f + cbh);
        btnStageChange.set(760f, 590f, 1160f, 730f);
        btnLuchar.set((WORLD_W - 700f) / 2f, WORLD_H - 220f,
                (WORLD_W + 700f) / 2f, WORLD_H - 80f);

        // Lore
        btnStartFight.set(660f, WORLD_H - 220f, 1260f, WORLD_H - 80f);

        // Pausa
        btnResume  .set(cx, 480f, cx + bw, 620f);
        btnExitMenu.set(cx, 660f, cx + bw, 800f);

        // Resultado
        btnResultAction.set(cx, 500f, cx + bw, 640f);
        btnResultMenu  .set(cx, 680f, cx + bw, 820f);

        // Evento
        btnEventOk.set(660f, 700f, 1260f, 840f);
    }

    // ── SurfaceHolder.Callback ────────────────────────────────────────────────

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Obtener dimensiones reales antes de arrancar el bucle
        Canvas c = holder.lockCanvas();
        if (c != null) {
            updateScaleMatrix(c.getWidth(), c.getHeight());
            holder.unlockCanvasAndPost(c);
        }
        bucle = new BucleJuego(holder, this);
        bucle.start();
        startMusic();
    }

    @Override
    public void surfaceChanged(SurfaceHolder h, int f, int w, int h2) {
        updateScaleMatrix(w, h2);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopBucle();
    }

    private void updateScaleMatrix(int screenW, int screenH) {
        worldMatrix.setScale(screenW / WORLD_W, screenH / WORLD_H);
        worldMatrix.invert(inverseMatrix);
        if (joystick != null) joystick.setInverseMatrix(inverseMatrix);
    }

    private void stopBucle() {
        if (bucle == null) return;
        bucle.enEjecucion = false;
        boolean retry = true;
        while (retry) {
            try { bucle.join(); retry = false; }
            catch (InterruptedException ignored) {}
        }
    }

    // ── Bucle principal ───────────────────────────────────────────────────────

    public void actualizar(float delta) {
        if (currentScreen == Screen.FIGHTING) updateFighting(delta);
    }

    public void renderizar(Canvas canvas) {
        canvas.save();
        canvas.concat(worldMatrix);

        switch (currentScreen) {
            case MAIN_MENU:   drawMainMenu(canvas);                        break;
            case CHAR_SELECT: drawCharSelect(canvas);                      break;
            case LORE:        drawLore(canvas);                            break;
            case FIGHTING:    drawFighting(canvas);                        break;
            case PAUSED:      drawFighting(canvas); drawPause(canvas);     break;
            case RESULT:      drawFighting(canvas); drawResult(canvas);    break;
            case BUFF_SELECT: drawFighting(canvas); drawBuffSelect(canvas);break;
            case EVENT:       drawFighting(canvas); drawEvent(canvas);     break;
        }

        canvas.restore();
    }

    // ── Update ────────────────────────────────────────────────────────────────

    private void updateFighting(float delta) {
        // Esperando que termine la animación de muerte antes de mostrar resultado
        if (pendingResult) {
            deathDelay += delta;
            player1.update(delta);
            player2.update(delta);
            if (deathDelay >= DEATH_ANIM_WAIT) {
                pendingResult = false;
                showResult(pendingResultWon);
            }
            return;
        }

        if (activeSlot == -2) {
            timeLeft          -= delta;
            totalTimeSurvived += delta;
            if (timeLeft <= 0) { timeLeft = 0; triggerResult(false); return; }
        }

        float ix = joystick.getKnobPercentX();
        if (ix >  0.2f) player1.move( MOVE_SPEED * player1.speedMultiplier * delta);
        if (ix < -0.2f) player1.move(-MOVE_SPEED * player1.speedMultiplier * delta);
        player1.updateDirection(ix > 0.2f, ix < -0.2f);

        player1.update(delta); player1.updateAttack(delta);
        player2.update(delta); player2.updateAttack(delta);
        enemyAI.update(delta);

        if (player1.justHitThisFrame)
            triggerHitFlash(player1, player2);
        if (player2.justHitThisFrame)
            triggerHitFlash(player2, player1);
        if (hitFlashTimer > 0) hitFlashTimer -= delta;

        float maxX = WORLD_W - MAP_MIN_X;
        player1.x = Math.max(MAP_MIN_X, Math.min(player1.x, maxX));
        player2.x = Math.max(MAP_MIN_X, Math.min(player2.x, maxX));

        if      (player1.currentHealth <= 0) triggerResult(false);
        else if (player2.currentHealth <= 0) triggerResult(true);
    }

    private void triggerHitFlash(Player attacker, Player defender) {
        // Punto de impacto: entre atacante y defensor, a altura del torso del defensor
        hitFlashX = (attacker.x + defender.x) / 2f;
        hitFlashY = defender.getBodyCanvasY();
        hitFlashTimer = HIT_FLASH_DURATION;
    }

    private void triggerResult(boolean won) {
        if (pendingResult) return;          // ya en espera, ignorar duplicados
        pendingResult    = true;
        pendingResultWon = won;
        deathDelay       = 0f;
        if (won) {
            player1.forceIdle();
            player2.startDeath();
        } else {
            player1.startDeath();
            player2.forceIdle();
        }
    }

    // ── Draw ──────────────────────────────────────────────────────────────────

    private void drawMainMenu(Canvas canvas) {
        canvas.drawBitmap(res.bgTitle, null, new RectF(0, 0, WORLD_W, WORLD_H), paintBitmap);

        res.fontBig.setColor(0xFFFF8800);
        drawCenteredText(canvas, "CHAOS ARENA", WORLD_H * 0.18f, res.fontBig);

        drawButton(canvas, btnStory,  "HISTORIA",    0xBB0A3A0A, res.fontMedium);
        drawButton(canvas, btnArcade, "ARCADE",      0xBB3A1A00, res.fontMedium);
        drawButton(canvas, btnDuel,   "DUELO LOCAL", 0xBB001A3A, res.fontMedium);
    }

    private void drawCharSelect(Canvas canvas) {
        canvas.drawBitmap(res.stages[selectedStageIdx].bgBitmap,
                null, new RectF(0, 0, WORLD_W, WORLD_H), paintBitmap);
        canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);

        res.fontBig.setColor(0xFFFFCC00);
        drawCenteredText(canvas, "ELIGE TU LUCHADOR", 150f, res.fontBig);

        String[] chars = { "Liu Kang", "Goro", "Sub Zero" };
        for (int i = 0; i < chars.length; i++) {
            int bg = (i == selectedCharIdx) ? 0xFF224400 : 0xBB111111;
            drawButton(canvas, btnChars[i], chars[i], bg, res.fontMedium);
        }

        res.fontSmall.setColor(0xFFFFFFFF);
        drawCenteredText(canvas, "ESCENARIO: " + res.stages[selectedStageIdx].name, 560f, res.fontSmall);
        drawButton(canvas, btnStageChange, "< CAMBIAR >", 0xBB222222, res.fontMedium);
        drawButton(canvas, btnLuchar,      "¡LUCHAR!",   0xBBAA1100, res.fontBig);
    }

    private void drawLore(Canvas canvas) {
        canvas.drawBitmap(currentStage.bgBitmap, null, new RectF(0, 0, WORLD_W, WORLD_H), paintBitmap);
        canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);

        String lore = ActividadJuego.LEVEL_LORE[Math.min(currentLevel - 1, ActividadJuego.LEVEL_LORE.length - 1)];
        res.fontSmall.setColor(0xFFFFFFFF);
        drawWrappedText(canvas, lore, 200f, 300f, WORLD_W - 400f, res.fontSmall);

        drawButton(canvas, btnStartFight, "EMPEZAR COMBATE", 0xBBAA1100, res.fontMedium);
    }

    private void drawFighting(Canvas canvas) {
        float pShiftX = (player1.x / WORLD_W) * 300f;
        // Siempre dibujar desde y=0 para que no haya franja negra en la parte superior
        canvas.drawBitmap(currentStage.bgBitmap, null,
                new RectF(-pShiftX, 0,
                        WORLD_W + 300f - pShiftX, WORLD_H),
                paintBitmap);

        player1.draw(canvas);
        player2.draw(canvas);

        // Flash de impacto: estrella amarilla en el punto de golpe
        if (hitFlashTimer > 0) {
            float alpha = hitFlashTimer / HIT_FLASH_DURATION;
            float radius = 90f + (1f - alpha) * 40f;
            paintHitFlash.setColor(0xFFFFEE00);
            paintHitFlash.setAlpha((int)(alpha * 230));
            canvas.drawCircle(hitFlashX, hitFlashY, radius, paintHitFlash);
            paintHitFlash.setColor(0xFFFFFFFF);
            paintHitFlash.setAlpha((int)(alpha * 180));
            canvas.drawCircle(hitFlashX, hitFlashY, radius * 0.5f, paintHitFlash);
        }

        // Flash rojo del especial: dibujado después de los personajes → capa uniforme sin huecos
        if (player1.isAttacking() && player1.getCurrentAttackType() == Player.AttackType.SPECIAL) {
            paintOverlay.setColor(0x44FF0000);
            canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);
            paintOverlay.setColor(0xCC000000);  // restaurar color original
        }

        drawBars(canvas);
        drawHUD(canvas);
        if (joystick != null) joystick.draw(canvas);
        drawAttackButtons(canvas);
    }

    private void drawPause(Canvas canvas) {
        canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);
        res.fontBig.setColor(0xFFFFCC00);
        drawCenteredText(canvas, "PAUSA", WORLD_H * 0.30f, res.fontBig);
        drawButton(canvas, btnResume,   "CONTINUAR",     0xBB004400, res.fontMedium);
        drawButton(canvas, btnExitMenu, "SALIR AL MENU", 0xBB440000, res.fontMedium);
    }

    private void drawResult(Canvas canvas) {
        canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);
        res.fontBig.setColor(lastBattleWon ? 0xFFFFCC00 : 0xFFCC2222);
        drawCenteredText(canvas, lastBattleWon ? "¡VICTORIA!" : "DERROTA", WORLD_H * 0.28f, res.fontBig);

        String actionLabel;
        if      (activeSlot == -2)                               actionLabel = lastBattleWon ? "ELEGIR MEJORA" : "GUARDAR Y SALIR";
        else if (lastBattleWon && activeSlot >= 0)               actionLabel = currentLevel > ActividadJuego.MAX_LEVELS ? "COMPLETADO" : "SIGUIENTE";
        else                                                     actionLabel = lastBattleWon ? "OTRO DUELO" : "REINTENTAR";

        drawButton(canvas, btnResultAction, actionLabel,       0xBB004400, res.fontMedium);
        drawButton(canvas, btnResultMenu,   "MENU PRINCIPAL",  0xBB440000, res.fontMedium);
    }

    private void drawBuffSelect(Canvas canvas) {
        canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);
        res.fontBig.setColor(0xFFFFCC00);
        drawCenteredText(canvas, "ELIGE UNA MEJORA", 180f, res.fontBig);
        res.fontSmall.setColor(0xFFCC4444);
        drawCenteredText(canvas, "Las que no elijas se las lleva el enemigo...", 280f, res.fontSmall);

        if (buffChoices != null) {
            float bw = 900f, bh = 120f, startY = 380f, gap = 150f;
            for (int i = 0; i < buffChoices.size(); i++) {
                Buff b = buffChoices.get(i);
                btnBuffs[i].set((WORLD_W - bw) / 2f, startY + i * gap,
                        (WORLD_W + bw) / 2f, startY + i * gap + bh);
                int color = b.rarity == Buff.Rarity.EPIC  ? 0xBB443300
                        : b.rarity == Buff.Rarity.RARE  ? 0xBB003344 : 0xBB222222;
                String label = b.getDescription() + " ("
                        + (int)(b.value * (b.value < 1 ? 100 : 1))
                        + (b.value < 1 ? "%" : "") + ")";
                drawButton(canvas, btnBuffs[i], label, color, res.fontSmall);
            }
        }
    }

    private void drawEvent(Canvas canvas) {
        canvas.drawRect(0, 0, WORLD_W, WORLD_H, paintOverlay);
        res.fontBig.setColor(0xFFCC44CC);
        drawCenteredText(canvas, "EVENTO ESPECIAL", 250f, res.fontBig);
        if (pendingEvent != null) {
            res.fontSmall.setColor(0xFFFFFFFF);
            drawCenteredText(canvas, pendingEvent.description, 450f, res.fontSmall);
        }
        drawButton(canvas, btnEventOk, "CONTINUAR", 0xBB004400, res.fontMedium);
    }

    // ── HUD y barras ─────────────────────────────────────────────────────────

    private void drawBars(Canvas canvas) {
        float barY   = 70f;
        float margin = 80f;

        // Nombres
        res.fontSmall.setColor(0xFFFFFFFF);
        res.fontSmall.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(player1.name, margin, barY - 12f, res.fontSmall);
        res.fontSmall.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(player2.name, WORLD_W - margin, barY - 12f, res.fontSmall);
        res.fontSmall.setTextAlign(Paint.Align.LEFT);

        // Barras de vida
        drawBar(canvas, margin, barY, BAR_WIDTH, BAR_HEIGHT,
                player1.currentHealth / player1.maxHealth, 0xFF22BB22);
        float x2 = WORLD_W - margin - BAR_WIDTH;
        drawBar(canvas, x2, barY, BAR_WIDTH, BAR_HEIGHT,
                player2.currentHealth / player2.maxHealth, 0xFF22BB22);

        // Barras de combo
        float comboY = barY + BAR_HEIGHT + 6f;
        drawBar(canvas, margin, comboY, BAR_WIDTH, BAR_COMBO_H,
                player1.comboCharge / Player.MAX_COMBO_CHARGE, 0xFFFFCC00);
        drawBar(canvas, x2, comboY, BAR_WIDTH, BAR_COMBO_H,
                player2.comboCharge / Player.MAX_COMBO_CHARGE, 0xFFFFCC00);
    }

    private void drawBar(Canvas canvas, float x, float y, float w, float h, float pct, int color) {
        canvas.drawRect(x, y, x + w, y + h, paintBlack);
        Paint p = new Paint(); p.setColor(color);
        canvas.drawRect(x, y, x + w * Math.max(0f, pct), y + h, p);
    }

    private void drawHUD(Canvas canvas) {
        // Botón pausa — compacto en el centro superior
        btnPause.set(WORLD_W / 2f - 80f, 12f, WORLD_W / 2f + 80f, 68f);
        drawButton(canvas, btnPause, "II", 0xBB222222, res.fontMedium);

        if (activeSlot == -2) {
            // Tiempo restante justo debajo del botón pausa
            res.fontBig.setColor(0xFFFFFFFF);
            drawCenteredText(canvas, String.format("%02d", (int) timeLeft), 145f, res.fontBig);
            // Enemigos derrotados debajo del tiempo
            res.fontSmall.setColor(0xFFFFAAFF);
            drawCenteredText(canvas, "DERROTADOS: " + enemiesDefeated, 192f, res.fontSmall);
        }
    }

    private void drawAttackButtons(Canvas canvas) {
        float right  = WORLD_W - 80f;
        float bottom = WORLD_H - 80f;
        float bs = 160f, gap = 20f;

        btnJump   .set(right - bs,           bottom - bs * 2 - gap, right,             bottom - bs - gap);
        btnKick   .set(right - bs,           bottom - bs,            right,             bottom);
        btnPunch  .set(right - bs * 2 - gap, bottom - bs,            right - bs - gap,  bottom);
        btnSpecial.set(right - bs * 2 - gap, bottom - bs * 2 - gap,  right - bs - gap,  bottom - bs - gap);

        drawButton(canvas, btnJump,    "↑",  0xBB116611, res.fontBig);
        drawButton(canvas, btnPunch,   "A1", 0xBB661166, res.fontBig);
        drawButton(canvas, btnKick,    "A2", 0xBB661111, res.fontBig);
        drawButton(canvas, btnSpecial, "S",  0xBB111166, res.fontBig);
    }

    // ── Touch ─────────────────────────────────────────────────────────────────

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // El joystick recibe todos los eventos de movimiento en combate
        if (currentScreen == Screen.FIGHTING || currentScreen == Screen.PAUSED) {
            if (joystick != null) joystick.onTouchEvent(e);
        }

        int action = e.getActionMasked();
        if (action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_POINTER_DOWN)
            return true;

        // Para multi-touch usar el índice del puntero que acaba de bajar
        int idx = e.getActionIndex();
        float[] pts = { e.getX(idx), e.getY(idx) };
        inverseMatrix.mapPoints(pts);

        handleTap(pts[0], pts[1]);
        return true;
    }

    private void handleTap(float wx, float wy) {
        switch (currentScreen) {
            case MAIN_MENU:
                if (hit(btnStory,  wx, wy)) { pendingSlot =  0; currentScreen = Screen.CHAR_SELECT; }
                if (hit(btnArcade, wx, wy)) { pendingSlot = -2; currentScreen = Screen.CHAR_SELECT; }
                if (hit(btnDuel,   wx, wy)) { pendingSlot = -1; currentScreen = Screen.CHAR_SELECT; }
                break;

            case CHAR_SELECT:
                for (int i = 0; i < btnChars.length; i++)
                    if (hit(btnChars[i], wx, wy)) selectedCharIdx = i;
                if (hit(btnStageChange, wx, wy))
                    selectedStageIdx = (selectedStageIdx + 1) % res.stages.length;
                if (hit(btnLuchar, wx, wy)) startFight();
                break;

            case LORE:
                if (hit(btnStartFight, wx, wy)) currentScreen = Screen.FIGHTING;
                break;

            case FIGHTING:
                if (hit(btnPause,   wx, wy)) currentScreen = Screen.PAUSED;
                if (hit(btnPunch,   wx, wy)) player1.attack(Player.AttackType.PUNCH);
                if (hit(btnKick,    wx, wy)) player1.attack(Player.AttackType.KICK);
                if (hit(btnSpecial, wx, wy)) player1.attack(Player.AttackType.SPECIAL);
                if (hit(btnJump,    wx, wy)) player1.jump();
                break;

            case PAUSED:
                if (hit(btnResume,   wx, wy)) currentScreen = Screen.FIGHTING;
                if (hit(btnExitMenu, wx, wy)) currentScreen = Screen.MAIN_MENU;
                break;

            case RESULT:
                if (hit(btnResultAction, wx, wy)) onResultAction();
                if (hit(btnResultMenu,   wx, wy)) currentScreen = Screen.MAIN_MENU;
                break;

            case BUFF_SELECT:
                if (buffChoices != null) {
                    for (int i = 0; i < buffChoices.size(); i++) {
                        if (hit(btnBuffs[i], wx, wy)) {
                            runManager.applyBuffToPlayer(buffChoices.get(i));
                            for (int j = 0; j < buffChoices.size(); j++)
                                if (j != i) runManager.addPendingEnemyBuff(buffChoices.get(j));
                            buffChoices = null;
                            checkForEvent();
                            break;
                        }
                    }
                }
                break;

            case EVENT:
                if (hit(btnEventOk, wx, wy)) { pendingEvent = null; resetCombat(); }
                break;
        }
    }

    // ── Lógica de pantallas ───────────────────────────────────────────────────

    private void startFight() {
        activeSlot   = pendingSlot;
        currentLevel = 1;
        currentStage = res.stages[selectedStageIdx];

        String[] chars  = { "Liu Kang", "Goro", "Sub Zero" };
        String   p1Name = chars[selectedCharIdx];
        String   p2Name = resolveEnemyName();

        player1 = new Player(p1Name, res.assets,
                res.getFolderForChar(p1Name), res.getPrefixForChar(p1Name),
                400f, currentStage.groundY + currentStage.getOffsetFor(p1Name),
                true, WORLD_H);
        player2 = new Player(p2Name, res.assets,
                res.getFolderForChar(p2Name), res.getPrefixForChar(p2Name),
                WORLD_W - 400f, currentStage.groundY + currentStage.getOffsetFor(p2Name),
                false, WORLD_H);
        player1.opponent = player2;
        player2.opponent = player1;

        enemyAI = new EnemyAI(player2, player1);
        applyModeConfig();

        if (activeSlot == -2) runManager = new RunManager(player1);

        currentScreen = (activeSlot >= 0) ? Screen.LORE : Screen.FIGHTING;
    }

    private String resolveEnemyName() {
        String[] chars = { "Liu Kang", "Goro", "Sub Zero" };
        if (activeSlot >= 0) {
            int idx = Math.min(currentLevel - 1, ActividadJuego.LEVEL_ENEMY_NAMES.length - 1);
            return ActividadJuego.LEVEL_ENEMY_NAMES[idx];
        }
        // Arcade/duelo: enemigo aleatorio distinto al jugador
        String p1Name = player1 != null ? player1.name : chars[selectedCharIdx];
        java.util.List<String> pool = new java.util.ArrayList<>();
        for (String c : chars) if (!c.equals(p1Name)) pool.add(c);
        return pool.get((int)(Math.random() * pool.size()));
    }

    /** Actualiza el sprite del enemigo (player2) sin recrear el objeto. */
    private void spawnNewEnemy() {
        String p2Name = resolveEnemyName();
        player2.name     = p2Name;
        player2.charType = p2Name;
        player2.loadAnimations(res.assets,
                res.getFolderForChar(p2Name),
                res.getPrefixForChar(p2Name),
                WORLD_H);
    }

    private void applyModeConfig() {
        if (activeSlot >= 0) {
            int idx = Math.min(currentLevel - 1, ActividadJuego.LEVEL_ENEMY_NAMES.length - 1);
            player2.maxHealth     = ActividadJuego.LEVEL_ENEMY_HEALTH[idx];
            player2.currentHealth = player2.maxHealth;
            enemyAI.setSpeed(ActividadJuego.LEVEL_ENEMY_SPEED[idx]);
            enemyAI.setDamage(ActividadJuego.LEVEL_ENEMY_DAMAGE[idx]);
            enemyAI.setAttackRate(ActividadJuego.LEVEL_ATTACK_RATE[idx]);
        } else if (activeSlot == -2) {
            player2.maxHealth = 250f; player2.currentHealth = 250f;
            enemyAI.setSpeed(350f); enemyAI.setDamage(10f); enemyAI.setAttackRate(1.5f);
        } else {
            player1.maxHealth = 600f; player1.currentHealth = 600f;
            player2.maxHealth = 600f; player2.currentHealth = 600f;
            enemyAI.setSpeed(500f); enemyAI.setDamage(12f); enemyAI.setAttackRate(1.2f);
        }
    }

    private void showResult(boolean won) {
        lastBattleWon = won;
        currentScreen = Screen.RESULT;
        if (activeSlot == -2 && won)  { enemiesDefeated++; runManager.onFightWon(); }
        if (activeSlot == -2 && !won) saveArcadeScore();
    }

    private void onResultAction() {
        if (activeSlot == -2 && lastBattleWon) {
            buffChoices   = runManager.getRandomBuffChoices();
            currentScreen = Screen.BUFF_SELECT;
        } else if (activeSlot == -2 && !lastBattleWon) {
            currentScreen = Screen.MAIN_MENU;
        } else {
            resetCombat();
        }
    }

    private void checkForEvent() {
        if (runManager != null && runManager.shouldTriggerEvent()) {
            pendingEvent = runManager.generateRandomEvent();
            pendingEvent.apply(runManager);
            currentScreen = Screen.EVENT;
        } else {
            resetCombat();
        }
    }

    private void resetCombat() {
        pendingResult = false;
        if (lastBattleWon && activeSlot >= 0) {
            currentLevel++;
            if (currentLevel > ActividadJuego.MAX_LEVELS) { currentScreen = Screen.MAIN_MENU; return; }
            applyModeConfig();
        }

        // Historia: enemigo cambia según nivel. Arcade: enemigo aleatorio nuevo cada ronda.
        if (activeSlot != -1) spawnNewEnemy();

        player1.forceIdle();
        player2.forceIdle();
        player1.currentHealth = player1.maxHealth;
        player1.comboCharge   = 0;
        player2.currentHealth = player2.maxHealth;
        player2.comboCharge   = 0;
        player1.x = 400f;        player1.y = player1.groundY;
        player2.x = WORLD_W - 400f; player2.y = player2.groundY;
        player1.velocityY = 0;   player2.velocityY = 0;
        timeLeft = INITIAL_TIME;

        if (activeSlot == -2 && runManager != null) runManager.applyEnemyScaling(enemyAI, player2);
        currentScreen = (activeSlot >= 0) ? Screen.LORE : Screen.FIGHTING;
    }

    // ── Puntuación arcade ─────────────────────────────────────────────────────

    private void saveArcadeScore() {
        int[]   topE = new int[6];
        float[] topT = new float[6];
        for (int i = 0; i < 5; i++) {
            topE[i] = prefs.getInt("arcade_enemies_" + i, 0);
            topT[i] = prefs.getFloat("arcade_time_"  + i, 0f);
        }
        topE[5] = enemiesDefeated; topT[5] = totalTimeSurvived;
        for (int i = 0; i < 6; i++)
            for (int j = i + 1; j < 6; j++)
                if (topT[j] > topT[i]) {
                    float tt = topT[i]; topT[i] = topT[j]; topT[j] = tt;
                    int   te = topE[i]; topE[i] = topE[j]; topE[j] = te;
                }
        SharedPreferences.Editor ed = prefs.edit();
        for (int i = 0; i < 5; i++) {
            ed.putInt("arcade_enemies_" + i, topE[i]);
            ed.putFloat("arcade_time_"  + i, topT[i]);
        }
        ed.apply();
    }

    // ── Música ────────────────────────────────────────────────────────────────

    private void startMusic() {
        try {
            android.content.res.AssetFileDescriptor afd =
                    activity.getAssets().openFd("sounds/Techno_Syndrome.mp3");
            music = new MediaPlayer();
            music.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            music.setLooping(true);
            music.setVolume(prefs.getBoolean("is_muted", false) ? 0f : 0.8f, 0.8f);
            music.prepare();
            music.start();
        } catch (Exception ignored) {}
    }

    public void pauseMusic()  { if (music != null && music.isPlaying()) music.pause(); }
    public void resumeMusic() { if (music != null && !music.isPlaying()) music.start(); }
    public void releaseMusic() { if (music != null) { music.release(); music = null; } }

    // ── Helpers de dibujo ─────────────────────────────────────────────────────

    private void drawButton(Canvas canvas, RectF r, String label, int bgColor, Paint font) {
        Paint bg = new Paint(); bg.setColor(bgColor);
        canvas.drawRoundRect(r, 24f, 24f, bg);
        Paint border = new Paint();
        border.setColor(0x88FFFFFF);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(3f);
        canvas.drawRoundRect(r, 24f, 24f, border);

        Paint.FontMetrics fm = font.getFontMetrics();
        float textY = r.centerY() - (fm.ascent + fm.descent) / 2f;
        font.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, r.centerX(), textY, font);
        font.setTextAlign(Paint.Align.LEFT);
    }

    private void drawCenteredText(Canvas canvas, String text, float y, Paint font) {
        font.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, WORLD_W / 2f, y, font);
        font.setTextAlign(Paint.Align.LEFT);
    }

    private void drawMultilineText(Canvas canvas, String text, float x, float startY, Paint font) {
        Paint.FontMetrics fm = font.getFontMetrics();
        float lineH = (fm.descent - fm.ascent) + 10f;
        float y = startY;
        for (String line : text.split("\n")) {
            canvas.drawText(line, x, y, font);
            y += lineH;
        }
    }

    private void drawWrappedText(Canvas canvas, String text, float x, float startY, float maxWidth, Paint font) {
        Paint.FontMetrics fm = font.getFontMetrics();
        float lineH = (fm.descent - fm.ascent) + 10f;
        float y = startY;
        for (String paragraph : text.split("\n")) {
            if (paragraph.isEmpty()) { y += lineH * 0.6f; continue; }
            StringBuilder line = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                String test = line.length() == 0 ? word : line + " " + word;
                if (font.measureText(test) > maxWidth && line.length() > 0) {
                    canvas.drawText(line.toString(), x, y, font);
                    y += lineH;
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(test);
                }
            }
            if (line.length() > 0) { canvas.drawText(line.toString(), x, y, font); y += lineH; }
        }
    }

    private boolean hit(RectF r, float x, float y) {
        return r != null && r.contains(x, y);
    }
}
