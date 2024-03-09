package me.sllly.buildwands.menus;

import com.octanepvp.splityosis.menulib.Menu;
import com.octanepvp.splityosis.menulib.MenuItem;
import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.objects.Wand;

public class EditMaterialsGUI extends Menu {
    public EditMaterialsGUI(Wand wand) {
        super(BuildWands.editMaterialsConfig.menuSize);
        setTitle(BuildWands.editMaterialsConfig.menuTitle);
        for (Integer borderSlot : BuildWands.editMaterialsConfig.borderSlots) {
            setStaticItem(borderSlot, new MenuItem(BuildWands.editMaterialsConfig.borderItem));
        }
        //TODO Set exit button
    }


}
