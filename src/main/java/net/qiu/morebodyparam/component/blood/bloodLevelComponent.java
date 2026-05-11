package net.qiu.morebodyparam.component.blood;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.load.ServerLoadAwareComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface bloodLevelComponent extends Component, ServerTickingComponent, ServerLoadAwareComponent {

    int getBlood();
    void setBlood(int newBlood);

    float getMiningSpeedMultiplier();

}
