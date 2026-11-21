package dev.qumara.combat.attack;

import dev.qumara.combat.Scale;

import java.util.function.Supplier;

/**
 * Catalogo de ataques con su logica de seleccion (la IA elige segun estos pesos). La distancia se normaliza por Scale.K
 * (los rangos estan pensados para el tamaño base). phase: 0 normal, 1 furia (<50 %), 2 ira (<25 %).
 */
public enum AttackType {
    SLAM(SlamAttack::new, 60) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 3.2 || n > 9.5 ? 0 : (n < 7 ? 6 : 4); }
    },
    SWEEP(SweepAttack::new, 80) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 2.2 || n > 7.2 ? 0 : 5 + phase; }
    },
    TONGUE(TongueAttack::new, 100) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 3.5 || n > 9.5 ? 0 : 5; }
    },
    ROOTS(RootsAttack::new, 160) {
        public int weight(double d, int players, int phase) { return 3 + Math.min(players - 1, 2) + (d / Scale.K > 12 ? 1 : 0); }
    },
    GARDEN(GardenAttack::new, 520) {
        public int weight(double d, int players, int phase) { return d / Scale.K > 9 ? 5 : 2 + Math.min(players - 1, 2); }
    },
    VINE(VineAttack::new, 120) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n > 12 ? 6 : (n > 6 ? 4 : 2); }
    },
    SLAM_BOTH(SlamBothAttack::new, 220) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 3 || n > 9 ? 0 : 3 + phase; }
    },
    ROOT_WAVE(RootWaveAttack::new, 200) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 3 || n > 14 ? 0 : 4; }
    },
    CYCLONE(CycloneAttack::new, 360) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return (phase < 1 && players < 6) || n > 8 ? 0 : 5 + phase; }
    },
    DEVOUR(DevourAttack::new, 420) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 2 || n > 14 ? 0 : 3 + (n > 9 ? 2 : 0); }
    },
    THORN_RAIN(ThornRainAttack::new, 300) {
        public int weight(double d, int players, int phase) { return 3 + Math.min(players - 1, 2) + phase; }
    },
    TONGUE_FAN(TongueFanAttack::new, 260) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return phase < 1 || n < 4 || n > 9.5 ? 0 : 5; }
    },
    ROAR(RoarAttack::new, 400) {
        public int weight(double d, int players, int phase) { double n = d / Scale.K; return n < 9 ? 3 : 1; }
    };

    private final Supplier<BossAttack> factory;
    public final int cooldownTicks;
    AttackType(Supplier<BossAttack> f, int cd) { this.factory = f; this.cooldownTicks = cd; }
    public BossAttack create() { return factory.get(); }
    /** 0 = no usable a esta distancia / fase. */
    public abstract int weight(double dist, int players, int phase);

    /** Ataques que cubren area alrededor del jefe: se priorizan cuando hay mucha gente a su espalda. */
    public boolean area() { return this == ROOTS || this == GARDEN || this == THORN_RAIN || this == CYCLONE || this == ROAR || this == ROOT_WAVE; }
}
