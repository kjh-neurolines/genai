document.addEventListener("DOMContentLoaded", () => {
  /* ---------- 사이드바 툴팁 ---------- */
  const icons = document.querySelectorAll(".app-sidebar .sidebar-icon[data-tooltip]");
  const tooltip = document.getElementById("sidebar-tooltip");
  let tooltipTimer = null;

  icons.forEach((icon) => {
    const message = icon.dataset.tooltip;

    icon.addEventListener("mouseenter", () => {
      showTooltip(icon, message);
    });

    icon.addEventListener("mouseleave", () => {
      hideTooltip();
    });

    icon.addEventListener("click", (e) => {
      // a 태그(페이지 이동)는 막지 않는다
      if (icon.tagName === "A") return;

      // 버튼(Help Desk 등)만 클릭 툴팁 + 기본동작 막기
      e.preventDefault();
      showTooltip(icon, message, true);
    });
  });

  function showTooltip(target, text, lock = false) {
    if (!tooltip) return;
    tooltip.textContent = text;

    const rect = target.getBoundingClientRect();
    tooltip.style.top = rect.top + rect.height / 2 - tooltip.offsetHeight / 2 + "px";

    tooltip.classList.add("show");

    if (!lock) {
      clearTimeout(tooltipTimer);
      tooltipTimer = setTimeout(hideTooltip, 1600);
    }
  }

  function hideTooltip() {
    if (!tooltip) return;
    tooltip.classList.remove("show");
  }

  /* ---------- 유저 메뉴 ---------- */
  const userToggle = document.querySelector(".user .user-toggle");
  const userMenu = document.getElementById("user-menu");

  if (userToggle && userMenu) {
    userToggle.addEventListener("click", (e) => {
      e.preventDefault();
      toggleUserMenu();
    });

    // 바깥 클릭 시 닫기
    document.addEventListener("click", (e) => {
      if (!userMenu.contains(e.target) && !userToggle.contains(e.target)) {
        userMenu.classList.remove("show");
      }
    });
  }

  function toggleUserMenu() {
    if (!userMenu || !userToggle) return;

    const isOpen = userMenu.classList.contains("show");
    if (isOpen) {
      userMenu.classList.remove("show");
      return;
    }

    const rect = userToggle.getBoundingClientRect();
    const offsetY = 16;
    const top = rect.top + rect.height / 2 - userMenu.offsetHeight / 2 - offsetY;
    const left = rect.right + 12;

    userMenu.style.top = top + "px";
    userMenu.style.left = left + "px";
    userMenu.classList.add("show");
  }

  /* ---------- 모바일 메뉴 ---------- */
  const mobileMenuBtn = document.querySelector(".app-header-mobile-menu");
  const mobileMenu = document.getElementById("mobile-menu");
  const mobileCloseBtn = document.querySelector(".mobile-menu-close");

  if (mobileMenuBtn && mobileMenu) {
    mobileMenuBtn.addEventListener("click", () => {
      mobileMenu.classList.add("show");
    });

    if (mobileCloseBtn) {
      mobileCloseBtn.addEventListener("click", () => {
        mobileMenu.classList.remove("show");
      });
    }

    // 바깥 클릭 시 닫기
    mobileMenu.addEventListener("click", (e) => {
      if (e.target === mobileMenu) mobileMenu.classList.remove("show");
    });
  }

  /* ---------- 프롬프트 textarea 자동 높이 ---------- */
  const textarea = document.querySelector(".prompt-textarea");
  if (textarea) {
    textarea.addEventListener("input", () => {
      textarea.style.height = "auto";
      textarea.style.height = textarea.scrollHeight + "px";
    });
  }

  /* ---------- 파일 첨부 ---------- */
  /* ---------- 파일 첨부 ---------- */
  const fileInput = document.getElementById("fileUpload");
  const fileName = document.getElementById("fileName");

  if (fileInput && fileName) {
    fileInput.addEventListener("change", () => {
      const file = fileInput.files?.[0];
      fileName.textContent = file ? file.name : "선택된 파일 없음";
    });
  }

  /* ---------- FAQ 토글 (여러 개 지원) ---------- */
  const faqBoxes = document.querySelectorAll(".faq-box");
  if (faqBoxes.length > 0) {
    faqBoxes.forEach((faqBox) => {
      const toggle = faqBox.querySelector(".faq-toggle");
      const faqGrid = faqBox.querySelector(".faq-grid");
      if (!toggle || !faqGrid) return;

      toggle.addEventListener("click", () => {
        faqGrid.classList.toggle("is-hidden");
        faqBox.classList.toggle("on");
      });
    });
  }

  /* ---------- Help Desk 패널 ---------- */
  const helpdeskPanel = document.getElementById("helpdesk-panel");
  const panelClose = document.getElementById("panel-close");
  const helpdeskTriggers = document.querySelectorAll(".js-helpdesk-trigger");

  if (helpdeskPanel && panelClose && helpdeskTriggers.length > 0) {
    // Help Desk 열기 (PC/모바일 공통)
    helpdeskTriggers.forEach((btn) => {
      btn.addEventListener("click", (e) => {
        e.preventDefault();
        helpdeskPanel.classList.add("open");

        // 모바일에서 Help Desk 클릭 시 모바일 메뉴 닫기
        const mobileMenu = document.getElementById("mobile-menu");
        if (mobileMenu) mobileMenu.classList.remove("show");
      });
    });

    // Help Desk 닫기 (PC/모바일 공통 — 닫기 버튼)
    panelClose.addEventListener("click", () => {
      helpdeskPanel.classList.remove("open");
    });

    // 모바일 전용: 패널 바깥(오버레이) 클릭 시 닫기
    document.addEventListener("click", (e) => {
      const isPanel = helpdeskPanel.contains(e.target);
      const isTrigger = [...helpdeskTriggers].some((btn) => btn.contains(e.target));

      // 패널이 열려 있고, 패널 내부도 아니고, 버튼도 아니면 닫음
      if (helpdeskPanel.classList.contains("open") && !isPanel && !isTrigger) {
        helpdeskPanel.classList.remove("open");
      }
    });
  }
  /* ---------- 모바일 FLOW (우측 슬라이드) ---------- */
  const mobileFlowBtn = document.querySelector(".app-header-mobile-flow");
  const mobileFlow = document.getElementById("mobile-flow");
  const mobileFlowCloseBtn = document.querySelector(".mobile-flow-close");

  if (mobileFlowBtn && mobileFlow) {
    mobileFlowBtn.addEventListener("click", () => {
      mobileFlow.classList.add("show");

      // (선택) Flow 열 때 모바일 메뉴가 열려있으면 닫기
      const mobileMenu = document.getElementById("mobile-menu");
      if (mobileMenu) mobileMenu.classList.remove("show");
    });

    if (mobileFlowCloseBtn) {
      mobileFlowCloseBtn.addEventListener("click", () => {
        mobileFlow.classList.remove("show");
      });
    }

    // 오버레이(바깥) 클릭 시 닫기
    mobileFlow.addEventListener("click", (e) => {
      if (e.target === mobileFlow) mobileFlow.classList.remove("show");
    });

    // ESC로 닫기
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && mobileFlow.classList.contains("show")) {
        mobileFlow.classList.remove("show");
      }
    });
  }
});
