// AI Interview Practice Agent - Frontend Logic (Milestone 35%)
document.addEventListener('DOMContentLoaded', () => {
  // State
  let appConfig = null;
  let selectedRole = 'JAVA_DEVELOPER';
  let selectedDifficulty = 'INTERMEDIATE';
  let questionCount = 5;

  let currentSessionId = null;
  let currentQuestion = null;
  let isAwaitingFollowUp = false;
  let questionStartTime = null;
  let timerInterval = null;

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
  const followUpBox = document.getElementById('follow-up-box');
  const followUpText = document.getElementById('follow-up-text');
  const followUpReason = document.getElementById('follow-up-reason');

  const answerLabel = document.getElementById('answer-label');
  const candidateAnswerInput = document.getElementById('candidate-answer-input');
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

  // DOM Elements - Baseline Modal
  const btnShowBaseline = document.getElementById('btn-show-baseline');
  const baselineModal = document.getElementById('baseline-modal');
  const btnCloseBaseline = document.getElementById('btn-close-baseline');
  const baseEvalLatency = document.getElementById('base-eval-latency');
  const baseCompletionRate = document.getElementById('base-completion-rate');
  const baseAnswersEvaluated = document.getElementById('base-answers-evaluated');
  const baseFollowupRate = document.getElementById('base-followup-rate');
  const baseEngineModel = document.getElementById('base-engine-model');

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

  // Question Counter Controls
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
    btnStartInterview.textContent = 'Initializing Session...';

    try {
      const res = await fetch('/api/interview/start', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          role: selectedRole,
          difficulty: selectedDifficulty,
          questionCount: questionCount
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

    // Reset UI fields
    followUpBox.style.display = 'none';
    feedbackCard.style.display = 'none';
    btnSubmitAnswer.style.display = 'flex';
    btnSubmitAnswer.disabled = false;
    btnSubmitAnswer.textContent = 'Submit Answer for Evaluation ⚡';
    candidateAnswerInput.value = '';
    candidateAnswerInput.disabled = false;
    candidateAnswerInput.placeholder = 'Type your structured technical answer here. Be thorough with concepts, trade-offs, and examples...';
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

  // Word counter
  candidateAnswerInput.addEventListener('input', () => {
    const text = candidateAnswerInput.value.trim();
    const words = text ? text.split(/\s+/).length : 0;
    wordCountLabel.textContent = `${words} words`;
  });

  // Submit Answer
  btnSubmitAnswer.addEventListener('click', async () => {
    const rawAnswer = candidateAnswerInput.value.trim();

    // Basic Validation: Warn on empty answers
    if (!rawAnswer) {
      showToast('Please enter an answer before submitting. Empty answers receive 0 points.');
      return;
    }

    const responseDuration = Date.now() - questionStartTime;
    btnSubmitAnswer.disabled = true;
    btnSubmitAnswer.textContent = 'Evaluating with Rule-Based Rubric...';

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
        // Trigger Follow-up flow
        handleFollowUpTrigger(evalData);
      } else {
        // Show standard immediate feedback
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
    showToast('Follow-up probing question triggered based on your response.');

    followUpBox.style.display = 'block';
    followUpText.textContent = evalData.followUpQuestion;
    followUpReason.textContent = `Trigger rationale: ${evalData.followUpReason}`;

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

    feedbackExplanationText.textContent = evalResult.explanation;

    if (evalData.finished) {
      btnNextQuestion.textContent = 'View Final Interview Results 🏆';
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
      displaySummary(summary);
    } catch (err) {
      showToast(err.message);
    }
  }

  function displaySummary(summary) {
    showView('result');

    finalScoreVal.textContent = summary.overallScore;
    finalPerfBadge.textContent = summary.performanceLevel;
    
    // Performance badge styling
    finalPerfBadge.className = 'perf-badge';
    if (summary.overallScore >= 85) finalPerfBadge.classList.add('perf-excellent');
    else if (summary.overallScore >= 70) finalPerfBadge.classList.add('perf-good');
    else if (summary.overallScore >= 50) finalPerfBadge.classList.add('perf-fair');
    else finalPerfBadge.classList.add('perf-dev');

    finalSessionMeta.textContent = `Role: ${getRoleDisplayName(summary.role)} | Difficulty: ${getDiffDisplayName(summary.difficulty)} | Duration: ${summary.totalDurationSeconds}s`;
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
          <div style="font-size: 20px; font-weight: 800; color: ${item.score >= 70 ? 'var(--accent-emerald)' : 'var(--accent-amber)'};">
            ${item.score}/100
          </div>
        </div>
        <div class="breakdown-content">
          <div style="margin-bottom: 8px;"><strong>Candidate Answer:</strong> <span style="color: var(--text-secondary);">${item.candidateAnswer}</span></div>
          ${item.hadFollowUp ? `<div style="margin-bottom: 8px; color: #fbbf24;"><strong>Follow-up Probed:</strong> ${item.followUpQuestion}<br><strong>Clarification:</strong> ${item.followUpAnswer || 'None'}</div>` : ''}
          <div style="font-size: 13px; color: var(--text-muted); margin-top: 6px;">${item.explanation}</div>
        </div>
      `;
      breakdownContainer.appendChild(card);
    });
  }

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

  btnCloseBaseline.addEventListener('click', () => {
    baselineModal.style.display = 'none';
  });

  baselineModal.addEventListener('click', (e) => {
    if (e.target === baselineModal) {
      baselineModal.style.display = 'none';
    }
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
