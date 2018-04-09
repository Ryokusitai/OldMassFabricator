package oldmassfabricator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import oldmassfabricator.container.OldMassFabricatorContainer;
import org.lwjgl.opengl.GL11;

public class GuiOldMassFabricator extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(OldMassFabricator.MODID.toLowerCase(), "textures/gui/GUIOldMatter.png");
	OldMassFabricatorTile tile;

	public GuiOldMassFabricator(InventoryPlayer invPlayer, OldMassFabricatorTile tile) {
		super(new OldMassFabricatorContainer(invPlayer, tile));
		this.tile = tile;
		this.xSize = 218;
		this.ySize = 165;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2) {
		this.fontRendererObj.drawString("Mass Fabricator", 40, 4, 4210752);

		this.fontRendererObj.drawString("Progress:", 14, 20, 4210752);
		this.fontRendererObj.drawString(Integer.toString(tile.getProgress())+"%", 14, 20+10, 4210752);

		if (tile.getScrap() != 0) {
			this.fontRendererObj.drawString("Amplifier:", 14, 20+10+17, 4210752);
			this.fontRendererObj.drawString(Integer.toString(tile.getScrap()), 14, 20+10+17+13, 4210752);
		}

		this.fontRendererObj.drawString("Inventory", 8, 20+10+17+13+13, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
