package plus.dragons.card_weaponery.capability.cardtype;

import plus.dragons.card_weaponery.card.CommonCard;
import plus.dragons.card_weaponery.card.CommonCards;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CardTypeData {
    public static final Capability<CardTypeData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private CommonCard type;
    private boolean isSwitchingCard;

    public CardTypeData() {
        type = CommonCards.UNKNOWN;
        isSwitchingCard = false;
    }

    public CommonCard get() {
        return type;
    }

    public void set(CommonCard type) {
        this.type = type;
    }

    public void setSwitchingCard(boolean i) {
        isSwitchingCard = i;
    }

    public boolean isSwitchingCard() {
        return isSwitchingCard;
    }
}
