package dev.qumara.registry;

import dev.qumara.QumaraMod;
import dev.qumara.entity.QumaraEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModEntities {
    public static final EntityType<QumaraEntity> QUMARA = Registry.register(
            Registries.ENTITY_TYPE, QumaraMod.id("qumara"),
            EntityType.Builder.create(QumaraEntity::new, SpawnGroup.MONSTER)
                    .dimensions(4.8f, 12.0f)
                    .maxTrackingRange(20)
                    .trackingTickInterval(1)
                    .makeFireImmune()
                    .build("qumara"));

    public static void init() {
        FabricDefaultAttributeRegistry.register(QUMARA, QumaraEntity.createAttributes());
    }

    private ModEntities() {}
}
