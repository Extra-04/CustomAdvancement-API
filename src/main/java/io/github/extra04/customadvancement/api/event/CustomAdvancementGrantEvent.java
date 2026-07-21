package io.github.extra04.customadvancement.api.event;

import io.github.extra04.customadvancement.api.CustomAdvancementInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 발전과제가 부여되기 <b>직전</b>에 발생한다. 취소할 수 있다.
 *
 * <p>취소하면 발전과제가 부여되지 않고 보상 명령어도 실행되지 않으며,
 * {@link CustomAdvancementGrantedEvent} 도 발생하지 않는다.
 *
 * <p>쓰임새 예시:
 * <ul>
 *   <li>특정 월드나 미니게임 중에는 발전과제를 막고 싶을 때</li>
 *   <li>자체 조건을 하나 더 얹고 싶을 때</li>
 *   <li>중복 보상을 막는 자체 로직이 있을 때</li>
 * </ul>
 *
 * <pre>{@code
 * @EventHandler
 * public void onGrant(CustomAdvancementGrantEvent event) {
 *     if (event.getPlayer().getWorld().getName().equals("minigame")) {
 *         event.setCancelled(true);   // 미니게임 중에는 발전과제 금지
 *     }
 * }
 * }</pre>
 *
 * <p>이 이벤트는 항상 메인 스레드에서 발생한다.
 */
public class CustomAdvancementGrantEvent extends CustomAdvancementEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /** 부여를 일으킨 원인. */
    public enum Cause {
        /** 설정된 트리거가 발동해서. */
        TRIGGER,
        /** {@code /ca grant} 명령어로. */
        COMMAND,
        /** 다른 플러그인이 API 로 호출해서. */
        API
    }

    private final Cause cause;
    private boolean cancelled;

    public CustomAdvancementGrantEvent(@NotNull Player player,
                                       @NotNull CustomAdvancementInfo advancement,
                                       @NotNull Cause cause) {
        super(player, advancement);
        this.cause = cause;
    }

    /**
     * 무엇 때문에 부여되려 하는지.
     *
     * <p>원인별로 다르게 처리하고 싶을 때 쓴다. 예를 들어 관리자가 명령어로 준 것은
     * 막지 않고, 트리거로 자동 달성되는 것만 막을 수 있다.
     */
    @NotNull
    public Cause getCause() {
        return cause;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
