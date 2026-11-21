package dev.qumara.client.render;

import dev.qumara.entity.QumaraEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QumaraRenderer extends GeoEntityRenderer<QumaraEntity> {
    public static final float SCALE = 0.75f;    // = 0.5 * Scale.K: las zonas de dano estan calculadas con esta escala

    public QumaraRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new QumaraModel());
        withScale(SCALE);
        this.shadowRadius = 2.5f;
    }

    @Override
    protected float getDeathMaxRotation(QumaraEntity animatable) { return 0f; }    // la muerte es una animacion, no un volcado
}
