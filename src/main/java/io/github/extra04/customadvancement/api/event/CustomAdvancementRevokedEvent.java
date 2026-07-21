package io.github.extra04.customadvancement.api.event;

import io.github.extra04.customadvancement.api.CustomAdvancementInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 발전과제 달성이 취소된 뒤에 발생한다.
 *
 * <p>{@code /ca revoke} 명령어나 {@link io.github.extra04.customadvancement.api.CustomAdvancementAPI#revoke}
 * 호출로 실제 회수가 일어났을 때만 발생한다. 애초에 달성하지 않은 상태였다면 발생하지 않는다.
 *
 * <p>부여할 때 준 보상을 되돌리는 용도로 쓸 수 있다. 다만 플러그인 본체는 보상을
 * 자동으로 회수하지 않으므로, 되돌리는 일은 이 이벤트를 받는 쪽에서 직접 해야 한다.
 */
public class CustomAdvancementRevokedEvent extends CustomAdvancementEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public CustomAdvancementRevokedEvent(@NotNull Player player,
                                         @NotNull CustomAdvancementInfo advancement) {
        super(player, advancement);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
