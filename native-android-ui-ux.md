# RichInsights — Native Android V2 UI/UX Blueprint

## 1. Purpose

This document defines the visual and user-experience direction for **RichInsights**, the native Android educational platform.

**Document boundary:** this file is authoritative for visual identity, UX principles, navigation experience, screen behavior, interaction patterns, accessibility, launch/splash experience, and user-facing connectivity states. It does not replace the roadmap, backend architecture, or commercial architecture documents.

**Documentation rule:** important design decisions are retained here; implementation and infrastructure details should be referenced from their authoritative documents instead of being duplicated unnecessarily.

V2 is a new native experience. V1 is reference material only and must not constrain the new design.

## 2. Product experience

**Product:** RichInsights  
**Tagline:** **Grow. Excel.**

Core feeling: **Clean Competitive**

RichInsights should feel:
- intelligent;
- modern;
- polished;
- energetic;
- trustworthy;
- accessible;
- educational without feeling childish.

The design should support RichInsights as a broader educational platform rather than making the product feel like a quiz app with extra sections.

Avoid:
- excessive gradients;
- visual clutter;
- generic unmodified Material styling;
- overly corporate styling;
- childish game styling;
- unnecessary decoration;
- separate visual identities for individual sections.

## 3. Visual identity

RichInsights uses a **Deep Navy → Intelligent Blue → Electric Cyan → restrained Gold** hierarchy. The values below match the current Android theme tokens in `ui/theme/Color.kt`; this implementation replaces the older palette values previously recorded here.

### Current brand and UI tokens

- **Deep Navy — `#0B2970`**: strong brand surfaces and the light theme's strong primary container.
- **Intelligent Blue — `#1769E0`**: primary actions and the light theme's primary color.
- **Electric Cyan — `#19B5FE`**: signature accent and the dark theme's primary color.
- **Warm Gold — `#F4B942`**: restrained achievement accent.
- **Standard surface / app canvas — `#FFFFFF`**.
- **Pale-blue surface accent — `#E8F7FF`**.
- **Outline — `#D9E2F2`**; **progress track — `#E3EAF7`**.
- **Dark background — `#061332`**; **dark surface — `#0D1F49`**; **dark outline — `#1C3672`**.
- **Primary text on light surfaces — `#0D0D0D`**; **body text — `#2C2C2C`**; **secondary text — `#565656`**.
- **Text on strong/dark surfaces — `#FFFFFF`**; **secondary text on dark surfaces — `#B6BCC8`**.
- **Success fill/icon — `#22A06B`**; **success text — `#167A4C`**.
- **Light-theme error — `#C93B3B`**; **dark-theme error — `#FFB4AB`**.

### Theme behavior

The app uses a light and dark Material 3 color scheme selected from the system theme. In the light scheme, Blue is primary, Cyan secondary, and Gold tertiary; in the dark scheme, Cyan is primary and Gold remains tertiary. Material 3 `surfaceTint` uses Blue in light mode and Cyan in dark mode. Compatibility aliases in the theme preserve existing component references.

The palette should remain controlled; sections should not introduce competing brand palettes. Check real foreground/background combinations for contrast before treating the tokens as accessibility-verified. The values above document the current implementation, not a claim that every pair has passed a full contrast audit.

## 4. Native design foundation

Use **Kotlin + Jetpack Compose** with a reusable RichInsights design system.

The design system will establish:
- typography;
- color roles;
- spacing;
- shapes;
- elevation;
- buttons;
- cards;
- inputs;
- dialogs;
- feedback states;
- loading/error/empty states;
- navigation;
- accessibility behavior;
- reusable product components.

Material 3 may provide underlying primitives, but RichInsights must have its own visual identity and token system.

**Implemented foundation:** the current theme defines the light/dark color schemes, Plus Jakarta Sans typography, Material 3 shapes (8/12/16/20/28 dp), and shared spacing tokens (4/8/12/16/24/32/40 dp). The current token definitions in `ui/theme/Color.kt`, `Theme.kt`, and `Type.kt` are authoritative; keep this summary aligned when those files change.

## 5. Brand & Launch Identity

This section defines the product identity layer that sits around the application UI. It is deliberately established before backend integration so later implementation does not force branding decisions into technical structure.

### 5.1 Brand architecture

The product identity has three distinct layers:

1. **Brand mark / symbol**
   - the recognizable visual symbol associated with RichInsights;
   - must remain identifiable at very small sizes;
   - should communicate intelligence, growth, learning, insight, or a related product concept without becoming a literal generic education icon;
   - must work independently from the wordmark.

2. **Wordmark / product name**
   - **RichInsights**
   - used where there is enough visual space to communicate the product name clearly;
   - typography and exact treatment will be finalized with the brand asset work.

3. **Tagline**
   - **Grow. Excel.**
   - supports the product promise but is not part of the compact launcher icon;
   - should appear selectively in launch, onboarding, store/marketing, and other appropriate branded surfaces.

The three layers must feel like one identity rather than three unrelated graphics.

### 5.2 App icon direction

The Android launcher icon is a high-priority brand asset because it is one of the user's first visual encounters with RichInsights.

The icon must:
- be recognizable without the app name;
- remain legible at small launcher sizes;
- work in Android adaptive-icon contexts;
- retain a strong silhouette;
- use the established RichInsights palette;
- avoid unnecessary text;
- avoid placing **Grow. Excel.** inside the icon;
- avoid tiny details that disappear at small sizes;
- avoid looking like a generic quiz, school, book, trophy, or finance icon unless such imagery is deliberately transformed into a distinctive RichInsights symbol.

The icon should primarily use the established visual hierarchy:

**Deep Navy → Intelligent Blue → Electric Cyan → restrained Gold**

Gold should remain an accent rather than becoming the dominant icon color.

### 5.3 Icon concept decision

**Option C is the selected RichInsights brand/icon direction.** The earlier six-way exploration is no longer an open selection task. Penpot remains the active workspace for retaining and refining the editable source assets.

The selected direction must remain distinctive, recognizable at launcher size, and consistent with the established palette. Any further refinement should preserve the approved direction rather than reopening concept selection without a clear usability or implementation reason. Validate the final assets across Android adaptive-icon masks and realistic small-size launcher previews before treating every asset variant as complete.

### 5.4 Adaptive icon requirements

The Android implementation will use the proper adaptive icon structure rather than a single flattened image where practical.

The final asset package should account for:
- foreground artwork;
- background treatment;
- safe visual area;
- launcher masking;
- different launcher shapes;
- small-size legibility;
- possible light/dark launcher environments.

Important visual elements must remain inside the safe area and must not depend on the exact launcher mask shape.

### 5.5 Icon asset system

Production branding will eventually provide the required Android resources, including:
- adaptive icon foreground;
- adaptive icon background;
- legacy/fallback launcher treatment where needed;
- appropriate density/resource variants;
- transparent artwork where required by the adaptive-icon structure.

The source artwork should be retained in an editable/vector-friendly form so future refinements do not require rebuilding the brand from a flattened screenshot.

### 5.6 Tagline usage

The official tagline is:

**Grow. Excel.**

It should be treated as a supporting brand statement, not a mandatory UI label.

Appropriate uses may include:
- launch/splash experience;
- onboarding;
- selected empty or welcome states;
- store listing and marketing materials;
- promotional graphics;
- selected brand-forward moments.

It should generally not appear:
- inside the launcher icon;
- repeatedly on every screen;
- inside compact navigation;
- in technical configuration;
- in places where it competes with functional content.

The tagline must remain visually secondary to the RichInsights name and core product action.

### 5.7 Launch and splash experience

The launch experience should create a polished first impression while remaining fast and restrained.

Conceptual sequence:

**RichInsights symbol → subtle brand motion → RichInsights → Grow. Excel. → application**

This is a conceptual direction, not a locked animation storyboard.

Requirements:
- use the final approved brand mark;
- avoid long splash delays;
- do not make users wait for decorative animation;
- respect Android's native splash-screen behavior;
- transition cleanly into the main RichInsights experience;
- remain performant on lower-end Android devices;
- avoid automatically starting product content or audio during launch.

The current Compose launch sequence is implemented and has been verified by the user on a physical Android device. The first-launch experience introduces the mark, resolves into the wordmark, then reveals **Grow. Excel.** The returning-launch experience uses the shorter wordmark/tagline treatment. After the tagline appears, transition directly into the already-composed app shell: do not fade to an empty Deep Navy frame or insert an artificial pause. The final overlay fade is 180 ms, with no additional 500 ms hold after the tagline. Any timing changes must be checked on-device and must not make users wait for decoration.

### 5.8 Launch implementation boundary

The launch experience is outside the primary navigation shell.

The conceptual application flow is:

**Android launch/splash → RichInsights application → primary navigation shell → Home / Learn / Quiz / Bible / News**

Therefore, adding the launch experience does not require restructuring the navigation architecture already established in V2.

The brand identity layer also remains independent from Firebase, quiz logic, content providers, and monetization.

### 5.9 Implementation timing

Brand and launch work is divided into deliberate phases:

**Phase A — documented direction**
- define brand architecture;
- define icon requirements;
- define tagline usage;
- define launch experience;
- establish asset requirements.

**Phase B — visual exploration (complete for direction selection)**
- compare the concept directions;
- select option C as the current approved direction.

**Phase C — production assets**
- retain and refine the selected editable/vector artwork;
- verify adaptive-icon safe areas and small-size rendering;
- verify launcher behavior across available masks and contexts.

**Phase D — Android integration (launch experience implemented)**
- integrate the brand mark into the Compose launch experience;
- implement the wordmark/tagline sequence;
- prepare the app shell behind the overlay;
- transition directly from the tagline into the app without a blank navy pause.

**Phase E — device verification (launch flow verified; asset checks remain as applicable)**
- build and install;
- test first-launch and returning-launch behavior on a physical device;
- check launcher icon appearance across available contexts;
- fix issues and checkpoint.

No production icon or animation should be considered final merely because it looks good in a design canvas. It must also survive actual Android rendering and device testing.

## 6. Primary navigation

### Phone navigation

The primary destinations are **For You | Learn | Quiz | Bible | News**. Profile & Settings is a separate global control, not a sixth bottom destination.

- **For You:** personalized platform entry point; the approved Option C leaning-R brand mark identifies it.
- **Learn:** open-book mark; supports course discovery and a Downloaded area for saved courses.
- **Quiz:** question/answer-card mark.
- **Bible:** upright book with a central cross and bookmark, visually distinct from Learn's open book.
- **News:** editorial document/feed mark.
- **Profile & Settings:** circular profile mark; keep the profile silhouette clean, without a diagonal chest line.

Use one coherent, restrained icon family. The selected destination has a subtle cyan-tinted squircle behind the icon only; the label remains outside the squircle. Selected and unselected labels/icons must stay distinguishable in both themes. Keep gold for meaningful achievement accents rather than decorating every navigation icon.

### App shell and system insets

The top profile control and bottom navigation remain fixed while the central destination content scrolls independently. Content must receive the appropriate insets so neither bar obscures it. Backgrounds may extend behind system bars, but interactive controls must remain clear of the status-bar cutout and gesture area.

### Larger screens

Use adaptive navigation appropriate to the available window size. Do not stretch a phone bottom bar across large screens. Preserve useful destination state where appropriate.

### Implementation checkpoint

**Navigation shell implemented; full UI verification remains a Stage 2 quality task.** The current Compose shell uses Navigation 3 with five primary destinations, a separate Profile & Settings action, selected/unselected outline and filled icons, theme-derived colors, a fixed top bar and bottom navigation, status-bar padding, and system-aware content insets. The destination bodies are currently placeholders, not completed product screens.

Before treating the shell as fully verified, test a debug build in light and dark themes, including scrolling, cutouts, gesture navigation, back behavior, selected/unselected states, and realistic screen sizes.

## 7. Home

Home is the platform entry point.

Potential hierarchy:
1. greeting/personal context;
2. daily challenge;
3. progress/streak;
4. continue learning;
5. featured categories/content;
6. relevant recommendations.

The exact layout will be established through later wireframes and requirements rather than prematurely locking the screen here.

## 8. Learn

Learn is the broader educational area.

It may contain:
- structured learning content;
- lessons;
- topics;
- explanations;
- study experiences;
- learning progress.

The first release should not attempt to build the entire future learning system. The destination exists so RichInsights can grow beyond quizzes cleanly.

## 9. Quiz

Quiz is a major platform experience, not the entire product.

Initial areas:
- General Knowledge;
- Science;
- IT — Information & Technology;
- Current Affairs;
- Bible quizzes.

The UI consumes validated V2 question objects and remains independent of content providers.

### Quiz experience

The quiz interface should clearly communicate:
- progress;
- question;
- answer choices;
- relevant timing;
- score/streak context;
- immediate feedback.

**Planned answer-option component — design specification only; not yet implemented.**

The answer-option component is a reusable quiz building block. The following records the agreed direction for future design and implementation; it does not claim that a Compose component or complete quiz screen already exists.

Required visual states:
- **Default:** standard surface, 1 dp outline using the established secondary-text token, outlined A–D marker.
- **Selected:** pale-blue accent surface, 2 dp blue outline, filled blue marker with white letter.
- **Correct:** success treatment with a check icon and the word **Correct**; use the established success fill/icon and darker success-text tokens appropriately.
- **Incorrect:** error treatment with a cross icon and the word **Incorrect**.
- **Long text:** answer text wraps naturally and the option grows vertically rather than clipping.

Do not introduce a separate **Locked** answer appearance as a normal quiz state. After submission, non-selected options should remain visually in their default state and become non-interactive where the active mode requires it. Exam Simulation is an exception to immediate correctness reveal: during an active exam, show the selected state only and reveal correctness later according to the declared exam rules.

Planned layout and typography:
- Minimum height: 56 dp; grow with wrapped text.
- Medium Material shape (16 dp corner radius).
- Horizontal padding: 16 dp; vertical padding: 12 dp.
- Full available width within 16 dp screen margins.
- 8 dp vertical spacing between options.
- Leading answer marker: 32 dp circle, 16 dp from the start edge, with a 12 dp gap before answer text.
- Answer text: bodyLarge (16 sp), PrimaryText; marker letter: 14 sp SemiBold; status label: 12 sp SemiBold at the end of the row.
- Draw outlines inside the component bounds using Compose BorderStroke; use theme tokens rather than hard-coded colors.

Interaction and feedback rules (planned):
- Tapping an option submits immediately; no Confirm button.
- Accept only the first valid submission; ignore duplicate taps.
- In modes that permit immediate feedback, show the selected answer as Correct or Incorrect. If incorrect, reveal the correct option where the mode policy permits it.
- Initial usability-test targets: about 800 ms after a correct answer and about 1,500 ms after an incorrect answer. Keep these values configurable, and validate on real devices.
- Automatic advance versus an explicit Next action is mode-configurable and remains subject to usability testing.
- A timeout is an unanswered question, not an answer-option visual state. Communicate timeout at question/session/results level according to the active mode.
- The timer is not part of this component; it belongs to the quiz screen and appears only in timed modes.

Accessibility:
- The full option row is one touch target (minimum 56 dp).
- Communicate option letter, answer text, and state (selected/correct/incorrect) to accessibility services.
- Never rely on color alone: use marker shape/icon and visible status text.
- Verify wrapping, contrast, touch targets, and font scaling at realistic Android sizes.

This specification must remain aligned with the planned quiz architecture in the roadmap. Detailed mode policies take precedence over generic component feedback, particularly for exam simulations and timed competitive challenges.

## 10. Results and progress

Results should communicate:
- completion;
- score;
- accuracy;
- correct/wrong count;
- points;
- streak;
- progress;
- next action.

Future experiences may add explanations, topic performance, achievements, recommendations, and learning feedback.

## 11. Bible

Bible is a dedicated product area.

Conceptual structure:

    Bible
    ├── Read Bible
    │   ├── Book
    │   ├── Chapter
    │   └── Reader
    ├── Bible Quiz
    └── Future Study

Reader requirements:
- offline reading when properly licensed content is available;
- book/chapter navigation;
- search;
- silent reading by default;
- user-triggered read-aloud;
- play/pause/resume/stop;
- future speed/voice controls.

Read-aloud must never start automatically.

## 12. News

News is a dedicated top-level experience.

It remains architecturally separate from the **Current Affairs** quiz category. News should have its own content and interaction model rather than forcing news content into the quiz system.

Detailed News UX will be designed after its product requirements and content decisions are established.

## 13. Typography and interaction

Typography should provide:
- strong headings;
- highly readable learning and quiz text;
- clear scores and progress;
- comfortable body text;
- consistent hierarchy.

The app-wide typography is implemented with bundled **Plus Jakarta Sans** fonts (Regular 400, SemiBold 600, Bold 700). Display/headline styles use Bold, titles use SemiBold, body styles use Regular, and label styles use SemiBold. Material 3 default sizes and line heights are retained for predictable scaling.

Use normal native Compose rendering. Do not introduce unnecessary text-selection restrictions as a copy-prevention mechanism.

Touch targets must be comfortable, and important meaning must never rely on color alone.

## 14. Responsive and adaptive design

Design for:
- phones;
- larger phones;
- landscape;
- tablets;
- foldables;
- split-screen and changing window sizes.

Do not build a phone-only layout and retrofit larger screens later.

Where useful, larger windows may display multiple related panes instead of simply enlarging a single-column phone layout.

## 15. Connectivity states

RichInsights is **online-first**, not broadly offline-first.

The UI must make connectivity requirements obvious.

### Limited local/offline support

- **Home:** selected basic/cached portions may remain available offline.
- **Profile:** selected cached information such as progress and history may remain visible offline.
- **Learn:** explicitly downloaded content may be available offline.
- **Bible:** properly licensed Bible reading should remain available offline.

### Internet-required

- Quiz;
- News;
- Current Affairs;
- fresh Learn browsing/downloads;
- fresh Home content/recommendations;
- Bible quizzes;
- server-backed Bible study features;
- synchronization and live content.

Design clear states for:
- loading;
- connecting;
- offline;
- connection lost;
- empty;
- temporary failure;
- retry;
- feature unavailable because internet is required;
- ad unavailable;
- unexpected error.

Messages should be short, human-readable, and actionable. Never expose raw provider/API errors.


## 16. Security and reliability UX

Security and reliability are also user-experience concerns.

The UI must:
- clearly distinguish loading, temporary failure, offline, and service-unavailable states;
- never expose raw API errors, stack traces, database details, tokens, or internal infrastructure information;
- avoid claiming an action succeeded until the authoritative operation has succeeded;
- provide safe retry behavior without creating uncontrolled repeated requests;
- preserve safe local state when a service temporarily fails;
- clearly communicate when internet is required;
- keep protected account, entitlement, and other server-authoritative state governed by the backend rather than by client-only UI state.

The UI should degrade gracefully when external providers or backend services are unavailable. Security-sensitive failures must fail closed rather than silently bypassing authorization or protection.

Detailed security, authorization, abuse protection, and reliability architecture belongs to **`RichInsights-backend/frontend/database-architecture.md`**.

## 17. Advertising UX

V2 AdMob is a fresh implementation.

Rules:
- ads never cover quiz content or controls;
- quiz functionality must remain usable when an ad fails;
- interstitials belong at appropriate transitions;
- rewarded ads are optional;
- development uses test ads;
- production identifiers/configuration remain separate;
- ad loading and failure states are handled gracefully.

Exact placements are implementation decisions and must be tested rather than copied from V1.

## 18. Premium and billing UX

Future commercial surfaces may include:
- Premium;
- Remove Ads;
- premium content/features;
- purchase and restore states;
- active entitlement state.

Requirements:
- communicate value clearly;
- show price and billing period clearly;
- distinguish subscriptions from one-time products;
- explain entitlements;
- avoid deceptive urgency or aggressive paywalls;
- preserve user context through purchase flows.

Billing is planned for later stages and is not part of the initial UI foundation.

## 19. Accessibility

Requirements:
- sufficient contrast;
- no reliance on color alone;
- meaningful content descriptions where needed;
- comfortable touch targets;
- scalable text and layouts;
- logical focus and navigation semantics;
- accessibility testing across major screens.

Accessibility is a design-system requirement, not a final cleanup task.

## 20. Motion

Motion should:
- confirm interactions;
- clarify transitions;
- communicate progress;
- reinforce meaningful feedback.

Avoid excessive motion during timed quizzes. Animations must remain performant on lower-end devices.

## 21. Reusable component foundation

The design system will eventually provide reusable components such as:
- primary and secondary buttons;
- cards;
- content/category cards;
- answer options;
- progress indicators;
- timers;
- score displays;
- streak indicators;
- dialogs;
- feedback states;
- loading/error/empty states;
- navigation components;
- ad containers;
- premium/entitlement indicators.

Components should be system-level building blocks, not one-off screen decorations.

## 22. Design process

**RESEARCH → WIREFRAME → DESIGN SYSTEM → HIGH-FIDELITY SCREENS → IMPLEMENT → BUILD → INSTALL → TEST → FIX → CHECKPOINT**

Do not design every future screen before its requirements are known.

## 23. Design-tool workflow

**Penpot is the active editable design workspace** for RichInsights V2 brand, icon, design-system and UI/UX work. Figma remains the reference/archive workspace for the existing **RichInsights — Brand & Icon Concepts** exploration and may be used later when appropriate. This tooling choice does not change the product identity or Android architecture.

## 24. Current V2 design and implementation status

This section is the current status snapshot. Code is authoritative for what is implemented; this document records the product/design intent and known gaps.

### Brand and launch identity
- **Option C** is the selected RichInsights brand/icon direction. The concept-selection decision is closed; do not reopen the six-way exploration without a concrete usability, production or legal reason.
- The selected direction uses the established RichInsights visual hierarchy and the refined mark/wordmark treatment documented in the active Penpot source.
- **Penpot** is the active editable design workspace; Figma is retained as reference/archive.
- The Compose brand intro and launch-to-app transition are implemented and were tested on a physical Android device.
- PR #5, **Fix brand intro to Home transition**, was squash-merged after user verification. The app shell is prepared behind the overlay; the unnecessary 500 ms post-tagline hold was removed and the final overlay fade is 180 ms.
- Launcher asset/mask and small-size checks, plus trademark/name searches, remain validation tasks. They do not reopen the selected concept or invalidate the tested launch-flow implementation.

### Implemented Android foundation
- **SDK configuration:** `compileSdk 37`, `targetSdk 37`, **`minSdk 24`**, as configured in `app/build.gradle.kts`. Any older handoff/spec text stating minSdk 23 is superseded.
- **Theme:** Material 3 light/dark color schemes are implemented. Current token values in `ui/theme/Color.kt` are authoritative; notably Deep Navy is `#0B2970`, Intelligent Blue `#1769E0`, Electric Cyan `#19B5FE`, and Warm Gold `#F4B942`.
- **Typography:** bundled Plus Jakarta Sans Regular 400, SemiBold 600 and Bold 700 is wired into Material 3 typography; default Material 3 sizes/line heights are retained.
- **Shapes and spacing:** shape tokens 8/12/16/20/28 dp and spacing tokens 4/8/12/16/24/32/40 dp are implemented.
- **Navigation:** five primary destinations are present (Home, labelled “For You” in the current top-level UI, Learn, Quiz, Bible and News); Profile & Settings is a top-app-bar secondary/global action. Navigation 3 back-stack support is present.
- **Destination content:** destination bodies are still placeholders. The shell must not be described as completed Home/Learn/Quiz/Bible/News experiences.
- **Adaptive behavior:** the current navigation implementation uses a compact bottom navigation bar. Adaptive rail/large-window behavior remains Stage 2 work; do not describe it as implemented.
- **Firebase:** Android project configuration and Analytics dependency are present, but Authentication, Firestore schema/rules, and server-backed content behavior are not part of the implemented UI foundation.

### Stage 2 design work

Stage 2A UI work and Stage 2B identity/security work are coordinated rather than strictly sequential. UI design and reusable components may proceed against agreed interfaces while the Firebase identity/repository/security foundation is built. Account-dependent screens must reflect the real authentication lifecycle, and protected-data UI must not be wired to mock authentication or unsecured client-side data.

The UI work must cover:
- adaptive navigation and layouts for compact and expanded windows;
- semantics, touch targets, contrast checks, TalkBack and font scaling;
- deliberate loading, empty, retryable error, offline, unavailable, signed-out, signed-in and permission-denied states;
- screen structures for the five primary product areas;
- account creation, sign-in, recovery, sign-out and Profile & Settings flows aligned with the approved Stage 2B provider/lifecycle decisions;
- ViewModel/repository contracts rather than direct Firebase access from Compose.

The detailed execution sequence, shared integration gates and Stage 2 exit criteria are authoritative in `native-android-roadmap.md`.

