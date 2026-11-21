package dev.qumara.client.render;

import dev.qumara.QumaraMod;
import dev.qumara.entity.QumaraEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/** Modelo GeckoLib: geo/entity/qumara.geo.json + animations/entity/qumara.animation.json. Cambia de textura en furia. */
public class QumaraModel extends DefaultedEntityGeoModel<QumaraEntity> {
    private static final Identifier NORMAL = QumaraMod.id("textures/entity/qumara.png");
    private static final Identifier FURY = QumaraMod.id("textures/entity/qumara_fury.png");

    public QumaraModel() { super(QumaraMod.id("qumara")); }

    @Override
    public Identifier getTextureResource(QumaraEntity animatable) {
        return animatable.fury() ? FURY : NORMAL;
    }
}
