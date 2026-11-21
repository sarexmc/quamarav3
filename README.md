# QUMARA — jefe cinemático (Minecraft 1.21.1 · Fabric · GeckoLib)

Versiones verificadas: Minecraft 1.21.1, Fabric Loader 0.16.14, Fabric API 0.116.10+1.21.1, GeckoLib 4.7.3
(`geckolib-fabric-1.21.1`), Fabric Loom 1.9.2, Yarn 1.21.1+build.3, Java 21, Gradle 8.11+.

## Compilar
```
gradle wrapper --gradle-version 8.11.1     # una sola vez (el .jar del wrapper no se puede incluir)
./gradlew build                            # el jar queda en build/libs/qumara-1.0.0.jar
./gradlew runClient                        # cliente de pruebas
./gradlew runServer                        # servidor de pruebas
```
En el juego: huevo generador de Qumara (pestaña de huevos) o `/summon qumara:qumara ~ ~ ~`.
Requiere GeckoLib y Fabric API en la carpeta `mods` (cliente y servidor).

## Estructura
- `src/main/java/dev/qumara/`
  - `entity/` QumaraEntity (maquina de estados SPAWN, IDLE, ATTACK, STUN, FURY, DEAD, BossBar, GeckoLib)
  - `ai/` QumaraBrain (objetivo, pesos por distancia, cooldowns, anti-repeticion)
  - `combat/` DangerShape/DangerZone (geometria compartida servidor-cliente), DangerZoneManager, Shockwave, RootedManager
  - `combat/attack/` un archivo por ataque + AttackType (catalogo)
  - `world/` ArenaTransformer (musgo/obstaculos con restauracion), PlantManager (BlockDisplay animados)
  - `vfx/` Vfx (particulas por ataque) · `net/` payloads · `registry/` entidad, sonidos, items
- `src/client/java/dev/qumara/client/` renderer GeckoLib, indicadores rojos (`fx/`), BossBar (`hud/`), mixin
- `tools/build_rig.py` genera huesos + animaciones · `tools/make_textures.py` textura de furia y BossBar

## Flujo de modelo y animaciones (importante)
1. `tools/out/kumara_rig.bbmodel` es tu modelo reorganizado (41 huesos) con las 21 animaciones. Abrelo en Blockbench.
2. Para la referencia exacta: File > Export > Export Bedrock Geometry y Export Animations, y sustituye
   `assets/qumara/geo/entity/qumara.geo.json` y `assets/qumara/animations/entity/qumara.animation.json`.
   (El proyecto ya incluye versiones generadas por script; si en el juego ves caras con la textura invertida o rotaciones
   al reves, usa el export de Blockbench.)
3. Para cambiar poses: edita `tools/build_rig.py` y ejecuta `python3 tools/build_rig.py` (genera de nuevo los tres archivos).

## Sonidos
Los 13 eventos (`ModSounds`) apuntan en `sounds.json` a sonidos vanilla con pitch/volumen distintos. Para usar los tuyos:
pon `.ogg` en `assets/qumara/sounds/` y cambia las entradas por `{"name": "qumara:nombre"}`.

## Ajustes rapidos
- Vida: `QumaraEntity.MAX_HEALTH` · tamaño: `QumaraRenderer.SCALE` (las zonas estan calculadas para 0.5)
- Daños y tiempos: constantes al inicio de cada clase en `combat/attack/`
- Umbral de furia: `getMaxHealth() * 0.5f` en `QumaraEntity.idleTick`

## Ataques (13) y fases
Fase 0 normal · fase 1 FURIA (<50 % vida, ataques x1.25 de velocidad, textura roja) · fase 2 IRA (<25 %, x1.45, tres golpes de transicion).
Raices atrapadoras · Lengua depredadora (+ Agarre encadenado) · Brazo aplastante · Ataque arrastrado · Jardin maldito ·
Enredadera cazadora · Martillo doble · Grieta de raices (abanico de 3/5/7 olas) · Ciclon (2 vueltas, anillo rojo) ·
Devorar (succion + mordisco en cono) · Lluvia de espinas (5 andanadas + veneno) · Abanico de lengua (3 latigazos, furia+) ·
Rugido ensordecedor (onda de choque saltable + lentitud). Cada uno con su zona roja.
Tamaño: render 0.75 (hitbox 4.8 x 12). Todas las distancias usan `combat/Scale.K` (=1.5).
Vida 1500, dano base x1.4 (x1.2 furia, x1.45 ira), tope de dano por golpe 3 % de la vida.

## Multitud (hasta ~100 jugadores)
- Vida base 3000 (con 1 jugador). El dano recibido se divide por participantes^0.75 (`SCALING_EXPONENT` en QumaraEntity).
- Sin frames de invulnerabilidad: cada golpe de cada jugador cuenta. Tope por golpe: 2 % de la vida maxima.
- Arena de 60 bloques; objetivos rotan; raices/jardin/espinas/enredaderas escalan con el numero de jugadores (con tope).
- Rendimiento: densidad de particulas adaptativa, tope de 150 brotes simultaneos, zonas reenviadas cada 2 s, cache de alturas en el cliente.
- Si hay muchos jugadores a la espalda del jefe, prioriza ataques de area (ciclon, rugido, ola de raices, espinas, jardin).
