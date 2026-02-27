document.addEventListener("DOMContentLoaded", () => {
  const body = document.body;

  const btnSidebarToggle = document.getElementById("btnSidebarToggle");
  const btnMobileNav = document.getElementById("btnMobileNav");
  const btnSidebarClose = document.getElementById("btnSidebarClose");
  const overlay = document.getElementById("inOverlay");

  const isMobile = () => window.matchMedia("(max-width: 992px)").matches;

  let savedScrollY = 0;

  const lockBodyScroll = () => {
    savedScrollY = window.scrollY || document.documentElement.scrollTop;
    body.classList.add("is-scroll-locked");
    body.style.top = `-${savedScrollY}px`;
  };

  const unlockBodyScroll = () => {
    body.classList.remove("is-scroll-locked");
    const top = body.style.top;
    body.style.top = "";
    const y = parseInt(top || "0", 10) * -1;
    window.scrollTo(0, y);
  };

  const closeMobileNav = () => {
    body.classList.remove("is-sidebar-open");
    unlockBodyScroll();
  };

  const toggleMobileNav = () => {
    const willOpen = !body.classList.contains("is-sidebar-open");
    body.classList.toggle("is-sidebar-open", willOpen);
    willOpen ? lockBodyScroll() : unlockBodyScroll();
  };

  // -------------------------------------------------
  // Tooltip
  // -------------------------------------------------
  const sidebarTooltipEls = Array.from(document.querySelectorAll('[data-side-tooltip="1"][data-bs-toggle="tooltip"]'));
  const sidebarTooltips = sidebarTooltipEls.map(
    (el) =>
      new bootstrap.Tooltip(el, {
        trigger: "hover focus",
        container: "body",
        popperConfig(defaultConfig) {
          return {
            ...defaultConfig,
            modifiers: [
              ...(defaultConfig.modifiers || []),
              { name: "offset", options: { offset: [0, 8] } },
              { name: "preventOverflow", options: { padding: 12 } },
              { name: "flip", options: { fallbackPlacements: ["top", "right", "bottom"] } },
            ],
          };
        },
      }),
  );

  const otherTooltips = Array.from(document.querySelectorAll('[data-bs-toggle="tooltip"]:not([data-side-tooltip="1"])')).map(
    (el) => new bootstrap.Tooltip(el, { trigger: "hover focus", container: "body" }),
  );

  const syncTooltips = () => {
    const collapsed = body.classList.contains("is-sidebar-collapsed");
    sidebarTooltips.forEach((t) => (collapsed ? t.enable() : t.disable()));
    otherTooltips.forEach((t) => t.enable()); // 테이블/기타는 항상 켜기
  };

  const toggleCollapsed = () => {
    body.classList.toggle("is-sidebar-collapsed");
    syncTooltips();
  };

  syncTooltips();

  // -------------------------------------------------
  // Nav Active (현재 URL 기준)
  // -------------------------------------------------
  const setActiveByUrl = () => {
    const path = location.pathname.split("/").pop() || "";
    const current = path.split("?")[0].split("#")[0];

    // 1) 기존 active 초기화
    document.querySelectorAll(".in-sidebar-nav .in-nav-link.active").forEach((el) => el.classList.remove("active"));
    document.querySelectorAll(".in-sidebar-nav .in-sub-link.active").forEach((el) => el.classList.remove("active"));
    document.querySelectorAll(".in-sidebar-nav .in-nav-group.is-active").forEach((el) => el.classList.remove("is-active"));
    document.querySelectorAll(".in-sidebar-nav .in-nav-group.is-open").forEach((g) => {
      g.classList.remove("is-open");
      const t = g.querySelector(".in-nav-toggle");
      if (t) t.setAttribute("aria-expanded", "false");
    });

    // 2) 1Depth(일반 메뉴) 매칭 → active
    if (current) {
      const topLink = document.querySelector(`.in-sidebar-nav .in-nav-link[href="${CSS.escape(current)}"]`);
      if (topLink) topLink.classList.add("active");
    }

    // 3) 2Depth(Setting 서브) 매칭 → sub active + group active + open
    if (current) {
      const subLink = document.querySelector(`.in-sidebar-nav .in-sub-link[href="${CSS.escape(current)}"]`);
      if (subLink) {
        subLink.classList.add("active");

        const group = subLink.closest(".in-nav-group");
        if (group) {
          group.classList.add("is-active");
          group.classList.add("is-open");

          const toggle = group.querySelector(".in-nav-toggle");
          if (toggle) toggle.setAttribute("aria-expanded", "true");
        }
      }
    }
  };

  setActiveByUrl();

  // -------------------------------------------------
  // Sidebar events
  // -------------------------------------------------
  if (btnSidebarToggle) {
    btnSidebarToggle.addEventListener("click", () => {
      if (isMobile()) {
        toggleMobileNav();
        return;
      }
      toggleCollapsed();
    });
  }

  if (btnMobileNav)
    btnMobileNav.addEventListener("click", () => {
      if (isMobile()) toggleMobileNav();
    });
  if (btnSidebarClose) btnSidebarClose.addEventListener("click", closeMobileNav);
  if (overlay) overlay.addEventListener("click", closeMobileNav);

  document.querySelectorAll(".in-sidebar-nav .in-nav-link").forEach((a) => {
    a.addEventListener("click", (e) => {
      if (a.classList.contains("in-nav-toggle")) return;
      if (isMobile()) closeMobileNav();
    });
  });

  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeMobileNav();
  });

  window.addEventListener("resize", () => {
    if (!isMobile()) closeMobileNav();
    syncTooltips();
  });

  // -------------------------------------------------
  // 파일 첨부
  // -------------------------------------------------
  document.querySelectorAll("[data-file-ui]").forEach((ui) => {
    const fileInput = ui.querySelector("[data-file-input]");
    const fileText = ui.querySelector("[data-file-text]");
    const fileBtn = ui.querySelector("[data-file-btn]");

    const openPicker = () => fileInput && fileInput.click();

    fileBtn && fileBtn.addEventListener("click", openPicker);
    fileText && fileText.addEventListener("click", openPicker);

    fileInput &&
      fileInput.addEventListener("change", () => {
        fileText.value = fileInput.files && fileInput.files.length ? fileInput.files[0].name : "";
      });
  });

  // -------------------------------------------------
  // Sidebar Submenu (Setting)
  // -------------------------------------------------
  const sidebar = document.getElementById("inSidebar");
  if (sidebar) {
    const groups = Array.from(sidebar.querySelectorAll(".in-nav-group"));

    const closeAllGroups = (exceptGroup = null) => {
      groups.forEach((g) => {
        if (exceptGroup && g === exceptGroup) return;
        g.classList.remove("is-open");
        const t = g.querySelector(".in-nav-toggle");
        if (t) t.setAttribute("aria-expanded", "false");
      });
    };

    const clearSubActive = () => {
      sidebar.querySelectorAll(".in-sub-link.active").forEach((l) => l.classList.remove("active"));
    };

    const clearGroupActive = () => {
      sidebar.querySelectorAll(".in-nav-group.is-active").forEach((g) => g.classList.remove("is-active"));
    };

    groups.forEach((group) => {
      const toggle = group.querySelector(".in-nav-toggle");
      const subnav = group.querySelector(".in-subnav");
      if (!toggle || !subnav) return;

      // 1) 상위(Setting) 클릭 → 하위 펼침/접힘
      toggle.addEventListener("click", (e) => {
        e.preventDefault();

        const willOpen = !group.classList.contains("is-open");
        closeAllGroups(willOpen ? group : null);

        group.classList.toggle("is-open", willOpen);
        toggle.setAttribute("aria-expanded", willOpen ? "true" : "false");

        // 접으면 상위 강조도 제거(원하면 유지 가능)
        if (!willOpen) group.classList.remove("is-active");

        e.stopPropagation();
      });

      // 2) 하위 링크 클릭 → active 처리 + 상위 그룹 active 느낌
      subnav.querySelectorAll(".in-sub-link").forEach((link) => {
        link.addEventListener("click", (e) => {
          clearSubActive();
          link.classList.add("active");

          clearGroupActive();
          group.classList.add("is-active");

          if (isMobile()) closeMobileNav();
        });
      });
    });

    // 3) 사이드바 접힘 상태(PC 축소)로 변경되면 하위메뉴는 닫기
    const closeGroupsIfCollapsed = () => {
      const collapsed = body.classList.contains("is-sidebar-collapsed");
      if (collapsed) {
        closeAllGroups();
      }
    };

    closeGroupsIfCollapsed();
    window.addEventListener("resize", closeGroupsIfCollapsed);
    if (btnSidebarToggle) btnSidebarToggle.addEventListener("click", closeGroupsIfCollapsed);
  }
});
