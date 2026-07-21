package io.github.extra04.customadvancement.api;

/**
 * 발전과제 테두리 모양.
 *
 * <p>게임 안에서 보이는 모양과 달성했을 때의 연출이 달라진다.
 */
public enum FrameType {

    /** 네모 테두리. 가장 일반적인 발전과제. */
    TASK,

    /** 둥근 테두리. 조금 더 무게가 있는 목표. */
    GOAL,

    /** 뾰족한 테두리. 달성 시 특별한 소리와 함께 표시된다. */
    CHALLENGE
}
