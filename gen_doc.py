# -*- coding: utf-8 -*-
"""Genera documentacion detallada de ChaosArena2 en PDF usando Helvetica."""

from fpdf import FPDF

W = 210  # A4 width mm
LM = RM = 15  # margins
TW = W - LM - RM  # text width = 180 mm

class Doc(FPDF):
    def __init__(self):
        super().__init__()
        self.set_auto_page_break(auto=True, margin=16)
        self.set_margins(LM, 16, RM)
        self.set_font("Helvetica", "", 10)

    def header(self):
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(140, 140, 140)
        self.cell(TW, 6, "ChaosArena2 - Documentacion tecnica del codigo fuente", align="C")
        self.ln(2)
        self.set_draw_color(180, 180, 180)
        self.line(LM, self.get_y(), W - RM, self.get_y())
        self.ln(3)
        self.set_text_color(0, 0, 0)
        self.set_draw_color(0, 0, 0)

    def footer(self):
        self.set_y(-13)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150, 150, 150)
        self.cell(TW, 8, f"Pagina {self.page_no()}", align="C")
        self.set_text_color(0, 0, 0)

    # ── helpers ───────────────────────────────────────────────────────────────

    def h1(self, txt):
        self.set_font("Helvetica", "B", 18)
        self.set_text_color(20, 20, 100)
        self.set_x(LM)
        self.multi_cell(TW, 9, txt)
        self.ln(2)
        self.set_text_color(0, 0, 0)

    def pkg_banner(self, txt, r=30, g=50, b=140):
        self.ln(3)
        self.set_fill_color(r, g, b)
        self.set_text_color(255, 255, 255)
        self.set_font("Helvetica", "B", 11)
        self.set_x(LM)
        self.cell(TW, 8, "  Paquete: " + txt, fill=True)
        self.ln(6)
        self.set_text_color(0, 0, 0)

    def class_hdr(self, name, pkg, kind="class"):
        self.ln(2)
        self.set_fill_color(225, 230, 255)
        self.set_font("Helvetica", "B", 13)
        self.set_x(LM)
        self.cell(TW, 8, "  " + kind + "  " + name, fill=True)
        self.ln(4)
        self.set_font("Courier", "", 8)
        self.set_text_color(70, 70, 180)
        self.set_x(LM)
        self.cell(TW, 5, "  " + pkg)
        self.ln(5)
        self.set_text_color(0, 0, 0)

    def desc(self, txt):
        self.set_font("Helvetica", "", 10)
        self.set_x(LM)
        self.multi_cell(TW, 5.5, txt)
        self.ln(2)

    def sub(self, txt):
        self.ln(1)
        self.set_font("Helvetica", "B", 10)
        self.set_text_color(40, 40, 160)
        self.set_x(LM)
        self.cell(TW, 7, txt)
        self.ln(3)
        self.set_text_color(0, 0, 0)

    def field(self, sig, description):
        self.set_fill_color(248, 248, 248)
        self.set_font("Courier", "", 8)
        self.set_x(LM)
        self.multi_cell(TW, 5, "  " + sig, fill=True)
        self.set_font("Helvetica", "", 9.5)
        self.set_text_color(55, 55, 55)
        self.set_x(LM)
        self.multi_cell(TW, 5, "    -> " + description)
        self.set_text_color(0, 0, 0)
        self.ln(1)

    def method(self, sig, description):
        self.set_fill_color(240, 250, 240)
        self.set_font("Courier", "", 8)
        self.set_x(LM)
        self.multi_cell(TW, 5, "  " + sig, fill=True)
        self.set_font("Helvetica", "", 9.5)
        self.set_text_color(55, 55, 55)
        self.set_x(LM)
        self.multi_cell(TW, 5, "    -> " + description)
        self.set_text_color(0, 0, 0)
        self.ln(1)

    def enum_row(self, vals, description):
        self.set_fill_color(255, 248, 228)
        self.set_font("Courier", "", 8)
        self.set_x(LM)
        self.multi_cell(TW, 5, "  enum { " + vals + " }", fill=True)
        self.set_font("Helvetica", "", 9.5)
        self.set_text_color(55, 55, 55)
        self.set_x(LM)
        self.multi_cell(TW, 5, "    -> " + description)
        self.set_text_color(0, 0, 0)
        self.ln(1)

    def hr(self):
        self.set_draw_color(200, 200, 220)
        self.line(LM, self.get_y(), W - RM, self.get_y())
        self.set_draw_color(0, 0, 0)
        self.ln(3)


# ── Instancia ─────────────────────────────────────────────────────────────────
pdf = Doc()

# ══════════════════════════════════════════════════════════════════════════════
# PORTADA
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.ln(35)
pdf.set_font("Helvetica", "B", 34)
pdf.set_text_color(20, 20, 100)
pdf.cell(TW, 18, "CHAOS ARENA 2", align="C"); pdf.ln(20)
pdf.set_font("Helvetica", "", 16)
pdf.set_text_color(60, 60, 60)
pdf.cell(TW, 10, "Documentacion tecnica del codigo fuente", align="C"); pdf.ln(8)
pdf.set_font("Helvetica", "", 12)
pdf.cell(TW, 8, "Android fighting game - Java / Android SDK", align="C"); pdf.ln(28)
pdf.set_draw_color(100, 100, 200); pdf.set_line_width(0.7)
pdf.line(45, pdf.get_y(), W - 45, pdf.get_y()); pdf.ln(14)
pdf.set_line_width(0.2); pdf.set_draw_color(0, 0, 0)
pdf.set_font("Helvetica", "", 11)
pdf.set_text_color(80, 80, 80)
info = [
    "Namespace : riberadeltajo.es.chaosarena2",
    "Paquetes  : engine | entity | ai | input | stage | progression",
    "Clases    : 13 ficheros Java",
    "Plataforma: Android  (minSdk 24, targetSdk 34)",
]
for l in info:
    pdf.cell(TW, 8, l, align="C"); pdf.ln()
pdf.set_text_color(0, 0, 0)

# ══════════════════════════════════════════════════════════════════════════════
# INDICE
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.h1("Indice de clases")
idx = [
    ("ActividadJuego",  "riberadeltajo.es.chaosarena2",            "Actividad Android principal (punto de entrada)"),
    ("BucleJuego",      "riberadeltajo.es.chaosarena2.engine",      "Hilo del bucle de juego a 60 FPS"),
    ("Juego",           "riberadeltajo.es.chaosarena2.engine",      "Vista principal: logica, pantallas y renderizado"),
    ("ResourceManager", "riberadeltajo.es.chaosarena2.engine",      "Carga y centraliza assets visuales"),
    ("Player",          "riberadeltajo.es.chaosarena2.entity",      "Entidad jugador/enemigo: fisica, stats, animacion, hitbox"),
    ("SpriteAnimation", "riberadeltajo.es.chaosarena2.entity",      "Animacion por frames desde assets PNG"),
    ("EnemyAI",         "riberadeltajo.es.chaosarena2.ai",          "Maquina de estados que controla al enemigo"),
    ("VirtualJoystick", "riberadeltajo.es.chaosarena2.input",       "Joystick tactil dibujado en Canvas"),
    ("StageDef",        "riberadeltajo.es.chaosarena2.stage",       "Datos de un escenario de combate"),
    ("Buff",            "riberadeltajo.es.chaosarena2.progression", "Mejora aplicable al jugador en modo Arcade"),
    ("BuffType",        "riberadeltajo.es.chaosarena2.progression", "Enum de tipos de mejora disponibles"),
    ("Event",           "riberadeltajo.es.chaosarena2.progression", "Evento especial aleatorio entre combates"),
    ("RunManager",      "riberadeltajo.es.chaosarena2.progression", "Gestor del estado completo de una partida Arcade"),
]
for name, pkg, desc in idx:
    pdf.set_x(LM)
    pdf.set_font("Helvetica", "B", 10)
    pdf.cell(38, 6, name)
    pdf.set_font("Courier", "", 8)
    pdf.set_text_color(60, 60, 180)
    pdf.cell(80, 6, pkg)
    pdf.set_text_color(0, 0, 0)
    pdf.set_font("Helvetica", "", 10)
    pdf.multi_cell(0, 6, desc)
    pdf.set_draw_color(210, 210, 210)
    pdf.line(LM, pdf.get_y(), W - RM, pdf.get_y())
    pdf.set_draw_color(0, 0, 0)
    pdf.ln(1)

# ══════════════════════════════════════════════════════════════════════════════
# 1. ActividadJuego
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2  (raiz)", 20, 20, 110)
pdf.class_hdr("ActividadJuego", "riberadeltajo.es.chaosarena2")
pdf.desc(
    "Punto de entrada de la aplicacion Android. Extiende AppCompatActivity y gestiona el ciclo de "
    "vida del sistema operativo (onCreate/onPause/onResume/onDestroy). Ademas centraliza todas las "
    "constantes de configuracion del juego como campos static publicos accesibles desde Juego.java "
    "sin referencias circulares entre paquetes."
)

pdf.sub("Constantes estaticas (configuracion global)")
pdf.field("static final int MAX_LEVELS = 3",
    "Numero de niveles que tiene el modo Historia. Juego comprueba currentLevel > MAX_LEVELS "
    "para detectar la victoria final.")
pdf.field("static final int MAX_SLOTS = 3",
    "Numero de ranuras de guardado. Reservado para una futura pantalla de seleccion de partida.")
pdf.field("static final float[] LEVEL_ENEMY_HEALTH = {250, 250, 250}",
    "Vida maxima del enemigo en cada nivel del modo Historia. Se asigna a player2.maxHealth "
    "en applyModeConfig().")
pdf.field("static final float[] LEVEL_ENEMY_SPEED  = {300, 400, 500}",
    "Velocidad de movimiento del enemigo en px/s (coordenadas mundo 1920x1080). Aumenta con "
    "cada nivel para hacer al enemigo progresivamente mas agresivo.")
pdf.field("static final float[] LEVEL_ENEMY_DAMAGE = {5, 5, 5}",
    "Dano base del enemigo. Se pasa a EnemyAI.setDamage(); internamente la IA actualiza "
    "player2.damageMultiplier = damage/10.")
pdf.field("static final float[] LEVEL_ATTACK_RATE  = {1.6, 1.0, 0.6}",
    "Segundos minimos entre ataques del enemigo por nivel. Valor menor = mas ataques por segundo. "
    "Nivel 1: ataque cada ~1.6s. Nivel 3: ataque cada ~0.6s.")
pdf.field("static final String[] LEVEL_ENEMY_NAMES",
    "Nombres de los rivales en modo Historia: 'Dark Fighter' (nv1), 'Ronin Boss' (nv2), "
    "'Shadow Master' (nv3). Usados en resolveEnemyName() de Juego para determinar el sprite.")
pdf.field("static final String[] LEVEL_LORE",
    "Array de 3 cadenas largas con el texto narrativo mostrado en la pantalla LORE antes de "
    "cada combate. Se indexa con Math.min(currentLevel-1, length-1) para evitar desbordamiento.")

pdf.sub("Campos de instancia")
pdf.field("Juego juego",
    "Vista principal del juego (SurfaceView). Creada en onCreate y destruida en onDestroy. "
    "Toda la logica del juego reside en esta clase.")
pdf.field("ResourceManager resources",
    "Cargador de assets. Se instancia antes que Juego para que los assets esten disponibles "
    "cuando Juego los necesite en el constructor.")
pdf.field("SharedPreferences prefs",
    "Archivo de preferencias 'ChaosArenaPrefs' con modo MODE_PRIVATE. "
    "Almacena el top-5 de puntuaciones arcade y el estado de silencio de la musica.")

pdf.sub("Metodos del ciclo de vida Android")
pdf.method("onCreate(Bundle savedInstanceState)",
    "Configura la actividad en pantalla completa sin barra de titulo: "
    "requestWindowFeature(FEATURE_NO_TITLE), FLAG_FULLSCREEN y FLAG_KEEP_SCREEN_ON. "
    "Luego instancia ResourceManager(this), Juego(this, resources, prefs) "
    "y llama a setContentView(juego) para ocupar toda la pantalla.")
pdf.method("onPause()",
    "Llama a juego.pauseMusic() para silenciar la musica cuando la app pasa a segundo plano. "
    "Evita consumir CPU/bateria con la reproduccion de audio.")
pdf.method("onResume()",
    "Llama a juego.resumeMusic() para reanudar la musica al volver a primer plano.")
pdf.method("onDestroy()",
    "Libera todos los recursos: juego.releaseMusic() libera el MediaPlayer y "
    "resources.recycle() recicla todos los Bitmaps de assets para evitar memory leaks.")

# ══════════════════════════════════════════════════════════════════════════════
# 2. BucleJuego
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2.engine", 0, 90, 0)
pdf.class_hdr("BucleJuego", "riberadeltajo.es.chaosarena2.engine", "class extends Thread")
pdf.desc(
    "Hilo dedicado al bucle de juego que mantiene 60 FPS. Implementa un esquema de tiempo fijo "
    "(fixed timestep) con catch-up: si un frame tarda mas de 16ms, ejecuta hasta "
    "MAX_FRAMES_SALTADOS actualizaciones logicas extra sin renderizar para recuperar el retraso "
    "acumulado, manteniendo la simulacion fisica consistente independientemente de la carga del dispositivo."
)

pdf.sub("Constantes")
pdf.field("static final int MAX_FPS = 60",
    "Frecuencia de actualizacion objetivo. Todos los calculos de tiempo se derivan de este valor.")
pdf.field("static final int MAX_FRAMES_SALTADOS = 5",
    "Maximo de actualizaciones logicas adicionales permitidas en un solo frame. Limita el efecto "
    "'spiral of death' cuando el dispositivo es demasiado lento para mantener 60 FPS.")
pdf.field("static final int TIEMPO_FRAME = 1000 / MAX_FPS  (= 16 ms)",
    "Duracion objetivo de cada frame en milisegundos. Se usa para calcular tiempoDormir.")

pdf.sub("Campos publicos")
pdf.field("volatile boolean enEjecucion",
    "Flag de control del bucle. La palabra clave volatile garantiza visibilidad entre hilos. "
    "El hilo principal lo pone a false en surfaceDestroyed() para parar el bucle limpiamente.")
pdf.field("int iteraciones",
    "Contador de frames completamente renderizados desde el inicio del bucle. "
    "Se puede usar para calcular FPS promedio: fps = iteraciones / (tiempoTotal / 1000f).")
pdf.field("long tiempoTotal",
    "Suma de tiempos de todos los frames en ms. Permite diagnosticar el rendimiento global.")
pdf.field("int maxX, maxY",
    "Dimensiones reales de la pantalla en pixeles. Se leen del primer Canvas bloqueado "
    "en el constructor. Actualmente no se usan externamente pero sirven para debug.")

pdf.sub("Constructor y metodo run")
pdf.method("BucleJuego(SurfaceHolder sh, Juego juego)",
    "Guarda referencias al SurfaceHolder y a Juego. Intenta bloquear el Canvas una vez para "
    "leer las dimensiones reales (maxX, maxY) y lo libera inmediatamente sin dibujar nada. "
    "El paquete-privado del constructor fuerza que solo Juego lo pueda instanciar.")
pdf.method("run()",
    "Bucle principal: (1) bloquea el Canvas del SurfaceHolder, (2) mide tiempoComienzo, "
    "(3) llama a juego.actualizar(delta) donde delta=TIEMPO_FRAME/1000f=0.01667s fijo, "
    "(4) llama a juego.renderizar(canvas), (5) incrementa iteraciones, "
    "(6) calcula tiempoDormir = TIEMPO_FRAME - tiempoRealTranscurrido, "
    "(7) si tiempoDormir > 0 duerme ese tiempo para no quemar CPU, "
    "(8) si tiempoDormir < 0 ejecuta bucle catch-up: repite actualizar(delta) hasta "
    "MAX_FRAMES_SALTADOS veces sin renderizar para ponerse al dia, "
    "(9) en el bloque finally libera siempre el Canvas.")

# ══════════════════════════════════════════════════════════════════════════════
# 3. Juego
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.class_hdr("Juego", "riberadeltajo.es.chaosarena2.engine",
              "class extends SurfaceView implements SurfaceHolder.Callback")
pdf.desc(
    "Clase central que actua como vista Android y como controlador de toda la logica. "
    "Implementa una maquina de estados con 8 pantallas (Screen enum). Escala el mundo virtual "
    "1920x1080 a cualquier resolucion real mediante una Matrix de transformacion. "
    "Gestiona jugadores, IA, musica, entrada tactil multi-punto y la progresion arcade completa."
)

pdf.sub("Constantes de diseno")
pdf.field("float WORLD_W = 1920, WORLD_H = 1080",
    "Resolucion del espacio de juego virtual. Toda la logica opera en estas coordenadas. "
    "worldMatrix las escala al renderizar para adaptarse a cualquier pantalla.")
pdf.field("float INITIAL_TIME = 90",
    "Segundos por combate en modo Arcade. Cuando llega a 0 el jugador pierde.")
pdf.field("float MOVE_SPEED = 650",
    "Velocidad horizontal base del jugador en px/s (coordenadas mundo). "
    "Se multiplica por player1.speedMultiplier en updateFighting().")
pdf.field("float MAP_MIN_X = 150",
    "Limite horizontal: los personajes no pueden salir del rango [150, WORLD_W-150].")
pdf.field("float DEATH_ANIM_WAIT = 2.5",
    "Segundos de espera tras activar pendingResult antes de mostrar la pantalla de resultado. "
    "Permite que se vea completa la animacion de muerte del personaje derrotado.")
pdf.field("float HIT_FLASH_DURATION = 0.10",
    "Duracion del circulo de impacto amarillo en el punto de golpe. 100ms de efecto visual.")

pdf.sub("Maquina de estados")
pdf.field("enum Screen { MAIN_MENU, CHAR_SELECT, LORE, FIGHTING, PAUSED, RESULT, BUFF_SELECT, EVENT }",
    "MAIN_MENU: pantalla de inicio con 3 modos. "
    "CHAR_SELECT: eleccion de personaje y escenario. "
    "LORE: texto narrativo antes del combate (solo Historia). "
    "FIGHTING: combate activo. "
    "PAUSED: pausa superpuesta sobre el combate. "
    "RESULT: resultado con opciones de continuar/menu. "
    "BUFF_SELECT: eleccion de mejora (solo Arcade). "
    "EVENT: evento especial aleatorio (solo Arcade).")

pdf.sub("Campos de estado del combate")
pdf.field("Player player1, player2",
    "Jugador humano (player1) y oponente controlado por la IA o segundo jugador humano (player2). "
    "Se crean en startFight() y se reutilizan entre combates via spawnNewEnemy().")
pdf.field("EnemyAI enemyAI",
    "Controlador de IA del enemigo. Su update(delta) se llama cada frame en updateFighting(). "
    "Solo controla a player2 en modos Historia y Arcade.")
pdf.field("RunManager runManager",
    "Instanciado solo en modo Arcade (activeSlot == -2). Gestiona buffs, eventos y escalado "
    "progresivo de enemigos. null en Historia y Duelo.")
pdf.field("int activeSlot",
    "Modo activo: >=0 Historia (indice de slot de guardado), -1 Duelo local, -2 Arcade.")
pdf.field("int currentLevel",
    "Nivel actual en modo Historia (1..MAX_LEVELS). Incrementado en resetCombat() tras victoria.")
pdf.field("boolean pendingResult, pendingResultWon",
    "pendingResult=true indica que se esta esperando que transcurra DEATH_ANIM_WAIT. "
    "pendingResultWon guarda si el jugador gano o perdio para pasarlo a showResult().")
pdf.field("float deathDelay",
    "Acumulador de tiempo (en segundos) desde que se activo pendingResult. "
    "Cuando supera DEATH_ANIM_WAIT se llama a showResult().")
pdf.field("StageDef currentStage",
    "Escenario activo durante el combate. Apunta a uno de los 3 elementos de res.stages[].")

pdf.sub("Escalado y entrada")
pdf.field("Matrix worldMatrix / inverseMatrix",
    "worldMatrix: setScale(screenW/WORLD_W, screenH/WORLD_H). "
    "Se aplica con canvas.concat(worldMatrix) al inicio de renderizar(). "
    "inverseMatrix: inversa calculada con worldMatrix.invert(inverseMatrix). "
    "Se usa para convertir coordenadas de toque de pantalla a coordenadas mundo.")
pdf.field("VirtualJoystick joystick",
    "Joystick virtual en coordenadas mundo (centerX=280, centerY=WORLD_H-280=800, radio=180). "
    "Creado en el constructor y actualizado con la inverseMatrix en updateScaleMatrix().")
pdf.field("float hitFlashTimer, hitFlashX, hitFlashY",
    "Estado del efecto de impacto. hitFlashTimer cuenta desde HIT_FLASH_DURATION hacia 0. "
    "Mientras > 0 se dibuja un circulo amarillo en (hitFlashX, hitFlashY) con radio y opacidad "
    "proporcionales al tiempo restante.")

pdf.sub("Metodos de ciclo de vida (SurfaceHolder.Callback)")
pdf.method("surfaceCreated(SurfaceHolder holder)",
    "Bloquea el Canvas para obtener dimensiones reales, llama a updateScaleMatrix(), "
    "crea e inicia BucleJuego y arranca la musica con startMusic().")
pdf.method("surfaceChanged(int format, int w, int h)",
    "Recalcula worldMatrix e inverseMatrix con las nuevas dimensiones. "
    "Tambien actualiza la inverseMatrix del joystick.")
pdf.method("surfaceDestroyed(SurfaceHolder holder)",
    "Para el hilo BucleJuego: pone enEjecucion=false y hace join() en bucle hasta que el "
    "hilo termine limpiamente.")

pdf.sub("Bucle principal")
pdf.method("actualizar(float delta)",
    "Delegado de BucleJuego. Solo llama a updateFighting(delta) si currentScreen==FIGHTING.")
pdf.method("renderizar(Canvas canvas)",
    "Aplica worldMatrix al canvas y despacha al metodo draw* segun currentScreen. "
    "PAUSED y RESULT dibujan primero drawFighting() y luego superponen su overlay.")

pdf.sub("Logica de combate")
pdf.method("updateFighting(float delta)",
    "Nucleo del combate: (1) si pendingResult acumula deathDelay hasta DEATH_ANIM_WAIT, "
    "actualiza ambos jugadores y transiciona a showResult; "
    "(2) en Arcade decrementa timeLeft y activa derrota si llega a 0; "
    "(3) lee joystick para mover/rotar player1; "
    "(4) llama a player1.update/updateAttack y player2.update/updateAttack; "
    "(5) llama a enemyAI.update; "
    "(6) procesa justHitThisFrame para disparar hitFlash; "
    "(7) clipa posicion X de ambos en [MAP_MIN_X, WORLD_W-MAP_MIN_X]; "
    "(8) comprueba currentHealth <= 0 y llama a triggerResult().")
pdf.method("triggerResult(boolean won)",
    "Activa pendingResult: llama a player2.startDeath() si el jugador gano o "
    "player1.startDeath() si el jugador perdio. El otro personaje queda en forceIdle(). "
    "Evita activarse dos veces con if(pendingResult) return.")
pdf.method("showResult(boolean won)",
    "Guarda el resultado en lastBattleWon y transiciona a Screen.RESULT. "
    "En Arcade: si won, llama a runManager.onFightWon() e incrementa enemiesDefeated; "
    "si no won, llama a saveArcadeScore().")

pdf.sub("Logica de pantallas y progresion")
pdf.method("startFight()",
    "Inicializa el combate: resuelve nombre de personaje del jugador y del enemigo, "
    "crea ambos Player con sus sprites y posiciones iniciales, los enlaza (opponent), "
    "crea EnemyAI, llama a applyModeConfig(), crea RunManager en Arcade. "
    "Transiciona a LORE (Historia) o FIGHTING (Arcade/Duelo).")
pdf.method("spawnNewEnemy()",
    "Reutiliza el objeto player2 cargando nuevas animaciones con loadAnimations(). "
    "Evita crear y destruir objetos Player entre rondas en Arcade e Historia.")
pdf.method("applyModeConfig()",
    "Configura los stats del enemigo segun el modo: Historia usa LEVEL_ENEMY_* de "
    "ActividadJuego; Arcade fija valores base (250 vida, 350 speed, 10 dmg, 1.5s rate); "
    "Duelo da 600 de vida a ambos y IA mas agresiva.")
pdf.method("onResultAction()",
    "Boton de accion de la pantalla RESULT: Arcade+victoria -> BUFF_SELECT; "
    "Arcade+derrota -> MAIN_MENU; Historia/Duelo -> resetCombat().")
pdf.method("checkForEvent()",
    "Si runManager.shouldTriggerEvent(): genera evento, lo aplica y va a EVENT. "
    "Si no: llama a resetCombat().")
pdf.method("resetCombat()",
    "Reinicia posiciones (player1.x=400, player2.x=WORLD_W-400), vidas, velocidades "
    "y comboCharge. En Historia avanza el nivel. En Arcade llama a "
    "runManager.applyEnemyScaling() para escalar al nuevo enemigo.")
pdf.method("saveArcadeScore()",
    "Lee el top-5 de SharedPreferences, agrega la nueva puntuacion al array[6], "
    "ordena por tiempo de supervivencia (burbuja) y guarda de vuelta los 5 primeros.")

pdf.sub("Entrada tactil")
pdf.method("onTouchEvent(MotionEvent e) -> boolean",
    "En combate/pausa: redirige el evento al joystick para procesarlo. "
    "Para ACTION_DOWN y ACTION_POINTER_DOWN: convierte las coordenadas del puntero activo "
    "de pantalla a mundo con inverseMatrix.mapPoints() y llama a handleTap(wx, wy).")
pdf.method("handleTap(float wx, float wy)",
    "Switch sobre currentScreen. Usa hit(RectF, x, y) para cada boton de la pantalla activa. "
    "Cubre los 8 estados: MAIN_MENU (3 modos), CHAR_SELECT (personaje+escenario+luchar), "
    "LORE (empezar), FIGHTING (pausa+4 ataques), PAUSED (continuar+salir), "
    "RESULT (accion+menu), BUFF_SELECT (3 mejoras), EVENT (continuar).")

pdf.sub("Musica y dibujo")
pdf.method("startMusic()",
    "Abre 'sounds/Techno_Syndrome.mp3' desde assets con AssetFileDescriptor, configura "
    "el MediaPlayer en loop y ajusta el volumen segun la preferencia is_muted.")
pdf.method("drawFighting(Canvas canvas)",
    "Dibuja: fondo con desplazamiento parallax horizontal (pShiftX basado en player1.x), "
    "player1.draw() y player2.draw(), circulo de impacto amarillo si hitFlashTimer>0, "
    "overlay rojo semi-transparente si player1 usa SPECIAL, barras de vida/combo y botones.")
pdf.method("initAllButtonRects()",
    "Pre-calcula posiciones de todos los botones de todas las pantallas una sola vez en el "
    "constructor. Los botones son RectF en coordenadas mundo que se comparan directamente "
    "con las coordenadas de toque convertidas.")

# ══════════════════════════════════════════════════════════════════════════════
# 4. ResourceManager
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.class_hdr("ResourceManager", "riberadeltajo.es.chaosarena2.engine")
pdf.desc(
    "Unico punto de carga de assets visuales. Se instancia antes de Juego y expone todos los "
    "recursos como campos publicos finales para acceso directo sin recargas. Proporciona helpers "
    "para resolver nombres de personaje a rutas de sprites."
)

pdf.sub("Assets de fuente")
pdf.field("Paint fontSmall  (~36sp, blanco, BOLD)",
    "Para textos de HUD: nombres de personaje, enemigos derrotados, textos pequenos en botones.")
pdf.field("Paint fontMedium (~52sp, blanco, BOLD)",
    "Para etiquetas de botones de accion en todas las pantallas.")
pdf.field("Paint fontBig    (~72sp, blanco, BOLD)",
    "Para titulos principales, 'CHAOS ARENA', '!VICTORIA!', 'DERROTA', contador de tiempo.")

pdf.sub("Assets graficos")
pdf.field("Bitmap bgTitle",
    "Fondo del menu principal. Cargado desde backgrounds/title_bg.png. "
    "Se dibuja escalado a WORLD_W x WORLD_H.")
pdf.field("Bitmap joystickBg, joystickKnob",
    "Imagenes del joystick virtual: joystick/AIR_joystick_bg600.png (fondo circular) y "
    "AIR_joystick_stick600.png (bola movil). Dibujadas por VirtualJoystick.draw().")
pdf.field("Bitmap soundOn, soundOff",
    "Iconos del boton de silencio (ui/sound_on.png, ui/sound_off.png). "
    "Reservados para la HUD, actualmente no se muestran en pantalla.")
pdf.field("StageDef[] stages  (3 elementos)",
    "Array de escenarios: [0] Bosque (forest_bg, floorOffset=-40, groundY=80), "
    "[1] Desierto (desert_bg, offset=0, groundY=60), [2] Ciudad (city_bg, offset=40, groundY=90).")
pdf.field("AssetManager assets",
    "Referencia al AssetManager de Android. Se pasa a Player.loadAnimations() para "
    "que pueda abrir los PNG de los sprites en tiempo de ejecucion.")

pdf.sub("Metodos")
pdf.method("ResourceManager(Context context)",
    "Obtiene assets=context.getAssets(), crea los tres Paint (makePaint), carga los "
    "Bitmaps de fondo e iconos (loadBitmap) y construye el array stages con sus fondos.")
pdf.method("getFolderForChar(String name) -> String",
    "Mapea nombre a carpeta de assets: contiene 'Goro' -> 'sprites/goro', "
    "contiene 'Sub Zero' -> 'sprites/subzero', resto -> 'sprites/liu_kang'.")
pdf.method("getPrefixForChar(String name) -> String",
    "Mapea nombre a prefijo de frames: 'goro', 'subzero' o 'liu_kang'. "
    "Se concatena con '_idle', '_run', '_attack1', etc. para buscar los PNGs.")
pdf.method("loadBitmap(String assetPath) -> Bitmap",
    "Decodifica un asset con BitmapFactory.decodeStream(inScaled=false). "
    "Si el fichero no existe devuelve Bitmap 1x1 magenta como indicador visual de error.")
pdf.method("recycle()",
    "Llama a recycleBitmap() en bgTitle, joystickBg, joystickKnob, soundOn, soundOff "
    "y en cada StageDef del array stages.")

# ══════════════════════════════════════════════════════════════════════════════
# 5. Player
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2.entity", 120, 0, 80)
pdf.class_hdr("Player", "riberadeltajo.es.chaosarena2.entity")
pdf.desc(
    "Clase mas compleja del juego. Representa tanto al jugador humano como al enemigo. "
    "Contiene fisica (gravedad, salto), animacion por estados, sistema de hitboxes en "
    "coordenadas canvas, stats modificables por buffs, y renderizado con espejo horizontal. "
    "El sistema de coordenadas usa Y-arriba para la fisica (y=0 en el suelo) y convierte "
    "a Y-abajo de Canvas solo en draw() con la formula: drawY = (worldHeight - y) + drawOffsetY."
)

pdf.sub("Enums")
pdf.enum_row("AttackType: PUNCH, KICK, SPECIAL",
    "Tipo de ataque activo. PUNCH: golpe basico (10 dmg, alcance 300px). "
    "KICK: patada (15 dmg, alcance 400px). "
    "SPECIAL: combo devastador (45 dmg, alcance 700px, requiere comboCharge >= 100).")
pdf.enum_row("State: IDLE, WALK, ATTACKING, HURT, DEAD",
    "Estado del personaje. Determina la animacion reproducida y las acciones permitidas. "
    "HURT y DEAD bloquean movimiento y ataques. ATTACKING solo acepta multiples golpes "
    "cuando hasHitInCurrentAttack es false.")

pdf.sub("Fisica")
pdf.field("float x, y",
    "Posicion en coordenadas mundo. x: horizontal (0=izquierda, 1920=derecha). "
    "y: vertical con origen en el suelo y Y aumentando hacia arriba. "
    "Convertido a canvas en draw(): drawY = (worldHeight - y) + drawOffsetY.")
pdf.field("float velocityY",
    "Velocidad vertical en px/s. Se reduce cada frame: velocityY += GRAVITY * delta. "
    "Al saltar se asigna velocityY = JUMP_FORCE.")
pdf.field("static final float GRAVITY = -2500",
    "Aceleracion gravitacional en px/s^2. Negativo porque el eje Y sube en la fisica. "
    "Con JUMP_FORCE=1150, el personaje tarda ~0.46s en llegar al punto mas alto del salto.")
pdf.field("static final float JUMP_FORCE = 1150",
    "Velocidad vertical inicial al saltar. Produce un salto con altura maxima de ~264px.")
pdf.field("float groundY",
    "Coordenada Y del suelo para este personaje especifico. Inicializada con el "
    "parametro y del constructor y se usa como limite inferior en update().")

pdf.sub("Escalado visual")
pdf.field("float scale = 4.0",
    "Factor de escala aplicado a todos los frames de sprite al dibujar. "
    "Un frame de 47x94px (Liu Kang idle) se dibuja como 188x376px en pantalla.")
pdf.field("float drawOffsetX = 0, drawOffsetY",
    "Offset en pixeles canvas aplicado DESPUES de convertir Y-fisica a Y-canvas. "
    "drawOffsetY es negativo para desplazar el sprite hacia arriba y alinear los pies "
    "del sprite con groundY. Valores: subzero=-636, goro=-472, liu_kang=-396.")
pdf.field("float hbWMult, hbHMult",
    "Multiplicadores sobre el tamano del frame para calcular dimensiones de la hitbox. "
    "subzero: w=0.105 (~84px), h=0.490 (~374px). "
    "goro y liu_kang: w=0.35, h=0.80.")
pdf.field("float hbOffsetX, hbOffsetY",
    "Offset de posicion de la hitbox en pixeles canvas respecto al centro del frame. "
    "Necesario cuando el contenido del sprite no esta centrado en su frame. "
    "subzero: hbOffsetX=-58 (personaje centrado en X=85 de un frame de 199px ancho, "
    "delta=(99.5-85)*4=58), hbOffsetY=164 (41px vacios arriba del frame * 4 = 164px).")

pdf.sub("Stats (modificables por buffs)")
pdf.field("float maxHealth = 250, currentHealth = 250",
    "Vida del personaje. currentHealth llega a 0 cuando muere. maxHealth puede aumentar "
    "con buffs HEALTH o con el escalado de RunManager en enemigos.")
pdf.field("float comboCharge, MAX_COMBO_CHARGE = 100",
    "Barra de ataque especial. Aumenta con cada golpe de PUNCH o KICK aplicado "
    "mediante addCharge(dmg). Se consume a 0 al usar SPECIAL.")
pdf.field("float damageMultiplier = 1.0",
    "Multiplicador aplicado al dano de todos los ataques. Aumentado por buffs DAMAGE "+
    "y por el escalado de RunManager en enemigos.")
pdf.field("float speedMultiplier = 1.0",
    "Multiplicador de MOVE_SPEED aplicado en Juego.updateFighting(). Aumentado por buffs SPEED.")
pdf.field("float lifestealPercent = 0",
    "Porcentaje (0..1) del dano infligido que se recupera como vida. Ej: 0.05 -> 5%. "
    "Se aplica despues de calcular el dano real en updateAttack().")
pdf.field("float armorPercent = 0",
    "Porcentaje de reduccion del dano recibido. Ej: 0.10 -> 10% de reduccion. "
    "Se aplica en takeDamage(): reduced = amount * max(0, 1 - armorPercent).")
pdf.field("float criticalChance = 0",
    "Probabilidad (0..1) de golpe critico (dano x2). Se evalua con Math.random() "
    "en updateAttack() antes de aplicar el dano.")
pdf.field("float regenPerSec = 0",
    "HP recuperados por segundo de forma pasiva. Se aplica en update() si "
    "currentHealth > 0 y currentHealth < maxHealth.")
pdf.field("boolean facingRight",
    "true si el personaje mira hacia la derecha. false invierte el sprite "
    "horizontalmente en draw() mediante Matrix.setScale(-1, 1).")
pdf.field("Player opponent",
    "Referencia al adversario. Usada en takeDamage() para calcular la direccion del "
    "knockback y en canHit() para detectar colision de hitboxes.")
pdf.field("boolean hasHitInCurrentAttack",
    "Evita multiples golpes en el mismo swing. Se resetea a false al iniciar y terminar "
    "un ataque en updateAttack().")
pdf.field("boolean justHitThisFrame",
    "true exactamente durante un frame cuando se produce un golpe. Juego.triggerHitFlash() "
    "lo lee para disparar el efecto visual de impacto.")

pdf.sub("Flags de animacion")
pdf.field("boolean hasJumpAnim, hasKickAnim, hasDeathAnim, hasHurtAnim",
    "Indican si el personaje tiene animacion propia para cada accion. Se detectan "
    "comprobando getFrameWidth() > 1 (la animacion fallback tiene 1px de ancho). "
    "Si no existe, currentFrame() usa animaciones alternativas.")

pdf.sub("Debug visual")
pdf.field("static boolean DEBUG_HITBOXES = true",
    "Si true, draw() superpone un rectangulo verde (hitbox de cuerpo) y uno rojo "
    "(zona de alcance del ataque) sobre cada personaje. Cambiar a false para release.")
pdf.field("Paint dbgBodyPaint (estatico, verde 0xFF00FF00, STROKE, grosor=4)",
    "Paint para dibujar el borde verde de la hitbox de cuerpo.")
pdf.field("Paint dbgAttackPaint (estatico, rojo 0xFFFF3333, STROKE, grosor=4)",
    "Paint para dibujar el borde rojo de la zona de alcance del ataque activo.")

pdf.sub("Metodos de carga")
pdf.method("Player(name, assets, folder, charPrefix, x, y, facingRight, worldHeight)",
    "Constructor. Configura hurtPaint con filtro PorterDuff rojo semi-transparente "
    "(0xAAFF0000, SRC_ATOP) para el efecto de parpadeo al recibir dano. "
    "Llama a loadAnimations().")
pdf.method("loadAnimations(AssetManager assets, String folder, String prefix, float worldHeight)",
    "Carga las 7 animaciones: idle(0.12s LOOP), run(0.10s LOOP), attack1(0.08s NORMAL), "
    "attack2(0.10s NORMAL), takehit(0.12s NORMAL), jump(0.10s LOOP), death(0.12s NORMAL). "
    "Detecta existencia comprobando frameWidth>1. "
    "kickVisualAnim = kickAnim si existe, o 'run' a 0.07s NORMAL como visual alternativo. "
    "Llama a calibrateScale().")
pdf.method("calibrateScale(String prefix)",
    "Ajusta los 6 parametros de escala/hitbox segun el personaje: "
    "subzero: scale=4, drawOffsetY=-636, hbWMult=0.105, hbHMult=0.490, hbOffsetX=-58, hbOffsetY=164. "
    "Calculo: frame 199x191px, contenido real en X=57..113 (centro=85), Y=41..159. "
    "Centro del frame=99.5 -> delta=(99.5-85)*4=58px canvas; vacios arriba=41*4=164px. "
    "goro: hasHurtAnim=false (animacion takehit tiene caida no deseada). "
    "liu_kang: valores por defecto (hbWMult=0.35, hbHMult=0.80).")

pdf.sub("Logica de actualizacion")
pdf.method("update(float delta)",
    "Gestiona la simulacion: si DEAD solo avanza deathTime y retorna. "
    "Si no: velocityY += GRAVITY*delta, y += velocityY*delta, clipa en groundY. "
    "Si HURT: avanza hurtTime, vuelve a IDLE si hurtAnim.isFinished(hurtTime). "
    "Aplica regeneracion si regenPerSec>0. Siempre avanza stateTime y jumpTime.")
pdf.method("updateAttack(float delta)",
    "Resetea justHitThisFrame. Si no ATTACKING retorna. Avanza attackTime. "
    "Calcula totalDuration segun AttackType (SPECIAL = punchDuration + kickDuration). "
    "Si attackTime >= totalDuration: fin del ataque, resetea estado. "
    "Si attackTime > hitTime y !hasHitInCurrentAttack y canHit(opponent): "
    "calcula dano (baseDmg * damageMultiplier, x2 si critico), aplica knockback, "
    "aplica lifesteal, marca hasHitInCurrentAttack=true y justHitThisFrame=true.")
pdf.method("move(float amount)",
    "Si no ATTACKING ni HURT: desplaza x en amount px y pone estado WALK (si esta en suelo).")
pdf.method("updateDirection(boolean r, boolean l)",
    "Actualiza facingRight segun entradas. Si ninguna: si WALK pone IDLE.")
pdf.method("attack(AttackType type)",
    "Si no ATTACKING ni HURT: SPECIAL requiere comboCharge>=100. "
    "Inicializa estado, tipo, attackTime=0, hasHitInCurrentAttack=false. "
    "SPECIAL consume comboCharge=0.")
pdf.method("takeDamage(float amount, float knockback)",
    "Calcula dano reducido por armadura, resta de currentHealth (minimo 0), "
    "pone estado HURT, resetea hurtTime=0. "
    "Knockback: pushDir = +1 si opponent esta a la izquierda, -1 si esta a la derecha. "
    "x += knockback * pushDir.")
pdf.method("jump()",
    "Si isGrounded() y no HURT: velocityY=JUMP_FORCE, jumpTime=0.")
pdf.method("forceIdle()",
    "Fuerza State.IDLE y resetea attackTime y hurtTime. Usado en resetCombat().")
pdf.method("startDeath()",
    "Pone State.DEAD, deathTime=0, velocityY=0. Llamado por Juego.triggerResult().")

pdf.sub("Hitbox y deteccion de colision")
pdf.method("getHitbox() -> RectF",
    "Calcula el rectangulo de cuerpo en coordenadas canvas: "
    "hbW = frameWidth * scale * hbWMult; hbH = frameHeight * scale * hbHMult. "
    "ox = facingRight ? hbOffsetX : -hbOffsetX  (inverso si mira a la izquierda). "
    "cx = x + ox. "
    "top = (worldHeight - y) + drawOffsetY + hbOffsetY. "
    "Retorna RectF(cx-hbW/2, top, cx+hbW/2, top+hbH).")
pdf.method("canHit(Player other) -> boolean",
    "Requiere other!=null y State.ATTACKING. "
    "Calcula reach segun tipo: KICK=400, SPECIAL=700, PUNCH=300. "
    "ax = facingRight ? body.right : body.left - reach. "
    "Retorna RectF.intersects(area, other.getHitbox()).")

pdf.sub("Dibujo")
pdf.method("draw(Canvas canvas)",
    "drawX = x - frameWidth*scale/2 + drawOffsetX. "
    "drawY = (worldHeight - y) + drawOffsetY. "
    "Si !facingRight: matrix.setScale(-1,1) + postTranslate(frameWidth,0) para espejo. "
    "matrix.postScale(scale,scale) + postTranslate(drawX, drawY). "
    "Dibuja con hurtPaint si State.HURT y (hurtTime*15)%2==0 (parpadeo cada 2 ticks de 15Hz). "
    "Si DEBUG_HITBOXES llama a drawDebugHitbox().")
pdf.method("getBodyCanvasY() -> float",
    "top + frameHeight * scale * 0.35f (35% del alto = nivel del torso). "
    "Usada por Juego.triggerHitFlash() como punto de impacto del efecto visual.")
pdf.method("currentFrame() -> Bitmap",
    "Selector de frame segun estado y timers: "
    "DEAD -> deathAnim(deathTime) o ultimo frame de hurtAnim si no hay death. "
    "HURT -> hurtAnim(hurtTime) o idle[0]. "
    "ATTACKING SPECIAL -> punchAnim primera mitad, kickVisualAnim segunda mitad. "
    "ATTACKING KICK -> kickVisualAnim. ATTACKING PUNCH -> punchAnim. "
    "En aire (!isGrounded) -> jumpAnim. WALK -> walkAnim. Default -> idleAnim.")

# ══════════════════════════════════════════════════════════════════════════════
# 6. SpriteAnimation
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.class_hdr("SpriteAnimation", "riberadeltajo.es.chaosarena2.entity")
pdf.desc(
    "Sistema de animacion por frames que sustituye TextureAtlas + Animation de libGDX. "
    "Carga frames PNG individuales desde assets/, los ordena alfabeticamente y los reproduce "
    "a velocidad configurable. La convencion de nomenclatura es: [personaje]_[accion]_NNN.png "
    "(ej: liu_kang_idle_001.png, liu_kang_idle_002.png, ...)."
)

pdf.sub("Enum")
pdf.enum_row("PlayMode: LOOP, NORMAL",
    "LOOP: el indice vuelve a 0 al superar el ultimo frame (modulo). "
    "NORMAL: se clipa en el ultimo frame e isFinished() devuelve true.")

pdf.sub("Campos")
pdf.field("Bitmap[] frames",
    "Array de Bitmaps ordenados por nombre de fichero. Todos los frames de la animacion.")
pdf.field("float frameDuration",
    "Segundos por frame. Ej: 0.08 -> 12.5 FPS. 0.12 -> ~8.3 FPS.")
pdf.field("PlayMode playMode",
    "Modo de reproduccion asignado en el constructor.")
pdf.field("float totalDuration = frameDuration * frames.length",
    "Duracion total de la animacion. Usada por isFinished() y Player.updateAttack() "
    "para saber cuando termina un ataque.")

pdf.sub("Metodos de instancia")
pdf.method("getKeyFrame(float stateTime) -> Bitmap",
    "LOOP: idx = (int)(stateTime / frameDuration) % frames.length. "
    "NORMAL: idx = Math.min((int)(stateTime / frameDuration), frames.length - 1).")
pdf.method("isFinished(float stateTime) -> boolean",
    "Solo tiene sentido en NORMAL. Devuelve stateTime >= totalDuration.")
pdf.method("getDuration() -> float",
    "Devuelve totalDuration.")
pdf.method("getFrameWidth() / getFrameHeight() -> int",
    "Dimensiones del primer frame. Devuelven 1 si el array esta vacio (fallback). "
    "Player.calibrateScale() los usa para los calculos de escala. "
    "Player.loadAnimations() los usa para detectar si la animacion existe (>1).")
pdf.method("recycle()",
    "Llama a Bitmap.recycle() en cada frame no reciclado. Libera memoria nativa.")

pdf.sub("Metodos estaticos")
pdf.method("load(AssetManager, folder, prefix, frameDuration, PlayMode) -> SpriteAnimation",
    "Lista assets.list(folder), filtra los que empiecen por 'prefix' y terminen en "
    ".png o .webp, ordena la lista, decodifica cada uno con inScaled=false. "
    "Si no encuentra frames devuelve fallback().")
pdf.method("fallback(float frameDuration, PlayMode mode) -> SpriteAnimation",
    "Crea un Bitmap 1x1 blanco (0xFFFFFFFF, ARGB_8888). "
    "Player detecta esta situacion comprobando getFrameWidth() == 1.")

# ══════════════════════════════════════════════════════════════════════════════
# 7. EnemyAI
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2.ai", 160, 60, 0)
pdf.class_hdr("EnemyAI", "riberadeltajo.es.chaosarena2.ai")
pdf.desc(
    "Controlador de IA del enemigo basado en maquina de estados finita de 5 estados. "
    "Toma decisiones basadas en la distancia al jugador, el cooldown de ataque y las "
    "acciones del jugador. Incluye comportamiento reactivo: si el jugador usa SPECIAL, "
    "la IA retrocede automaticamente. El tipo de ataque elegido depende de la distancia "
    "y del nivel de comboCharge del NPC."
)

pdf.sub("Enum")
pdf.enum_row("State: IDLE, CHASE, ATTACK, RETREAT, COOLDOWN",
    "IDLE: espera hasta que stateTimer expire y luego llama a decideNextState(). "
    "CHASE: se mueve hacia el objetivo hasta llegar a rango de ataque (<250px). "
    "ATTACK: ejecuta executeProAttack() y pasa a COOLDOWN. "
    "RETREAT: se aleja del objetivo durante stateTimer segundos. "
    "COOLDOWN: pausa post-ataque; puede transicionar a RETREAT aleatoriamente.")

pdf.sub("Campos")
pdf.field("Player npc",
    "El Player que controla la IA (player2 en Juego). La IA llama a npc.move() y npc.attack().")
pdf.field("Player target",
    "El Player objetivo (player1, el humano). La IA lee target.x y target.isAttacking().")
pdf.field("float stateTimer",
    "Tiempo restante en el estado actual en segundos. Se decrementa cada frame. "
    "Cuando llega a 0 se reevalua la logica del estado.")
pdf.field("float attackCooldownTimer",
    "Tiempo de espera entre ataques. Se fija a attackRate tras cada ataque. "
    "La IA no puede pasar a ATTACK mientras este > 0.")
pdf.field("float speed = 350",
    "Velocidad de movimiento del NPC en px/s. Se multiplica por delta en npc.move(). "
    "Configurable por RunManager y applyModeConfig() en Juego.")
pdf.field("float damage = 5",
    "Valor de referencia del dano. setDamage(v) tambien ajusta npc.damageMultiplier = v/10f "
    "para que el escalado sea consistente con el sistema de stats del Player.")
pdf.field("float attackRate = 1.8",
    "Segundos base de cooldown entre ataques. El delay real es attackRate + random(-0.2, +0.4) "
    "para dar variabilidad.")

pdf.sub("Metodos publicos")
pdf.method("update(float delta)",
    "Si alguno tiene currentHealth<=0: retorna. "
    "Calcula dist=target.x-npc.x y absDist. Decrementa timers. "
    "Actualiza npc.facingRight si no esta atacando. "
    "Si npc.isHurt(): IDLE, stateTimer=0.2s. "
    "Comportamiento reactivo: si target usa SPECIAL -> RETREAT 0.5s; "
    "si target ataca, dist<250 y cooldown activo -> 70% prob de RETREAT 0.4s. "
    "Switch sobre currentState con la logica de cada estado.")
pdf.method("setSpeed/setDamage/setAttackRate / getSpeed/getDamage/getAttackRate",
    "Getters y setters. setDamage actualiza tambien npc.damageMultiplier = v/10f.")

pdf.sub("Metodos privados")
pdf.method("decideNextState(float absDist)",
    "Transicion segun distancia: "
    "absDist > 350 -> CHASE (stateTimer=0.2..0.6s). "
    "absDist < 180 -> 30% RETREAT (0.2..0.4s), sino ATTACK si !cooldown, sino IDLE (0.1s). "
    "180..350 -> si !cooldown: 80% ATTACK, 20% CHASE; si cooldown: 40% RETREAT, 60% IDLE.")
pdf.method("executeProAttack(float distance)",
    "Elige tipo: SPECIAL si comboCharge>=MAX_COMBO_CHARGE; "
    "KICK si distance>200; sino PUNCH(60%) o KICK(40%). "
    "Llama a npc.attack(type). Fija stateTimer y attackCooldownTimer = "
    "attackRate + random en [-0.2, 0.4].")

# ══════════════════════════════════════════════════════════════════════════════
# 8. VirtualJoystick
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2.input", 0, 100, 100)
pdf.class_hdr("VirtualJoystick", "riberadeltajo.es.chaosarena2.input")
pdf.desc(
    "Joystick tactil dibujado en Canvas que sustituye el touchpad de libGDX Scene2D. "
    "Soporta multi-touch rastreando un unico puntero activo (trackingPointerId). "
    "Requiere una Matrix inversa para convertir coordenadas de pantalla a coordenadas mundo. "
    "Juego usa getKnobPercentX() con umbral +-0.2 para detectar movimiento horizontal."
)

pdf.sub("Campos")
pdf.field("float centerX, centerY",
    "Centro del joystick en coordenadas mundo. Fijado en (280, WORLD_H-280 = 800). "
    "No cambia durante la partida.")
pdf.field("float radius = 180",
    "Radio de la zona activa de la base en px mundo. El area de deteccion de toque "
    "tiene un margen de 1.3x (234px) para facilitar el uso con el pulgar.")
pdf.field("float knobRadius = radius * 0.70 = 126",
    "Radio con el que se dibuja la bola del joystick. La bola no puede alejarse mas "
    "de 'radius' del centro (clipping en updateKnob).")
pdf.field("float knobX, knobY",
    "Posicion actual del centro de la bola. En reposo = (centerX, centerY). "
    "Se actualiza cada frame de ACTION_MOVE.")
pdf.field("int trackingPointerId",
    "-1 si libre. ID del puntero que controla el joystick si esta activo. "
    "Permite ignorar otros dedos en la pantalla.")
pdf.field("Matrix inverseMatrix",
    "Inversa de worldMatrix. Inyectada por Juego.updateScaleMatrix(). "
    "Convierte puntos de toque de px pantalla a coordenadas mundo antes de comparar.")

pdf.sub("Metodos")
pdf.method("onTouchEvent(MotionEvent e) -> boolean",
    "ACTION_DOWN/POINTER_DOWN: si el toque cae en el area de la base (radio*1.3) y "
    "trackingPointerId==-1, registra el puntero y llama a updateKnob(). "
    "ACTION_MOVE: si hay puntero activo, busca su indice actual y actualiza la bola. "
    "ACTION_UP/POINTER_UP/CANCEL: si es el puntero activo, lo libera y centra la bola.")
pdf.method("updateKnob(float tx, float ty)",
    "Calcula dx=tx-centerX, dy=ty-centerY. "
    "Si sqrt(dx^2+dy^2) > radius: normaliza (dx,dy) al radio (clipping circular). "
    "knobX = centerX + dx; knobY = centerY + dy.")
pdf.method("getKnobPercentX() -> float",
    "(knobX - centerX) / radius. Rango exacto [-1.0, +1.0]. "
    "Juego lee: si > 0.2 mueve derecha, si < -0.2 mueve izquierda.")
pdf.method("getKnobPercentY() -> float",
    "(knobY - centerY) / radius. No se usa actualmente en la logica de combate.")
pdf.method("isActive() -> boolean",
    "trackingPointerId != -1. Indica si hay un dedo activo en el joystick.")
pdf.method("draw(Canvas canvas)",
    "Dibuja bgBitmap en RectF(center-radius, center+radius) y "
    "knobBitmap en RectF(knob-knobRadius, knob+knobRadius) con FILTER_BITMAP_FLAG.")

# ══════════════════════════════════════════════════════════════════════════════
# 9. StageDef
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2.stage", 100, 60, 0)
pdf.class_hdr("StageDef", "riberadeltajo.es.chaosarena2.stage")
pdf.desc(
    "Contenedor de datos de un escenario de combate. Encapsula el fondo visual, los parametros "
    "de posicion del suelo y un mapa opcional de offsets Y por personaje para ajuste fino. "
    "Se instancian 3 escenarios en ResourceManager y se selecciona uno en la pantalla de "
    "seleccion de personaje."
)

pdf.sub("Campos publicos")
pdf.field("String name",
    "Nombre del escenario mostrado en la pantalla CHAR_SELECT. "
    "Valores: 'Bosque', 'Desierto', 'Ciudad'.")
pdf.field("Bitmap bgBitmap",
    "Imagen de fondo del escenario cargada desde assets/backgrounds/. "
    "Se dibuja en drawFighting() con desplazamiento parallax horizontal.")
pdf.field("float floorVisualOffset",
    "Offset visual del suelo respecto a la base de la imagen. Permite ajustar "
    "visualmente donde parece estar el suelo en el arte. "
    "Bosque=-40, Desierto=0, Ciudad=+40.")
pdf.field("float groundY",
    "Coordenada Y mundo donde se apoyan los personajes. Bosque=80, Desierto=60, Ciudad=90. "
    "Se pasa al constructor de Player como parametro 'y'.")

pdf.sub("Metodos")
pdf.method("StageDef(name, bgBitmap, floorVisualOffset, groundY)",
    "Constructor que asigna los cuatro campos publicos.")
pdf.method("addOffset(String charName, float offset) -> StageDef",
    "Anade un offset de Y en el mapa charYOffsets para un personaje especifico. "
    "Retorna this para encadenado de llamadas. "
    "No se usa actualmente en la creacion de escenarios en ResourceManager.")
pdf.method("getOffsetFor(String charName) -> float",
    "Busca el offset en charYOffsets. Si no existe devuelve 0f. "
    "Juego lo usa en startFight(): player.y = groundY + stage.getOffsetFor(playerName).")
pdf.method("recycle()",
    "Llama a bgBitmap.recycle() si el Bitmap no esta ya reciclado. "
    "Llamado desde ResourceManager.recycle() en el onDestroy de la actividad.")

# ══════════════════════════════════════════════════════════════════════════════
# 10-12. Progression
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.pkg_banner("riberadeltajo.es.chaosarena2.progression", 80, 0, 120)

pdf.class_hdr("Buff", "riberadeltajo.es.chaosarena2.progression")
pdf.desc(
    "Representa una mejora permanente para el jugador en modo Arcade. Encapsula tipo, valor, "
    "permanencia y rareza. Se generan en grupos de 3 por RunManager y se ofrecen al jugador; "
    "los no elegidos se aplican al enemigo siguiente."
)

pdf.sub("Enum")
pdf.enum_row("Rarity: COMMON, RARE, EPIC",
    "COMMON (prob 40%): mult=1.0. RARE (prob 30%): mult=1.5. EPIC (prob 10%): mult=2.5. "
    "La rareza determina el color del boton en BUFF_SELECT: EPIC=naranja oscuro, "
    "RARE=cyan oscuro, COMMON=gris.")

pdf.sub("Campos y metodo")
pdf.field("BuffType type",
    "Tipo de stat que mejora (ver BuffType).")
pdf.field("float value",
    "Valor del efecto ya multiplicado por rareza. "
    "DAMAGE: 0.15/0.225/0.375. SPEED: 0.10/0.15/0.25. HEALTH: 30/45/75. "
    "LIFESTEAL: 0.03/0.045/0.075. ARMOR: 0.05/0.075/0.125. "
    "CRITICAL: 0.10/0.15/0.25. REGEN: 2/3/5 hp/s.")
pdf.field("boolean isPermanent",
    "Siempre true. Reservado para posibles buffs temporales en versiones futuras.")
pdf.field("Rarity rarity",
    "Rareza del buff. Determina color de fondo del boton de seleccion.")
pdf.method("getDescription() -> String",
    "Retorna '[RARITY] +NombreStat'. Ej: '[EPIC] +Dano', '[COMMON] +Regen. Vida'.")

pdf.hr()
pdf.class_hdr("BuffType", "riberadeltajo.es.chaosarena2.progression", "enum")
pdf.desc("Enumera los 7 stats mejorables. Usado como llave en los switch de RunManager y Buff.")
pdf.enum_row("DAMAGE",    "Aumenta Player.damageMultiplier.")
pdf.enum_row("SPEED",     "Aumenta Player.speedMultiplier.")
pdf.enum_row("HEALTH",    "Aumenta Player.maxHealth y currentHealth.")
pdf.enum_row("LIFESTEAL", "Aumenta Player.lifestealPercent.")
pdf.enum_row("ARMOR",     "Aumenta Player.armorPercent (reduccion de dano recibido).")
pdf.enum_row("CRITICAL",  "Aumenta Player.criticalChance (probabilidad de golpe x2).")
pdf.enum_row("REGEN",     "Aumenta Player.regenPerSec (regeneracion pasiva de vida).")

pdf.hr()
pdf.class_hdr("Event", "riberadeltajo.es.chaosarena2.progression")
pdf.desc(
    "Evento especial que ocurre cada 3 victorias en Arcade (shouldTriggerEvent). "
    "Algunos benefician al jugador (HEAL) y otros penalizan o modifican el siguiente combate."
)

pdf.sub("Enum")
pdf.enum_row("EventType: HEAL, HARDER_COMBAT, GLOBAL_MODIFIER, POISON, ENEMY_HEAL",
    "HEAL: currentHealth += maxHealth*0.3 (clampeado a maxHealth). "
    "HARDER_COMBAT: RunManager.harderCombatNext=true (enemigo +50% vida/dano/velocidad). "
    "GLOBAL_MODIFIER: globalDamageMultiplier=1.5 (dano de ambos x1.5 en la proxima pelea). "
    "POISON: currentHealth -= maxHealth*0.2 (minimo 1). "
    "ENEMY_HEAL: enemyHealNext=true (proximo enemigo +100 vida maxima).")

pdf.sub("Campos y metodo")
pdf.field("EventType type",
    "Tipo del evento. Determina que efecto aplica apply().")
pdf.field("String description",
    "Texto mostrado al jugador en la pantalla EVENT antes de pulsar continuar.")
pdf.method("apply(RunManager runManager)",
    "Ejecuta el efecto: HEAL y POISON modifican currentHealth directamente via "
    "runManager.getPlayer(). El resto llaman a setters de RunManager.")

# ══════════════════════════════════════════════════════════════════════════════
# 13. RunManager
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.class_hdr("RunManager", "riberadeltajo.es.chaosarena2.progression")
pdf.desc(
    "Gestor de estado de una partida completa en modo Arcade. Es la unica fuente de verdad "
    "sobre el estado de la 'run': buffs activos, modificadores del proximo combate y "
    "escalado progresivo de enemigos. Instanciado por Juego al inicio de cada partida Arcade "
    "y destruido implicitamente al volver al menu principal."
)

pdf.sub("Constantes y campos")
pdf.field("static final int MAX_BUFF_SLOTS = 6",
    "Limite de buffs acumulables. applyBuffToPlayer() ignora buffs adicionales si se supera.")
pdf.field("Player player",
    "Referencia al jugador. Puede actualizarse con setPlayer() si el objeto se recrea. "
    "reapplyBuffs() re-aplica todos los buffs activos al nuevo objeto.")
pdf.field("int fightsWon",
    "Contador de combates ganados en la run. Determina el escalado progresivo: "
    ">=3 -> 1 buff extra al enemigo; >=6 -> 2 buffs extra al enemigo.")
pdf.field("List<Buff> activeBuffs",
    "Buffs elegidos por el jugador. Maximo MAX_BUFF_SLOTS=6.")
pdf.field("boolean harderCombatNext",
    "Si true: proximo enemigo tiene vida/dano x1.5 y velocidad x1.2. "
    "Se resetea en onFightWon().")
pdf.field("boolean enemyHealNext",
    "Si true: proximo enemigo recibe +100 vida maxima adicional. Se resetea en onFightWon().")
pdf.field("float globalDamageMultiplier",
    "Multiplicador de dano aplicado a ambos jugadores el proximo combate. "
    "Normalmente 1.0; sube a 1.5 con evento GLOBAL_MODIFIER. Se resetea en onFightWon().")
pdf.field("List<Buff> pendingEnemyBuffs",
    "Buffs que el jugador rechazo en BUFF_SELECT. Se aplican al siguiente enemigo "
    "en applyEnemyScaling() y se limpian.")

pdf.sub("Metodos de progresion")
pdf.method("onFightWon()",
    "fightsWon++. Resetea harderCombatNext=false, enemyHealNext=false, "
    "globalDamageMultiplier=1.0. Llamado en Juego.showResult() tras victoria Arcade.")
pdf.method("shouldTriggerEvent() -> boolean",
    "fightsWon > 0 && fightsWon % 3 == 0. Cada 3 victorias hay evento especial.")
pdf.method("generateRandomEvent() -> Event",
    "r = random(0..4). Selecciona uno de los 5 EventType con probabilidad uniforme.")

pdf.sub("Gestion de buffs del jugador")
pdf.method("getRandomBuffChoices() -> List<Buff>",
    "Genera 3 Buff. Para cada uno: BuffType aleatorio. "
    "r > 0.9 -> EPIC (mult=2.5); r > 0.6 -> RARE (mult=1.5); sino COMMON (mult=1.0). "
    "Valor base por tipo multiplicado por mult de rareza.")
pdf.method("applyBuffToPlayer(Buff buff)",
    "Si activeBuffs.size() < MAX_BUFF_SLOTS: anade a la lista y llama a applyBuffDirectly().")
pdf.method("addPendingEnemyBuff(Buff buff)",
    "Anade el buff rechazado a pendingEnemyBuffs. Llamado por Juego.handleTap() "
    "para los buffs no elegidos.")

pdf.sub("Escalado de enemigos")
pdf.method("applyEnemyScaling(EnemyAI enemyAI, Player enemyPlayer)",
    "Aplica en orden: (1) harderCombatNext: vida/dano*1.5, velocidad*1.2; "
    "(2) enemyHealNext: maxHealth+=100, currentHealth=maxHealth; "
    "(3) globalDamageMultiplier al dano y damageMultiplier del enemy; "
    "(4) pendingEnemyBuffs -> applyEnemyBuff() para cada uno, luego clear(); "
    "(5) escalado progresivo: fightsWon>=3 -> 1 buff random; fightsWon>=6 -> 2 buffs random.")
pdf.method("applyEnemyBuff(EnemyAI, Player, BuffType)",
    "Aplica buff con valores fijos de enemigo: "
    "DAMAGE: damageMultiplier+=0.2. SPEED: speed*1.15. HEALTH: maxHealth+=50. "
    "LIFESTEAL: lifestealPercent+=0.05. ARMOR: armorPercent+=0.10. "
    "CRITICAL: criticalChance+=0.15. REGEN: regenPerSec+=2.")

# ══════════════════════════════════════════════════════════════════════════════
# Pagina final: dependencias y flujo
# ══════════════════════════════════════════════════════════════════════════════
pdf.add_page()
pdf.h1("Dependencias entre clases")
deps = [
    ("ActividadJuego",        "-> engine.Juego, engine.ResourceManager"),
    ("engine.Juego",          "-> engine.BucleJuego, engine.ResourceManager, entity.Player,"),
    ("",                      "   ai.EnemyAI, input.VirtualJoystick, stage.StageDef,"),
    ("",                      "   progression.Buff, progression.Event, progression.RunManager"),
    ("engine.ResourceManager","-> stage.StageDef"),
    ("entity.Player",         "-> entity.SpriteAnimation"),
    ("ai.EnemyAI",            "-> entity.Player"),
    ("progression.Event",     "-> entity.Player, progression.RunManager (mismo paquete)"),
    ("progression.RunManager","-> entity.Player, ai.EnemyAI, progression.Buff, progression.BuffType"),
    ("input.VirtualJoystick", "-> (solo Android SDK)"),
    ("stage.StageDef",        "-> (solo Android SDK)"),
    ("entity.SpriteAnimation","-> (solo Android SDK)"),
    ("progression.Buff",      "-> progression.BuffType"),
    ("progression.BuffType",  "-> (enum, sin dependencias)"),
]
for cls, dep in deps:
    pdf.set_x(LM)
    pdf.set_font("Helvetica", "B" if cls else "", 9.5)
    pdf.cell(48, 6, cls)
    pdf.set_font("Courier", "", 8.5)
    pdf.set_text_color(50, 50, 50)
    pdf.multi_cell(0, 6, dep)
    pdf.set_text_color(0, 0, 0)
pdf.ln(6)

pdf.h1("Flujo de ejecucion - Modo Arcade")
steps = [
    "1. ActividadJuego.onCreate",
    "   -> new ResourceManager(context): carga assets, fuentes, escenarios",
    "   -> new Juego(activity, res, prefs): crea VirtualJoystick, inicializa botones",
    "   -> setContentView(juego): la SurfaceView ocupa toda la pantalla",
    "",
    "2. Juego.surfaceCreated: calcula worldMatrix, new BucleJuego.start(), startMusic()",
    "",
    "3. BucleJuego.run() a 60 FPS:",
    "   -> Juego.actualizar(delta=0.01667s) -> updateFighting(delta)",
    "   -> Juego.renderizar(canvas) -> drawFighting(canvas)",
    "",
    "4. Usuario toca 'ARCADE' -> currentScreen=CHAR_SELECT",
    "   -> Elige personaje+escenario -> Juego.startFight()",
    "      new Player(jugador), new Player(enemigo), new EnemyAI, new RunManager",
    "      currentScreen=FIGHTING",
    "",
    "5. Combat loop (updateFighting cada frame):",
    "   joystick.getKnobPercentX() -> player1.move() si |x| > 0.2",
    "   player1.update/updateAttack(delta) | player2.update/updateAttack(delta)",
    "   enemyAI.update(delta): maquina estados -> npc.move() o npc.attack()",
    "   Si justHitThisFrame: triggerHitFlash (circulo amarillo 100ms)",
    "   Si currentHealth<=0: triggerResult(won)",
    "",
    "6. triggerResult: pendingResult=true, startDeath/forceIdle",
    "   updateFighting acumula deathDelay hasta 2.5s -> showResult()",
    "   showResult: currentScreen=RESULT, runManager.onFightWon(), enemiesDefeated++",
    "",
    "7. 'ELEGIR MEJORA' -> BUFF_SELECT: runManager.getRandomBuffChoices() -> 3 Buff",
    "   Jugador elige: runManager.applyBuffToPlayer(elegido)",
    "   Rechazados: runManager.addPendingEnemyBuff(rechazado) x2",
    "   checkForEvent: fightsWon%3==0 -> EVENT screen, Event.apply(runManager)",
    "   resetCombat: spawnNewEnemy + runManager.applyEnemyScaling(enemyAI, player2)",
    "",
    "8. Derrota (currentHealth<=0) -> saveArcadeScore() -> top-5 en SharedPreferences",
    "   currentScreen=MAIN_MENU",
]
for s in steps:
    indent = 0
    if s.startswith("   "):
        indent = 8
    pdf.set_x(LM + indent)
    pdf.set_font("Helvetica", "", 9.5 if s else 6)
    pdf.set_text_color(30, 30, 30)
    if s:
        pdf.multi_cell(TW - indent, 5.5, s)
    else:
        pdf.ln(3)
pdf.set_text_color(0, 0, 0)

out = r"C:\Users\alvar\AndroidStudioProjects\ChaosArena2\ChaosArena2_doc.pdf"
pdf.output(out)
print("PDF generado:", out)
