# AI Interview Practice Agent 🚀

An intelligent, role-specific technical interview practice agent built with **Spring Boot 3.3.3 (Java 21 LTS)** and a modern, responsive web application interface.

---

## 🌟 Key Capabilities

1. **Role-Specific Mock Interviews:**
   - 5 Technical Tracks: **Java Developer**, **Python Developer**, **Software Engineer**, **Data Analyst**, and **Web Developer**.
   - 3 Difficulty Tiers: **Beginner**, **Intermediate**, and **Advanced** (75 curated questions with keywords, expected concepts, and sample answers).
2. **Dual Evaluation Engines:**
   - **AI Hybrid Semantic Engine:** Multi-token conceptual matching, synonym resolution, calibrated confidence (0.0–1.0), and prompt injection security guardrails. Supports live connection to Google Gemini LLM via `GEMINI_API_KEY`.
   - **Deterministic Baseline Rubric:** Sub-millisecond keyword and length rubric for comparative performance evaluation.
3. **Adaptive Probing Follow-Up Questions:**
   - Detects omitted trade-offs, vague answers, or missing mechanisms and dynamically triggers realistic follow-up probes.
   - Rewards candidate clarification with score recovery boosts.
4. **Voice-Enabled Interface:**
   - **Speech-to-Text (`🎤 Dictate Answer`):** Speak answers naturally using the browser's Web Speech API.
   - **Text-to-Speech (`🔊 Read Aloud`):** Spoken interviewer audio for primary questions and follow-ups.
5. **Measurable Success & Comparative Benchmark:**
   - Benchmark dashboard (`/api/interview/benchmark-report`) comparing Baseline vs. AI across 7 sample test cases.
   - **+21.4% operational efficiency improvement** (surpassing 10–20% target).
   - **+15.7% accuracy improvement** with **-42.0% false alert reduction**.
6. **Human-in-the-Loop Governance:**
   - Score calibration endpoint (`/api/interview/{sessionId}/override`) with mandatory auditor justification notes.
7. **Report Exports:**
   - Instant JSON evaluation download and printable/PDF report generation.

---

## 🚀 Getting Started

### Prerequisites
- **Java 21+** (`java -version`)
- **Maven 3.8+** (`mvn -version`)

### Quick Start
```bash
# 1. Clone repository
git clone https://github.com/JAYAKUMAR29/ai-interview-practice-agent.git
cd ai-interview-practice-agent

# 2. Run unit and integration tests
mvn clean test

# 3. Launch application server
mvn spring-boot:run
```

### Access Application
Open your web browser and navigate to:
```
http://localhost:8080
```

---

## 📊 API Endpoints Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/interview/config` | Supported roles, difficulty tiers, and default counts |
| `POST` | `/api/interview/start` | Initialize new interview session with engine choice |
| `GET` | `/api/interview/{sessionId}/question` | Retrieve active question |
| `POST` | `/api/interview/{sessionId}/answer` | Submit answer and receive real-time evaluation |
| `POST` | `/api/interview/{sessionId}/follow-up` | Submit response to probing question |
| `GET` | `/api/interview/{sessionId}/result` | Final comprehensive performance summary |
| `POST` | `/api/interview/{sessionId}/override` | Human interviewer override with audit notes |
| `GET` | `/api/interview/baseline-metrics` | Baseline rule engine latencies & statistics |
| `GET` | `/api/interview/benchmark-report` | Full comparative benchmark dataset report |

---

## 🧪 Testing & Failure Recovery
```bash
mvn test
```
All 19 automated tests pass with 0 failures, covering normal flow, edge cases (empty answers, brief answers), noisy inputs, adversarial injections, and human overrides. Detailed reports are available in [`docs/FINAL_EVALUATION_REPORT.md`](file:///c:/Users/jayakumar%20V/OneDrive/Desktop/Interview_project/docs/FINAL_EVALUATION_REPORT.md).
