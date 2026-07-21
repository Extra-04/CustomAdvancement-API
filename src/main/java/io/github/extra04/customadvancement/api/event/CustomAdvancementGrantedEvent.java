package io.github.extra04.customadvancement.api.event;

import io.github.extra04.customadvancement.api.CustomAdvancementInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 발전과제가 실제로 부여된 <b>뒤</b>에 발생한다. 취소할 수 없다.
 *
 * <p>보상을 추가로 주거나, 기록을 남기거나, 디스코드로 알리는 등
 * "달성했을 때 무언가를 하는" 용도에 쓴다.
 *
 * <pre>{@code
 * @EventHandler
 * public void onGranted(CustomAdvancementGrantedEvent event) {
 *     Player player = event.getPlayer();
 *     getLogger().info(player.getName() + " 달성: " + event.getAdvancementId());
 *
 *     if (event.getAdvancement().getFrame() == FrameType.CHALLENGE) {
 *         Bukkit.broadcastMessage("§6" + player.getName() + " 님이 도전 과제를 깼습니다!");
 *     }
 * }
 * }</pre>
 *
 * <p>부여를 <b>막고 싶다면</b> 이 이벤트가 아니라
 * {@link CustomAdvancementGrantEvent} 를 취소해야 한다.
 *
 * <p>이 이벤트는 항상 메인 스레드에서 발생한다.
 */
public class CustomAdvancementGrantedEvent extends CustomAdvancementEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CustomAdvancementGrantEvent.Cause cause;

    public CustomAdvancementGrantedEvent(@NotNull Player player,
                                         @NotNull CustomAdvancementInfo advancement,
                                         @NotNull CustomAdvancementGrantEvent.Cause cause) {
        super(player, advancement);
        this.cause = cause;
    }

    /** 무엇 때문에 부여됐는지. */
    @NotNull
    public CustomAdvancementGrantEvent.Cause getCause() {
        return cause;
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
