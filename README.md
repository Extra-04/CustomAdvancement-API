# Custom Advancement API

[Custom Advancement](https://github.com/) 플러그인의 공개 API입니다.
다른 플러그인에서 커스텀 발전과제를 조회하거나, 달성 여부를 다루거나,
달성 순간에 개입하고 싶을 때 씁니다.

> 이 저장소에는 **API(인터페이스와 이벤트)만** 들어 있습니다.
> 플러그인 본체의 구현 코드는 포함되지 않습니다.

---

## 무엇을 할 수 있나요

- 등록된 발전과제 목록·정보 조회
- 플레이어의 달성 여부 확인
- 발전과제 부여 / 회수
- 달성 **직전**에 끼어들어 **취소**
- 달성·회수·리로드 시점에 이벤트 수신

---

## 설치

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Extra04:CustomAdvancement-API:1.0")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Extra04:CustomAdvancement-API:1.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Extra04</groupId>
    <artifactId>CustomAdvancement-API</artifactId>
    <version>1.0</version>
    <scope>provided</scope>
</dependency>
```

`compileOnly` / `provided` 를 쓰세요. API 클래스는 Custom Advancement 플러그인 안에
이미 들어 있으므로, 여러분의 jar에 함께 넣으면 클래스가 중복됩니다.

### plugin.yml

```yaml
softdepend: [Custom_Advancement]
```

`depend` 대신 `softdepend` 를 권장합니다. Custom Advancement 가 없어도
여러분의 플러그인은 켜지고, API 부분만 건너뛰면 되기 때문입니다.

---

## 빠르게 시작하기

```java
CustomAdvancementAPI api = CustomAdvancementAPI.get();
if (api == null) {
    getLogger().warning("Custom Advancement 가 설치되어 있지 않습니다.");
    return;
}

// 조회
CustomAdvancementInfo info = api.getAdvancement("first_diamond");
getLogger().info(info.getTitle() + " / 아이콘: " + info.getIcon());

// 달성 여부
if (!api.isGranted(player, "first_diamond")) {
    api.grant(player, "first_diamond");
}
```

`CustomAdvancementAPI.get()` 은 Bukkit의 `ServicesManager` 를 통해 가져옵니다.
정적 필드를 캐싱하지 마세요. 플러그인이 다시 로드되면 낡은 참조가 남습니다.

---

## 이벤트

### 달성했을 때 무언가 하기

```java
@EventHandler
public void onGranted(CustomAdvancementGrantedEvent event) {
    Player player = event.getPlayer();

    // 도전 과제(CHALLENGE)만 서버 전체에 알리기
    if (event.getAdvancement().getFrame() == FrameType.CHALLENGE) {
        Bukkit.broadcastMessage("§6" + player.getName() + " 님이 도전 과제를 깼습니다!");
    }
}
```

### 달성을 막기

```java
@EventHandler
public void onGrant(CustomAdvancementGrantEvent event) {
    // 미니게임 중에는 발전과제가 깨지지 않도록
    if (event.getPlayer().getWorld().getName().equals("minigame")) {
        event.setCancelled(true);
    }
}
```

`CustomAdvancementGrantEvent` 는 **취소 가능**하고, 취소하면 보상 명령어도 실행되지 않습니다.
`CustomAdvancementGrantedEvent` 는 이미 부여된 뒤라 취소할 수 없습니다.

### 원인 구분하기

두 이벤트 모두 `getCause()` 로 원인을 알려줍니다.

| 값 | 뜻 |
|---|---|
| `TRIGGER` | 설정된 자동 달성 트리거가 발동 |
| `COMMAND` | `/ca grant` 명령어 |
| `API` | 다른 플러그인이 `grant()` 호출 |

관리자가 명령어로 준 것은 허용하고 자동 달성만 막고 싶다면 이렇게 씁니다.

```java
if (event.getCause() == CustomAdvancementGrantEvent.Cause.TRIGGER) {
    event.setCancelled(true);
}
```

### 전체 이벤트 목록

| 이벤트 | 시점 | 취소 |
|---|---|---|
| `CustomAdvancementGrantEvent` | 부여 직전 | 가능 |
| `CustomAdvancementGrantedEvent` | 부여 완료 후 | 불가 |
| `CustomAdvancementRevokedEvent` | 회수 완료 후 | 불가 |
| `CustomAdvancementReloadEvent` | 설정 리로드 후 | 불가 |

---

## 주의할 점

**메인 스레드에서만 호출하세요.**
발전과제 부여는 플레이어 상태와 패킷 전송을 건드립니다. 비동기로 호출하면 서버가 불안정해집니다.

**`CustomAdvancementInfo` 를 오래 들고 있지 마세요.**
설정이 리로드되면 전부 새 객체로 교체됩니다. `CustomAdvancementReloadEvent` 를 받아
캐시를 비우거나, 필요할 때마다 다시 조회하세요.

**오프라인 플레이어는 조회할 수 없습니다.**
저장소를 비동기로 읽어야 해서 이 API(동기 방식)로는 정확히 답할 수 없습니다.
틀린 값을 돌려주느니 넣지 않는 편이 낫다고 판단했습니다.

---

## 직접 빌드하기

```bash
./gradlew build
```

결과물은 `build/libs/` 에 생성됩니다.
필요한 것은 JDK 21 뿐이며, 다른 준비물은 없습니다.

---

## 라이선스

MIT. 자유롭게 쓰세요.

이 라이선스는 **이 저장소의 API 코드에만** 적용됩니다.
Custom Advancement 플러그인 본체는 별도의 상용 라이선스를 따릅니다.
