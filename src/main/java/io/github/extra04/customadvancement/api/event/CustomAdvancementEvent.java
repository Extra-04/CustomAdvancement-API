package io.github.extra04.customadvancement.api.event;

import io.github.extra04.customadvancement.api.CustomAdvancementInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 커스텀 발전과제와 관련된 모든 이벤트의 공통 부모.
 *
 * <p>이 클래스를 직접 listen 할 수는 없다. 실제로 발생하는 것은 하위 클래스들이다.
 */
public abstract class CustomAdvancementEvent extends PlayerEvent {

    private final CustomAdvancementInfo advancement;

    protected CustomAdvancementEvent(@NotNull Player player, @NotNull CustomAdvancementInfo advancement) {
        super(player);
        this.advancement = advancement;
    }

    /** 대상 발전과제의 정보. */
    @NotNull
    public CustomAdvancementInfo getAdvancement() {
        return advancement;
    }

    /** 대상 발전과제의 ID. {@code getAdvancement().getId()} 와 같다. */
    @NotNull
    public String getAdvancementId() {
        return advancement.getId();
    }
}
