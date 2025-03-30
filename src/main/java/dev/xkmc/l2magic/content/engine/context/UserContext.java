package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public record UserContext(Level level, LivingEntity user, Holder<SpellAction> root, @Nullable Scheduler scheduler) {

}
