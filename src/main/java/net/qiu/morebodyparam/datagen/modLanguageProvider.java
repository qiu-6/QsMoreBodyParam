package net.qiu.morebodyparam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.qiu.morebodyparam.config.modConfig;

public class modLanguageProvider extends FabricLanguageProvider {

    public modLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    
    private static final String DEATH_MESSAGE_KEY = "death.attack.";
    private static final String CONFIG_KEY = "qmorebodyparam.midnightconfig.";
    private static final String COMMAND_KEY = "commands.qmorebodyparam.";

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {

        // Death message
        translationBuilder.add(DEATH_MESSAGE_KEY + "bleed", "%s bleed to death");
        translationBuilder.add(DEATH_MESSAGE_KEY + "thirst", "%s turns into a salt fish");
        
        // Config
        translationBuilder.add(CONFIG_KEY + "title", "Q's More Body Parameters");
        
        translationBuilder.add(CONFIG_KEY + "disable_blood_in_peaceful", "Disable blood functionality on peaceful difficulty");
        
        translationBuilder.add( CONFIG_KEY + "blood_threshold_comment1", "Blood level thresholds to gain the effects");
        translationBuilder.add( CONFIG_KEY + "blood_threshold_comment2", "Set to 0 to disable the effects");

        translationBuilder.add(CONFIG_KEY + "complex_blood_threshold", "Complex condition thresholds for blood loss");
        translationBuilder.add(CONFIG_KEY + "mild_blood_loss", "Threshold to reach medium blood loss");
        translationBuilder.add(CONFIG_KEY + "heavy_blood_loss", "Threshold to reach heavy blood loss");
        translationBuilder.add(CONFIG_KEY + "attack_reduction_threshold", "Threshold to cause attack damage penality");
        translationBuilder.add( CONFIG_KEY + "hit_speed_reduction_threshold", "Threshold to cause hit speed penality");
        translationBuilder.add(CONFIG_KEY + "mining_speed_reduction_threshold", "Threshold to cause mining speed penality");
        translationBuilder.add(CONFIG_KEY + "speed_reduction_threshold", "Threshold to cause speed penality");
        
        translationBuilder.add(CONFIG_KEY + "blood_effect_comment", "Intensity of penality from blood loss");
        translationBuilder.add(CONFIG_KEY + "blood_attack_penality", "Attack damage reduction (%)");
        translationBuilder.add(CONFIG_KEY + "blood_attack_speed_penality", "Attack speed reduction (%)");
        translationBuilder.add(CONFIG_KEY + "blood_mining_speed_penality", "Mining speed reduction (%)");
        translationBuilder.add(CONFIG_KEY + "blood_speed_penality", "Movement speed reduction (%)");
        
        translationBuilder.add(CONFIG_KEY + "blood_regen_comment", "Blood level regeneration parameters");
        translationBuilder.add(CONFIG_KEY + "blood_regen_per_day", "Blood level regenerated per day");
        
        translationBuilder.add(CONFIG_KEY + "bleeding_comment", "Bleeding Effect");
        
        translationBuilder.add(CONFIG_KEY + "maximum_bleeding_duration", "Maximum bleeding duration stacked (min)");
        translationBuilder.add(CONFIG_KEY + "bleed_duration_added_per_hit", "Bleed duration added per hit (min)");
        translationBuilder.add(CONFIG_KEY + "bleed_duration_reduced_per_damage", "Bleed duration reduced per health reduction (min)");
        
        translationBuilder.add(CONFIG_KEY + "bleed_threshold_comment", "Damage threshold to receive inflict bleeding");
        translationBuilder.add(CONFIG_KEY + "low_bleed_threshold", "Low intensity");
        translationBuilder.add(CONFIG_KEY + "mid_bleed_threshold", "Medium intensity");
        translationBuilder.add(CONFIG_KEY + "high_bleed_threshold", "High intensity");
        
        translationBuilder.add(CONFIG_KEY + "bleed_drain_speed_comment", "Time to drain 1 blood level when bleeding (min)");
        translationBuilder.add(CONFIG_KEY + "blood_low_drain", "Low intensity");
        translationBuilder.add(CONFIG_KEY + "blood_mid_drain", "Medium intensity");
        translationBuilder.add(CONFIG_KEY + "blood_high_drain", "High intensity");
        
        translationBuilder.add(CONFIG_KEY + "default_bleed_drain", "Default time to drain 1 level of blood (s)");
        translationBuilder.add(CONFIG_KEY + "bleed_water_multiplier", "Multiplier when in water");
        translationBuilder.add(CONFIG_KEY + "bleed_walk_multiplier", "Multiplier when walking");
        translationBuilder.add(CONFIG_KEY + "bleed_sprint_multiplier", "Multiplier when swimming");
        
        translationBuilder.add(CONFIG_KEY + "thirst_drain_comment", "Thirst level natural drain");
        translationBuilder.add(CONFIG_KEY + "disable_thirst_in_peaceful", "Disable thirst functionality on peaceful difficulty");
        translationBuilder.add(CONFIG_KEY + "thirst_natural_drain", "Natural draining of thirst level (s)");
        translationBuilder.add(CONFIG_KEY + "thirst_optimal_temperature", "Optimal temperature where natural thirst draining is lowest");
        translationBuilder.add(CONFIG_KEY + "thirst_hot_multiplier", "Multiplier of natural draining in hot biomes");
        translationBuilder.add(CONFIG_KEY + "thirst_cold_multiplier", "Multiplier of natural draining in cold biomes");
        translationBuilder.add(CONFIG_KEY + "thirst_item_regen_comment", "Thirst level regeneration from item");
        translationBuilder.add(CONFIG_KEY + "thirst_regen_water_bucket", "Water bucket");
        
        translationBuilder.add(CONFIG_KEY + "category." + modConfig.BLOOD, "Blood");
        translationBuilder.add(CONFIG_KEY + "category." + modConfig.THIRST, "Thirst");
        
        // Commands
        // Components
        translationBuilder.add(COMMAND_KEY + "componenterror", "This command can only be run by a player when no target is specified");
        // Blood
        translationBuilder.add(COMMAND_KEY + "getblood", "Blood level of %1$s is %2$s");
        translationBuilder.add(COMMAND_KEY + "getblooderror", "Could not find blood component on player %s");
        translationBuilder.add(COMMAND_KEY + "setblood", "Set blood level of %1$s to %2$s");
        // Bleed
        translationBuilder.add(COMMAND_KEY + "getbleed", "Duration: %1$s, Intensity: %2$s, Combo: %3$s");
        translationBuilder.add(COMMAND_KEY + "updatebleed", "Updated bleed on %s");
        translationBuilder.add(COMMAND_KEY + "getbleedcombo", "Bleed combo of %1$s to %2$s");
        translationBuilder.add(COMMAND_KEY + "setbleedcombo", "Bleed combo of %a$s is set to %2$s");
        translationBuilder.add(COMMAND_KEY + "getbleederror", "Could not find bleed component on player %s");
        // Thirst
        translationBuilder.add(COMMAND_KEY + "getthirst", "Thirst level of %1$s is %2$s");
        translationBuilder.add(COMMAND_KEY + "getthirsterror", "Could not find thirst component on player %s");
        translationBuilder.add(COMMAND_KEY + "setthirst", "Set thirst level of %1$s to %2$s");
    }
}
