package me.flopsterstream.bfutility.mixin.client;

import me.flopsterstream.bfutility.BFUtilityClient;
import me.flopsterstream.bfutility.modules.Module;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class SimpleOptionMixin {
//this thing makes me put the britness higher for the fullbright thing
    @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("RETURN"), cancellable = true)
    public void removeValidation(Double double_, CallbackInfoReturnable<Optional<Double>> cir) {
        Module fullbright = BFUtilityClient.getModuleManager().getModuleByName("Fullbright");
        if (fullbright != null && fullbright.isEnabled()) {
            if (double_ == 1500.0) {
                cir.setReturnValue(Optional.of(1500.0));
            }
        }
    }
}
