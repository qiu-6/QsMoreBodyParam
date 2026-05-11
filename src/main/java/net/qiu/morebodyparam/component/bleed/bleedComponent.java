package net.qiu.morebodyparam.component.bleed;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface bleedComponent extends Component, ServerTickingComponent {

    int getBleedDuration();
    int getBleedIntensity();

    /**
     * Sets the active bleed effect.
     *
     * @param duration  How long (in ticks) the bleed lasts. Clamped to the configured maximum.
     * @param intensity Severity: 0 = low, 1 = mid, 2 = high. Ignored when duration is 0.
     */
    void setBleed(int duration, int intensity);

    int getCombo();
    void setCombo(int newCombo);

}
