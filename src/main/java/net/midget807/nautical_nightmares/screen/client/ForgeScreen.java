package net.midget807.nautical_nightmares.screen.client;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.screen.ForgeScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class ForgeScreen extends HandledScreen<ForgeScreenHandler> {
    private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.ofVanilla("container/blast_furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("container/blast_furnace/burn_progress");
    private static final Identifier TEXTURE = NauticalNightmaresMain.id("textures/gui/container/forge.png");

    private static final Identifier EMPTY_COAL_TEXTURE = NauticalNightmaresMain.id("textures/item/empty_coal.png");
    private static final Identifier EMPTY_CHARCOAL_TEXTURE = NauticalNightmaresMain.id("textures/item/empty_charcoal.png");
    private static final Identifier EMPTY_LAVA_TEXTURE = NauticalNightmaresMain.id("textures/item/empty_lava.png");
    private static final Identifier EMPTY_BLAZE_ROD_TEXTURE = NauticalNightmaresMain.id("textures/item/empty_blaze_rod.png");
    private static final Identifier EMPTY_BARITE_SHARD_TEXTURE = Identifier.ofVanilla("item/empty_slot_amethyst_shard");
    private static final Identifier EMPTY_QUARTZ_TEXTURE = Identifier.ofVanilla("item/empty_slot_quartz");
    private final CyclingSlotIcon fuelSlotIcon = new CyclingSlotIcon(ForgeScreenHandler.FUEL_SLOT);
    private final CyclingSlotIcon catalystSlotIcon = new CyclingSlotIcon(ForgeScreenHandler.CATALYST_SLOT);

    public ForgeScreen(ForgeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.fuelSlotIcon.updateTexture(this.getFuelSlotTextures());
        this.catalystSlotIcon.updateTexture(this.getCatalystSlotTextures());
    }

    private List<Identifier> getFuelSlotTextures() {
        return List.of(
                EMPTY_COAL_TEXTURE,
                EMPTY_CHARCOAL_TEXTURE,
                EMPTY_LAVA_TEXTURE,
                EMPTY_BLAZE_ROD_TEXTURE
        );
    }

    private List<Identifier> getCatalystSlotTextures() {
        return List.of(
                EMPTY_BARITE_SHARD_TEXTURE,
                EMPTY_QUARTZ_TEXTURE
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (this.handler.isBurning()) {
            int k = 14;
            int l = MathHelper.ceil(this.handler.getFuelProgress() * 13.0f) + 1;
            context.drawGuiTexture(LIT_PROGRESS_TEXTURE, k, k, 0, k - l, i + 56, j + 36 + k - l, k, l);
        }
        int k = 24;
        int l = MathHelper.ceil(this.handler.getCookProgress() * 24.0f);
        context.drawGuiTexture(BURN_PROGRESS_TEXTURE, k, 16, 0, 0, i + 79, j + 34, l, 16);
        this.fuelSlotIcon.render(this.handler, context, delta, this.x, this.y);
        this.catalystSlotIcon.render(this.handler, context, delta, this.x, this.y);
    }
}
