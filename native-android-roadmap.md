# RichInsights — Native Android V2 Roadmap

> **Master V2 project roadmap.** This document is authoritative for product scope, implementation stages, sequencing, checkpoints, and Android architecture at the project level. Detailed backend/infrastructure decisions belong to `RichInsights-backend/frontend/database-architecture.md`; detailed experience/design decisions belong to `native-android-ui-ux.md`; commercial decisions belong to `native-android-commercial-monetization.md`.
>
> **Documentation rule:** preserve important decisions, avoid unnecessary duplication, and when a detailed decision changes, update its authoritative document first and then synchronize only the affected summaries here.

> **V2 foundation:** This repository is a clean native Android rebuild. V1 is a prototype/reference only; V1 code and behavior are not assumed to be production-ready.

## 1. Product identity

- **Company/studio:** Ricven Studios Limited
- **Product/platform:** RichInsights
- **Tagline:** **Grow. Excel.**
- **Repository:** `ricvenlimited/richinsights-android`
- **Android application/package identity:** `com.ricven.richinsights`

RichInsights is an educational platform, not only a quiz app. The initial platform centers on **Home, Learn, Quiz, Bible, and News**, with Profile & Settings as secondary/global navigation.

The native V2 architecture must make future learning tools, content libraries, study features, progress, accounts, and commercial capabilities possible without rebuilding the application shell.

## 2. V2 reset principle

V2 is a **new architecture**, not a port of React/Vite/Capacitor.

V1 may be inspected for:
- product lessons;
- useful UX ideas;
- known failures;
- requirements we do not want to repeat.

V1 must **not** be treated as a source of production-ready code, question data, API architecture, AdMob implementation, or application structure.

Do not mechanically copy V1 systems into V2.

## 3. Primary navigation

### Compact phones
**Home | Learn | Quiz | Bible | News**

### Secondary/global
**Profile & Settings** is accessed from the top app bar/profile action rather than taking a sixth primary slot.

### Adaptive behavior
On larger windows, primary navigation should adapt to a navigation rail or another appropriate adaptive pattern rather than forcing a phone-sized bottom bar. Android currently recommends 3–5 primary destinations for compact navigation and adapting navigation for larger screens.

Top-level destinations should retain their navigation state/back stack where appropriate. Navigation 3 is a strong candidate for V2 because it provides explicit back-stack control and adaptive navigation patterns; the exact library/version will be locked during implementation against the current project setup.

## 4. Core V2 architecture

Target stack:
- Kotlin
- Jetpack Compose
- modern Android architecture
- ViewModel
- Coroutines / Flow / StateFlow
- repository/data-source separation
- type-safe navigation approach
- Room where local structured persistence is required
- DataStore for preferences/settings
- native networking
- Hilt if dependency-injection complexity justifies it
- WorkManager only for genuine background work
- native Google Mobile Ads SDK
- backend/provider architecture selected for V2 requirements rather than inherited automatically from V1

Conceptual client flow:

```
Compose UI
   ↓
UI State / ViewModel
   ↓
Repository
   ↓
Remote + Local Data Sources
```

Provider-specific generation logic belongs outside the Android presentation layer.

## 5. Backend and infrastructure architecture

The current V2 backend direction is **Firebase-first**:
- Firebase Cloud Functions using Python for server-side/backend logic;
- Cloud Firestore as the initial database;
- Firebase Authentication;
- Firebase Cloud Storage;
- Firebase Cloud Messaging;
- Firebase Analytics;
- Firebase Crashlytics;
- Firebase Remote Config;
- Firebase App Check;
- protected backend secrets/Secret Manager;
- Google Play Billing for Android commercial flows when implemented.

SQL Connect/PostgreSQL is deliberately deferred. Cloudflare is not required for V2 initially and the V1 Cloudflare Worker remains a prototype/reference rather than the V2 foundation.

RichInsights is **online-first**, not broadly offline-first. Selected Home/Profile data may be cached, explicitly downloaded Learn content may work offline, and properly licensed Bible reading is intended to work offline. Quiz, News, Current Affairs, fresh Learn browsing/downloads, synchronization, and server-backed Bible features require internet.

The detailed backend, security, content-pipeline, connectivity, cost-control, Firebase, Cloudflare, and infrastructure decisions are maintained in **`RichInsights-backend/frontend/database-architecture.md`**. That document is authoritative for detailed backend architecture.

## 5. Content and question architecture

The question system is being rebuilt from the ground up.

The platform must separate:

1. **Content sources** — APIs, public/structured data, licensed data, manual authoring, curated packs, trusted sources.
2. **Ingestion/normalization** — convert source material into a consistent internal representation.
3. **Verification and validation** — factual, structural, answer, distractor, language, and quality checks.
4. **Question generation/authoring** — create legitimate question variants from verified material.
5. **Question bank** — store approved questions and their provenance.
6. **Selection engine** — choose appropriate fresh questions for a user/session.
7. **History** — record what the user has already seen.

Question identity must support at least:
- `factId`
- `familyId`
- `variantId`
- `questionId`
- category/topic
- difficulty
- provenance/source
- validation/lifecycle status

A wording change does not automatically make a genuinely new educational question. Family-level repetition must be controlled where appropriate.

### Scale requirement

V2 must be capable of growing to a very large question universe, potentially millions of legitimate variants over the life of the platform. Scale must come from verified content and meaningful variants, not low-quality duplication.

The system must continuously replenish content. A small exhausted cache must not be the normal failure mode.


### Security & reliability requirement

Security and reliability are **core V2 requirements** and begin with the architecture rather than being postponed until release.

The Android client must never be trusted as the authority for protected state. Authentication, authorization, Firestore/Storage rules, server-side validation, protected secrets, App Check where appropriate, abuse controls, dependency hygiene, monitoring, and safe error handling must be designed into the platform.

The platform must also account for provider/API outages, network instability, rate limits, malformed responses, service degradation, and recovery. Failures should be isolated, detected, handled safely, and communicated clearly without exposing internal details.

The authoritative security and reliability architecture is maintained in **`RichInsights-backend/frontend/database-architecture.md`**. Security and reliability testing are release requirements, not optional polish.

## 6. Provider independence

No single provider is the architecture.

Providers may be added, removed, replaced, or combined without rewriting the quiz engine. A provider outage, pricing change, quota, or quality problem should be isolated from the learner-facing quiz experience as much as reasonably possible.

The final provider mix is deliberately **not locked yet**.

## 7. Initial learning/content areas

The initial product direction includes:
- General Knowledge
- Science
- IT — Information & Technology
- Current Affairs
- Bible

**News** is a separate top-level product area and should not be treated as merely another Current Affairs API.

Bible is a dedicated platform section, not simply a quiz category.

## 8. Bible direction

Planned future Bible capabilities:
- offline reading using appropriately licensed text;
- book/chapter navigation;
- search;
- online Bible quizzes;
- future study tools;
- user-triggered read-aloud with play/pause/resume/stop controls.

WEB remains the leading translation direction from earlier planning, but licensing/distribution requirements must be verified before implementation or distribution.

Read-aloud must never start automatically when a chapter opens.

## 9. Network and local data behavior

RichInsights is **online-first / internet-required for most platform functionality**, not broadly offline-first.

Selected local/offline support is intentional:
- **Home:** only selected basic/cached portions may remain available offline; fresh/live content and recommendations require internet.
- **Profile:** selected cached profile data such as profile image, streaks, progress, and history may remain available offline; synchronization requires internet.
- **Learn:** only courses/content the user explicitly downloaded may be available offline; fresh browsing and downloads require internet.
- **Bible:** properly licensed Bible reading is intended to work offline, including book/chapter/verse navigation and reading.

The following require internet:
- Quiz;
- News;
- Current Affairs;
- fresh Learn browsing/downloads;
- fresh Home content/recommendations;
- Bible quizzes;
- server-backed Bible study features;
- synchronization/live content.

The app must provide deliberate states for:
- online;
- connecting;
- offline;
- connection lost;
- loading/requesting;
- empty result;
- temporary failure;
- retry;
- feature unavailable because internet is required.

Local persistence may use Room/DataStore and other appropriate local storage. Cached history must not silently become an offline quiz source when the product requires online quiz availability.

See `RichInsights-backend/frontend/database-architecture.md` for the authoritative connectivity and local-data boundaries.

## 10. Monetization and commercial architecture

V2 will use a fresh commercial architecture rather than copying V1.

Planned capabilities:
- AdMob advertising;
- optional rewarded ads;
- Premium subscription;
- Remove Ads;
- premium content/features;
- future content packs;
- Google Play Billing;
- centralized entitlement state.

Commercial enforcement is **not** an initial prerequisite for the native shell.

The client must not let individual question providers, screens, or question records independently decide Premium access.

## 11. Development and quality rules

Use:

**BUILD → INSTALL → TEST → FIX → DOCUMENT → CHECKPOINT → NEXT STAGE**

Requirements:
- keep V1 untouched while V2 is built;
- keep V2 buildable at every meaningful checkpoint;
- test on real Android hardware;
- add automated tests where they provide meaningful protection;
- do not optimize for feature count at the expense of reliability;
- document only decisions that remain useful to implementation.

## 12. Implementation stages

### Stage 0 — Requirements and architecture
**Status: documented / checkpointed**

Product identity, V2 reset principle, navigation, architecture boundaries, content/question requirements, and major quality requirements are documented.

### Stage 1 — Native application foundation
**Status: in progress**

Stage 1 is being executed strictly in order. We do not jump ahead to backend/content features while a foundation step is still being established.

#### Stage 1 execution order

1. **Create the native Android project**
   - Kotlin
   - Jetpack Compose
   - application/package identity: `com.ricven.richinsights`
   - native Android project structure

2. **Establish the Gradle/project foundation**
   - Gradle configuration
   - SDK configuration
   - dependency management
   - Compose/build configuration

3. **Establish the initial RichInsights design system**
   - theme foundation
   - color tokens
   - typography
   - spacing/shapes conventions
   - reusable UI foundation
   - light-theme foundation with structure ready for dark theme later
   - existing placeholder connected to `RichInsightsTheme`

4. **Establish the navigation shell**
   - initial primary navigation architecture
   - Home / Learn / Quiz / Bible / News shell
   - Profile & Settings remains secondary/global
   - navigation structure only; no premature full product screens

5. **Firebase foundation**
   - create/connect the RichInsights Firebase project
   - register `com.ricven.richinsights`
   - add Firebase configuration
   - connect Firebase to Android
   - verify the connection with a minimal test
   - do **not** yet populate Firestore collections, quiz questions, authentication flows, Cloud Functions, or production content logic

   **Important sequencing note:** Firebase was intentionally paused while the product identity layer was being properly defined. The Firebase step remains next after the brand/launch work is sufficiently settled.

6. **Build and install the first V2 APK**
   - build the real project
   - install on a physical Android device
   - verify launch and foundation behavior

7. **GitHub checkpoint**
   - commit the working foundation
   - push to `ricvenlimited/richinsights-android`

8. **Stage 1 checkpoint**
   - verify the foundation
   - resolve remaining issues
   - freeze the Stage 1 foundation
   - proceed to Stage 2

#### Brand & Launch Identity checkpoint inserted before Firebase implementation

The product identity work was identified as important before moving into Firebase integration. It does **not** replace or restructure the navigation shell.

The identity work covers:
- RichInsights brand mark/symbol;
- launcher/app icon direction;
- adaptive icon requirements;
- `Grow. Excel.` tagline usage;
- Android launch/splash experience;
- Design-tool-based visual exploration and editable design assets.

**Design-tool transition note:** Figma remains the reference/archive workspace for the existing **RichInsights — Brand & Icon Concepts** exploration. Because the connected Figma Starter/View setup imposes MCP usage limits that can interrupt the planned workflow, **Penpot is now the active design workspace for continuing the brand/launch work and subsequent UI/UX design work**. This is a workflow change, not a product or architecture change. The existing Figma work is preserved and may still be used later.

The project will continue through the remaining Stage 1 steps once the brand/launch direction is sufficiently settled:
**brand/launch work → Firebase foundation → first V2 APK → GitHub checkpoint → Stage 1 checkpoint → Stage 2**.

Current state:
- **Brand/icon direction:** option C has been selected as the RichInsights brand direction.
- **Launch experience:** the Compose brand intro is integrated and has been tested on a physical Android device.
- **Launch transition checkpoint:** PR #5 was squash-merged after the user verified the change. The navigation shell is prepared behind the brand overlay, the unnecessary 500 ms post-tagline hold is removed, and the final overlay fade is 180 ms to avoid revealing an empty navy screen.
- **Documentation checkpoint:** roadmap and UI/UX status have been synchronized with the verified implementation.
- **Next step:** Firebase foundation, following Stage 1 Step 5. Do not jump ahead to full authentication flows, Firestore content collections, Cloud Functions, quiz data, or production content logic.

This identity checkpoint is sufficiently settled to resume the planned Stage 1 sequence. It does not invalidate Steps 1–4 or require the navigation shell to be rebuilt.

### Stage 2 — Platform UI foundation
- RichInsights visual system
- reusable components
- adaptive layouts
- loading/error/empty states
- accessibility foundations
- Home/Learn/Quiz/Bible/News shell

### Stage 3 — Content/domain contracts
- category/topic models
- question/fact/family/variant/ID models
- provenance and validation metadata
- quiz-session/result models
- stable API contracts

### Stage 4 — Quiz engine
- session state
- question presentation
- answer handling
- timer
- scoring
- progression
- completion/results
- failure/retry states

#### Quiz navigation and behavior architecture — planned

**Status: architectural planning documented; not yet implemented.** This section defines the intended V1 quiz experience and implementation boundaries. It must not be interpreted as a claim that these screens or behaviors already exist in the Android app.

##### Product entry and navigation

The existing top-level navigation remains **Home | Learn | Quiz | Bible | News**. The Quiz destination is the hub for quiz-specific experiences; this decision does not add a sixth primary navigation destination.

The Quiz Hub separates two user intentions:

- **Learn / Practice:** self-paced quizzes selected by subject, topic, or available quiz. Untimed by default; learning and review take priority over speed.
- **Challenge:** Daily Challenge, Exam Simulation, and Competitive Challenges. Each challenge declares its rules before the attempt begins, including whether a timer applies, duration, scoring, timeout handling, and when answers/explanations become available.

The UI should communicate availability and relevant progress where useful without making all modes appear equally urgent. Challenge-specific details must be presented before the user starts.

##### Shared engine and mode-specific rules

Use one shared quiz engine for common mechanics while keeping mode policies separate. Shared responsibilities include question presentation, session/attempt state, answer submission, progression, progress tracking, scoring primitives, completion, results, and failure/retry handling.

Mode policies control timing, feedback/reveal behavior, scoring details, timeout handling, review availability, and any challenge-specific constraints. Do not fork the entire quiz engine for each mode, and do not let learning-mode assumptions leak into exams or competitive challenges.

##### Standard user flow

1. **Quiz Hub:** choose Learn / Practice or a challenge.
2. **Selection / details:** choose a subject, topic, or quiz in Learn; for a challenge, review its rules, question count, applicable duration, scoring, and relevant restrictions.
3. **Quiz in progress:** show the question, answer options, and progress. Show a timer only when the active mode requires one. Provide a clear exit action.
4. **Answer feedback:** tapping an answer submits it immediately; there is no separate Confirm button. Prevent duplicate submissions and make the selected/submitted state clear.
5. **Completion and results:** show score and performance, correct/incorrect/unanswered counts where applicable, and a clear next action. Provide answer review and explanations when allowed by the mode and available for the question. Challenge results may include challenge-specific comparison/ranking only when supported and appropriate.

##### Answer feedback and progression

Initial design/testing targets—not immutable product constants—are:

- Correct-answer feedback: approximately **800 ms**.
- Incorrect-answer feedback: approximately **1,500 ms**, with the correct answer revealed where the active mode permits it.
- Keep feedback durations configurable so usability testing can refine them.

The UI must represent answer states clearly, including default, pressed/selected, correct, incorrect, and long-answer text. Avoid a separate locked-answer state unless a real product rule requires it.

The policy for moving to the next question (automatic advance versus an explicit Next action) should be mode-configurable and validated through realistic-device usability testing. Feedback must remain accessible and understandable; do not make fast animation the only indication of correctness.

**Exam Simulation:** do not reveal correct answers during an active assessment by default. Reveal answers and explanations only at the point defined by the exam policy (for example, after submission or after an allowed review stage).

**Timed challenges:** feedback animation or presentation must not introduce an accidental or unfair timing penalty. Each timed mode must explicitly define whether the clock is session-wide or per-question, whether it continues during feedback/backgrounding, and what constitutes the authoritative deadline. Competitive scoring and timing must not rely on a client-side timer as the sole authority.

##### Timeout, interruption, and recovery

- **Timeout:** treat an expired unanswered question as unanswered, not as a user-selected incorrect answer, unless a documented challenge scoring rule explicitly defines a different score treatment. Apply the declared timeout policy exactly once. Reveal the correct answer only when permitted by that mode.
- **Exit:** warn before abandoning an active attempt. Preserve or discard progress according to the attempt/mode policy and communicate the consequence clearly.
- **Interruption/process recreation:** restore an eligible attempt and its state where the mode allows it. Timed modes must reconcile against a trustworthy deadline rather than silently resetting time.
- **Network failure:** Quiz requires internet under the current online-first product direction. Show clear connection/failure/retry states, prevent duplicate submissions, and avoid losing an answer already accepted locally while clearly distinguishing locally recorded state from server-confirmed state.
- **Completion/rewards:** prevent duplicate final submissions, scores, and reward grants. Treat server-side validation as authoritative for protected competitive results and rewards.

##### Domain and implementation boundaries

Represent the selected mode and its rules explicitly in the session/attempt domain model rather than scattering mode checks across Compose screens. Keep UI state, domain/session rules, repository access, and remote/local data sources separated in line with the core V2 architecture. Persist only the state needed for supported recovery; do not treat cached quiz history as permission to run quizzes offline.

##### Acceptance criteria before implementation is considered complete

- Learn / Practice starts untimed unless an explicitly selected product rule says otherwise.
- Challenge details and applicable timing/scoring rules are visible before starting.
- Answer selection submits once, with duplicate taps safely ignored.
- Exam answer-reveal behavior follows the declared assessment policy.
- Timeout, exit, interruption, retry, and completion each have explicit, tested state transitions.
- Results and any rewards cannot be duplicated by retries or repeated requests.
- Loading, empty, offline, unavailable, and recoverable failure states are designed and tested.
- Important flows are validated at realistic Android screen sizes and accessibility/font-scale settings.
- Debug build and relevant automated tests pass before the work is considered implemented.



### Stage 5 — Backend/content integration
- connect to the selected V2 backend/content services
- network handling
- content validation boundaries
- provider-independent contracts

### Stage 6 — History and freshness
- local history persistence
- exact-question and family-level repetition rules
- selection/freshness integration
- replenishment behavior

### Stage 7 — Initial content rollout
Bring categories online incrementally:
- General Knowledge
- Science
- IT — Information & Technology
- Current Affairs
- Bible quiz capability where ready

### Stage 8 — Bible platform experience
- offline reader
- licensed content
- search/navigation
- read-aloud controls
- Bible-specific study foundations

### Stage 9 — News and broader platform features
- News experience
- progress/account foundations
- future learning resources
- additional educational modules

### Stage 10 — Commercial and release hardening
- native AdMob
- Google Play Billing when required
- entitlement enforcement
- performance/accessibility/security testing
- release preparation

Stages can be subdivided as implementation progresses. A stage is complete only when its scope is tested and documented.

## 13. Definition of success

V2 succeeds when RichInsights has a maintainable native Android foundation with:
- clear platform navigation;
- strong quiz/session behavior;
- scalable and provider-independent content architecture;
- validated, traceable questions;
- robust repetition prevention and freshness;
- continuous content replenishment;
- deliberate Bible support;
- reliable network behavior;
- adaptive native UI;
- clean separation of client, content, infrastructure, and commercial concerns;
- room for future educational products.

**V2 is an educational platform foundation — not V1 rebuilt with a new UI.**

### Current Stage 1 checkpoint

The native project, Gradle foundation, initial design system, and navigation shell are established. The Brand & Launch Identity checkpoint is now sufficiently settled to resume Stage 1 Step 5:
- selected brand direction: option C;
- Compose brand intro implemented and physically device-tested;
- PR #5, **Fix brand intro to Home transition**, squash-merged after user verification;
- app navigation is composed behind the brand overlay; the extra post-tagline hold was removed and the final fade shortened to 180 ms to avoid a blank navy pause.

**Next action: Firebase foundation.** Create/connect the RichInsights Firebase project, register com.ricven.richinsights, configure Firebase in Android, and verify the connection with a minimal test. Keep authentication flows, Firestore content collections, Cloud Functions, quiz data, and production content logic out of scope for this initial Firebase connection checkpoint. After implementation, build, install, test, document, and checkpoint before proceeding.