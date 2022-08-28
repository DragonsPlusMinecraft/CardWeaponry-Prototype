package plus.dragons.card_weaponery.misc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue CARD_SPEED;
    public static ForgeConfigSpec.DoubleValue CARD_DAMAGE;
    public static ForgeConfigSpec.BooleanValue HURT_VILLAGER;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("general");
        CARD_SPEED = builder.comment("Default Flying Speed Of Card.").defineInRange("CARD_SPEED", 1.0, 0.1, 99.9);
        CARD_DAMAGE = builder.comment("Default Damage Of Card.").defineInRange("CARD_DAMAGE", 6.0, 0.1, 2048.0);
        HURT_VILLAGER = builder.comment("Should Card Hurt Villager?").define("HURT_VILLAGER", false);
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
