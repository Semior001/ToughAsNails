package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;

import java.util.stream.IntStream;

public class ArmorModifier extends TemperatureModifier {
    public ArmorModifier(String id) {
        super(id);
    }

    @Override
    public Temperature applyPlayerModifiers(EntityPlayer player, Temperature initialTemperature, IModifierMonitor monitor) {
        int newTemperatureLevel = initialTemperature.getRawValue();

        InventoryPlayer inventory = player.inventory;

        int modifier = IntStream.range(0, 4)
                .map(i -> ModConfig.armorTemperatureData.stream()
                        .filter(atd -> atd.names.contains(inventory.armorInventory.get(i)
                        .getItem().getRegistryName().toString()))
                        .findFirst()
                        .map(atd -> atd.modifier).orElse(0)).sum();
        newTemperatureLevel += modifier;
        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Armor", initialTemperature, new Temperature(newTemperatureLevel)));
        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific() {
        return true;
    }
}
