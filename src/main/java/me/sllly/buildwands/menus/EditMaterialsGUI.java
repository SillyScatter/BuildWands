package me.sllly.buildwands.menus;

import com.octanepvp.splityosis.menulib.Menu;
import com.octanepvp.splityosis.menulib.MenuItem;
import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.Material;

import java.util.Map;

public class EditMaterialsGUI extends Menu {
    private Wand wand;
    public EditMaterialsGUI(Wand wand) {
        super(BuildWands.editMaterialsConfig.menuSize);
        this.wand = wand;
        setTitle(BuildWands.editMaterialsConfig.menuTitle);
        for (Integer borderSlot : BuildWands.editMaterialsConfig.borderSlots) {
            setStaticItem(borderSlot, new MenuItem(BuildWands.editMaterialsConfig.borderItem));
        }
        setStaticItem(BuildWands.editMaterialsConfig.backSlot, new ExitMenuButton(BuildWands.editMaterialsConfig.backItem));
        for (Map.Entry<Material, Integer> materialIntegerEntry : wand.getMaterialAmounts().entrySet()) {
            addListedItem(new MaterialMenuItem(wand, materialIntegerEntry.getKey(), materialIntegerEntry.getValue()));
        }
    }

    public Wand getWand() {
        return wand;
    }
}
