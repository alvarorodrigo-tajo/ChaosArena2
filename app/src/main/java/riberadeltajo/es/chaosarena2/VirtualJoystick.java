package riberadeltajo.es.chaosarena2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Joystick virtual dibujado en Canvas.
 * Reemplaza el Touchpad de libGDX Scene2D.
 */
public class VirtualJoystick {

    private final float centerX, centerY;
    private final float radius;          // radio de la base
    private final float knobRadius;

    private float knobX, knobY;          // posición actual del knob
    private int   trackingPointerId = -1;

    private final Bitmap bgBitmap;
    private final Bitmap knobBitmap;
    private final Paint  paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    public VirtualJoystick(float centerX, float centerY, float radius,
                           Bitmap bgBitmap, Bitmap knobBitmap) {
        this.centerX    = centerX;
        this.centerY    = centerY;
        this.radius     = radius;
        this.knobRadius = radius * 0.4f;
        this.bgBitmap   = bgBitmap;
        this.knobBitmap = knobBitmap;
        this.knobX      = centerX;
        this.knobY      = centerY;
    }

    // ── Touch ─────────────────────────────────────────────────────────────────

    public boolean onTouchEvent(MotionEvent e) {
        int action  = e.getActionMasked();
        int ptrIdx  = e.getActionIndex();
        int ptId    = e.getPointerId(ptrIdx);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                float tx = e.getX(ptrIdx);
                float ty = e.getY(ptrIdx);
                if (isInsideBase(tx, ty) && trackingPointerId == -1) {
                    trackingPointerId = ptId;
                    updateKnob(tx, ty);
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (trackingPointerId != -1) {
                    int idx = e.findPointerIndex(trackingPointerId);
                    if (idx >= 0) updateKnob(e.getX(idx), e.getY(idx));
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (ptId == trackingPointerId) {
                    trackingPointerId = -1;
                    knobX = centerX;
                    knobY = centerY;
                }
                break;
        }
        return false;
    }

    private void updateKnob(float tx, float ty) {
        float dx  = tx - centerX;
        float dy  = ty - centerY;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len > radius) {
            dx = dx / len * radius;
            dy = dy / len * radius;
        }
        knobX = centerX + dx;
        knobY = centerY + dy;
    }

    private boolean isInsideBase(float tx, float ty) {
        float dx = tx - centerX;
        float dy = ty - centerY;
        return Math.sqrt(dx * dx + dy * dy) <= radius * 1.3f; // margen generoso
    }

    // ── Valores de salida ─────────────────────────────────────────────────────

    /** -1.0 (izquierda) a +1.0 (derecha) */
    public float getKnobPercentX() {
        return (knobX - centerX) / radius;
    }

    /** -1.0 (arriba) a +1.0 (abajo) — raramente necesario en juego de lucha */
    public float getKnobPercentY() {
        return (knobY - centerY) / radius;
    }

    public boolean isActive() { return trackingPointerId != -1; }

    // ── Dibujo ────────────────────────────────────────────────────────────────

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bgBitmap,
                null,
                new RectF(centerX - radius, centerY - radius,
                        centerX + radius, centerY + radius),
                paint);

        float kr = knobRadius;
        canvas.drawBitmap(knobBitmap,
                null,
                new RectF(knobX - kr, knobY - kr, knobX + kr, knobY + kr),
                paint);
    }
}