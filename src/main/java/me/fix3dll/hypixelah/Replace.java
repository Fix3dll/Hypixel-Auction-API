package me.fix3dll.hypixelah;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replace {

    public static String modifiedEnchs(String enchList) {
        String modifiedList = enchList.toLowerCase(Locale.ENGLISH)
                .replace("harvesting:6", "&(200,0,200)harvesting:6&(85,255,255)")
                .replace("bane_of_arthropods:6", "&(255,170,0)bane_of_arthropods:6&(85,255,255)")
                .replace("bane_of_arthropods:7", "&(200,0,200)bane_of_arthropods:7&(85,255,255)")
                .replace("expertise:10", "&(255,170,0)expertise:10&(85,255,255)")
                .replace("compact:10", "&(200,0,200)compact:10&(85,255,255)")
                .replace("dragon_hunter:5", "&(200,0,200)dragon_hunter:5&(85,255,255)")
                .replace("cleave:6", "&(200,0,200)cleave:6&(85,255,255)")
                .replace("execute:6", "&(200,0,200)execute:6&(85,255,255)")
                .replace("critical:6", "&(255,170,0)critical:6&(85,255,255)")
                .replace("critical:7", "&(200,0,200)critical:7&(85,255,255)")
                .replace("efficiency:6", "&(255,170,0)efficiency:6&(85,255,255)")
                .replace("efficiency:7", "&(255,170,0)efficiency:7&(85,255,255)")
                .replace("efficiency:8", "&(255,170,0)efficiency:8&(85,255,255)")
                .replace("efficiency:9", "&(255,170,0)efficiency:9&(85,255,255)")
                .replace("efficiency:10", "&(200,0,200)efficiency:10&(85,255,255)")
                .replace("pristine:5", "&(200,0,200)pristine:5&(85,255,255)")
                .replace("ender_slayer:6", "&(255,170,0)ender_slayer:6&(85,255,255)")
                .replace("ender_slayer:7", "&(200,0,200)ender_slayer:7&(85,255,255)")
                .replace("experience:4", "&(200,0,200)experience:4&(85,255,255)")        
                .replace("feather_falling:10", "&(200,0,200)feather_falling:10&(85,255,255)")
                .replace("infinite_quiver:10", "&(200,0,200)infinite_quiver:10&(85,255,255)")
                .replace("frail:6", "&(200,0,200)frail:6&(85,255,255)")
                .replace("titan_killer:6", "&(255,170,0)titan_killer:6&(85,255,255)")
                .replace("titan_killer:7", "&(200,0,200)titan_killer:7&(85,255,255)")
                .replace("giant_killer:6", "&(255,170,0)giant_killer:6&(85,255,255)")
                .replace("giant_killer:7", "&(200,0,200)giant_killer:7&(85,255,255)")
                .replace("growth:6", "&(255,170,0)growth:6&(85,255,255)")
                .replace("growth:7", "&(200,0,200)growth:7&(85,255,255)")
                .replace("lethality:6", "&(200,0,200)lethality:6&(85,255,255)")
                .replace("life_steal:4", "&(255,170,0)life_steal:4&(85,255,255)")
                .replace("life_steal:5", "&(200,0,200)life_steal:5&(85,255,255)")
                .replace("looting:4", "&(255,170,0)looting:4&(85,255,255)")
                .replace("looting:5", "&(200,0,200)looting:5&(85,255,255)")
                .replace("luck:6", "&(255,170,0)luck:6&(85,255,255)")
                .replace("luck:7", "&(200,0,200)luck:7&(85,255,255)")
                .replace("luck_of_the_sea:6", "&(200,0,200)luck_of_the_sea:6&(85,255,255)")
                .replace("lure:6", "&(200,0,200)lure:6&(85,255,255)")
                .replace("magnet:6", "&(200,0,200)magnet:6&(85,255,255)")
                .replace("spiked_hook:6", "&(200,0,200)spiked_hook:6&(85,255,255)")
                .replace("angler:6", "&(200,0,200)angler:6&(85,255,255)")
                .replace("caster:6", "&(200,0,200)caster:6&(85,255,255)")
                .replace("overload:5", "&(200,0,200)overload:5&(85,255,255)")
                .replace("power:6", "&(255,170,0)power:6&(85,255,255)")
                .replace("power:7", "&(200,0,200)power:7&(85,255,255)")
                .replace("chance:5", "&(200,0,200)chance:5&(85,255,255)")
                .replace("vicious:5", "&(200,0,200)vicious:5&(85,255,255)")
                .replace("first_strike:5", "&(200,0,200)first_strike:5&(85,255,255)")
                .replace("cubism:6", "&(200,0,200)cubism:6&(85,255,255)")
                .replace("protection:6", "&(255,170,0)protection:6&(85,255,255)")
                .replace("protection:7", "&(200,0,200)protection:7&(85,255,255)")
                .replace("scavenger:4", "&(255,170,0)scavenger:4&(85,255,255)")
                .replace("scavenger:5", "&(200,0,200)scavenger:5&(85,255,255)")
                .replace("sharpness:6", "&(255,170,0)sharpness:6&(85,255,255)")
                .replace("sharpness:7", "&(200,0,200)sharpness:7&(85,255,255)")
                .replace("smite:6", "&(255,170,0)smite:6&(85,255,255)")
                .replace("smite:7", "&(200,0,200)smite:7&(85,255,255)")
                .replace("syphon:5", "&(200,0,200)syphon:5&(85,255,255)")
                .replace("thunderlord:6", "&(255,170,0)thunderlord:6&(85,255,255)")
                .replace("thunderlord:7", "&(200,0,200)thunderlord:7&(85,255,255)")
                .replace("thunderbolt:6", "&(255,170,0)thunderbolt:6&(85,255,255)")
                .replace("thunderbolt:7", "&(200,0,200)thunderbolt:7&(85,255,255)")
                .replace("venomous:6", "&(200,0,200)venomous:6&(85,255,255)")
                .replace("vampirism:6", "&(200,0,200)vampirism:6&(85,255,255)")
                .replace("counter_strike:5", "&(200,0,200)counter_strike:5&(85,255,255)")
                .replace("big_brain:3", "&(255,170,0)big_brain:3&(85,255,255)")
                .replace("big_brain:4", "&(255,170,0)big_brain:4&(85,255,255)")
                .replace("big_brain:5", "&(200,0,200)big_brain:5&(85,255,255)")
                .replace("smarty_pants:5", "&(200,0,200)smarty_pants:5&(85,255,255)")
                .replace("mana_steal:3", "&(200,0,200)mana_steal:3&(85,255,255)");
        Matcher ultimate = Pattern.compile("ultimate_.*?(?=,)").matcher(modifiedList);
        if (ultimate.find()) {
            modifiedList = modifiedList.replace(ultimate.group(), "&(255,85,255)" + ultimate.group() + "&(85,255,255)");
        }
        return modifiedList;
    }

    public static String modifiedColor(String text) {
        return text
                .replaceAll("§0", "&(0,0,0)")
                .replaceAll("§1", "&(0,0,170)")
                .replaceAll("§2", "&(0,170,0)")
                .replaceAll("§3", "&(0,170,170)")
                .replaceAll("§4", "&(170,0,0)")
                .replaceAll("§5", "&(170,0,170)")
                .replaceAll("§6", "&(255,170,0)")
                .replaceAll("§7", "&(170,170,170)")
                .replaceAll("§8", "&(85,85,85)")
                .replaceAll("§9", "&(85,85,255)")
                .replaceAll("§a", "&(85,255,85)")
                .replaceAll("§b", "&(85,255,255)")
                .replaceAll("§c", "&(255,85,85)")
                .replaceAll("§d", "&(255,85,255)")
                .replaceAll("§e", "&(255,255,85)")
                .replaceAll("§f", "&(255,255,255)");
    }

    public static String modifiedGems(String gemList) {
        return gemList
                .replace("\"RUBY\"", "\"&(176,46,38)Ruby&(255,255,255)\"")
                .replace("\"AMETHYST\"", "\"&(137,50,184)Amethyst\"&(255,255,255)\"")
                .replace("\"JADE\"", "\"&(128,199,31)Jade&(255,255,255)\"")
                .replace("\"SAPPHIRE\"", "\"&(85,150,255)Sapphire&(255,255,255)\"")
                .replace("\"AMBER\"", "\"&(249,128,29)Amber&(255,255,255)\"")
                .replace("\"TOPAZ\"", "\"&(254,216,61)Topaz&(255,255,255)\"")
                .replace("\"JASPER\"", "\"&(199,78,189)Jasper&(255,255,255)\"")
                .replaceAll("FLAWED", "&(85,255,85)Flawed&(255,255,255)")
                .replaceAll("FINE", "&(85,85,255)Fine&(255,255,255)")
                .replaceAll("FLAWLESS", "&(170,0,170)Flawless&(255,255,255)")
                .replaceAll("PERFECT", "&(255,170,0)Perfect&(255,255,255)");
    }
}
