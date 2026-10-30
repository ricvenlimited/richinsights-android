# RichInsights — Backend / Frontend / Database Architecture

> **Authoritative V2 backend architecture and infrastructure decision document.**
>
> This document records the backend, data, security, connectivity, content-generation, infrastructure, and cost-control decisions for RichInsights V2. It is the detailed companion to `native-android-roadmap.md`, `native-android-ui-ux.md`, and `native-android-commercial-monetization.md`.
>
> **Product:** RichInsights  
> **Company/studio:** Ricven Studios Limited  
> **Tagline:** **Grow. Excel.**  
> **Repository:** `ricvenlimited/richinsights-android`  
> **Android package/application identity:** `com.ricven.richinsights`

## 1. Purpose and authority

RichInsights V2 is being built as a new native Android educational platform.

**Documentation boundary:** this file is authoritative for backend platforms, data models, server-side logic, external providers, content pipelines, security, connectivity/local-data boundaries, infrastructure, and backend cost control.

**Synchronization rule:** this document contains the detailed technical decision record. The roadmap should summarize these decisions at project level; the UI/UX and commercial documents should describe only the user-facing consequences relevant to their domains. A change to a backend decision must be reflected in those summaries when necessary, without copying the entire backend design into them. The backend must therefore be designed around V2 requirements rather than inherited from V1.

This document is authoritative for:
- backend platform selection;
- Firebase usage;
- data storage;
- server-side functions;
- authentication and security;
- external API integration;
- content/question generation;
- question validation and deduplication;
- question history and freshness;
- connectivity and local-data boundaries;
- Bible offline reading support;
- commercial/backend boundaries;
- cost-control principles;
- the relationship between Firebase, Cloudflare, and future infrastructure.

If another V2 document discusses these subjects at a high level, this document contains the detailed architecture and decision record.

The documents must remain synchronized. If an implementation decision changes, update the relevant authoritative section rather than allowing conflicting descriptions to remain.

## 2. V2 backend principle

> **Build for quality; operate for affordability.**

A strong architecture does not require every available cloud service on day one.

V2 should:
- use reliable managed services where they solve a real problem;
- keep the number of infrastructure dependencies deliberately small;
- avoid paid infrastructure that is not yet needed;
- protect API credentials and commercial logic server-side;
- minimize unnecessary database reads/writes;
- pre-generate and validate content where practical;
- monitor usage and cost;
- preserve the ability to introduce additional infrastructure later without rewriting the Android application.

V2 is online-first, not broadly offline-first. Selected data and content may be available locally, but the platform should never pretend that all features work without an internet connection.

## 3. Authoritative V2 backend direction

The current V2 backend direction is:

```
Android
Kotlin + Jetpack Compose
        |
        v
Firebase services
        |
        +--> Authentication
        +--> Cloud Functions (Python)
        +--> Firestore
        +--> Cloud Storage
        +--> Cloud Messaging
        +--> Analytics
        +--> Crashlytics
        +--> Remote Config
        +--> App Check
        +--> Secret Manager / protected server secrets
        |
        +--> External content/API providers
        +--> Optional AI services
        +--> Google Play Billing
```

### Selected initial services

| Area | V2 direction |
|---|---|
| Android client | Kotlin + Jetpack Compose |
| Backend/server logic | Firebase Cloud Functions |
| Preferred backend language | Python |
| Primary database | Cloud Firestore |
| Authentication | Firebase Authentication |
| File/object storage | Firebase Cloud Storage |
| Push notifications | Firebase Cloud Messaging |
| Analytics | Firebase Analytics |
| Crash reporting | Firebase Crashlytics |
| Remote configuration | Firebase Remote Config |
| App integrity | Firebase App Check |
| Server credentials | protected secrets / Secret Manager |
| Android purchases | Google Play Billing |
| Relational SQL | Deferred |
| Cloudflare | Not required for V2 initially |

This is the current architecture decision. It can be revisited later if measured requirements justify a change.

## 4. Why Firebase is the current V2 foundation

Firebase is a strong fit because RichInsights needs several connected capabilities rather than only one API endpoint:
- user accounts;
- persistent learner state;
- question/content data;
- secure backend functions;
- external API integrations;
- file storage;
- notifications;
- analytics;
- crash reporting;
- remote configuration;
- application integrity;
- commercial entitlement support.

Using one primary platform reduces unnecessary infrastructure fragmentation during the early stages of V2.

Firebase is not being selected because it is the only technically possible solution. It is being selected because it can provide the required V2 capabilities while keeping the initial architecture manageable for a solo developer with a limited starting budget.

The goal is not to maximize services. The goal is to establish a stable foundation that can grow.

## 4A. Current Android Firebase implementation status

The Android repository currently has the Firebase project configuration for `richinsights-v2`, the Google Services Gradle plugin, and the Firebase Analytics dependency. The latest recorded debug workflow passed and uploaded an APK artifact.

This confirms build/configuration integration, **not** that Analytics events have been received by Firebase. Event delivery remains unverified.

Firebase Authentication, Firestore collections/data models, Firestore Security Rules, Cloud Functions, Storage rules, and production backend operations remain architecture decisions or planned implementation work unless explicitly marked implemented elsewhere. Do not describe the Firebase foundation as a completed production backend. Implement and verify identity, authorization, and data access before storing real user data.

## 5. Firestore as the initial database

**Cloud Firestore is the V2 database direction.**

Firestore should hold server-side data that needs persistence and synchronization, including appropriate portions of:
- question/content metadata;
- approved question records;
- question families and variants;
- provenance/validation state;
- category/topic metadata;
- user profiles;
- progress;
- streaks;
- quiz/session/result summaries;
- question history;
- entitlement state or entitlement references;
- downloaded-content metadata where server coordination is required;
- other product state that is intentionally synchronized.

Firestore data models must be designed around actual access patterns.

### Cost-sensitive Firestore rules

The implementation must avoid treating Firestore like an unrestricted SQL database.

Design for:
- efficient document reads;
- targeted queries;
- pagination;
- appropriate indexes;
- bounded result sets;
- avoiding repeated reads of the same data;
- local caching where appropriate;
- batched writes where appropriate;
- server-side generation/processing rather than per-user repeated generation;
- avoiding unnecessary real-time listeners;
- explicit ownership and security rules.

Question selection should not require downloading a massive question collection to the device.

### Local data versus Firestore

Some user-facing state can be cached locally for fast access, including:
- profile information;
- streak/progress summaries;
- history;
- settings;
- other deliberately cached data.

Local data is not automatically the authoritative server record. Synchronization rules must be explicit.

## 6. Why SQL Connect/PostgreSQL is deferred

Firebase SQL Connect / managed PostgreSQL is **not part of the initial V2 architecture**.

A relational database can be valuable for certain workloads, but the current product does not justify adding the operational and cost requirements of a managed SQL/Cloud SQL environment at this stage.

The initial V2 decision is therefore:
- use Firestore;
- model data carefully;
- monitor real access patterns;
- introduce SQL only if a concrete future requirement justifies it.

Possible future reasons to revisit SQL could include:
- relational workloads that become genuinely difficult or inefficient in Firestore;
- reporting/query requirements that need relational joins;
- measured scale or cost characteristics that make another database more appropriate;
- a future service whose requirements are materially different from the initial product.

Do not introduce SQL merely because it is theoretically more powerful.

## 7. Python Cloud Functions

**Python is the preferred backend function language for V2.**

Cloud Functions will provide server-side operations that should not be performed directly in the Android client.

Potential responsibilities include:
- calling external APIs;
- normalizing provider responses;
- validating incoming/outgoing content;
- content ingestion;
- question generation orchestration;
- question validation;
- deduplication;
- replenishment jobs;
- secure server-side operations;
- entitlement verification/supporting commercial logic;
- notification-triggering logic;
- other protected business logic.

Python was selected as the preferred V2 backend language because it is well suited to data processing, content pipelines, validation, API integration, and future AI-assisted workflows while keeping the Android client independent of server implementation details.

The Android app should consume stable contracts. It should not care whether a backend operation is implemented in Python, another language, or a future service.

## 8. External API integration

External APIs must be accessed through controlled backend boundaries when:
- credentials must remain private;
- provider-specific transformation is required;
- validation is required;
- rate limits/quotas need centralized handling;
- the response should be normalized into a V2 contract;
- multiple providers may later be combined.

The Android app should not embed private provider API keys.

Provider integrations must be isolated behind internal interfaces/contracts so that:
- a provider can be replaced;
- a provider can be disabled;
- multiple providers can coexist;
- provider outages do not redefine the application architecture;
- provider-specific response formats do not leak throughout the Android codebase.

The final provider mix remains deliberately open.

## 9. Content and question-generation architecture

Question quality is a core V2 requirement.

The platform must not repeat the V1 pattern of depending on weak, generic, repetitive, or poorly controlled question generation.

The preferred pipeline is:

```
Trusted source / licensed source / curated material
                    |
                    v
               Ingestion
                    |
                    v
          Normalization / facts
                    |
                    v
          Verification / validation
                    |
                    v
      AI-assisted or human authoring
                    |
                    v
        Question validation
                    |
                    v
          Deduplication checks
                    |
                    v
        Question family/variant
                    |
                    v
             Question bank
                    |
                    v
          Selection engine
                    |
                    v
                Learner
```

### Source of truth

AI must **not** be the source of truth.

The preferred principle is:

> **Trusted fact/source first; AI assistance second.**

AI may help transform verified material into question wording, distractors, explanations, or legitimate variants, but the system must retain provenance and validation boundaries.

The final approved question should be traceable to its underlying source material.

## 10. Question identity and anti-repetition

The V2 question system must support distinct identities for:
- `factId`;
- `familyId`;
- `variantId`;
- `questionId`.

Additional metadata should include:
- category;
- topic;
- difficulty;
- source/provenance;
- validation status;
- lifecycle status;
- timestamps where useful.

A changed sentence is not automatically a new educational question.

The system should detect:
- exact duplicate questions;
- duplicate answers;
- near-duplicate wording;
- duplicate fact usage where family-level repetition matters;
- repeated question families;
- low-value generated variants.

Question history should allow the selection engine to know what the learner has already seen.

The goal is not merely “different IDs.” The goal is meaningful educational freshness.

## 11. Question families and legitimate variants

A source fact may support multiple legitimate questions.

For example, a single verified fact may produce:
- direct factual question;
- reverse formulation;
- contextual question;
- comparison question;
- application question;
- different difficulty levels.

These variants must be educationally meaningful.

The system must not inflate question counts by:
- changing punctuation;
- swapping trivial wording;
- changing only sentence order;
- repeating the same fact with negligible difference;
- creating obviously weak distractors.

V2 should be capable of growing toward a very large question universe, potentially millions of legitimate variants over the life of the platform, but quality remains more important than an artificial count.

## 12. Validation layers

Question/content validation should cover, as appropriate:
- source validity;
- factual consistency;
- answer correctness;
- distractor plausibility;
- uniqueness;
- family-level duplication;
- language clarity;
- category correctness;
- difficulty consistency;
- safety/quality rules;
- provenance completeness;
- lifecycle status.

Validation should happen before content becomes normal learner-facing question-bank material.

Failed content should be rejected, quarantined, or sent for review rather than silently served.

## 13. Content replenishment

The question bank must be designed for continuous replenishment.

The platform should not normally stop after a small fixed bank is exhausted.

Potential replenishment mechanisms include:
- scheduled backend jobs;
- threshold-based generation;
- provider ingestion;
- curated content imports;
- pre-generation pipelines;
- manual review queues.

Generation should preferably happen ahead of user demand rather than synchronously during a quiz.

This improves:
- quality control;
- response time;
- provider resilience;
- cost predictability;
- learner experience.

The exact scheduling and threshold mechanism will be implemented after the content/domain contracts are defined.

## 14. Provider independence

No single API provider is the architecture.

A provider may fail because of:
- outage;
- quota;
- rate limit;
- authentication failure;
- pricing change;
- licensing change;
- poor content quality;
- changed response format.

Provider-specific failures should be contained within the backend integration layer.

Where useful and economically justified, a category may have:
- a primary provider;
- a fallback provider;
- curated/static content;
- pre-approved content already stored in the question bank.

A fallback is not mandatory for every provider; it must be justified by the importance and cost of the workload.

## 15. Authentication and user accounts

Firebase Authentication is the current identity layer.

The architecture should support appropriate account flows such as:
- sign-in;
- account creation;
- persistent user identity;
- supported authentication providers;
- anonymous or limited initial identity where appropriate;
- linking an initial anonymous identity to a permanent account where the final product flow supports it.

Authentication should not be required for every basic screen if the product does not need it there.

User-owned data must be associated with the authenticated identity using server-side security rules and validated access boundaries.

## 16. Security model

Security is a system boundary, not a UI feature.

V2 should use:
- Firebase Authentication for identity;
- Firestore Security Rules for data access;
- Cloud Storage Security Rules for files;
- Firebase App Check for app integrity/abuse reduction;
- protected backend secrets;
- server-side validation for privileged operations;
- least-privilege service access;
- no private provider keys embedded in the Android application.

Client-side checks are for user experience. They must not be treated as the sole authorization mechanism.

Sensitive operations should be performed or verified server-side.

## 17. Secrets and API keys

Private credentials must not be committed to the GitHub repository or embedded in the Android application.

Examples include:
- external content/API keys;
- AI service credentials;
- privileged service credentials;
- commercial verification credentials where applicable.

Secrets should be stored in the appropriate protected backend secret mechanism.

The V2 repository should contain configuration references, not live secret values.

## 18. Cloud Storage

Firebase Cloud Storage may be used for user or platform files that should not live directly in Firestore.

Potential uses include:
- profile images;
- educational media;
- approved downloadable course assets;
- Bible content packages if the licensing/distribution model permits and the chosen format requires storage;
- other future files.

Storage must follow:
- access-control rules;
- size/type restrictions;
- lifecycle/retention decisions;
- cost awareness;
- licensing requirements.

Large files should not be stored as Firestore documents.

## 19. Analytics and Crashlytics

Firebase Analytics can provide product usage information needed to understand:
- feature usage;
- navigation;
- quiz behavior;
- content engagement;
- retention patterns;
- commercial flow behavior where appropriate.

Firebase Crashlytics should provide crash/error visibility for Android production quality.

Analytics should be used responsibly and only for legitimate product/technical purposes. Do not collect unnecessary personal data.

Crash reporting must help diagnose:
- crashes;
- failed critical flows;
- device/version-specific problems;
- release regressions.

## 20. Remote Config

Firebase Remote Config may be used for server-controlled product configuration that does not require an application update.

Potential uses:
- feature flags;
- controlled rollout settings;
- non-sensitive thresholds;
- UI/content configuration;
- experimentation parameters where appropriate;
- commercial/ad configuration that is safe to expose as remote configuration.

Remote Config must not be treated as a secure storage mechanism for secrets.

Critical authorization decisions must remain server-enforced.

## 21. Firebase Cloud Messaging

Firebase Cloud Messaging may support:
- learning reminders;
- quiz reminders;
- streak-related notifications;
- content availability;
- account/product notifications;
- other user-consented communications.

Notification design must respect user choice and avoid unnecessary messaging.

Notification scheduling should be deliberate rather than implemented merely because FCM exists.

## 22. Online-first connectivity model

RichInsights is **online-first / internet-required for most platform functionality**.

It is intentionally **not** an offline-first app.

### Features available with limited local/offline support

#### Home
Only selected basic/cached portions may remain available without internet.

Fresh/live Home content, recommendations, remote content, and synchronization require internet.

#### Profile
Locally cached profile information may remain visible, including:
- profile image;
- streaks;
- progress;
- history;
- other deliberately cached profile data.

Server synchronization requires internet.

#### Learn
Only courses/content that the user has explicitly downloaded should be available offline.

Fresh browsing, discovery, downloads, and server-backed learning content require internet.

#### Bible
Bible reading itself is intended to work offline when the required text is properly licensed and stored locally.

Offline Bible functionality includes:
- book navigation;
- chapter navigation;
- verse reading;
- local reader behavior.

Bible quizzes and server-backed Bible study features require internet.

### Internet-required features

The following are online:
- Quiz;
- News;
- Current Affairs;
- fresh Learn browsing;
- Learn downloads;
- fresh Home content/recommendations;
- Bible quizzes;
- server-backed study features;
- synchronization;
- live/provider-backed content.

The app must show clear connectivity states instead of pretending these features are available offline.

## 23. Connectivity UX requirements

The client must distinguish:
- connected;
- connecting;
- offline;
- connection lost;
- loading/requesting;
- empty result;
- temporary server/provider failure;
- retryable failure;
- feature unavailable because internet is required.

Example:

> **Internet connection required**  
> Connect to the internet to start a quiz.

Error messages should be human-readable. Raw provider errors, stack traces, HTTP codes, or internal infrastructure details should not be exposed as learner-facing copy.

Offline Bible reading should remain usable even when online quiz functionality is unavailable.


## Security & Reliability Hardening

Security and reliability are **core V2 architecture requirements**, not late-stage polish. RichInsights is an educational platform with user accounts, learning data, quizzes, Bible content, news/current-affairs content, external services, and future commercial functionality. The architecture must therefore assume that malicious users, automated abuse, compromised clients, provider failures, and infrastructure outages can occur.

### Security principle

> **The client is never trusted. The backend verifies.**

The Android application may perform client-side validation for usability, but client-side checks are never the sole authorization boundary. Protected operations must be enforced by Firebase Security Rules and/or trusted backend functions.

### Android/frontend security

The Android client must:
- never contain private backend/provider credentials;
- use HTTPS/TLS for network communication;
- treat all server responses as untrusted input;
- validate user-facing data before rendering where appropriate;
- avoid storing secrets in SharedPreferences, DataStore, logs, or source control;
- use secure authentication/session handling;
- minimize sensitive local data;
- use App Check where appropriate to reduce abuse from unauthorized clients;
- keep production configuration separate from development/test configuration;
- avoid exposing internal provider errors, stack traces, database details, or secrets to users.

The package/application identity `com.ricven.richinsights` is **not a secret**. Public package identity is normal Android practice. Secrets, signing credentials, privileged tokens, and private provider credentials must remain protected.

### Backend/API security

All privileged backend operations must:
- authenticate the caller where authentication is required;
- authorize the requested resource/action;
- validate and normalize input;
- enforce ownership boundaries;
- enforce rate/abuse controls where needed;
- avoid trusting client-supplied entitlement, score, role, or administrative state;
- return only the data the caller is permitted to receive;
- fail safely without exposing internal implementation details.

Business rules that affect trust, rewards, entitlements, protected content, or authoritative learner state must be verified server-side.

### Firestore security

Firestore Security Rules are an independent security boundary.

Rules must explicitly define:
- who may read each protected collection/document;
- who may create/update/delete data;
- which fields a client is permitted to modify;
- ownership requirements;
- immutable/server-controlled fields;
- role/administrative boundaries;
- validation constraints appropriate to the data.

The Android client must never be treated as the authority for Firestore authorization.

Server-side administrative access must use least privilege and must not be exposed to the client.

### Cloud Storage security

Storage rules must enforce:
- authenticated/authorized access where required;
- ownership boundaries;
- permitted file types;
- reasonable size limits;
- safe upload paths;
- appropriate read/write/delete permissions;
- retention/lifecycle controls where needed.

User-uploaded files must never automatically become trusted platform content.

### Secrets and credentials

No live secret belongs in:
- the GitHub repository;
- README/documentation;
- Android source/resources;
- the APK/AAB;
- client-side configuration intended for public distribution.

Protected secrets should use the appropriate server-side secret mechanism. Development and production credentials must be separated.

If a secret is ever exposed, the response must be **revoke/rotate → investigate → remove exposure → verify dependent services → document the incident**, rather than merely deleting the visible value.

### Authentication and authorization

Authentication establishes identity; it does not automatically grant access.

Authorization must be evaluated against:
- authenticated identity;
- resource ownership;
- role/permission;
- product entitlement where applicable;
- server-controlled state.

Administrative and privileged operations must use stronger controls than ordinary learner operations.

### Abuse and fraud resistance

RichInsights should be designed to reduce:
- automated account abuse;
- API scraping;
- excessive request volume;
- quiz/session manipulation;
- score manipulation;
- entitlement manipulation;
- malicious content submission;
- repeated automated generation requests;
- storage abuse;
- notification abuse.

Controls may include App Check, authentication requirements, rate limits, quotas, server-side validation, anomaly monitoring, and backend-side throttling. Controls should be proportional to the risk and implemented without making normal learning unnecessarily difficult.

### Content and question integrity

Questions, answers, scores, validation state, provenance, and approved content are trusted platform data.

The client must not be allowed to:
- mark its own answer as correct;
- rewrite authoritative scores;
- promote unvalidated content;
- alter question provenance;
- grant itself Premium/paid entitlements;
- bypass server-side content or session rules.

The backend remains authoritative for protected state.

### Dependency and supply-chain security

V2 dependencies must be:
- intentionally selected;
- kept reasonably current;
- reviewed for known security issues;
- removed when no longer required.

Do not add libraries merely for convenience when the Android platform or existing architecture already provides the required capability.

Build configuration, signing configuration, CI/CD credentials, and release artifacts must be treated as security-sensitive.

### Logging, monitoring, and incident visibility

Production systems should provide enough telemetry to detect and investigate:
- authentication failures;
- repeated authorization failures;
- abnormal API usage;
- function failures;
- database/security-rule failures;
- provider failures;
- crash spikes;
- unusual content-generation behavior;
- service degradation.

Logs must not contain passwords, access tokens, private keys, or unnecessary sensitive user information.

Crash and security telemetry should support investigation without becoming a source of new privacy risk.

### Reliability and service resilience

Security alone does not prevent service failure. RichInsights must also be designed to continue operating safely when dependencies fail.

The architecture should account for:
- Firebase/service outages;
- external API outages;
- provider rate limits;
- expired credentials;
- network instability;
- malformed provider responses;
- function failures;
- database contention or unexpected usage;
- third-party pricing/licensing changes.

Provider failures should be isolated behind backend contracts. Where justified, use cached/pre-approved content or a fallback provider rather than making every learner request depend on a single live provider.

### Graceful degradation

When a dependency fails:
1. detect the failure;
2. fail safely;
3. preserve already available user data where appropriate;
4. show a clear user-facing state;
5. retry only when retrying is reasonable;
6. avoid retry storms;
7. use approved fallback/cached data where appropriate;
8. record enough telemetry for diagnosis.

The app must never silently convert a failed secure operation into an unauthorized successful one.

### Data integrity and recovery

Authoritative data should have appropriate:
- validation;
- lifecycle controls;
- backup/recovery planning;
- deletion rules;
- migration strategy;
- consistency expectations.

Recovery planning should identify which data can be recreated, which must be restored, and which can safely be temporarily unavailable.

### Availability is not a promise of zero downtime

RichInsights should be engineered for resilience, but no architecture can honestly guarantee that every external dependency will remain available at all times. The goal is to minimize failure impact, detect problems quickly, recover safely, and keep unaffected parts of the product usable.

### Security testing gates

Before major releases, test at minimum:
- authentication/authorization boundaries;
- Firestore rules;
- Storage rules;
- protected Cloud Functions;
- App Check behavior where enabled;
- secret exposure;
- invalid/malicious inputs;
- duplicate/replayed requests where relevant;
- entitlement enforcement;
- offline/online transitions;
- provider failure states;
- crash and error handling.

Security testing is part of release hardening, not an optional final audit.

### Security and reliability staging

Security foundations begin early:

**Stage 1–2**
- secret-handling rules;
- Firebase project separation/configuration;
- App Check planning;
- secure networking;
- safe local storage;
- error-state design.

**Stage 3–5**
- authentication;
- Firestore/Storage rules;
- server-side authorization;
- backend input validation;
- provider isolation;
- rate/abuse controls;
- protected secrets.

**Stage 6–9**
- monitoring;
- anomaly/abuse detection;
- recovery procedures;
- provider fallback/caching where justified;
- data integrity testing.

**Stage 10**
- full security/reliability release hardening;
- dependency review;
- penetration/security testing where feasible;
- backup/recovery verification;
- production configuration review;
- incident-response readiness.

Security requirements may cause a stage to pause until its security boundary is adequately implemented and tested.


## 24. Local persistence

The Android application may use:
- Room for structured local data;
- DataStore for preferences/settings;
- local caches for intentionally cached server state;
- local downloaded learning/Bible content where licensing and storage design permit.

Local persistence is not permission to make the entire app offline.

The local layer should have explicit ownership and synchronization rules.

Examples:
- quiz history can be cached;
- profile summaries can be cached;
- downloaded Learn courses can be intentionally stored;
- licensed Bible text can be stored for offline reading;
- fresh News cannot be fabricated from stale data and presented as current;
- a new quiz should not silently use an outdated cache when the product requires online quiz availability.

## 25. News and Current Affairs

News and Current Affairs are separate product concerns.

### News
News is a top-level RichInsights destination.

Its provider and content pipeline should be designed for news browsing and current content.

### Current Affairs
Current Affairs is a quiz/content area.

Current Affairs content must pass the same V2 provenance, validation, freshness, and anti-duplication requirements as other question content.

A news API is not automatically a question bank.

The backend may ingest news, but learner-facing questions should be produced through the controlled content pipeline rather than blindly exposing provider responses.

## 26. IT — Information & Technology content

IT — Information & Technology is an initial V2 content area intended for a global audience.

The category should be broad enough to support foundational and practical technology knowledge without hard-wiring the quiz engine to a fixed list of subtopics.

Potential subject areas may include:
- computer fundamentals and hardware;
- software and operating systems;
- networking and internet technologies;
- databases and data management;
- programming and software development fundamentals;
- cybersecurity and digital safety;
- cloud computing;
- web and mobile technologies;
- information systems and IT support;
- emerging technologies such as artificial intelligence and IoT.

These are subject areas the content model should be capable of supporting; they do not require separate product categories or implementation now.

The new architecture should:
- ingest trusted factual source material;
- normalize it;
- validate it;
- generate/author meaningful questions;
- preserve provenance;
- assign stable IDs;
- deduplicate;
- store approved questions;
- replenish content.

No paid API should be assumed merely because one existed in V1.

Provider decisions will be made against V2 quality, licensing, reliability, and cost requirements.

## 27. Bible content architecture

Bible content requires an additional licensing boundary.

The product should:
- use only text/resources whose distribution rights are confirmed;
- preserve the required licensing/attribution information;
- keep the reader separate from online quiz infrastructure;
- allow properly licensed Bible text to be stored locally for offline reading where permitted.

WEB remains the leading translation direction from earlier planning, but it is not an automatic license decision. Licensing and distribution requirements must be verified before implementation/distribution.

Bible quiz questions follow the normal V2 question pipeline.

Read-aloud is a user-triggered feature:
- play;
- pause;
- resume;
- stop.

It must not begin automatically when a chapter opens.

## 28. Commercial and backend architecture

Commercial state must remain separate from content.

The conceptual flow is:

```
Google Play purchase
       |
       v
Backend verification / entitlement state
       |
       v
Central entitlement model
       |
       +--> Android UI
       +--> content access
       +--> advertising behavior
       +--> premium features
```

Planned commercial capabilities include:
- AdMob;
- Premium subscriptions;
- Remove Ads;
- premium content/features;
- future content packs.

### Remove Ads

Remove Ads is a separate entitlement.

**Remove Ads != Premium**

A user who purchases Remove Ads does not automatically receive all Premium benefits unless a future product decision explicitly combines the products.

### AdMob

V2 AdMob is a fresh implementation.

Requirements:
- native Google Mobile Ads SDK;
- test/production separation;
- centralized ad management;
- lifecycle-aware loading;
- ready/failed states;
- graceful failure;
- appropriate frequency controls;
- no dependency between ad availability and quiz availability.

An ad failure must never make the quiz unusable.

The V1 AdMob implementation is not the V2 implementation.

## 29. Google Play Billing

Google Play Billing will be the Android purchase mechanism when commercial implementation begins.

The backend architecture must eventually support:
- purchase verification;
- subscription state;
- one-time purchases;
- pending transactions;
- failed transactions;
- cancellations/expiration;
- restore/synchronization;
- centralized entitlement state.

Prices, product IDs, and the final product catalog remain deliberately undecided.

Billing is not required before the initial native application foundation is stable.

## 30. Cost-control strategy

RichInsights begins with a limited budget. Cost control is therefore an architecture requirement.

Principles:
1. Prefer managed services with useful no-cost allowances where appropriate.
2. Use Blaze/pay-as-you-go only with monitoring and disciplined usage.
3. Do not add SQL/Cloud SQL at the beginning.
4. Do not add Cloudflare simply because V1 used it.
5. Avoid paid APIs unless their value is justified.
6. Prefer trusted/free/public/licensed sources where appropriate.
7. Generate and validate questions ahead of user demand where possible.
8. Avoid per-user AI generation during every quiz.
9. Minimize Firestore reads/writes.
10. Use pagination and bounded queries.
11. Cache appropriate data.
12. Monitor backend errors and usage.
13. Keep large files out of Firestore.
14. Use background generation/replenishment where economically justified.
15. Keep provider integrations replaceable.

The target is not “zero cost at all times.” The target is controlled spending that scales with real product use.

## 31. Blaze plan position

The project may use Firebase's Blaze/pay-as-you-go model when needed for V2 services.

Blaze should be treated as a controlled operating model, not permission to consume resources without limits.

Before production:
- understand which services generate charges;
- set monitoring/alerts where available;
- test expensive paths carefully;
- avoid runaway functions;
- avoid unbounded database operations;
- review usage regularly.

A strong architecture should make costs understandable.

## 32. V1 Cloudflare Worker: status and lessons

V1 used a Cloudflare Worker named in the existing V1 environment as `daily-quiz-intermidiary`.

The Worker is **not** the V2 backend foundation.

It remains useful as a V1 engineering reference.

The V1 Worker was described as having:
- provider/data-fetching separation;
- validation;
- question generation;
- orchestration;
- defensive error handling;
- difficulty-aware generation;
- deduplication;
- provider-specific handling.

The V1 Cloudflare assistant review also identified infrastructure gaps such as:
- single data source;
- lack of caching;
- lack of persistence;
- lack of authentication/user state;
- hardcoded question material.

These observations are useful lessons, but they do not automatically define V2.

### V1 ideas worth carrying forward

V2 may reuse the architectural lessons behind:
- provider abstraction;
- validation boundaries;
- defensive error handling;
- difficulty-aware generation;
- deduplication;
- API gateway thinking;
- separation of orchestration from provider code.

### V1 assumptions not carried forward automatically

Do not automatically copy:
- V1 question-generation behavior;
- V1 question bank;
- V1 provider choices;
- V1 route structure;
- V1 Cloudflare architecture;
- V1 database assumptions;
- V1 AdMob implementation;
- V1 API credentials/configuration.

The V1 Worker is a prototype/reference, not a dependency.

## 33. Cloudflare decision

Cloudflare is **not required for V2 initially**.

Firebase can provide the initial backend foundation.

Cloudflare may be reconsidered later if a concrete workload justifies it, for example:
- a specific edge/API requirement;
- a measured performance need;
- a cost optimization supported by real usage data;
- a specialized caching/distribution workload;
- another infrastructure need that Firebase does not address efficiently.

If Cloudflare is introduced, it should have a clearly defined responsibility rather than becoming a second backend simply because it is available.

The architecture should avoid unnecessary “Firebase + Cloudflare + other services” complexity.

## 34. AI services

AI can be used as an assistant inside the content pipeline.

Appropriate roles may include:
- transforming verified facts into draft questions;
- generating candidate distractors;
- producing explanations;
- generating legitimate variants;
- classifying or normalizing content;
- assisting editorial workflows.

AI should not:
- become the unquestioned source of factual truth;
- publish unvalidated questions directly to learners;
- replace provenance;
- replace validation;
- generate unlimited per-user quiz content without cost/quality controls.

The final AI provider is deliberately undecided.

## 35. API and domain contracts

The Android client should depend on stable V2 domain contracts rather than raw provider payloads.

Examples of domain objects:
- User;
- Profile;
- Category;
- Topic;
- Fact;
- Question;
- QuestionFamily;
- QuestionVariant;
- QuizSession;
- QuizResult;
- QuestionHistory;
- Progress;
- Course;
- DownloadedCourse;
- BibleBook;
- BibleChapter;
- NewsItem;
- Entitlement.

Provider DTOs should be converted into internal/domain models before reaching presentation code.

This keeps provider changes away from Compose UI and quiz logic.

## 36. Backend responsibilities versus Android responsibilities

### Android

Responsible for:
- UI;
- navigation;
- local state;
- user interaction;
- presentation;
- local caching;
- offline reader behavior;
- connectivity presentation;
- purchase UI;
- displaying entitlement state.

### Backend

Responsible for:
- privileged operations;
- external API access;
- secrets;
- content ingestion;
- content validation;
- question generation orchestration;
- deduplication;
- replenishment;
- synchronized user state;
- entitlement verification/support;
- notification-triggering operations;
- server-side business rules.

The client must not be trusted with privileged decisions simply because it can technically perform them.

## 37. Development sequence

Backend work follows the same V2 quality loop:

**BUILD → INSTALL → TEST → FIX → DOCUMENT → CHECKPOINT → NEXT STAGE**

Backend-specific additions:
1. define domain contract;
2. define data access pattern;
3. implement backend boundary;
4. test locally/against safe test data;
5. test Android integration;
6. inspect cost/performance behavior;
7. document the stable decision.

Do not build a large backend before the corresponding product requirement exists.

## 38. Implementation alignment with the V2 roadmap

The backend architecture maps to the roadmap as follows:

### Stage 0 — Requirements and architecture
- Firebase-first decision;
- Firestore decision;
- Python Cloud Functions decision;
- connectivity model;
- security boundaries;
- provider independence.

### Stage 1 — Native foundation
- Android Firebase project/configuration and Analytics dependency are present;
- package identity `com.ricven.richinsights`;
- successful debug build recorded; Analytics event delivery remains unverified;
- Authentication, Firestore data/security rules, and Cloud Functions are not part of the completed foundation.

### Stage 2 — Platform UI foundation (next implementation stage)
- reusable Compose components and consistent loading/error/empty/retry states;
- explicit online/offline/connectivity presentation;
- adaptive layouts and accessibility foundations;
- Home/Learn/Quiz/Bible/News experience, without pretending backend-dependent data is already available;
- keep UI contracts independent of unfinished authentication and Firestore implementation.

### Stage 3 — Content/domain contracts
- domain models;
- question IDs/families/variants;
- provenance;
- validation/lifecycle state;
- backend API contracts.

### Stage 4 — Quiz engine
- online quiz flow;
- question selection contract;
- result/progress models.

### Stage 5 — Backend/content integration
- Python Cloud Functions;
- Firestore;
- external providers;
- validation and ingestion;
- secure API boundaries.

### Stage 6 — History and freshness
- question history;
- family-level repetition controls;
- replenishment;
- local/server synchronization.

### Stage 7 — Initial content rollout
- General Knowledge;
- Science;
- IT — Information & Technology;
- Current Affairs;
- Bible quiz capability.

### Stage 8 — Bible platform
- licensed offline text;
- local reader storage;
- search/navigation;
- read-aloud;
- online quiz/study boundaries.

### Stage 9 — News and broader platform
- News;
- accounts/profile/progress;
- learning resources;
- future modules.

### Stage 10 — Commercial/release hardening
- AdMob;
- Google Play Billing;
- entitlement verification;
- security;
- monitoring;
- performance;
- release readiness.

## 39. Deliberately undecided items

Do not invent final decisions for:
- exact Firebase data schema;
- exact Firestore collection names;
- exact Cloud Function names/routes;
- exact provider mix;
- exact AI provider;
- fallback-provider policy per category;
- final Bible translation/licensing choice;
- exact synchronization algorithm;
- exact cache durations;
- subscription prices;
- Remove Ads price;
- product IDs;
- free/premium content split;
- final account schema;
- exact notification strategy;
- exact Cloudflare involvement;
- future SQL adoption.

These are implementation/product decisions to be made when the relevant requirements are sufficiently known.

## 40. Non-negotiable V2 backend principles

1. V2 is a clean architecture, not a V1 migration.
2. Firebase is the current primary backend foundation.
3. Firestore is the initial database.
4. Python Cloud Functions are the preferred backend function layer.
5. SQL Connect/PostgreSQL is deferred.
6. Cloudflare is not required initially.
7. Provider credentials stay server-side.
8. AI is an assistant, not the source of truth.
9. Approved questions must be validated and traceable.
10. Question IDs alone are not enough; family/variant/history controls are required.
11. Question quality must not be sacrificed for artificial scale.
12. Content must be replenishable.
13. The Android client must consume stable domain contracts.
14. Online-first means most live platform features require internet.
15. Offline support is intentionally limited to selected Home/Profile caches, explicitly downloaded Learn content, and licensed Bible reading.
16. Quiz, News, Current Affairs, fresh Learn, and server-backed Bible features require internet.
17. Ad availability must never determine quiz availability.
18. Commercial entitlements must be centralized.
19. Security rules must be enforced server-side.
20. Cost must be monitored as the platform grows.

## 41. Current authoritative backend status

**Implementation checkpoint:** Android Firebase configuration and the Analytics dependency are present, and the recorded debug build passed. Analytics event delivery has not yet been independently verified. Authentication flows, Firestore schema/security rules, and production Cloud Functions remain unimplemented.

- Backend direction: **Firebase-first**
- Database: **Firestore**
- Backend functions: **Python Cloud Functions**
- Authentication: **Firebase Authentication**
- Storage: **Firebase Cloud Storage**
- Notifications: **Firebase Cloud Messaging**
- Analytics: **Firebase Analytics**
- Crash reporting: **Firebase Crashlytics**
- Remote configuration: **Firebase Remote Config**
- App integrity: **Firebase App Check**
- SQL Connect/PostgreSQL: **deferred**
- Cloudflare: **not required initially**
- AI: **optional assistant within a validated content pipeline**
- Connectivity: **online-first / internet-required for most live platform functionality**
- V1 Cloudflare Worker and V1 question/API/AdMob systems: **reference/prototype only, not V2 production dependencies**

This snapshot summarizes the authoritative backend decisions above; detailed changes should be made in the relevant sections rather than by maintaining duplicate copies here.
