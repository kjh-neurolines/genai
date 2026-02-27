(function () {
  if (window.__navInitialized) return;
  window.__navInitialized = true;

  document.addEventListener("DOMContentLoaded", function () {
    function getCurrentFile() {
      let file = window.location.pathname.split("/").pop() || "";
      file = file.split("?")[0].split("#")[0];
      if (!file || file === "index.html" || file === "dashboard") file = "dashboard.html";
      return file;
    }

    const currentFile = getCurrentFile();

    // 2) breadcrumb 데이터 (기존 구조 유지)
    const breadcrumbMap = {
      "dashboard.html": ["Dashboard"],
      "inventory-register.html": ["Inventory 등록"],
      "ai-auto-register.html": ["AI 자동 등록"],
      "ai-auto-register-verify.html": ["AI 자동 등록 검증"],
      "inventory-list.html": ["Inventory"],
      "setting-business-unit.html": ["Setting", "사용사업 본부관리"],
      "setting-business-site.html": ["Setting", "사용사업장 관리"],
      "setting-department.html": ["Setting", "부서관리"],
      "setting-material-code.html": ["Setting", "자재코드 관리"],
      "setting-vendor.html": ["Setting", "벤더 관리"],
      "material-department.html": ["Setting", "자재 ID 부서 매핑관리"],
      "setting-user.html": ["Setting", "사용자 관리"],
      "setting-permission.html": ["Setting", "권한 관리"],
    };

    const crumbs = breadcrumbMap[currentFile] || [];
    const breadcrumb = document.getElementById("breadcrumb");

    if (breadcrumb) {
      breadcrumb.innerHTML = "";

      const homeLi = document.createElement("li");
      homeLi.className = "in-breadcrumb-item";
      homeLi.innerHTML = `<i></i>`;
      breadcrumb.appendChild(homeLi);

      crumbs.forEach((name, idx) => {
        const li = document.createElement("li");
        li.className = "in-breadcrumb-item" + (idx === crumbs.length - 1 ? " is-active" : "");
        li.textContent = name;
        breadcrumb.appendChild(li);
      });
    }

    const links = document.querySelectorAll("a[href]");
    links.forEach((a) => {
      const href = (a.getAttribute("href") || "").trim();
      if (!href || href === "#" || href.startsWith("javascript:")) return;

      const hrefFile = href.split("/").pop().split("?")[0].split("#")[0];
      const normalizedHrefFile = !hrefFile || hrefFile === "index.html" || hrefFile === "dashboard" ? "dashboard.html" : hrefFile;

      if (normalizedHrefFile === currentFile) {
        a.classList.add("active", "is-active");

        const li = a.closest("li");
        if (li) li.classList.add("active", "is-active");
      }
    });
  });
})();
