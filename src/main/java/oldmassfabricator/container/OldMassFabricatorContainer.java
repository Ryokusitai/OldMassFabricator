package oldmassfabricator.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import oldmassfabricator.OldMassFabricatorTile;


public class OldMassFabricatorContainer extends Container
{
	private OldMassFabricatorTile tile;

	// インベントリの情報
	public final int scrapIndex = 0;
	public final int matterIndex = 1;
	public final int inventoryIndex = 2;
	public final int inventorySize = 27;
	public final int hotbarIndex = 29;
	public final int hotbarSize = 9;


	public OldMassFabricatorContainer(InventoryPlayer invPlayer, OldMassFabricatorTile tile)
	{
		this.tile = tile;
		tile.openInventory();

		// Matter Slot
		this.addSlotToContainer(new SlotMatter(tile, 1, 114, 18));

		// Scrap Slot
		this.addSlotToContainer(new SlotScrap(tile, 0, 114, 54));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		// Player hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));

	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		Slot slot = this.getSlot(slotIndex);

		if (slot == null || !slot.getHasStack())
		{
			return null;
		}

		// クリックされたスロットのアイテムスタックを取得
		ItemStack itemstack = slot.getStack();

		// 変更前のスタックの情報
		ItemStack itemstackOrg = itemstack.copy();

		// マタースロットもしくはスクラップスロットがクリックされた
		if (slotIndex == this.matterIndex || slotIndex == this.scrapIndex)
		{
			if (!this.mergeItemStack(itemstack, inventoryIndex, inventoryIndex + inventorySize + hotbarSize, false)) {
				return null;
			}

			slot.onSlotChange(itemstack, itemstackOrg);
		}

		// インベントリスロットがクリックされた
		if (slotIndex >= this.inventoryIndex && slotIndex < this.inventoryIndex + this.inventorySize)
		{
			// スクラップだった時
			if (tile.isItemScrap(itemstack))
			{
				if (!this.mergeItemStack(itemstack, matterIndex, matterIndex + 1, false)) {
					return null;
				}
			}
			else
			{
				if (!this.mergeItemStack(itemstack, hotbarIndex, hotbarIndex + hotbarSize, false)) {
					return null;
				}
			}
		}

		// ホットバーがクリックされた
		if (slotIndex >= this.hotbarIndex && slotIndex < this.hotbarIndex + this.hotbarSize)
		{
			// スクラップだったとき
			if (tile.isItemScrap(itemstack))
			{
				if (!this.mergeItemStack(itemstack, matterIndex, matterIndex + 1, false)) {
					return null;
				}
			}
			else
			{
				if (!this.mergeItemStack(itemstack, inventoryIndex, inventoryIndex + inventorySize, false)) {
					return null;
				}
			}
		}

		// シフトクリックで移動先スロットが溢れなかった場合は移動元スロットを空にする
		if (itemstack.stackSize == 0) {
			slot.putStack((ItemStack)null);
		}
		// 移動先スロットが溢れた場合は数だけ変わって元スロットにアイテムが残るので更新通知
		else {
			slot.onSlotChanged();
		}

		// シフトクリック前後で数が変わらなかった = 移動失敗
		if (itemstack.stackSize == itemstackOrg.stackSize) {
			return null;
		}

		slot.onPickupFromSlot(player, itemstack);

		return itemstackOrg;
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return this.tile.isUseableByPlayer(var1);
	}

	public TileEntity getTile()
	{
		return tile;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		tile.closeInventory();
	}

}
