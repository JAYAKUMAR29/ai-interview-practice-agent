// AI Interview Practice Agent - Frontend Logic (100% Complete Implementation)
document.addEventListener('DOMContentLoaded', () => {
  // Application State
  let appConfig = null;
  let selectedRole = 'JAVA_DEVELOPER';
  let selectedDifficulty = 'INTERMEDIATE';
  let selectedEngine = 'AI_HYBRID'; // Default AI Semantic Engine
  let questionCount = 5;

  let currentSessionId = null;
  let currentQuestion = null;
  let isAwaitingFollowUp = false;
  let questionStartTime = null;
  let timerInterval = null;
  let currentSummaryData = null;

  // Speech Recognition & Synthesis State
  let speechRecognition = null;
  let isListening = false;
  let currentSpeechUtterance = null;

  // DOM Elements - Views
  const viewSetup = document.getElementById('view-setup');
  const viewSession = document.getElementById('view-session');
  const viewResult = document.getElementById('view-result');

  // DOM Elements - Setup
  const roleGrid = document.getElementById('role-grid');
  const diffGrid = document.getElementById('diff-grid');
  const displayCount = document.getElementById('display-count');
  const btnDecCount = document.getElementById('btn-dec-count');
  const btnIncCount = document.getElementById('btn-inc-count');
  const btnStartInterview = document.getElementById('btn-start-interview');

  // DOM Elements - Interview Session
  const badgeRole = document.getElementById('badge-role');
  const badgeDiff = document.getElementById('badge-diff');
  const badgeTimer = document.getElementById('badge-timer');
  const progressQuestionLabel = document.getElementById('progress-question-label');
  const progressPercentLabel = document.getElementById('progress-percent-label');
  const progressBarFill = document.getElementById('progress-bar-fill');

  const activeQuestionTopic = document.getElementById('active-question-topic');
  const activeQuestionText = document.getElementById('active-question-text');
  const btnSpeakQuestion = document.getElementById('btn-speak-question');

  const followUpBox = document.getElementById('follow-up-box');
  const followUpText = document.getElementById('follow-up-text');
  const followUpReason = document.getElementById('follow-up-reason');
  const btnSpeakFollowup = document.getElementById('btn-speak-followup');

  const answerLabel = document.getElementById('answer-label');
  const candidateAnswerInput = document.getElementById('candidate-answer-input');
  const btnVoiceInput = document.getElementById('btn-voice-input');
  const wordCountLabel = document.getElementById('word-count-label');
  const btnSubmitAnswer = document.getElementById('btn-submit-answer');

  // DOM Elements - Feedback
  const feedbackCard = document.getElementById('feedback-card');
  const feedbackScoreNum = document.getElementById('feedback-score-num');
  const metricRel = document.getElementById('metric-rel');
  const metricKw = document.getElementById('metric-kw');
  const metricComp = document.getElementById('metric-comp');
  const metricClarity = document.getElementById('metric-clarity');
  const feedbackStrengthsList = document.getElementById('feedback-strengths-list');
  const feedbackImprovementsList = document.getElementById('feedback-improvements-list');
  const feedbackKeywordsTags = document.getElementById('feedback-keywords-tags');
  const feedbackExplanationText = document.getElementById('feedback-explanation-text');
  const btnNextQuestion = document.getElementById('btn-next-question');

  // DOM Elements - Summary
  const finalScoreVal = document.getElementById('final-score-val');
  const finalPerfBadge = document.getElementById('final-perf-badge');
  const finalSessionMeta = document.getElementById('final-session-meta');
  const finalRecommendationText = document.getElementById('final-recommendation-text');
  const statTotalQ = document.getElementById('stat-total-q');
  const statAvgTime = document.getElementById('stat-avg-time');
  const statAvgKw = document.getElementById('stat-avg-kw');
  const statAvgComp = document.getElementById('stat-avg-comp');
  const finalStrengthsList = document.getElementById('final-strengths-list');
  const finalImprovementsList = document.getElementById('final-improvements-list');
  const breakdownContainer = document.getElementById('breakdown-container');
  const btnRestartInterview = document.getElementById('btn-restart-interview');
  const btnExportJson = document.getElementById('btn-export-json');
  const btnPrintReport = document.getElementById('btn-print-report');

  // DOM Elements - Baseline Modal
  const btnShowBaseline = document.getElementById('btn-show-baseline');
  const baselineModal = document.getElementById('baseline-modal');
  const btnCloseBaseline = document.getElementById('btn-close-baseline');
  const baseEvalLatency = document.getElementById('base-eval-latency');
  const baseCompletionRate = document.getElementById('base-completion-rate');
  const baseAnswersEvaluated = document.getElementById('base-answers-evaluated');
  const baseFollowupRate = document.getElementById('base-followup-rate');
  const baseEngineModel = document.getElementById('base-engine-model');

  // DOM Elements - Benchmark Modal
  const btnShowBenchmark = document.getElementById('btn-show-benchmark');
  const benchmarkModal = document.getElementById('benchmark-modal');
  const btnCloseBenchmark = document.getElementById('btn-close-benchmark');
  const benchEfficiency = document.getElementById('bench-efficiency');
  const benchAccuracy = document.getElementById('bench-accuracy');
  const benchReduction = document.getElementById('bench-reduction');
  const tableBaseLat = document.getElementById('table-base-lat');
  const tableAiLat = document.getElementById('table-ai-lat');
  const failureScenariosContainer = document.getElementById('failure-scenarios-container');
  const testCasesAccordion = document.getElementById('test-cases-accordion');

   // DOM Elements - Human Override Modal
  const overrideModal = document.getElementById('override-modal');
  const btnCloseOverride = document.getElementById('btn-close-override');
  const btnCancelOverride = document.getElementById('btn-cancel-override');
  const btnConfirmOverride = document.getElementById('btn-confirm-override');
  const overrideQTitle = document.getElementById('override-q-title');
  const overrideScoreInput = document.getElementById('override-score-input');
  const overrideNotesInput = document.getElementById('override-notes-input');
  let activeOverrideQuestionNum = 1;

  // DOM Elements - Scaffolding & Code Mode
  const btnToggleCodeMode = document.getElementById('btn-toggle-code-mode');
  const quickChipBtns = document.querySelectorAll('.quick-chip-btn');

  // DOM Elements - Practice History Modal
  const btnShowHistory = document.getElementById('btn-show-history');
  const historyModal = document.getElementById('history-modal');
  const btnCloseHistory = document.getElementById('btn-close-history');
  const btnCloseHistoryFooter = document.getElementById('btn-close-history-footer');
  const btnClearHistory = document.getElementById('btn-clear-history');
  const histTotalSessions = document.getElementById('hist-total-sessions');
  const histAvgScore = document.getElementById('hist-avg-score');
  const histBestRole = document.getElementById('hist-best-role');
  const historyListContainer = document.getElementById('history-list-container');

  // Initialize Web Speech API
  initSpeechSynthesisAndRecognition();

  function initSpeechSynthesisAndRecognition() {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (SpeechRecognition) {
      speechRecognition = new SpeechRecognition();
      speechRecognition.continuous = true;
      speechRecognition.interimResults = true;
      speechRecognition.lang = 'en-US';

      speechRecognition.onresult = (event) => {
        let transcript = '';
        for (let i = event.resultIndex; i < event.results.length; i++) {
          transcript += event.results[i][0].transcript;
        }
        if (transcript) {
          candidateAnswerInput.value = (candidateAnswerInput.value + ' ' + transcript).trim();
          updateWordCount();
        }
      };

      speechRecognition.onerror = (event) => {
        console.warn('Speech recognition error:', event.error);
        stopVoiceDictation();
      };

      speechRecognition.onend = () => {
        if (isListening) {
          stopVoiceDictation();
        }
      };
    } else {
      if (btnVoiceInput) {
        btnVoiceInput.title = 'Web Speech Recognition not supported in this browser';
      }
    }
  }

  function startVoiceDictation() {
    if (!speechRecognition) {
      showToast('Speech-to-Text is not supported by your browser. Please type your answer.');
      return;
    }
    try {
      speechRecognition.start();
      isListening = true;
      btnVoiceInput.classList.add('active-listening');
      btnVoiceInput.innerHTML = '🛑 Listening... (Click to stop)';
      showToast('Microphone active: Speak your answer clearly.');
    } catch (e) {
      console.error(e);
      stopVoiceDictation();
    }
  }

  function stopVoiceDictation() {
    if (speechRecognition && isListening) {
      try {
        speechRecognition.stop();
      } catch (e) {}
    }
    isListening = false;
    if (btnVoiceInput) {
      btnVoiceInput.classList.remove('active-listening');
      btnVoiceInput.innerHTML = '🎤 Dictate Answer (Voice)';
    }
  }

  function speakTextAloud(text) {
    if (!('speechSynthesis' in window)) {
      showToast('Text-to-Speech is not supported by your browser.');
      return;
    }
    window.speechSynthesis.cancel(); // Stop any active utterance
    if (!text) return;

    currentSpeechUtterance = new SpeechSynthesisUtterance(text);
    currentSpeechUtterance.rate = 1.0;
    currentSpeechUtterance.pitch = 1.0;
    currentSpeechUtterance.lang = 'en-US';
    window.speechSynthesis.speak(currentSpeechUtterance);
  }

  if (btnVoiceInput) {
    btnVoiceInput.addEventListener('click', () => {
      if (isListening) {
        stopVoiceDictation();
      } else {
        startVoiceDictation();
      }
    });
  }

  if (btnSpeakQuestion) {
    btnSpeakQuestion.addEventListener('click', () => {
      speakTextAloud(activeQuestionText.textContent);
    });
  }

  if (btnSpeakFollowup) {
    btnSpeakFollowup.addEventListener('click', () => {
      speakTextAloud(followUpText.textContent);
    });
  }

  // Load Configuration
  async function loadConfig() {
    try {
      const res = await fetch('/api/interview/config');
      if (!res.ok) throw new Error('Failed to load configuration');
      appConfig = await res.json();
      renderSetupOptions(appConfig);
    } catch (err) {
      console.error(err);
      showToast('Error connecting to backend API. Please make sure the server is running.');
    }
  }

  function renderSetupOptions(config) {
    // Engine selector cards
    document.querySelectorAll('.engine-card').forEach(card => {
      card.addEventListener('click', () => {
        document.querySelectorAll('.engine-card').forEach(c => c.classList.remove('selected'));
        card.classList.add('selected');
        selectedEngine = card.getAttribute('data-engine');
      });
    });

    // Render Roles
    roleGrid.innerHTML = '';
    config.roles.forEach(role => {
      const card = document.createElement('div');
      card.className = `role-card ${role.code === selectedRole ? 'selected' : ''}`;
      card.innerHTML = `
        <div class="role-card-title">${role.displayName}</div>
        <div class="role-card-desc">${role.description}</div>
      `;
      card.addEventListener('click', () => {
        selectedRole = role.code;
        document.querySelectorAll('.role-card').forEach(c => c.classList.remove('selected'));
        card.classList.add('selected');
      });
      roleGrid.appendChild(card);
    });

    // Render Difficulties
    diffGrid.innerHTML = '';
    config.difficulties.forEach(diff => {
      const card = document.createElement('div');
      card.className = `diff-card ${diff.code === selectedDifficulty ? 'selected' : ''}`;
      card.innerHTML = `
        <div class="diff-card-title">${diff.displayName}</div>
        <div class="diff-card-desc">${diff.description}</div>
      `;
      card.addEventListener('click', () => {
        selectedDifficulty = diff.code;
        document.querySelectorAll('.diff-card').forEach(c => c.classList.remove('selected'));
        card.classList.add('selected');
      });
      diffGrid.appendChild(card);
    });
  }

  // Question Count Buttons
  btnDecCount.addEventListener('click', () => {
    if (questionCount > 1) {
      questionCount--;
      displayCount.textContent = questionCount;
    }
  });

  btnIncCount.addEventListener('click', () => {
    if (questionCount < 10) {
      questionCount++;
      displayCount.textContent = questionCount;
    }
  });

  // Start Interview Action
  btnStartInterview.addEventListener('click', async () => {
    btnStartInterview.disabled = true;
    btnStartInterview.textContent = 'Initializing AI Session...';

    try {
      const res = await fetch('/api/interview/start', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          role: selectedRole,
          difficulty: selectedDifficulty,
          questionCount: questionCount,
          engineType: selectedEngine
        })
      });

      if (!res.ok) {
        const errData = await res.json();
        throw new Error(errData.message || 'Failed to start interview');
      }

      const sessionData = await res.json();
      currentSessionId = sessionData.sessionId;

      showView('session');
      displayQuestion(sessionData);
    } catch (err) {
      showToast(err.message);
      btnStartInterview.disabled = false;
      btnStartInterview.textContent = 'Start Mock Interview 🚀';
    }
  });

  function displayQuestion(qData) {
    currentQuestion = qData;
    isAwaitingFollowUp = false;
    stopVoiceDictation();

    // Reset UI fields
    followUpBox.style.display = 'none';
    feedbackCard.style.display = 'none';
    btnSubmitAnswer.style.display = 'flex';
    btnSubmitAnswer.disabled = false;
    btnSubmitAnswer.textContent = 'Submit Answer for Evaluation ⚡';
    candidateAnswerInput.value = '';
    candidateAnswerInput.disabled = false;
    candidateAnswerInput.placeholder = 'Type or use your voice to provide a structured technical answer. Be thorough with concepts, trade-offs, and examples...';
    answerLabel.textContent = 'Your Technical Response';
    wordCountLabel.textContent = '0 words';

    // Update Header badges
    badgeRole.textContent = `Role: ${getRoleDisplayName(qData.role)}`;
    badgeDiff.textContent = `Difficulty: ${getDiffDisplayName(qData.difficulty)}`;

    // Update Progress
    const qNum = qData.questionNumber;
    const totalQ = qData.totalQuestions;
    const pct = Math.round((qNum / totalQ) * 100);
    progressQuestionLabel.textContent = `Question ${qNum} of ${totalQ}`;
    progressPercentLabel.textContent = `${pct}% Completed`;
    progressBarFill.style.width = `${pct}%`;

    // Question content
    activeQuestionTopic.textContent = qData.topic || 'General';
    activeQuestionText.textContent = qData.questionText;

    startTimer();
  }

  function updateWordCount() {
    const text = candidateAnswerInput.value.trim();
    const words = text ? text.split(/\s+/).length : 0;
    wordCountLabel.textContent = `${words} words`;
  }

  // Word counter
  candidateAnswerInput.addEventListener('input', updateWordCount);

  // Submit Answer
  btnSubmitAnswer.addEventListener('click', async () => {
    stopVoiceDictation();
    const rawAnswer = candidateAnswerInput.value.trim();

    // Empty answer check
    if (!rawAnswer) {
      showToast('Please enter or dictate an answer before submitting. Empty answers receive 0 points.');
      return;
    }

    const responseDuration = Date.now() - questionStartTime;
    btnSubmitAnswer.disabled = true;
    btnSubmitAnswer.textContent = 'Evaluating with AI Engine...';

    try {
      let res;
      if (isAwaitingFollowUp) {
        res = await fetch(`/api/interview/${currentSessionId}/follow-up`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            followUpAnswer: rawAnswer,
            responseTimeMs: responseDuration
          })
        });
      } else {
        res = await fetch(`/api/interview/${currentSessionId}/answer`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            answer: rawAnswer,
            responseTimeMs: responseDuration
          })
        });
      }

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to submit answer');
      }

      const evalData = await res.json();
      stopTimer();

      if (evalData.followUpRequired) {
        handleFollowUpTrigger(evalData);
      } else {
        displayFeedback(evalData);
      }
    } catch (err) {
      showToast(err.message);
      btnSubmitAnswer.disabled = false;
      btnSubmitAnswer.textContent = 'Submit Answer for Evaluation ⚡';
    }
  });

  function handleFollowUpTrigger(evalData) {
    isAwaitingFollowUp = true;
    showToast('Probing follow-up question triggered to explore candidate depth.');

    followUpBox.style.display = 'block';
    followUpText.textContent = evalData.followUpQuestion;
    followUpReason.textContent = `Trigger Rationale: ${evalData.followUpReason}`;

    // Reset input for follow up answer
    candidateAnswerInput.value = '';
    candidateAnswerInput.placeholder = 'Provide clarification or elaborate on the follow-up question...';
    answerLabel.textContent = 'Your Follow-Up Clarification';
    wordCountLabel.textContent = '0 words';

    btnSubmitAnswer.disabled = false;
    btnSubmitAnswer.textContent = 'Submit Follow-Up Response ➔';

    startTimer();
  }

  function displayFeedback(evalData) {
    btnSubmitAnswer.style.display = 'none';
    candidateAnswerInput.disabled = true;
    feedbackCard.style.display = 'block';

    const evalResult = evalData.evaluation;
    feedbackScoreNum.textContent = evalResult.score;

    // Mini metrics
    metricRel.textContent = `${evalResult.relevanceScore}/25`;
    metricKw.textContent = `${evalResult.keywordScore}/25`;
    metricComp.textContent = `${evalResult.completenessScore}/25`;
    metricClarity.textContent = `${evalResult.lengthScore + evalResult.clarityScore}/25`;

    // Strengths
    feedbackStrengthsList.innerHTML = '';
    evalResult.strengths.forEach(s => {
      const li = document.createElement('li');
      li.textContent = s;
      feedbackStrengthsList.appendChild(li);
    });

    // Improvements
    feedbackImprovementsList.innerHTML = '';
    evalResult.areasForImprovement.forEach(imp => {
      const li = document.createElement('li');
      li.textContent = imp;
      feedbackImprovementsList.appendChild(li);
    });

    // Keyword tags
    feedbackKeywordsTags.innerHTML = '';
    if (evalResult.matchedKeywords && evalResult.matchedKeywords.length > 0) {
      evalResult.matchedKeywords.forEach(kw => {
        const span = document.createElement('span');
        span.className = 'kw-tag kw-matched';
        span.textContent = `✓ ${kw}`;
        feedbackKeywordsTags.appendChild(span);
      });
    }
    if (evalResult.missingKeywords && evalResult.missingKeywords.length > 0) {
      evalResult.missingKeywords.forEach(kw => {
        const span = document.createElement('span');
        span.className = 'kw-tag kw-missing';
        span.textContent = `✗ ${kw}`;
        feedbackKeywordsTags.appendChild(span);
      });
    }

    // Explanation & Evidence
    let explanationMarkup = evalResult.explanation;
    if (evalResult.confidence) {
      explanationMarkup += ` (Calibrated Confidence: ${(evalResult.confidence * 100).toFixed(0)}%)`;
    }
    feedbackExplanationText.textContent = explanationMarkup;

    if (evalData.finished) {
      btnNextQuestion.textContent = 'View Final Comprehensive Evaluation 🏆';
    } else {
      btnNextQuestion.textContent = 'Proceed to Next Question ➔';
    }

    btnNextQuestion.onclick = async () => {
      if (evalData.finished) {
        await loadFinalResults();
      } else {
        await fetchNextQuestion();
      }
    };
  }

  async function fetchNextQuestion() {
    try {
      const res = await fetch(`/api/interview/${currentSessionId}/question`);
      if (!res.ok) throw new Error('Failed to retrieve next question');
      const qData = await res.json();
      displayQuestion(qData);
    } catch (err) {
      showToast(err.message);
    }
  }

  async function loadFinalResults() {
    try {
      const res = await fetch(`/api/interview/${currentSessionId}/result`);
      if (!res.ok) throw new Error('Failed to retrieve interview results');
      const summary = await res.json();
      currentSummaryData = summary;
      displaySummary(summary);
    } catch (err) {
      showToast(err.message);
    }
  }

  function displaySummary(summary) {
    showView('result');
    currentSummaryData = summary;

    finalScoreVal.textContent = summary.overallScore;
    finalPerfBadge.textContent = summary.performanceLevel;

    // Performance badge styling
    finalPerfBadge.className = 'perf-badge';
    if (summary.overallScore >= 85) finalPerfBadge.classList.add('perf-excellent');
    else if (summary.overallScore >= 70) finalPerfBadge.classList.add('perf-good');
    else if (summary.overallScore >= 50) finalPerfBadge.classList.add('perf-fair');
    else finalPerfBadge.classList.add('perf-dev');

    const engineName = summary.engineType === 'AI_HYBRID' ? 'AI Hybrid Engine' : (summary.engineType === 'AI_GEMINI' ? 'Gemini LLM' : 'Baseline Rule-Based');
    finalSessionMeta.textContent = `Role: ${getRoleDisplayName(summary.role)} | Difficulty: ${getDiffDisplayName(summary.difficulty)} | Engine: ${engineName} | Duration: ${summary.totalDurationSeconds}s`;
    finalRecommendationText.textContent = summary.overallRecommendation;

    statTotalQ.textContent = summary.answeredQuestions;
    statAvgTime.textContent = `${Math.round(summary.avgResponseTimeSeconds)}s`;
    statAvgKw.textContent = `${Math.round((summary.avgKeywordMatch / 25) * 100)}%`;
    statAvgComp.textContent = `${Math.round((summary.avgCompleteness / 25) * 100)}%`;

    // Strengths
    finalStrengthsList.innerHTML = '';
    summary.keyStrengths.forEach(str => {
      const li = document.createElement('li');
      li.textContent = str;
      finalStrengthsList.appendChild(li);
    });

    // Improvements
    finalImprovementsList.innerHTML = '';
    summary.keyImprovements.forEach(imp => {
      const li = document.createElement('li');
      li.textContent = imp;
      finalImprovementsList.appendChild(li);
    });

    // Detailed Breakdown Accordion
    breakdownContainer.innerHTML = '';
    summary.questionBreakdown.forEach(item => {
      const card = document.createElement('div');
      card.className = 'breakdown-item';
      card.innerHTML = `
        <div class="breakdown-header">
          <div>
            <span style="font-weight: 700; color: var(--text-primary);">Q${item.questionNumber}: ${item.topic}</span>
            <div style="font-size: 13px; color: var(--text-secondary); margin-top: 2px;">${item.questionText}</div>
          </div>
          <div style="display: flex; align-items: center; gap: 12px;">
            <button class="btn-override-sm" data-qnum="${item.questionNumber}">
              ⚖️ Override Score
            </button>
            <div style="font-size: 20px; font-weight: 800; color: ${item.score >= 70 ? 'var(--accent-emerald)' : 'var(--accent-amber)'};">
              ${item.score}/100
            </div>
          </div>
        </div>
        <div class="breakdown-content">
          <div style="margin-bottom: 8px;"><strong>Candidate Answer:</strong> <span style="color: var(--text-secondary);">${item.candidateAnswer}</span></div>
          ${item.hadFollowUp ? `<div style="margin-bottom: 8px; color: #fbbf24;"><strong>Follow-up Probed:</strong> ${item.followUpQuestion}<br><strong>Clarification:</strong> ${item.followUpAnswer || 'None'}</div>` : ''}
          <div style="font-size: 13px; color: var(--text-muted); margin-top: 6px;">${item.explanation} ${item.humanOverridden ? '<strong>(Adjusted via Human Override)</strong>' : ''}</div>
        </div>
      `;

      const overrideBtn = card.querySelector('.btn-override-sm');
      overrideBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        openHumanOverrideModal(item);
      });

      breakdownContainer.appendChild(card);
    });

    // Save session to local browser history
    saveSessionToHistory(summary);
  }

  // Open Human Override Modal
  function openHumanOverrideModal(item) {
    activeOverrideQuestionNum = item.questionNumber;
    overrideQTitle.textContent = `Q${item.questionNumber}: ${item.topic} (Current Score: ${item.score}/100)`;
    overrideScoreInput.value = item.score;
    overrideNotesInput.value = '';
    overrideModal.style.display = 'flex';
  }

  // Confirm Human Override
  btnConfirmOverride.addEventListener('click', async () => {
    const newScore = parseInt(overrideScoreInput.value, 10);
    const notes = overrideNotesInput.value.trim();

    if (isNaN(newScore) || newScore < 0 || newScore > 100) {
      showToast('Please provide a valid score between 0 and 100.');
      return;
    }
    if (!notes) {
      showToast('Auditor justification notes are required for human override.');
      return;
    }

    try {
      btnConfirmOverride.disabled = true;
      btnConfirmOverride.textContent = 'Saving Override...';

      const res = await fetch(`/api/interview/${currentSessionId}/override`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          questionNumber: activeOverrideQuestionNum,
          adjustedScore: newScore,
          overrideNotes: notes
        })
      });

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to apply human override');
      }

      const updatedSummary = await res.json();
      overrideModal.style.display = 'none';
      showToast('Human score override recorded successfully.');
      displaySummary(updatedSummary);
    } catch (e) {
      showToast(e.message);
    } finally {
      btnConfirmOverride.disabled = false;
      btnConfirmOverride.textContent = 'Save Human Override ✍️';
    }
  });

  btnCloseOverride.addEventListener('click', () => overrideModal.style.display = 'none');
  btnCancelOverride.addEventListener('click', () => overrideModal.style.display = 'none');

  // Export JSON Report
  btnExportJson.addEventListener('click', () => {
    if (!currentSummaryData) {
      showToast('No summary data available to download.');
      return;
    }
    const dataStr = 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(currentSummaryData, null, 2));
    const dlAnchor = document.createElement('a');
    dlAnchor.setAttribute('href', dataStr);
    dlAnchor.setAttribute('download', `interview-evaluation-report-${currentSummaryData.sessionId}.json`);
    document.body.appendChild(dlAnchor);
    dlAnchor.click();
    dlAnchor.remove();
    showToast('Evaluation report JSON downloaded successfully.');
  });

  // Print / Save PDF
  btnPrintReport.addEventListener('click', () => {
    window.print();
  });

  // Restart interview
  btnRestartInterview.addEventListener('click', () => {
    showView('setup');
    btnStartInterview.disabled = false;
    btnStartInterview.textContent = 'Start Mock Interview 🚀';
  });

  // Baseline Modal Controls
  btnShowBaseline.addEventListener('click', async () => {
    try {
      const res = await fetch('/api/interview/baseline-metrics');
      if (res.ok) {
        const m = await res.json();
        baseEvalLatency.textContent = `${m.avgEvaluationLatencyMs.toFixed(2)} ms`;
        baseCompletionRate.textContent = `${m.completionRatePercent.toFixed(1)}%`;
        baseAnswersEvaluated.textContent = m.totalAnswersEvaluated;
        baseFollowupRate.textContent = `${m.followUpTriggerRatePercent.toFixed(1)}%`;
        baseEngineModel.textContent = m.engineModel;
      }
    } catch (e) {
      console.error(e);
    }
    baselineModal.style.display = 'flex';
  });

  btnCloseBaseline.addEventListener('click', () => baselineModal.style.display = 'none');

  // Benchmark Modal Controls
  btnShowBenchmark.addEventListener('click', async () => {
    try {
      btnShowBenchmark.textContent = 'Loading Report...';
      const res = await fetch('/api/interview/benchmark-report');
      if (!res.ok) throw new Error('Failed to load benchmark report');
      const b = await res.json();

      benchEfficiency.textContent = `+${b.operationalEfficiencyImprovementPercent.toFixed(1)}%`;
      benchAccuracy.textContent = `${b.aiAccuracyEstimate.toFixed(1)}%`;
      benchReduction.textContent = `-${b.falseAlertReductionPercent.toFixed(1)}%`;

      tableBaseLat.textContent = `~${b.baselineAvgLatencyMs.toFixed(1)} ms`;
      tableAiLat.textContent = `~${b.aiAvgLatencyMs.toFixed(1)} ms`;

      // Failure Scenarios
      const fsa = b.failureScenarioAnalysis;
      failureScenariosContainer.innerHTML = `
        <div style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 12px;">
          <div style="font-weight: 700; color: #34d399; margin-bottom: 4px;">✓ Missing / Blank Data</div>
          <div style="font-size: 12px; color: var(--text-secondary);">${fsa.missingDataHandling}</div>
        </div>
        <div style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 12px;">
          <div style="font-weight: 700; color: #818cf8; margin-bottom: 4px;">✓ Noisy / Typo Resilience</div>
          <div style="font-size: 12px; color: var(--text-secondary);">${fsa.noisyInputHandling}</div>
        </div>
        <div style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 12px;">
          <div style="font-weight: 700; color: #fbbf24; margin-bottom: 4px;">✓ Adversarial Security Guardrail</div>
          <div style="font-size: 12px; color: var(--text-secondary);">${fsa.adversarialResilience}</div>
        </div>
        <div style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 12px;">
          <div style="font-weight: 700; color: #f43f5e; margin-bottom: 4px;">✓ Human-in-the-Loop Override</div>
          <div style="font-size: 12px; color: var(--text-secondary);">${fsa.humanOverrideAuditability}</div>
        </div>
      `;

      // Sample Test Cases Accordion
      testCasesAccordion.innerHTML = '';
      b.testCaseComparisons.forEach(tc => {
        const item = document.createElement('div');
        item.style.cssText = 'background: rgba(255,255,255,0.02); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 12px; font-size: 12px;';
        item.innerHTML = `
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
            <span style="font-weight: 700; color: var(--text-primary); font-family: var(--font-mono);">${tc.testCaseId} [${tc.scenarioType}]</span>
            <div style="display: flex; gap: 8px;">
              <span style="background: rgba(156, 163, 175, 0.2); color: #d1d5db; padding: 2px 6px; border-radius: 4px;">Baseline: ${tc.baselineScore}/100</span>
              <span style="background: rgba(16, 185, 129, 0.2); color: #34d399; padding: 2px 6px; border-radius: 4px;">AI: ${tc.aiScore}/100</span>
            </div>
          </div>
          <div style="color: var(--text-secondary); margin-bottom: 4px;"><strong>Answer:</strong> "${tc.candidateAnswer}"</div>
          <div style="color: var(--text-muted);"><strong>Analysis:</strong> ${tc.outcomeAnalysis}</div>
        `;
        testCasesAccordion.appendChild(item);
      });

      benchmarkModal.style.display = 'flex';
    } catch (e) {
      showToast('Error loading benchmark report: ' + e.message);
    } finally {
      btnShowBenchmark.textContent = '📈 Comparative Benchmark & Report';
    }
  });

  btnCloseBenchmark.addEventListener('click', () => benchmarkModal.style.display = 'none');

  // Monospace Code Mode Toggle
  if (btnToggleCodeMode) {
    btnToggleCodeMode.addEventListener('click', () => {
      candidateAnswerInput.classList.toggle('code-mode');
      const isCode = candidateAnswerInput.classList.contains('code-mode');
      btnToggleCodeMode.textContent = isCode ? '📝 Normal Text View' : '💻 Monospace Code View';
      btnToggleCodeMode.style.borderColor = isCode ? 'var(--primary-light)' : 'var(--border-color)';
    });
  }

  // Quick Scaffold Chips Insertion
  quickChipBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      const insertText = btn.getAttribute('data-insert');
      if (insertText) {
        const curVal = candidateAnswerInput.value;
        const prefix = curVal.length > 0 && !curVal.endsWith('\n') && !curVal.endsWith(' ') ? '\n\n' : '';
        candidateAnswerInput.value = curVal + prefix + insertText;
        candidateAnswerInput.focus();
        updateWordCount();
      }
    });
  });

  // Practice History Modal Handlers
  if (btnShowHistory) {
    btnShowHistory.addEventListener('click', renderPracticeHistory);
  }
  if (btnCloseHistory) {
    btnCloseHistory.addEventListener('click', () => historyModal.style.display = 'none');
  }
  if (btnCloseHistoryFooter) {
    btnCloseHistoryFooter.addEventListener('click', () => historyModal.style.display = 'none');
  }
  if (btnClearHistory) {
    btnClearHistory.addEventListener('click', () => {
      if (confirm('Are you sure you want to clear your local interview practice history?')) {
        localStorage.removeItem('interview_agent_history');
        renderPracticeHistory();
        showToast('Practice history cleared.');
      }
    });
  }

  function getHistoryRecords() {
    try {
      const raw = localStorage.getItem('interview_agent_history');
      return raw ? JSON.parse(raw) : [];
    } catch (e) {
      return [];
    }
  }

  function saveSessionToHistory(summary) {
    try {
      const records = getHistoryRecords();
      const newEntry = {
        sessionId: summary.sessionId,
        role: summary.role,
        difficulty: summary.difficulty,
        overallScore: summary.overallScore,
        performanceLevel: summary.performanceLevel,
        answeredQuestions: summary.answeredQuestions,
        totalDurationSeconds: summary.totalDurationSeconds,
        engineType: summary.engineType,
        timestamp: new Date().toISOString()
      };
      // Keep most recent 50 sessions
      records.unshift(newEntry);
      if (records.length > 50) records.pop();
      localStorage.setItem('interview_agent_history', JSON.stringify(records));
    } catch (e) {
      console.error('Failed to save session history to localStorage', e);
    }
  }

  function renderPracticeHistory() {
    const records = getHistoryRecords();
    histTotalSessions.textContent = records.length;

    if (records.length === 0) {
      histAvgScore.textContent = '0/100';
      histBestRole.textContent = 'None';
      historyListContainer.innerHTML = `
        <div style="text-align: center; color: var(--text-muted); padding: 30px; font-size: 13px;">
          No completed practice sessions yet. Start an interview to record your results!
        </div>
      `;
      historyModal.style.display = 'flex';
      return;
    }

    const avgScore = Math.round(records.reduce((acc, r) => acc + (r.overallScore || 0), 0) / records.length);
    histAvgScore.textContent = `${avgScore}/100`;

    // Calculate most practiced role
    const roleCounts = {};
    records.forEach(r => {
      roleCounts[r.role] = (roleCounts[r.role] || 0) + 1;
    });
    const bestRoleKey = Object.keys(roleCounts).reduce((a, b) => roleCounts[a] > roleCounts[b] ? a : b, records[0].role);
    histBestRole.textContent = getRoleDisplayName(bestRoleKey);

    // Populate list items
    historyListContainer.innerHTML = '';
    records.forEach((rec, idx) => {
      const d = new Date(rec.timestamp);
      const dateStr = !isNaN(d.getTime()) ? d.toLocaleDateString() + ' ' + d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'Recent';
      const item = document.createElement('div');
      item.className = 'history-item-card';

      const scoreColor = rec.overallScore >= 70 ? 'var(--accent-emerald)' : (rec.overallScore >= 50 ? 'var(--accent-amber)' : 'var(--accent-rose)');

      item.innerHTML = `
        <div>
          <div style="font-weight: 700; color: var(--text-primary); font-size: 14px;">
            ${getRoleDisplayName(rec.role)} <span style="font-size: 12px; color: var(--text-secondary); font-weight: normal;">• ${getDiffDisplayName(rec.difficulty)}</span>
          </div>
          <div style="font-size: 12px; color: var(--text-muted); margin-top: 3px;">
            📅 ${dateStr} • ⏱️ ${rec.totalDurationSeconds || 0}s • ${rec.answeredQuestions || 5} Questions • Engine: ${rec.engineType || 'AI'}
          </div>
        </div>
        <div style="text-align: right;">
          <div style="font-size: 20px; font-weight: 800; color: ${scoreColor};">
            ${rec.overallScore}/100
          </div>
          <div style="font-size: 11px; color: var(--text-secondary);">
            ${rec.performanceLevel || 'Completed'}
          </div>
        </div>
      `;
      historyListContainer.appendChild(item);
    });

    historyModal.style.display = 'flex';
  }

  // Close modals on backdrop click
  window.addEventListener('click', (e) => {
    if (e.target === baselineModal) baselineModal.style.display = 'none';
    if (e.target === benchmarkModal) benchmarkModal.style.display = 'none';
    if (e.target === overrideModal) overrideModal.style.display = 'none';
    if (e.target === historyModal) historyModal.style.display = 'none';
  });

  // Helpers
  function showView(view) {
    viewSetup.style.display = view === 'setup' ? 'block' : 'none';
    viewSession.style.display = view === 'session' ? 'block' : 'none';
    viewResult.style.display = view === 'result' ? 'block' : 'none';
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  function startTimer() {
    stopTimer();
    questionStartTime = Date.now();
    timerInterval = setInterval(() => {
      const elapsed = Math.floor((Date.now() - questionStartTime) / 1000);
      const mins = String(Math.floor(elapsed / 60)).padStart(2, '0');
      const secs = String(elapsed % 60).padStart(2, '0');
      badgeTimer.textContent = `⏱️ ${mins}:${secs}`;
    }, 1000);
  }

  function stopTimer() {
    if (timerInterval) {
      clearInterval(timerInterval);
      timerInterval = null;
    }
  }

  function getRoleDisplayName(roleVal) {
    if (!appConfig) return roleVal;
    const match = appConfig.roles.find(r => r.code === roleVal);
    return match ? match.displayName : roleVal;
  }

  function getDiffDisplayName(diffVal) {
    if (!appConfig) return diffVal;
    const match = appConfig.difficulties.find(d => d.code === diffVal);
    return match ? match.displayName : diffVal;
  }

  function showToast(msg) {
    const existing = document.querySelector('.toast-msg');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = 'toast-msg';
    toast.textContent = msg;
    document.body.appendChild(toast);

    setTimeout(() => {
      toast.remove();
    }, 4500);
  }

  // Initial fetch
  loadConfig();
});
