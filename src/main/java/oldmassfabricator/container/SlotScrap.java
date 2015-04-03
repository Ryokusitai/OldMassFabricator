package oldmassfabricator.container;

import ic2.api.item.IC2Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotScrap extends Slot
{
	public SlotScrap(IInventory inv, int slot, int x, int y)
	{
		super(inv, slot, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return isItemScrap(stack);
	}

	public static boolean isItemScrap(ItemStack stack)
	{
		return stack.getItem().equals(IC2Items.getItem("scrap").getItem()) || stack.getItem().equals(IC2Items.getItem("scrapBox").getItem());
	}

}
