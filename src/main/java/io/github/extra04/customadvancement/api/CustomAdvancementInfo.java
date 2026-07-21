package io.github.extra04.customadvancement.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 발전과제 하나에 대한 읽기 전용 정보.
 *
 * <p>이 객체로 설정을 바꿀 수는 없다. 발전과제 정의는 서버 관리자가 웹 에디터나 YAML 로
 * 관리하는 것이고, 다른 플러그인이 임의로 바꾸면 관리자가 만든 구성과 어긋나기 때문이다.
 * 플레이어의 달성 여부를 다루려면 {@link CustomAdvancementAPI} 쪽을 쓴다.
 *
 * <p>구현체는 불변(immutable)이다. 설정이 다시 로드되면 새 객체로 교체되므로,
 * 이 객체를 오래 들고 있지 말고 필요할 때마다 {@link CustomAdvancementAPI#getAdvancement(String)}
 * 으로 다시 조회하는 편이 안전하다.
 */
public interface CustomAdvancementInfo {

    /**
     * 설정 파일에서 쓰는 고유 ID. 예: {@code first_diamond}
     *
     * <p>{@link CustomAdvancementAPI} 의 모든 메서드가 이 값을 기준으로 동작한다.
     */
    @NotNull
    String getId();

    /** 게임에 표시되는 제목. 색상 코드({@code §a} 등)가 포함될 수 있다. */
    @NotNull
    String getTitle();

    /** 게임에 표시되는 설명. 색상 코드가 포함될 수 있다. */
    @NotNull
    String getDescription();

    /**
     * 아이콘으로 쓰는 아이템 문자열.
     *
     * <p>세 가지 형태가 올 수 있다.
     * <ul>
     *   <li>{@code DIAMOND} — 바닐라 Material 이름</li>
     *   <li>{@code minecraft:diamond} — 네임스페이스 표기의 바닐라 아이템</li>
     *   <li>{@code myitems:mithril} — ItemsAdder 커스텀 아이템</li>
     * </ul>
     */
    @NotNull
    String getIcon();

    /**
     * 아이콘에 적용되는 리소스팩 커스텀 모델 번호. 지정하지 않았으면 {@code null}.
     *
     * <p>ItemsAdder 아이콘에는 적용되지 않는다. ItemsAdder 가 자체적으로 모델을 관리하므로
     * 여기서 덮어쓰면 엉뚱한 아이템 그림이 나온다.
     */
    @Nullable
    Integer getCustomModelData();

    /** 테두리 모양. */
    @NotNull
    FrameType getFrame();

    /**
     * 부모 발전과제의 ID. 루트(탭의 시작점)이면 {@code null}.
     *
     * @see #isRoot()
     */
    @Nullable
    String getParentId();

    /**
     * 이 발전과제가 속한 루트의 ID. 자기 자신이 루트면 자기 ID 를 돌려준다.
     *
     * <p>루트 하나가 게임 안의 발전과제 탭 하나에 대응한다.
     */
    @NotNull
    String getRootId();

    /** 부모가 없는 루트인지. 루트는 게임에서 독립된 탭으로 표시된다. */
    boolean isRoot();

    /**
     * 달성에 필요한 기준 수. 기본값은 1.
     *
     * <p>트리거의 반복 횟수(예: 블록 10개 파괴)와는 다른 개념이다.
     */
    int getCriteria();

    /** 트리에서의 가로 위치. 0 이상. */
    float getX();

    /** 트리에서의 세로 위치. 0 이상. */
    float getY();

    /**
     * 표시 방식. {@code ALWAYS}, {@code PARENT_GRANTED}, {@code VANILLA}, {@code HIDDEN} 중 하나.
     *
     * <p>문자열로 두는 이유는 서버 관리자가 설정에 임의의 값을 넣을 수 있고,
     * 그때 열거형이면 API 호출부에서 예외가 나기 때문이다.
     */
    @NotNull
    String getVisibility();

    /**
     * 이 발전과제에 자동 달성 트리거가 설정되어 있는지.
     *
     * <p>{@code false} 면 {@link CustomAdvancementAPI#grant} 나 명령어로만 달성시킬 수 있다.
     */
    boolean hasTrigger();

    /**
     * 설정된 트리거의 종류. 예: {@code obtain_item}, {@code kill_entity}.
     * 트리거가 없으면 {@code null}.
     */
    @Nullable
    String getTriggerType();
}
