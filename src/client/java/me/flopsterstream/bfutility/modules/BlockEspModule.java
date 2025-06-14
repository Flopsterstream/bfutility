package me.flopsterstream.bfutility.modules;




import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Block;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.registry.Registries;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.*;



public class BlockEspModule extends Module {


    private final MinecraftClient client = MinecraftClient.getInstance();
    private int range = 100; // Range to search for blocks
    private Block targetBlock = Blocks.EMERALD_BLOCK; // Default target block
    private String testInput = ""; // Variable to store the test input value
    private final Map<String, Integer> sliderValues = new HashMap<>();

    private final List<BlockPos> highlightedBlocks = new CopyOnWriteArrayList<>();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();







    public BlockEspModule() {
        super("BlockESP", Category.RENDER, "Highlights specific blocks in the world.");
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> onRender(context.matrixStack()));


        addOption("Highlight Range", OptionType.SLIDER);
        addOption("Block (like diamond_block): ", OptionType.INPUT);
    }


    public void setTargetBlock(Block block) {
        this.targetBlock = block;
    }






    private void setTargetBlockByName(String blockName) {
        String normalized = blockName.trim().toLowerCase().replace(' ', '_');
        Identifier id = Identifier.of("minecraft", normalized);
        Block block = Registries.BLOCK.get(id);




        if (block != Blocks.AIR) {
            this.targetBlock = block;

        }


    }






    private void onRender(MatrixStack matrices) {
        if (!this.isEnabled() || client.world == null || client.player == null) return;

        for (BlockPos pos : highlightedBlocks) {
            renderBox(matrices, new Box(pos), 0f, 1f, 0f, 1f);
        }
    }



    public static void renderBox(MatrixStack matrices, Box box, float red, float green, float blue, float alpha) {
        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        Box shiftedBox = box.offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);


        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getLines());// Use a layer that ignores depth


        GL11.glDisable(GL11.GL_DEPTH_TEST);


        MatrixStack.Entry entry = matrices.peek();
        float minX = (float) shiftedBox.minX;
        float minY = (float) shiftedBox.minY;
        float minZ = (float) shiftedBox.minZ;
        float maxX = (float) shiftedBox.maxX;
        float maxY = (float) shiftedBox.maxY;
        float maxZ = (float) shiftedBox.maxZ;


        // Draw edges of the box
        vertexConsumer.vertex(entry.getPositionMatrix(), minX, minY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, minY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, minY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, maxY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, maxY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), minX, maxY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), minX, maxY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), minX, minY, minZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), minX, minY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, minY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, minY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, maxY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), maxX, maxY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), minX, maxY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);


        vertexConsumer.vertex(entry.getPositionMatrix(), minX, maxY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);
        vertexConsumer.vertex(entry.getPositionMatrix(), minX, minY, maxZ).color(red, green, blue, alpha).normal(0, 0, 0);


        immediate.draw();


        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void scanBlocks() {
        if (client.player == null || client.world == null) return;

        World world = client.world;
        BlockPos playerPos = client.player.getBlockPos();

        List<BlockPos> found = new ArrayList<>();

        for (BlockPos blockPos : BlockPos.iterate(playerPos.add(-range, -range, -range), playerPos.add(range, range, range))) {
            if (world.getBlockState(blockPos).isOf(targetBlock)) {
                found.add(blockPos.toImmutable()); // use immutable for thread safety
            }
        }

        highlightedBlocks.clear();           // replace previous data
        highlightedBlocks.addAll(found);     // update with new data
    }





    @Override
    public void onEnable() {
        executor.scheduleAtFixedRate(this::scanBlocks, 0, 1, TimeUnit.SECONDS); // scan every second
    }



    @Override
    public void onDisable() {
        executor.shutdownNow();
        highlightedBlocks.clear(); // avoid rendering outdated results
    }



    @Override
    public void onTick() {


    }






    @Override
    public void onOptionValueChanged(String optionName, Object value) {





        switch (optionName) {
            case "Highlight Range" -> {
                if (value instanceof Integer intValue) {
                    this.range = intValue;

                }


            }
            case "Block (like diamond_block): " -> {
                if (value instanceof String strValue) {
                    this.testInput = strValue;
                    setTargetBlockByName(strValue);
                }


            }


        }
    }
}

