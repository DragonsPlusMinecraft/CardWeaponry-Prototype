package plus.dragons.card_weaponery.misc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue FLYING_CARD_SPEED;
    public static ForgeConfigSpec.BooleanValue HURT_VILLAGER;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("general");
        FLYING_CARD_SPEED = builder.comment("Flying Speed Of Card.", "Default is 15.0").defineInRange("FLYING_CARD_SPEED", 15.0, 0.1, 99.9);
        HURT_VILLAGER = builder.comment("Should Card Hurt Villager?").define("HURT_VILLAGER", false);
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
