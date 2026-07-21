package io.github.extra04.customadvancement.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 발전과제 설정을 다시 읽은 뒤에 발생한다.
 *
 * <p>{@code /ca reload}, 웹 에디터의 "서버에 적용", ItemsAdder 로드 완료 등으로 발생한다.
 *
 * <p>{@link io.github.extra04.customadvancement.api.CustomAdvancementInfo} 객체를 캐싱해 뒀다면
 * 이 시점에 버려야 한다. 리로드 후에는 전부 새 객체로 교체되기 때문이다.
 *
 * <pre>{@code
 * @EventHandler
 * public void onReload(CustomAdvancementReloadEvent event) {
 *     myCache.clear();
 *     getLogger().info("발전과제 " + event.getAdvancementCount() + "개로 갱신됨");
 * }
 * }</pre>
 */
public class CustomAdvancementReloadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final int advancementCount;

    public CustomAdvancementReloadEvent(int advancementCount) {
        this.advancementCount = advancementCount;
    }

    /** 다시 읽은 뒤 등록된 발전과제 개수. */
    public int getAdvancementCount() {
        return advancementCount;
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
