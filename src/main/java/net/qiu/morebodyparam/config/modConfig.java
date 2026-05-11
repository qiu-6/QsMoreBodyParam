package net.qiu.morebodyparam.config;

import eu.midnightdust.lib.config.MidnightConfig;

import static net.qiu.morebodyparam.QsMoreBodyParameters.MOD_ID;

public class modConfig extends MidnightConfig {

    public static final String BLOOD = "blood";
    public static final String THIRST = "thirst";

    @Entry(category = BLOOD) public static boolean disable_blood_in_peaceful = true;

    @Comment(category = BLOOD) public static Comment spacer;

    @Comment(category = BLOOD, centered = true) public static Comment blood_threshold_comment1;
    @Comment(category = BLOOD) public static Comment blood_threshold_comment2;
    @Entry(category = BLOOD) public static boolean complex_blood_threshold = false;
    @Condition(requiredOption = MOD_ID + ":complex_blood_threshold", requiredValue = "false", visibleButLocked = true)
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 19) public static int mild_blood_loss = 12;
    @Condition(requiredOption = MOD_ID + ":complex_blood_threshold", requiredValue = "false", visibleButLocked = true)
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 19) public static int heavy_blood_loss = 8;

    @Condition(requiredOption = MOD_ID + ":complex_blood_threshold", visibleButLocked = true)
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 19) public static int attack_reduction_threshold = 12;
    @Condition(requiredOption = MOD_ID + ":complex_blood_threshold", visibleButLocked = true)
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 19) public static int hit_speed_reduction_threshold = 12;
    @Condition(requiredOption = MOD_ID + ":complex_blood_threshold", visibleButLocked = true)
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 19) public static int mining_speed_reduction_threshold = 12;
    @Condition(requiredOption = MOD_ID + ":complex_blood_threshold", visibleButLocked = true)
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 19) public static int speed_reduction_threshold = 8;

    @Comment(category = BLOOD) public static Comment spacer1;

    @Comment(category = BLOOD, centered = true) public static Comment blood_effect_comment;
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 100) public static int blood_attack_penality = 50;
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 100) public static int blood_attack_speed_penality = 50;
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 100) public static int blood_mining_speed_penality = 80;
    @Entry(category = BLOOD, isSlider = true, min = 0, max = 100) public static int blood_speed_penality = 30;

    @Comment(category = BLOOD) public static Comment spacer2;

    @Comment(category = BLOOD, centered = true) public static Comment blood_regen_comment;
    @Entry(category = BLOOD) public static float blood_regen_per_day = 1f;

    @Comment(category = BLOOD) public static Comment spacer0;

    @Comment(category = BLOOD, centered = true) public static Comment bleeding_comment;

    @Comment(category = BLOOD) public static Comment spacer5;

    @Entry(category = BLOOD) public static int maximum_bleeding_duration = 24;
    @Entry(category = BLOOD) public static float bleed_duration_added_per_hit = 6.0f;
    @Entry(category = BLOOD) public static float bleed_duration_reduced_per_damage = 2f;

    @Comment(category = BLOOD) public static Comment spacer6;

    @Comment(category = BLOOD, centered = true) public static Comment bleed_threshold_comment;
    @Entry(category = BLOOD, min = 0) public static int low_bleed_threshold = 0;
    @Entry(category = BLOOD, min = 0) public static int mid_bleed_threshold = 3;
    @Entry(category = BLOOD, min = 0) public static int high_bleed_threshold = 8;

    @Comment(category = BLOOD, centered = true) public static Comment bleed_drain_speed_comment;
    @Entry(category = BLOOD) public static float blood_low_drain = 5.0f;
    @Entry(category = BLOOD) public static float blood_mid_drain = 1.0f;
    @Entry(category = BLOOD) public static float blood_high_drain = 0.5f;

    @Comment(category = BLOOD) public static Comment spacer7;

    @Entry(category = BLOOD) public static float bleed_water_multiplier = 3.0f;
    @Entry(category = BLOOD) public static float bleed_walk_multiplier = 1.2f;
    @Entry(category = BLOOD) public static float bleed_sprint_multiplier = 1.5f;

    @Comment(category = THIRST) public static Comment spacer3;

    @Entry(category = THIRST) public static boolean disable_thirst_in_peaceful = false;

    @Comment(category = THIRST, centered = true) public static Comment thirst_drain_comment;
    @Entry(category = THIRST) public static int thirst_natural_drain = 300;
    @Entry(category = THIRST, isSlider = true, min = 0.0f, max = 2.0f, precision = 10) public static float thirst_optimal_temperature = 0.9f;
    @Entry(category = THIRST, min = 1.0f) public static float thirst_hot_multiplier = 2.0f;
    @Entry(category = THIRST, min = 1.0f) public static float thirst_cold_multiplier = 1.8f;

    @Comment(category = THIRST) public static Comment spacer4;

    @Comment(category = THIRST, centered = true) public static Comment thirst_item_regen_comment;
    @Entry(category = THIRST,isSlider = true, min = 0, max = 20) public static int thirst_regen_water_bucket = 8;
}
