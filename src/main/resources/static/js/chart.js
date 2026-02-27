document.addEventListener("DOMContentLoaded", () => {
  const el = document.getElementById("msdsChart");
  if (!el) return;

  const labels = ["원료", "시약", "소모품", "제품", "반제품"];
  const dataIn = [4, 2, 6, 4, 4];
  const dataOver = [1, 1, 3, 1, 1];

  const isMobile = window.matchMedia("(max-width: 768px)").matches;

  new Chart(el, {
    type: "bar",
    data: {
      labels,
      datasets: [
        {
          label: "180일 이내",
          data: dataIn,
          backgroundColor: "#3463BF",
          borderColor: "#3EA1E8",
          borderWidth: 0,
          borderRadius: 0,
          barPercentage: isMobile ? 0.7 : 0.6,
          categoryPercentage: isMobile ? 0.7 : 0.6,
        },
        {
          label: "기간 초과",
          data: dataOver,
          backgroundColor: "#E96C55",
          borderColor: "#E96C55",
          borderWidth: 0,
          borderRadius: 0,
          barPercentage: isMobile ? 0.7 : 0.6,
          categoryPercentage: isMobile ? 0.7 : 0.6,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 300 },
      layout: { padding: { top: 10, right: 10, left: 6 } },
      scales: {
        x: {
          grid: { display: false },
          ticks: { font: { size: isMobile ? 11 : 12 } },
        },
        y: {
          beginAtZero: true,
          ticks: { stepSize: 1, font: { size: isMobile ? 11 : 12 } },
        },
      },
      plugins: {
        legend: {
          position: "top",
          align: "end",
          labels: {
            boxWidth: 12,
            boxHeight: 10,
            usePointStyle: true,
            pointStyle: "rectRounded",
            font: { size: isMobile ? 11 : 12 },
          },
        },
        tooltip: { intersect: false, mode: "index" },
      },
    },
  });
});
