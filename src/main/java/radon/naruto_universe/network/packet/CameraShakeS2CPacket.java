package radon.naruto_universe.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import radon.naruto_universe.capability.data.NarutoDataHandler;
import radon.naruto_universe.client.CameraShakeHandler;

import java.util.function.Supplier;

public class CameraShakeS2CPacket {
    private final float intensity;
    private final float speed;
    private final int duration;

    public CameraShakeS2CPacket(float intensity, float speed, int duration) {
        this.intensity = intensity;
        this.speed = speed;
        this.duration = duration;
    }

    public CameraShakeS2CPacket(FriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readFloat(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(this.intensity);
        buf.writeFloat(this.speed);
        buf.writeInt(this.duration);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CameraShakeHandler.shakeCamera(this.intensity, this.speed, this.duration)));

        ctx.setPacketHandled(true);
    }
}