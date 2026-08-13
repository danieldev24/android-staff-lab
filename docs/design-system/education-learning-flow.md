# Education Learning Flow

Status: Adopted  
Scope: Application Startup topic and future Android Staff Lab topics

## Problem

The original topic placed the mental model, nine-stage timeline, simulation, live evidence, and six staff caveats in one long document. The material was accurate, but a learner had to scroll through unrelated detail before reaching the next learning action.

## Course Structure

Each topic is a short guided course with four lessons:

1. **Overview — orient:** state the question, boundaries, and learning outcomes.
2. **Flow — trace:** follow one launch and inspect one stage at a time.
3. **Lab — experiment:** change controlled costs and observe TTID/TTFD effects.
4. **Evidence — validate:** compare the model with a live trace or one staff caveat.

The lesson header communicates position and permits direct access. The fixed bottom action provides a predictable Previous/Next path in the thumb zone. Completing the fourth lesson produces a clear completion state and a review action.

## Progressive Disclosure Rules

- Render only the content for the selected lesson; do not keep prior lessons above it.
- Represent the nine-stage trace as a horizontal overview and one selected detail card.
- Keep Live trace and Staff notes as mutually exclusive evidence modes.
- Show one staff caveat at a time, with a numbered horizontal selector.
- Keep sources adjacent to the claim they support and open them in the existing source sheet.
- Preserve independent scroll position per lesson only when it helps the learner resume context.

## Library Pattern

The library behaves like an education product rather than a technical menu:

- Track-level introduction explains the learning promise.
- A compact path summary communicates available progress.
- Each topic card exposes level, readiness, lesson count, stage count, and interaction type before the learner enters.
- The primary action uses an explicit learning verb: **Start lesson**.

## Typography Contract

The interface uses four base sizes—32, 24, 16, and 12 sp—and two weights—Bold and Normal. Material text roles map onto this small scale so hierarchy remains consistent across lessons.

At font scale 150% and above:

- library identity, level, and topic status reflow vertically;
- competing metadata must not constrain a title to a narrow column;
- lesson navigation actions stack when necessary;
- educational copy remains unconstrained in height and reachable by scrolling.

## Acceptance Criteria

- A learner can move from Overview to completion without scrolling to find the lesson navigation.
- Flow exposes all nine stages while rendering only one detailed explanation.
- Evidence never renders the live log and all six caveat cards at the same time.
- All lesson and evidence controls have at least a 48 dp target and semantic labels.
- Default and 200% font-scale layouts contain no clipped instructional text.

