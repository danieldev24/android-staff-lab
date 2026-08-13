# Android Staff Lab — Visual Language

Status: Adopted  
Reference set: three education-app concept images supplied on 2026-08-13

## Intent

Use the visual character of the references without copying their course, calendar, or dashboard functionality. Android Staff Lab remains a systems-learning product: every visual pattern must make a startup flow, state, measurement, or caveat easier to understand.

## Visual Grammar

### Color

- **Canvas:** near-white, with pale lavender used to group related learning content.
- **Primary:** saturated violet for the main topic, selected states, and primary actions.
- **Accents:** coral, amber, and mint identify different semantic lanes or statuses.
- **Text:** near-black for hierarchy; muted neutral grey for supporting copy.
- Never encode a lane or state with color alone. Pair color with a label, icon, order, or shape.

### Typography

- Use a clean sans-serif family throughout.
- Headlines are large, compact, and high-contrast; body copy stays conversational and readable.
- Use four base sizes (32, 24, 16, and 12 sp) and two weights (Bold and Normal) to keep hierarchy teachable and repeatable.
- Labels use sentence case or short uppercase overlines. Monospace is reserved for genuine runtime data such as timestamps, not for general navigation.

### Shape and Depth

- Cards use 20–30 dp corners and generous internal padding.
- Pale section panels may use a centered concave notch to create the soft, organic silhouette seen in the references.
- Pills are fully rounded. Icon badges are circular and use a strong accent fill.
- Depth is subtle: use tonal layering and a restrained shadow, not heavy borders.
- Diagonal hatching and translucent circles are decorative texture only and must remain behind readable content.

### Layout

- Use 20–24 dp screen gutters and 16–24 dp vertical rhythm.
- Prefer one strong hero card followed by grouped learning sections.
- Leave deliberate negative space around headings and controls.
- Dense technical content reflows vertically at large font scales instead of preserving a fixed card height.
- Split a topic into focused lessons and disclose one technical detail at a time instead of building a single long article.
- Keep lesson progress at the top and Previous/Next learning actions fixed in the bottom thumb zone.

## Reusable Compose Primitives

| Primitive | Purpose | Intended reuse |
|---|---|---|
| `LabPill` | Compact status, mode, or metadata label | Cold/Warm/Hot selector, source tags, duration |
| `LabIconBadge` | Color + symbol anchor | Topic cards, trace lanes, staff notes |
| `LabHatchedBand` | Non-semantic visual texture | Hero and timeline card backplates |
| `LabOrganicPanel` | Tonal grouping with a concave top edge | Topic groups, explainers, simulation results |
| `LabSectionHeader` | Consistent section title and optional action text | Library, timeline, lab, live trace |

## Feature Mapping

| Android Staff Lab function | Visual treatment |
|---|---|
| Topic library | Track introduction, path summary, and editorial topic cards with explicit lesson metadata |
| Application Startup overview | Violet hero card, learning outcomes, four-lesson progress, and a fixed next action |
| Cold/Warm/Hot modes | Rounded labeled pills; selection is expressed by fill, text, and semantics |
| Five startup lanes | Horizontal labeled lane overview plus one selected stage detail |
| Critical-path simulation | Lavender organic panel containing white result cards |
| Live event log | Timeline cards with timestamp, process/thread label, and measured-state badge |
| Staff notes and sources | Numbered selector plus one focused caveat card and adjacent sources |

## Accessibility Contract

- Interactive targets are at least 48 dp.
- Text contrast follows Material color roles; decorative accent fills never replace readable labels.
- Content remains available and ordered when animations are disabled.
- No important text is drawn directly on Canvas.
- Layout must be manually verified at 200% font scale on an emulator.

## Guardrails

- Do not reproduce the sample avatars, course imagery, schedule data, or navigation model.
- Do not add organic shapes where they obscure ordering or causality.
- Do not introduce a new raw color inside a feature; add or reuse a semantic theme role.
- Do not use fixed-height content containers for educational copy.
