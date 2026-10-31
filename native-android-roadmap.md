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
**Status: foundation implemented and checkpointed; one non-blocking Analytics verification item remains.**

Stage 1 established the native Android application, initial design system, primary navigation shell, RichInsights launch identity, and initial Firebase Android configuration. It is not a claim that the full Firebase backend or production data services are implemented.

#### Stage 1 implementation record

1. **Native Android project — implemented**
   - Kotlin + Jetpack Compose;
   - application/package identity: `com.ricven.richinsights`;
   - native Android project structure.

2. **Gradle and SDK foundation — implemented**
   - Gradle, SDK, dependency and Compose configuration are present;
   - the current app module configuration is authoritative: `compileSdk 37`, `targetSdk 37`, **`minSdk 24`**;
   - older design/splash handoff notes that state `minSdk 23` are superseded by the current Gradle configuration and must not be used to infer API 23 support.

3. **RichInsights design system — implemented foundation**
   - Material 3 light/dark color schemes;
   - current brand color tokens;
   - bundled Plus Jakarta Sans (400/600/700);
   - Material 3 shape tokens (8/12/16/20/28 dp);
   - shared spacing tokens (4/8/12/16/24/32/40 dp).
   - See `native-android-ui-ux.md` and the theme source files for the current implementation.

4. **Primary navigation shell — implemented**
   - Home (currently labelled “For You” in the top-level UI) / Learn / Quiz / Bible / News;
   - Profile & Settings is a secondary/global top-app-bar action;
   - Navigation 3 back-stack foundation is present;
   - destination bodies remain placeholders; adaptive navigation for larger windows and full destination experiences are Stage 2 work.

5. **Brand and launch identity — implemented checkpoint**
   - Option C is the selected brand/icon direction; concept selection is closed unless a concrete usability or production issue justifies revisiting it;
   - Penpot is the active editable design workspace; Figma remains reference/archive;
   - Compose brand intro and launch-to-app transition were installed and tested on a physical Android device;
   - PR #5 was squash-merged after user verification; the extra post-tagline hold was removed and the final overlay fade is 180 ms;
   - remaining launcher asset/mask, small-size, and trademark checks are validation tasks, not reasons to reopen concept selection.

6. **Initial Firebase Android configuration — present**
   - Firebase project configuration for `richinsights-v2`;
   - Android package registration and Google Services Gradle plugin;
   - Firebase Analytics dependency;
   - recorded debug build passed.
   - Analytics event delivery has not yet been independently verified. Authentication flows, Firestore schema/security rules, Storage rules, Cloud Functions and production backend operations are not implemented by Stage 1.

7. **Build and user verification**
   - the recorded Stage 1 debug workflow passed and produced an APK artifact;
   - the launch experience was tested by the user on a physical device;
   - future implementation changes still require their own debug build and user testing.

#### Stage 1 closeout

The native foundation and initial Firebase configuration are sufficient to proceed to Stage 2. Analytics event-delivery verification remains a tracked, non-blocking follow-up and must be completed before treating Analytics as operationally verified. Do not restart completed native, design-system, navigation, or launch work merely to begin Stage 2.

### Stage 2 — Coordinated platform UI, identity and data-security foundation

**Status: planned; execution has not yet begun.** The current working branch, `codex/stage-2-platform-ui-foundation`, is the documentation and implementation starting point for this stage.

Stage 2 contains two coordinated workstreams, not two isolated phases that must be completed one after the other:

- **Stage 2A — Platform UI foundation**
- **Stage 2B — Firebase identity and user-data security foundation**

The workstreams should proceed in parallel where their contracts are clear and converge at defined integration gates. Neither workstream may invent or simulate the responsibilities of the other. UI can be designed and built against explicit state/repository interfaces while Firebase identity and authorization are implemented; protected, account-dependent behavior must not be presented as functional until its Stage 2B dependency is real and tested.

#### 2A — Platform UI foundation

**Purpose:** complete the reusable, accessible Compose presentation foundation and prepare the core product areas for real data and identity integration.

Scope:
- audit and extend the existing theme, tokens and reusable Compose components; preserve working Stage 1 foundations;
- implement adaptive navigation/layout behavior for compact phones, larger phones, tablets, foldables, landscape and resizable windows;
- accessibility: semantics, focus/navigation behavior, touch targets, contrast verification, TalkBack and font scaling;
- shared UI state patterns for loading, content, empty, retryable error, permission-denied, offline, service-unavailable and signed-out states;
- establish the presentation shells and interaction requirements for Home, Learn, Quiz, Bible and News;
- design account entry, sign-in, account creation, recovery and Profile & Settings experiences in coordination with the approved authentication lifecycle;
- define screen-level UI state contracts and ViewModel boundaries without wiring Compose directly to Firebase SDKs;
- test important layouts at realistic device sizes and accessibility settings.

Exit criteria:
- reusable components and documented usage patterns are implemented;
- adaptive navigation and core layouts are verified on representative window sizes;
- accessibility checks cover semantics, touch targets, contrast and font scaling;
- core product destinations have intentional screen structures and state handling rather than empty placeholder bodies;
- account-related screens match the approved Stage 2B lifecycle and do not simulate successful authentication;
- UI is connected through ViewModels/repository contracts, not direct Firebase calls.

#### 2B — Firebase identity and user-data security foundation

**Purpose:** establish real identity, authorization and safe data access in parallel with the UI contracts that depend on them.

Scope:
- inspect and document the actual Firebase project/environment, enabled services, IAM roles, billing state and existing resources before changing anything;
- establish development/production environment separation and environment-specific Android configuration; use Firebase Emulator Suite or an isolated development project for tests;
- define the agent/automation permission policy: read/prepare by default; explicit approval for resource changes; no production deployment or destructive operation without specific authorization;
- decide the initial sign-in/account-creation providers before implementation; provider selection is a decision gate, not an assumption;
- implement Firebase Authentication lifecycle for approved providers: sign-in, account creation, sign-out, session restoration, account linking/recovery where required, and clear failure handling;
- define the minimum initial Firestore model and ownership boundaries needed by Stage 2 and the next dependent features; do not prebuild every future collection;
- implement Firestore Security Rules and tests for allowed, denied, cross-user and unauthenticated access;
- define repository/data-source boundaries so Compose and ViewModels do not call Firebase directly;
- handle auth-state changes, invalid/expired sessions, permission failures, network loss, retries and recoverable errors;
- establish safe local persistence boundaries; never store passwords or private provider credentials in app preferences;
- plan App Check and protected server-side operations at the appropriate point; do not treat client checks as authorization;
- verify with Auth/Rules tests, emulator or isolated-project integration tests, and a debug build.

Exit criteria:
- the approved provider set and account lifecycle are documented and implemented;
- authenticated identity is the basis for protected user-owned data access;
- Firestore rules enforce ownership and deny unauthorized access, with automated positive and negative tests;
- all app data access uses the agreed repository boundary;
- signed-out, signed-in, loading, permission-denied, offline and recoverable-failure states are handled deliberately;
- development/production configuration and the no-unapproved-deployment policy are documented;
- the user has installed and tested the debug APK before the Stage 2 checkpoint is accepted.

#### Shared 2A/2B integration plan and dependency gates

1. **Discovery and contracts:** inspect the existing UI/navigation and Firebase project; decide account requirements/providers and identify the first user-owned data needs. Record decisions before implementation.
2. **Parallel foundations:** build reusable UI/state components and the Auth/Firestore security foundation in parallel. Agree on interfaces first: authentication state, repository contracts, result/error types and loading/permission/offline states.
3. **Account-flow integration:** implement the account UI against the real Auth repository; verify signed-out/signed-in transitions, session restoration, sign-out and failure/recovery paths.
4. **Protected-data integration:** only after the corresponding Firestore model and rules exist and pass tests, connect the relevant repository and UI. No mock-auth or client-only authorization shortcut.
5. **End-to-end verification:** run unit, UI, Auth and Firestore Rules tests; test network loss and permission failures; run the debug workflow; inspect the APK and wait for user testing.
6. **Checkpoint:** review the actual code, rules, tests, configuration and documentation together. Do not open a PR, merge, deploy or promote an environment without the user's explicit authorization.

#### Stage 2 boundaries

- Stage 2 establishes identity, baseline authorization and repository boundaries; it does not implement all future user-data collections or every backend service.
- Stage 3 content/domain contracts must define and secure any new user-owned data dependency before dependent features are implemented.
- No AI provider or end-user AI feature is selected or implemented in Stage 2. Stage 2 should preserve a secure server-side integration boundary so future AI features can be added without exposing provider keys or calling privileged providers directly from Android.
- Future AI-assisted capabilities are planned across relevant product areas (including Learn, Quiz/Bible Quiz, News and selected Home experiences), but their product requirements, sources, safety/quality controls, provider choice, usage limits and backend contracts belong to later design and implementation decisions. Bible reading itself is not assumed to require AI.
- Firebase Console's Production environment label is a warning/identification aid, not an access-control or deployment-approval mechanism. IAM permissions, environment separation, tooling approvals and deployment controls remain necessary.

### Stage 3 — Content/domain contracts
- category/topic models
- question/fact/family/variant/ID models
- provenance and validation metadata
- quiz-session/result models
- stable API contracts
- identify any user-owned data contracts required by the quiz/history features and confirm their authorization requirements before dependent implementation

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
- connect content and platform services to the established repository/data-source boundaries
- implement the backend/content services required by the approved domain contracts
- network handling and provider-independent contracts
- server-side validation and authorization for protected operations
- add service-specific Firestore/Storage rules and tests before exposing each new protected data path
- do not treat Stage 2B as a substitute for the security design and tests required by later collections, functions, or services

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
- expanded profile, progress, and account-dependent product features built on the identity/security foundation established in Stage 2B
- future learning resources
- additional educational modules

**Sequencing clarification:** Stage 9 is for expanded account/profile/progress experiences—not the first implementation of Firebase Authentication, persistent identity, or the security foundation for user-owned data. Those prerequisites must be completed in Stage 2B before dependent features are built.

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

### Current project checkpoint

- **Current working branch:** `codex/stage-2-platform-ui-foundation`.
- **Stage 1:** native Android foundation, design-system foundation, navigation shell, launch identity and initial Firebase Android configuration are in place. The recorded Stage 1 debug build passed. Analytics event delivery remains unverified and is a non-blocking verification follow-up.
- **Current Android SDK configuration:** `compileSdk 37`, `targetSdk 37`, `minSdk 24` in `app/build.gradle.kts`. This supersedes older handoff/spec references to minSdk 23.
- **Brand direction:** Option C is selected; Penpot is active and Figma is retained as reference/archive.
- **Stage 2:** documentation is being finalized on the current working branch. No Stage 2 implementation or Firebase Auth/Firestore security work is claimed complete.
- **Next action:** complete the coordinated 2A/2B documentation and discovery, inspect the live Firebase environment and agent permissions, then begin implementation through explicit UI/backend contracts.