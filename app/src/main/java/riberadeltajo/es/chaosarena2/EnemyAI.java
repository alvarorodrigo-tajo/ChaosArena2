package riberadeltajo.es.chaosarena2;

public class EnemyAI {

    public enum State { IDLE, CHASE, ATTACK, RETREAT, COOLDOWN }

    private final Player npc;
    private final Player target;

    private State currentState      = State.IDLE;
    private float stateTimer        = 0f;
    private float attackCooldownTimer = 0f;

    private float speed      = 350f;
    private float damage     = 5f;
    private float attackRate = 1.8f;

    public EnemyAI(Player npc, Player target) {
        this.npc    = npc;
        this.target = target;
    }

    public void setSpeed(float v)      { this.speed = v; }
    public void setDamage(float v)     { this.damage = v; npc.damageMultiplier = v / 10f; }
    public void setAttackRate(float v) { this.attackRate = v; }
    public float getSpeed()            { return speed; }
    public float getDamage()           { return damage; }
    public float getAttackRate()       { return attackRate; }

    public void update(float delta) {
        if (npc.currentHealth <= 0 || target.currentHealth <= 0) return;

        float dist    = target.x - npc.x;
        float absDist = Math.abs(dist);

        if (stateTimer > 0)          stateTimer          -= delta;
        if (attackCooldownTimer > 0) attackCooldownTimer -= delta;

        if (!npc.isAttacking()) npc.facingRight = dist > 0;

        if (npc.isHurt()) {
            currentState = State.IDLE;
            stateTimer   = 0.2f;
            return;
        }

        // Reacción al ataque especial del jugador
        if (target.isAttacking()) {
            if (target.getCurrentAttackType() == Player.AttackType.SPECIAL) {
                if (currentState != State.RETREAT) { currentState = State.RETREAT; stateTimer = 0.5f; }
            } else if (absDist < 250 && currentState != State.RETREAT && attackCooldownTimer > 0) {
                if (Math.random() < 0.7) { currentState = State.RETREAT; stateTimer = 0.4f; }
            }
        }

        switch (currentState) {
            case IDLE:
                if (stateTimer <= 0) decideNextState(absDist);
                break;

            case CHASE:
                if (absDist < 250) {
                    currentState = attackCooldownTimer <= 0 ? State.ATTACK : State.IDLE;
                    if (currentState == State.IDLE) stateTimer = 0.2f + (float)(Math.random() * 0.3);
                } else if (stateTimer <= 0) {
                    decideNextState(absDist);
                } else if (!npc.isAttacking()) {
                    npc.move(dist > 0 ? speed * delta : -speed * delta);
                }
                break;

            case RETREAT:
                if (stateTimer <= 0) {
                    currentState = State.IDLE;
                    stateTimer   = 0.1f + (float)(Math.random() * 0.2);
                } else if (!npc.isAttacking()) {
                    npc.move(dist > 0 ? -speed * delta : speed * delta);
                }
                break;

            case ATTACK:
                if (!npc.isAttacking()) {
                    executeProAttack(absDist);
                    float delay = attackRate + (float)(Math.random() * 0.6 - 0.2);
                    currentState        = State.COOLDOWN;
                    stateTimer          = delay;
                    attackCooldownTimer = delay;
                }
                break;

            case COOLDOWN:
                if (stateTimer <= 0) {
                    currentState = State.IDLE;
                } else if (!npc.isAttacking() && Math.random() < 0.015) {
                    currentState = State.RETREAT;
                    stateTimer   = 0.3f + (float)(Math.random() * 0.3);
                }
                break;
        }
    }

    private void decideNextState(float absDist) {
        if (absDist > 350) {
            currentState = State.CHASE;
            stateTimer   = 0.2f + (float)(Math.random() * 0.4);
        } else if (absDist < 180) {
            if (Math.random() < 0.30) {
                currentState = State.RETREAT;
                stateTimer   = 0.2f + (float)(Math.random() * 0.2);
            } else if (attackCooldownTimer <= 0) {
                currentState = State.ATTACK;
            } else {
                currentState = State.IDLE;
                stateTimer   = 0.1f;
            }
        } else {
            if (attackCooldownTimer <= 0) {
                currentState = Math.random() < 0.8 ? State.ATTACK : State.CHASE;
            } else {
                currentState = Math.random() < 0.4 ? State.RETREAT : State.IDLE;
                stateTimer   = 0.1f + (float)(Math.random() * 0.2);
            }
        }
    }

    private void executeProAttack(float distance) {
        Player.AttackType type;
        if (npc.comboCharge >= Player.MAX_COMBO_CHARGE) {
            type = Player.AttackType.SPECIAL;
        } else if (distance > 200) {
            type = Player.AttackType.KICK;
        } else {
            type = Math.random() < 0.6 ? Player.AttackType.PUNCH : Player.AttackType.KICK;
        }
        npc.attack(type);
    }
}

