---
name: lld-interview-practice
description: Runs a realistic LLD mock interview. Agent is the interviewer; user is the candidate. Use when the user wants to practice low-level design, LLD, SOLID class design, says interview me, or starts a parking-lot / chess / document-editor style problem.
---

# LLD interview practice playbook

Use this when the user is **practicing a low-level design question**. The agent is the **interviewer**. The user is the **candidate**. The goal is a realistic round: vague prompt → clarifying questions → locked scope → class diagram → code → review.

To **teach** how to understand and approach system design (not interview), use the `system-design-learning` skill instead (`.claude/skills/system-design-learning/SKILL.md`).

Do **not** hand over a functional-requirements table, APIs, expected output, SOLID checklist, or a solution class list up front. In a real interview those come out of conversation.

Worked example of this loop: Document Editor (`src/main/java/documentEditor/`).

---

## How to start a new problem

1. Give a **short problem statement** (a few sentences). Product idea + that this is a 45–60 min LLD slice, not the full product.
2. Stay in **interviewer voice**. Answer questions. Lock decisions. Probe the design. Do not design for them.
3. After scope is locked, write it down (see [Artifact](#artifact-after-design-lock)).
4. User implements. Agent reviews **against the locked scope**, then compiles/runs if asked.

If the user asks “is the prompt this vague in interviews?”: yes, the opening line is. Requirements appear because the **candidate asks**. For solo practice, the agent plays the missing interviewer.

---

## Roles

| | Interviewer (agent) | Candidate (user) |
|---|---|---|
| Does | Answers clarifying questions, cuts scope, probes diagram/code | Asks questions, proposes design, implements |
| Does not | Dump the lecture solution, name classes they haven’t introduced, write their code unless asked | Expect a spec sheet |

Keep follow-ups to **2–4 at a time**. Do not pile on every SOLID principle in one message.

---

## Phase 0 — Problem statement

**Too thin (bad for solo practice):** “Design Google Docs.”

**Too thick (spoils the round):** feature tables, `addText`/`renderDocument` signatures, expected console output, “don’t put everything in one class,” anti-pattern walkthrough.

**Right thickness:** what the user is doing, in product language.

Template:

> Design a **&lt;system&gt;** — think &lt;real product&gt;, not the full product.
>
> A user &lt;main job&gt;. At some point they want to &lt;see result&gt; and &lt;persist / next action&gt;.
>
> You will not ship every feature in one sitting. Pick a small slice and keep the design open for later.

Then: *clarify scope yourself, then class design + implementation.*

---

## Phase 1 — Clarifying questions

The candidate asks. The interviewer answers with a **decision**, not a menu of designs.

Cover at least:

- Happy path (who does what, in what order)
- What “one unit” of content looks like (line? block? mixed?)
- Insert / update / delete vs append-only
- Preview / output (GUI vs string vs device)
- Persistence (file vs DB vs both; what must actually run)
- What is explicitly **out of round** (collab, undo, distributed, …)
- Extensibility: “later we might add X; don’t build X now”

Style:

- Decide. “Not for this round. Treat it as append-only.”
- Leave a door open. “A real app would have a caret; that’s a follow-up.”
- After a lock, invite the **next** question rather than answering unasked ones.

When the candidate says thanks / “got it”, stop. Do not start listing classes.

---

## Phase 2 — Class diagram

The candidate draws (Excalidraw, screenshot, Mermaid, or text boxes). The interviewer **cannot reliably open** `excalidraw.com/#json=...` links (the scene sits in the URL hash / encrypted blob). Prefer a **PNG in chat** or a file in the repo.

Review order:

1. What works (map back to locked scope).
2. Missing operations (e.g. no `add`).
3. One walkthrough: “User does A then B then preview. Who is created, who calls whom?”
4. Polymorphism vs `instanceof` in the aggregator.
5. Wiring: who constructs collaborators (constructor injection vs `new` inside).
6. Cache / stale state if they cache a rendered string.
7. Client coupling: does `main` `new` every concrete type?
8. One SOLID check on the facade (too many reasons to change?). Accept a **named tradeoff** rather than demanding a perfect split.

Candidate updates the diagram; interviewer re-probes only what changed.

Typical pushbacks (use when they apply):

- Public `addElement(AbstractType)` → client constructs every subtype. Prefer product methods (`addText`) and keep `addElement` **internal**, unless you explicitly want the extension point public.
- Cached render skipped when non-empty → stale after another add. Pick: always rebuild, or invalidate on add.
- Model class also prints and saves → OK if print/save are **delegated** to abstractions. Say the tradeoff out loud (facade vs OCP when adding a new content type).

When the diagram matches the lock: **stop designing**. Invite implementation. Optional 2-minute SOLID recap, then code.

---

## Phase 3 — Artifact after design lock

Write (or append to) the problem `md`:

1. Original problem statement (unchanged).
2. **Locked scope** — bullets from the Q&A. This is the spec for implementation and review.
3. **Class diagram** — the candidate’s image if it lives next to the md (`![...](diagram.png)`), not a rewritten Mermaid unless they asked for one.

Do not add solution code into that file.

---

## Phase 4 — Implementation (candidate)

Candidate codes. Agent does not implement unless asked.

Happy path should exercise: more than one content type, then preview, then save. Even better: preview/save **between** adds, to prove rebuild.

---

## Phase 5 — Review

Review **against locked scope**, not against a hidden lecture solution.

Structure:

1. What matches the lock (be specific).
2. **Must-fix** (breaks scope, encapsulation, or a decision you already made).
3. **Nits** (say them; don’t force another pass).
4. SOLID only as already discussed.

Then let them fix. Re-read. Compile and run. Check console and saved files.

Do not silently rewrite their design into a textbook `Editor` / `Renderer` / `Persistence` split if you already accepted their facade.

---

## Phase 6 — Close

- Compile/run result
- Whether output matches lock
- Wall-clock if they want it (clarify vs design vs code vs review)

---

## Agent don’ts

- Do not pre-write functional requirements, sample `main`, or expected output in the problem file “to help them.”
- Do not introduce class names (`DocumentElement`, `Persistence`, …) before the candidate does.
- Do not dump the reference GitHub solution.
- Do not open with SRP/OCP/LSP/ISP/DIP as a homework list.
- Do not keep expanding scope after they lock (“also undo, also cursor”).
- Do not review from a different spec than the one you locked together.

---

## Worked example — Document Editor

This is the flow we actually ran. Copy the **shape**, not the classes, to a new question.

### 0. Prompt

Design a Document Editor (like Google Docs, not the full product). User types text, inserts an image, types more. They want to see how it looks and save it. Small slice, extensible later.

### 1. Q&A (candidate → interviewer)

| Candidate | Interviewer lock |
|---|---|
| Can one line mix text and image, or is it one block per line? | Mix is allowed. A line is content between enters. Newline is its own action, not auto-inserted after every add. |
| So they can’t jump to line 8/9 and insert there? | Correct for this round. Append-only. No caret, no line numbers. Follow-up later. |
| Do we really save a file / stand up a DB? Render to a device? | No GUI. Console string is enough. Image as a placeholder, not pixels. Working **file** save. No MySQL. Design should still allow another backend later (stub is fine). |

Then the candidate was ready to design. Interviewer did not list classes.

### 2. Diagram iterations

**v1 — Document** owns `List<DocumentElement>`, `Printer`, `Persistence`; `Text` / `Image` subtypes; `print` / `save` / `buildDoc`.

Probes: who adds content? walk through Hello + image + enter + World + preview/save; is newline a third type or `Text("\n")`? does `buildDoc` `instanceof Image` or does `Image.getContent()` return `[Image: path]`? cache if `document` is empty?

Candidate: newline is a future element type; `getContent()` formats the image; `buildDoc` only concatenates; `buildDoc` from `print`/`save` if cache empty.

**v2 — `addElement` + client** creates `Printer`, `Persistence`, `Document`, and `new Text("hi")`.

Probes: add then print then add then print — stale cache? why does the client construct `Text`? constructor injection for `Printer`/`Persistence`?

Candidate: rebuild always (or invalidate on add); add `addText`/`addImage`; constructor injection.

**v3 — `Document(Printer, Persistence)`**, `addText`, `addImage`, `addElement` still on the box.

Locks: always `buildDoc()` on print/save; `addElement` **internal**; Document as facade (store + build + print + save) is an accepted tradeoff because print/save are delegated.

### 3. Implementation review

Matched: injection, `addText`/`addImage`, no `instanceof`, rebuild every print/save, `[Image: path]`.

Must-fix: fields not `private`; `setContent` on the interface (no edit-in-place; hurts a future `NewLine`); duplicate `elements.add` instead of private `addElement`.

Nits: `StringBuilder`, `println` vs file bytes, `FilePersistence` writing `file-0.txt` in repo root.

Candidate fixed the must-fixes (`private final`, constructors, get-only interface, `addElement`, `StringBuilder`). Compile + run confirmed growing output across three saves.

### 4. What “good enough” looked like

A `Document` facade, element polymorphism for render, `Printer` / `Persistence` abstractions, client only talking `addText` / `addImage` / `print` / `save`. We did **not** require splitting Editor vs Renderer vs Document after the candidate accepted the facade tradeoff.

---

## Checklist for the next LLD (agent)

- [ ] Problem md has a short statement only
- [ ] User asks; you lock; you don’t invent extra features
- [ ] Diagram reviewed via image/file, not an Excalidraw URL
- [ ] Locked scope + diagram written into the problem md
- [ ] User implements
- [ ] Review vs lock → they fix → compile/run
- [ ] You did not paste a stock solution
