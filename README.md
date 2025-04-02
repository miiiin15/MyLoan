![헤더](https://capsule-render.vercel.app/api?type=rect&height=100&color=EADDFF&text=MyLoan%20💸&fontColor=6750A4&animation=fadeIn&fontSize=45&desc=모바일%20대출신청%20절차%20체험%20해보기&descAlignY=80&fontAlignY=40&descSize=20&textBg=false)
# 기술 스택

- **구조**: MVVM, MVI
- **기본**: Kotlin, compose, Coroutines, Room
- **빌드**: Multi Module: 기능별 분리(feature), Precompiled Scripts
- **의존성 주입(DI)**: koin
- **UI**: material3
- **SDK**:
  - **Firebase**: Realtime Database, firestore
  - **Logging**: Timber

# 특징

### MVI 패턴 기반 화면 상태 관리와 사용자 이벤트 처리
- 구조 예시 [📍LoanApplyPolicyContract.kt](feature_list/src/main/kotlin/com/miiiin15/myloan/list/presentation/screen/apply/policy/LoanApplyPolicyContract.kt)
```kotlin
// Intent 정의
sealed interface ViewIntent : MviIntent {
    object Initial : ViewIntent
    object Back : ViewIntent
    data class PolicyChecked(val checkedList: List<String>, val allPolicyChecked: Boolean) : ViewIntent
    // ...
}

// 부분 상태 변화
sealed interface PartialStateChange {
    fun reduce(viewState: ViewState): ViewState

    data class PolicyChecked(val checkedList: List<String>, val isAllPolicyChecked: Boolean) :
        PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(
                checkedPolicyList = checkedList,
                isAllPolicyChecked = isAllPolicyChecked
            )
    }
    // ...
}

// ViewState 정의
data class ViewState(
    val policyList: List<PolicyItem>,
    val checkedPolicyList: List<String>,
    val isLoading: Boolean,
    // ...
) : MviViewState
```
- ViewModel 예시 [📍LoanApplyPolicyViewModel.kt](feature_list/src/main/kotlin/com/miiiin15/myloan/list/presentation/screen/apply/policy/LoanApplyPolicyViewModel.kt) / [ℹ️AbstractMviViewModel.kt](feature_base/src/main/kotlin/com/miiiin15/myloan/base/presentation/viewmodel2/AbstractMviViewModel.kt)
```kotlin
  class LoanApplyPolicyViewModel(
    // ...
) : AbstractMviViewModel<ViewIntent, ViewState, SingleEvent>() {
    override val viewState: StateFlow<ViewState>

    init {
        // Intent Flow를 PartialStateChange로 변환 → ViewState로 누적
        viewState = merge(
            intentSharedFlow.filterIsInstance<ViewIntent.Initial>().take(1),
            intentSharedFlow.filterNot { it is ViewIntent.Initial }
        ).shareWhileSubscribed()
            .toPartialStateChangeFlow() // SharedFlow<ViewIntent>를 받아, 각 인텐트 유형별로 PartialStateChange로 변환
            .sendSingleEvent() // PartialStateChange를 처리하면서, 변화(예: 오류, 성공, 뒤로가기 등)에 대해 SingleEvent를 발생
            .scan(ViewState.initial()) { vs, change -> change.reduce(vs) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, ViewState.initial())
    }
    // ...
}
```
- Compose View(Fragment) 예시 [📍LoanApplyPolicyFragment.kt](feature_list/src/main/kotlin/com/miiiin15/myloan/list/presentation/screen/apply/policy/LoanApplyPolicyFragment.kt)
```kotlin
@Composable
fun LoanApplyPolicyScreen(viewModel: LoanApplyPolicyViewModel) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val intentChannel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }
    val dispatch = remember { { intent: ViewIntent -> intentChannel.trySend(intent).getOrThrow() } }

    // Intent 전송 및 수집
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            intentChannel.consumeAsFlow()
                .onStart { emit(ViewIntent.Initial) }
                .onEach(viewModel::processIntent)
                .collect()
        }
    }

    // 유틸리티를 통해 Compose UI에서 Flow를 구독하며, 단일 이벤트 수집
    viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is SingleEvent.Failure -> {
              // event.errorMessage 값을 받아 UI 처리 예) Alert 등..
            }
            is // 다른 이벤트..
        }
    }

    // UI 상태에 따라 화면 렌더링
    LoanApplyPolicyContent(
        viewState = viewState,
        onPolicyCheck = { list, checked -> dispatch(ViewIntent.PolicyChecked(list, checked)) },
        onBackClick = { dispatch(ViewIntent.Back) },
        // ...
    )
}
```

# 기능
<table border="1" style="text-align: center; width: 100%; margin-bottom: 40px;">
  <tr>
    <th colspan="3">인앱 화면</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/fbe1e0cb-d539-4924-a6d5-3724634434f6" alt="1" height="500"></td>
    <td><img src="https://github.com/user-attachments/assets/4b96f85d-650c-42a0-a572-c3ff054bfa1d" alt="2" height="500"></td>
    <td><img src="https://github.com/user-attachments/assets/94a2e34a-1dfe-41e1-9321-9bf02096db78" alt="3" height="500"></td>
  </tr>
</table>

- 모의로 대출 신청 절차 진행 (약관, 적합성, 정보입력, 조회 등..) - 개발 진행 중
- 대출 신청 후 승인 상태로 변경 되면 모의 대출 실행 - 예정
- 모든 신청/실행 절차는 진행 상태를 매 단계마다 원격 DB(firebase)에 저장 - 예정
- 앱 이탈 후 재진입 시 원격(firebase)에 저장된 절차를 조회해서 대출 이어하기 기능 - 예정

# 환경

- Kotlin: 2.0.0
- AGP: 8.7.2
- compose: 1.8.1
- composeMaterial: 1.3.2
- coroutines: 1.10.2
- koin: 3.5.6


