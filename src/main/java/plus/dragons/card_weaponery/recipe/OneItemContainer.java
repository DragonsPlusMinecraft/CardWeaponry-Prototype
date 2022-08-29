package plus.dragons.card_weaponery.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class OneItemContainer extends SimpleContainer {
    public OneItemContainer(ItemStack itemStack) {
        super(1);
        this.addItem(itemStack);
    }

    public ItemStack get(){
        return this.getItem(0);
    }
}
