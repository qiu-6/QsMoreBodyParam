package net.qiu.morebodyparam.component.thirst;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface thirstComponent extends Component, ServerTickingComponent {
    int getThirst();
    void setThirst(int value);

}
