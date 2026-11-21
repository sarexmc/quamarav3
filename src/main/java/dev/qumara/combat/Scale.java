package dev.qumara.combat;

/**
 * Factor de tamaño respecto a la primera version (render 0.5 -> 0.75). TODAS las distancias del combate (alcance de
 * brazos, lengua, radios de zonas...) se multiplican por K para que las zonas rojas sigan coincidiendo con el modelo.
 */
public final class Scale {
    public static final double K = 1.5;
    public static final float KF = 1.5f;
    public static final float RENDER = 0.75f;      // QumaraRenderer.SCALE
    private Scale() {}
}
