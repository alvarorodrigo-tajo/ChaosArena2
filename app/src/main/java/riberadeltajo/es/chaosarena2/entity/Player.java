package riberadeltajo.es.chaosarena2.entity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public class Player {

    public enum AttackType { PUNCH, KICK, SPECIAL }
    public enum State      { IDLE, WALK, ATTACKING, HURT, DEAD }

    // ── Animaciones ───────────────────────────────────────────────────────────
    private SpriteAnimation walkAnim, idleAnim, punchAnim, kickAnim, kickVisualAnim, hurtAnim, jumpAnim, deathAnim;

    // ── Estado ────────────────────────────────────────────────────────────────
    private State      currentState     = State.IDLE;
    private AttackType currentAttackType;
    private float      stateTime, attackTime, hurtTime, jumpTime, deathTime;
    private boolean    hasJumpAnim, hasKickAnim, hasDeathAnim, hasHurtAnim;

    // ── Física ────────────────────────────────────────────────────────────────
    public float x, y, velocityY = 0;
    private static final float GRAVITY    = -2500f;
    private static final float JUMP_FORCE =  1150f;
    public  float groundY;

    // ── Escalado visual ───────────────────────────────────────────────────────
    public float scale       = 1.0f;
    public float drawOffsetX = 0f;
    public float drawOffsetY = 0f;
    private float worldHeight;
    private float hbWMult    = 0.35f;
    private float hbHMult    = 0.80f;
    private float hbOffsetX  = 0f;   // canvas px; positivo=derecha cuando facingRight, invertido si facing left
    private float hbOffsetY  = 0f;   // canvas px hacia abajo desde el tope del frame

    // ── Stats ─────────────────────────────────────────────────────────────────
    public String name, charType, charPrefix;
    public float maxHealth = 250, currentHealth = 250, comboCharge = 0;
    public static final float MAX_COMBO_CHARGE = 100;
    public float damageMultiplier = 1.0f;
    public float speedMultiplier  = 1.0f;
    public float lifestealPercent = 0f;
    public float armorPercent     = 0f;
    public float criticalChance   = 0f;
    public float regenPerSec      = 0f;
    public boolean facingRight;
    public Player  opponent;
    public boolean hasHitInCurrentAttack = false;
    public boolean justHitThisFrame     = false;

    // ── Paint para el blit ────────────────────────────────────────────────────
    private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final Paint hurtPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final Matrix matrix   = new Matrix();

    // ── Debug hitboxes ────────────────────────────────────────────────────────
    public static boolean DEBUG_HITBOXES = false;
    private static final Paint dbgBodyPaint;
    private static final Paint dbgAttackPaint;
    static {
        dbgBodyPaint = new Paint();
        dbgBodyPaint.setStyle(Paint.Style.STROKE);
        dbgBodyPaint.setStrokeWidth(4f);
        dbgBodyPaint.setColor(0xFF00FF00);

        dbgAttackPaint = new Paint();
        dbgAttackPaint.setStyle(Paint.Style.STROKE);
        dbgAttackPaint.setStrokeWidth(4f);
        dbgAttackPaint.setColor(0xFFFF3333);
    }

    public Player(String name, AssetManager assets, String folder, String charPrefix,
                  float x, float y, boolean facingRight, float worldHeight) {
        this.name       = name;
        this.charType   = name;
        this.charPrefix = charPrefix;
        this.x           = x;
        this.y           = y;
        this.groundY     = y;
        this.facingRight = facingRight;
        this.worldHeight = worldHeight;

        hurtPaint.setColorFilter(new android.graphics.PorterDuffColorFilter(
                0xAAFF0000, android.graphics.PorterDuff.Mode.SRC_ATOP));

        loadAnimations(assets, folder, charPrefix, worldHeight);
    }

    // ── Carga de animaciones ──────────────────────────────────────────────────

    public void loadAnimations(AssetManager assets, String folder, String charPrefix, float worldHeight) {
        this.charPrefix = charPrefix;

        idleAnim  = SpriteAnimation.load(assets, folder, charPrefix + "_idle",   0.12f, SpriteAnimation.PlayMode.LOOP);
        walkAnim  = SpriteAnimation.load(assets, folder, charPrefix + "_run",    0.10f, SpriteAnimation.PlayMode.LOOP);
        punchAnim = SpriteAnimation.load(assets, folder, charPrefix + "_attack1",0.08f, SpriteAnimation.PlayMode.NORMAL);
        kickAnim  = SpriteAnimation.load(assets, folder, charPrefix + "_attack2",0.10f, SpriteAnimation.PlayMode.NORMAL);
        hurtAnim  = SpriteAnimation.load(assets, folder, charPrefix + "_takehit",0.12f, SpriteAnimation.PlayMode.NORMAL);
        jumpAnim    = SpriteAnimation.load(assets, folder, charPrefix + "_jump",  0.10f, SpriteAnimation.PlayMode.LOOP);
        deathAnim   = SpriteAnimation.load(assets, folder, charPrefix + "_death", 0.12f, SpriteAnimation.PlayMode.NORMAL);
        hasJumpAnim  = jumpAnim.getFrameWidth()  > 1;
        hasKickAnim  = kickAnim.getFrameWidth()  > 1;
        hasDeathAnim = deathAnim.getFrameWidth() > 1;
        hasHurtAnim  = hurtAnim.getFrameWidth()  > 1;

        // Si no hay animación de patada, usar la animación de carrera como "ataque de carga"
        // → visualmente completamente distinta al ataque normal (dash/embestida)
        if (kickVisualAnim != null && kickVisualAnim != kickAnim) kickVisualAnim.recycle();
        kickVisualAnim = hasKickAnim ? kickAnim
                : SpriteAnimation.load(assets, folder, charPrefix + "_run", 0.07f, SpriteAnimation.PlayMode.NORMAL);

        calibrateScale(charPrefix);
    }

    private void calibrateScale(String prefix) {
        // drawOffsetY = -(idle_frame_h_px * scale): alinea los pies del sprite con groundY
        if (prefix.contains("subzero")) {
            scale = 4.0f; drawOffsetX = 0f; drawOffsetY = -636f;  // ~159px
            // sprite 199x191px, contenido real X=57..113 Y=41..159
            hbWMult   = 0.105f;   // 199*4*0.105 ≈ 84px  (igual que goro)
            hbHMult   = 0.490f;   // 191*4*0.490 ≈ 374px (igual que goro)
            hbOffsetX = -58f;     // centro del personaje 14.5px izq del centro del frame → 14.5*4=58 canvas px
            hbOffsetY = 164f;     // 41px vacíos en la parte superior del frame → 41*4=164 canvas px
        } else if (prefix.contains("goro")) {
            scale = 4.0f; drawOffsetX = 0f; drawOffsetY = -472f;  // ~118px
            hasHurtAnim = false;  // takehit tiene animación de caída no deseada
        } else {
            scale = 4.0f; drawOffsetX = 0f; drawOffsetY = -396f;  // ~99px (liu_kang)
        }
    }

    /** Y en coordenadas canvas-mundo donde está el centro del torso (para efectos visuales). */
    public float getBodyCanvasY() {
        float top = (worldHeight - y) + drawOffsetY;
        return top + idleAnim.getFrameHeight() * scale * 0.35f;
    }

    // ── Update (lógica, sin dibujo) ───────────────────────────────────────────

    public void update(float delta) {
        // Muerto: solo avanzar el timer de muerte, nada más
        if (currentState == State.DEAD) { deathTime += delta; return; }

        // Gravedad
        velocityY += GRAVITY * delta;
        y         += velocityY * delta;
        if (y <= groundY) { y = groundY; velocityY = 0; }

        // Timer de salto (solo cuando está en el aire)
        if (!isGrounded()) jumpTime += delta;

        // Estado de daño
        if (currentState == State.HURT) {
            hurtTime += delta;
            if (hurtAnim.isFinished(hurtTime)) currentState = State.IDLE;
        }

        // Regeneración
        if (regenPerSec > 0 && currentHealth > 0 && currentHealth < maxHealth)
            currentHealth = Math.min(maxHealth, currentHealth + regenPerSec * delta);

        stateTime += delta;
    }

    public void updateAttack(float delta) {
        justHitThisFrame = false;
        if (currentState != State.ATTACKING) return;

        attackTime += delta;

        // Si no hay animación de patada, usar punchAnim como referencia de duración
        float kickDuration = hasKickAnim ? kickAnim.getDuration() : punchAnim.getDuration();
        float totalDuration = punchAnim.getDuration();
        if (currentAttackType == AttackType.SPECIAL)
            totalDuration = punchAnim.getDuration() + kickDuration;
        else if (currentAttackType == AttackType.KICK)
            totalDuration = kickDuration;

        if (attackTime >= totalDuration) {
            currentState          = State.IDLE;
            hasHitInCurrentAttack = false;
            currentAttackType     = null;
            return;
        }

        if (!hasHitInCurrentAttack && canHit(opponent)) {
            float hitTime = (currentAttackType == AttackType.SPECIAL)
                    ? totalDuration * 0.45f : totalDuration / 2f;
            if (attackTime > hitTime) {
                float baseDmg  = (currentAttackType == AttackType.SPECIAL) ? 45f
                        : (currentAttackType == AttackType.KICK)    ? 15f : 10f;
                float knockback = (currentAttackType == AttackType.SPECIAL) ? 120f
                        : (currentAttackType == AttackType.KICK)    ?  80f : 45f;
                float dmg = baseDmg * damageMultiplier;
                if (Math.random() < criticalChance) dmg *= 2f;
                if (currentAttackType != AttackType.SPECIAL) addCharge(dmg);
                opponent.takeDamage(dmg, knockback);
                if (lifestealPercent > 0)
                    currentHealth = Math.min(maxHealth, currentHealth + dmg * lifestealPercent);
                hasHitInCurrentAttack = true;
                justHitThisFrame      = true;
            }
        }
    }

    // ── Acciones ──────────────────────────────────────────────────────────────

    public void jump() {
        if (isGrounded() && currentState != State.HURT) { velocityY = JUMP_FORCE; jumpTime = 0; }
    }

    public void move(float amount) {
        if (currentState != State.ATTACKING && currentState != State.HURT) {
            x += amount;
            if (isGrounded()) currentState = State.WALK;
        }
    }

    public void updateDirection(boolean r, boolean l) {
        if (currentState != State.ATTACKING && currentState != State.HURT) {
            if (r) facingRight = true;
            if (l) facingRight = false;
            if (!r && !l && currentState == State.WALK) currentState = State.IDLE;
        }
    }

    public void attack(AttackType type) {
        if (currentState != State.ATTACKING && currentState != State.HURT) {
            if (type == AttackType.SPECIAL && comboCharge < MAX_COMBO_CHARGE) return;
            currentState          = State.ATTACKING;
            currentAttackType     = type;
            attackTime            = 0;
            hasHitInCurrentAttack = false;
            if (type == AttackType.SPECIAL) comboCharge = 0;
        }
    }

    public void takeDamage(float amount, float knockback) {
        float reduced = amount * Math.max(0, 1.0f - armorPercent);
        currentHealth = Math.max(0, currentHealth - reduced);
        currentState  = State.HURT;
        hurtTime      = 0;
        float pushDir = (opponent != null && opponent.x < this.x) ? 1f : -1f;
        x += knockback * pushDir;
    }

    public void addCharge(float a) { comboCharge = Math.min(MAX_COMBO_CHARGE, comboCharge + a); }
    public void forceIdle()        { currentState = State.IDLE; attackTime = 0; hurtTime = 0; }
    public void startDeath()       { currentState = State.DEAD; deathTime = 0; velocityY = 0; }

    // ── Hitbox y colisión ─────────────────────────────────────────────────────

    public RectF getHitbox() {
        float hbW = idleAnim.getFrameWidth()  * scale * hbWMult;
        float hbH = idleAnim.getFrameHeight() * scale * hbHMult;
        float ox  = facingRight ? hbOffsetX : -hbOffsetX;
        float cx  = x + ox;
        float top = (worldHeight - y) + drawOffsetY + hbOffsetY;
        return new RectF(cx - hbW / 2f, top, cx + hbW / 2f, top + hbH);
    }

    public boolean canHit(Player other) {
        if (other == null || currentState != State.ATTACKING) return false;
        RectF body  = getHitbox();
        float reach = (currentAttackType == AttackType.KICK)    ? 400
                : (currentAttackType == AttackType.SPECIAL)  ? 350 : 300;
        float ax    = facingRight ? body.right : body.left - reach;
        RectF area  = new RectF(ax, body.top, ax + reach, body.bottom);
        return RectF.intersects(area, other.getHitbox());
    }

    // ── Dibujo ────────────────────────────────────────────────────────────────

    public void draw(Canvas canvas) {
        Bitmap frame = currentFrame();
        if (frame == null || frame.isRecycled()) return;

        float pivotHalfW = idleAnim.getFrameWidth() * scale / 2f;

        float drawX = x - pivotHalfW + drawOffsetX;
        // La física usa Y-arriba (0=suelo), el canvas usa Y-abajo: convertir antes de dibujar
        float drawY = (worldHeight - y) + drawOffsetY;

        matrix.reset();
        if (!facingRight) {
            // Espejo: usar ancho SIN escalar en postTranslate; postScale se aplica después
            matrix.setScale(-1, 1);
            matrix.postTranslate(frame.getWidth(), 0);
        }
        matrix.postScale(scale, scale);
        matrix.postTranslate(drawX, drawY);

        boolean blinking = currentState == State.HURT && (int)(hurtTime * 15) % 2 == 0;
        canvas.drawBitmap(frame, matrix, blinking ? hurtPaint : paint);
        if (DEBUG_HITBOXES) drawDebugHitbox(canvas);
    }

    private void drawDebugHitbox(Canvas canvas) {
        RectF hb = getHitbox();
        canvas.drawRect(hb.left, hb.top, hb.right, hb.bottom, dbgBodyPaint);

        if (currentState == State.ATTACKING) {
            float reach = (currentAttackType == AttackType.KICK)    ? 400
                        : (currentAttackType == AttackType.SPECIAL)  ? 700 : 300;
            float ax = facingRight ? hb.right : hb.left - reach;
            canvas.drawRect(ax, hb.top, ax + reach, hb.bottom, dbgAttackPaint);
        }
    }

    private Bitmap currentFrame() {
        if (currentState == State.DEAD) {
            if (hasDeathAnim) return deathAnim.getKeyFrame(deathTime);
            if (hasHurtAnim)  return hurtAnim.getKeyFrame(hurtAnim.getDuration()); // último frame hurt
            return idleAnim.getKeyFrame(0); // sin death ni hurt → congelado en pose idle
        }
        if (currentState == State.HURT)
            return hasHurtAnim ? hurtAnim.getKeyFrame(hurtTime) : idleAnim.getKeyFrame(0);
        if (currentState == State.ATTACKING) {
            // kickVisualAnim: kickAnim si existe, o versión rápida de attack1 como visual distinto
            SpriteAnimation kick = kickVisualAnim;
            if (currentAttackType == AttackType.SPECIAL) {
                float pd = punchAnim.getDuration();
                return attackTime < pd
                        ? punchAnim.getKeyFrame(attackTime)
                        : kick.getKeyFrame(attackTime - pd);
            }
            return (currentAttackType == AttackType.KICK)
                    ? kick.getKeyFrame(attackTime)
                    : punchAnim.getKeyFrame(attackTime);
        }
        if (!isGrounded())              return hasJumpAnim ? jumpAnim.getKeyFrame(jumpTime) : idleAnim.getKeyFrame(stateTime);
        if (currentState == State.WALK) return walkAnim.getKeyFrame(stateTime);
        return idleAnim.getKeyFrame(stateTime);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public boolean    isAttacking()          { return currentState == State.ATTACKING; }
    public boolean    isHurt()               { return currentState == State.HURT; }
    public boolean    isDead()               { return currentState == State.DEAD; }
    public boolean    isGrounded()           { return y <= groundY + 5; }
    public AttackType getCurrentAttackType() { return currentAttackType; }

    // ── Dispose ───────────────────────────────────────────────────────────────

    public void recycle() {
        walkAnim.recycle();  idleAnim.recycle();   punchAnim.recycle();
        kickAnim.recycle();  hurtAnim.recycle();   jumpAnim.recycle();
        deathAnim.recycle();
        if (kickVisualAnim != null && kickVisualAnim != kickAnim) kickVisualAnim.recycle();
    }
}

