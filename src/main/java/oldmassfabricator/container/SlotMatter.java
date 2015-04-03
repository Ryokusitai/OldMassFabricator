package oldmassfabricator.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMatter extends Slot
{
	public SlotMatter(IInventory inv, int slot, int x, int y)
	{
		super(inv, slot, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}

}
