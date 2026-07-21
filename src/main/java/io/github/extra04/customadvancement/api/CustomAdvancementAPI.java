package io.github.extra04.customadvancement.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

/**
 * Custom Advancement 플러그인의 공개 진입점.
 *
 * <h2>가져오는 법</h2>
 * <pre>{@code
 * CustomAdvancementAPI api = CustomAdvancementAPI.get();
 * if (api == null) {
 *     getLogger().warning("Custom Advancement 가 설치되어 있지 않습니다.");
 *     return;
 * }
 * }</pre>
 *
 * <h2>주의: 언제 가져오는가</h2>
 * 플러그인의 {@code onEnable()} 안에서 곧바로 부르면 {@code null} 이 나올 수 있다.
 * Custom Advancement 가 아직 켜지지 않았을 수 있기 때문이다. 두 가지 중 하나를 쓴다.
 * <ul>
 *   <li>{@code plugin.yml} 에 {@code softdepend: [Custom_Advancement]} 를 적는다 (권장)</li>
 *   <li>실제로 필요해지는 시점에 {@link #get()} 을 부른다</li>
 * </ul>
 *
 * <h2>스레드</h2>
 * 모든 메서드는 <b>메인 스레드에서만</b> 불러야 한다. 발전과제 부여는 플레이어 상태와
 * 패킷 전송을 건드리므로 비동기로 호출하면 서버가 불안정해진다.
 */
public interface CustomAdvancementAPI {

    /**
     * 서버에 설치된 Custom Advancement 의 API 를 가져온다.
     * 플러그인이 없거나 아직 켜지지 않았으면 {@code null}.
     *
     * <p>내부적으로 Bukkit 의 {@link org.bukkit.plugin.ServicesManager} 를 쓴다.
     * 정적 필드를 두지 않으므로 플러그인이 다시 로드돼도 오래된 참조가 남지 않는다.
     */
    @Nullable
    static CustomAdvancementAPI get() {
        var registration = Bukkit.getServicesManager().getRegistration(CustomAdvancementAPI.class);
        return registration == null ? null : registration.getProvider();
    }

    /**
     * {@link #get()} 의 Optional 판. 없을 때의 처리를 강제하고 싶을 때 쓴다.
     */
    @NotNull
    static Optional<CustomAdvancementAPI> getOptional() {
        return Optional.ofNullable(get());
    }

    // ─── 조회 ───

    /**
     * ID 로 발전과제 정보를 찾는다. 없으면 {@code null}.
     *
     * @param advancementId 설정 파일에 적은 ID (예: {@code first_diamond})
     */
    @Nullable
    CustomAdvancementInfo getAdvancement(@NotNull String advancementId);

    /**
     * 등록된 모든 발전과제. 수정할 수 없는 목록이다.
     */
    @NotNull
    Collection<CustomAdvancementInfo> getAdvancements();

    /**
     * 루트(= 게임 안의 탭)만 골라서 돌려준다.
     */
    @NotNull
    Collection<CustomAdvancementInfo> getRoots();

    /**
     * 특정 루트에 속한 발전과제 전부. 루트 자신도 포함된다.
     *
     * @param rootId 루트의 ID
     */
    @NotNull
    Collection<CustomAdvancementInfo> getAdvancementsInRoot(@NotNull String rootId);

    // ─── 달성 여부 ───

    /**
     * 이 플레이어가 해당 발전과제를 달성했는지.
     *
     * <p><b>접속 중인 플레이어만 조회할 수 있다.</b> 오프라인 플레이어의 기록은 저장소에서
     * 비동기로 읽어야 하므로 이 API(동기 방식)로는 정확히 답할 수 없어 넣지 않았다.
     * 잘못된 값을 돌려주느니 없는 편이 낫다고 판단했다.
     *
     * @return 달성했으면 {@code true}. 발전과제 ID 가 없으면 {@code false}.
     */
    boolean isGranted(@NotNull Player player, @NotNull String advancementId);

    /**
     * 이 플레이어가 달성한 발전과제 ID 목록.
     */
    @NotNull
    Collection<String> getGrantedIds(@NotNull Player player);

    // ─── 부여 / 회수 ───

    /**
     * 발전과제를 강제로 달성시킨다.
     *
     * <p>설정된 보상 명령어가 함께 실행되고, {@link io.github.extra04.customadvancement.api.event.CustomAdvancementGrantedEvent}
     * 가 발생한다. 이미 달성한 상태면 아무 일도 하지 않고 {@code false} 를 돌려준다.
     *
     * <p>트리거의 상태 조건(웅크리기 등)은 <b>검사하지 않는다</b>. 이 메서드는
     * "조건을 만족했다고 판단했으니 달성시켜라"라는 뜻이므로, 조건 판단은 호출하는 쪽 몫이다.
     *
     * @return 실제로 부여됐으면 {@code true}
     */
    boolean grant(@NotNull Player player, @NotNull String advancementId);

    /**
     * 달성을 취소한다.
     *
     * <p>이미 달성하지 않은 상태면 {@code false}. 보상 명령어는 되돌려지지 않는다.
     *
     * @return 실제로 회수됐으면 {@code true}
     */
    boolean revoke(@NotNull Player player, @NotNull String advancementId);

    // ─── 기타 ───

    /**
     * 설정 파일을 다시 읽는다. {@code /ca reload} 와 같은 동작이다.
     *
     * <p>플레이어의 달성 기록은 지워지지 않는다.
     */
    void reload();

    /**
     * 이 플러그인이 ItemsAdder 와 연동 가능한 상태인지.
     *
     * <p>ItemsAdder 는 서버가 켜진 뒤 조금 늦게 아이템을 등록하므로, 서버 시작 직후에는
     * 설치되어 있어도 {@code false} 일 수 있다.
     */
    boolean isItemsAdderReady();

    /** API 버전. 호환성 확인이 필요할 때 쓴다. */
    @NotNull
    String getApiVersion();
}
