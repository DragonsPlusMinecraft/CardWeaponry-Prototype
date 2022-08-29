package plus.dragons.card_weaponery.misc;

import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.ini.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup {

    public final static GeneralGroup GENERAL = new GeneralGroup();

    public static class GeneralGroup extends CreativeModeTab {

        public GeneralGroup() {
            super(CardWeaponry.MODID + ".general");
        }

        @Override
        public ItemStack makeIcon() {
            return ItemRegistry.BLANK_CARD.get().getDefaultInstance();
        }
    }
}
