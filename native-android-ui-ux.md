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

RichInsights uses a **deep navy → intelligent blue → electric cyan → restrained gold** visual hierarchy.

### Brand palette

- **Deep Navy — `#102A43`**
  - primary foundation for strong hierarchy, navigation, headings, and trusted brand surfaces.
- **Intelligent Blue — `#1769E0`**
  - principal action and learning color.
- **Electric Cyan — `#19B5FE`**
  - controlled signature accent for discovery, emphasis, interactive highlights, and selected modern details.
- **Warm Gold — `#F4B942`**
  - restrained achievement accent for points, streaks, milestones, certificates, and similar positive accomplishments.

### Supporting UI palette

- **App canvas / standard surface — `#FFFFFF`**
- **Pale-blue surface accent — `#E8F7FF`**
- **Strong surface — Deep Navy, `#102A43`**
- **Primary text — `#172033`**
- **Secondary text — `#5B6474`**
- **Success fill/icon — `#22A06B`**
- **Success text — `#167A4C`**
- **Error — `#C93B3B`**

The main app canvas and standard Material surfaces use pure white. Pale-blue accent surfaces are reserved for components that need a distinct container treatment; spacing, outlines, and hierarchy should keep white cards legible on the white canvas. Material 3 `surfaceTint` is an elevation-tint role, not a pale background token, and maps to the primary blue.

Semantic green and red are reserved primarily for success, confirmation, incorrect, warning/error, and validation states. Use the darker success token for small success text; the brighter success token remains available for suitable fills and icons. Cyan and gold remain signature accents, but should not be used for normal-sized text on white unless contrast is sufficient. Gold is not a general-purpose status color.

The secondary text token is intentionally darker to improve contrast consistency across white and pale-blue surfaces. Contrast must be evaluated for each actual foreground/background pair; do not infer accessibility from token names alone.

The palette should remain controlled. RichInsights should not become a rainbow-style educational app, and individual product sections should not introduce competing brand palettes.

Final implementation tokens must be checked for contrast and accessibility before being treated as production-final.

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

### Compact phones

**Home | Learn | Quiz | Bible | News**

### Profile & Settings

Profile and settings are secondary/global destinations, accessed through the appropriate profile or account action rather than taking a permanent primary-navigation slot.

### Larger screens

Use adaptive navigation appropriate to the available window size. Do not stretch a phone bottom bar across large screens.

Each primary destination should preserve useful navigation state where appropriate.

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

Feedback must be clear and accessible. Timer behavior and other mode-specific rules remain governed by the roadmap and must be validated before implementation.

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

The RichInsights V2 design workflow uses **Penpot as the active editable design workspace** for continuing brand, icon, design-system, and UI/UX work.

Figma remains the **reference/archive workspace** for the existing **RichInsights — Brand & Icon Concepts** file and may still be used later when appropriate. The move to Penpot is due to the connected Figma Starter/View MCP usage limitations that can interrupt continued design work. It does not change the RichInsights visual direction, product architecture, or implementation stages.

### Current brand work

The existing Figma board contains:
- Brand Direction;
- Approved Color Direction;
- six editable icon concept directions;
- Selection & Production Path.

The six exploratory directions are:
1. Insight / Discovery
2. Growth / Progress
3. Learning / Knowledge
4. RI Monogram
5. Layered Information
6. Refined Abstract

These remain **exploration concepts only**. The **RI Monogram** is the leading refinement candidate, but no final RichInsights brand mark/icon has been approved.

### Active design → Android workflow

The intended sequence is:

**Explore/refine in Penpot → select/refine concept → finalize vector/brand assets → prepare adaptive-icon assets → integrate into Android → build/install → test on real devices → checkpoint**

Figma remains available as a reference and possible later design tool. The design-tool change does not alter the existing navigation shell or backend architecture.

## 23. Current V2 design status

- **Option C** is the selected RichInsights brand/icon direction; concept selection is no longer pending.
- **Penpot** is the active editable design workspace; the existing Figma file remains a reference/archive.
- The Compose brand intro is implemented and has been verified on a physical Android device.
- The verified launch transition prepares the navigation shell behind the brand overlay, removes the extra 500 ms post-tagline hold, and uses a 180 ms final fade so the app appears directly instead of exposing a blank Deep Navy frame.
- PR #5, **Fix brand intro to Home transition**, was squash-merged after user verification.
- The primary navigation shell remains **Home / Learn / Quiz / Bible / News** with Profile & Settings secondary/global.
- Remaining icon asset/mask checks should be performed as part of the relevant launcher asset validation; they do not block the already-verified launch-flow checkpoint.
- **Next roadmap step: Firebase foundation**, not full Home-screen design or implementation.

