document.addEventListener('DOMContentLoaded', function () {
    const deptLabels = window.deptLabels || [];
    const deptData = window.deptData || [];

    if (deptLabels.length > 0 && deptData.length > 0) {
        const colors = [
            '#dc3545', '#fd7e14', '#ffc107', '#28a745', '#17a2b8',
            '#6f42c1', '#e83e8c', '#20c997', '#007bff', '#6c757d'
        ];
        const ctx1 = document.getElementById('employeesChart');
        if (ctx1) {
            new Chart(ctx1.getContext('2d'), {
                type: 'pie',
                data: {
                    labels: deptLabels,
                    datasets: [{
                        data: deptData,
                        backgroundColor: colors.slice(0, deptLabels.length)
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {font: {size: 10}}
                        }
                    }
                }
            });
        }
    } else {
        const el = document.getElementById('employeesChart');
        if (el) el.style.display = 'none';
    }

    const tmcLabels = window.tmcLabels || [];
    const tmcData = window.tmcData || [];

    if (tmcLabels.length > 0 && tmcData.length > 0) {
        const ctx2 = document.getElementById('tmcChart');
        if (ctx2) {
            new Chart(ctx2.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: tmcLabels,
                    datasets: [{
                        label: 'Количество ТМЦ',
                        data: tmcData,
                        backgroundColor: ['#28a745', '#007bff', '#ffc107']
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {display: false}
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {stepSize: 1}
                        }
                    }
                }
            });
        }
    } else {
        const el = document.getElementById('tmcChart');
        if (el) el.style.display = 'none';
    }

    const calcLabels = window.calcLabels || [];
    const calcData = window.calcData || [];

    if (calcLabels.length > 0 && calcData.length > 0) {
        const ctx3 = document.getElementById('calculationsChart');
        if (ctx3) {
            new Chart(ctx3.getContext('2d'), {
                type: 'line',
                data: {
                    labels: calcLabels,
                    datasets: [{
                        label: 'Количество расчётов',
                        data: calcData,
                        borderColor: '#0d6efd',
                        backgroundColor: 'rgba(13, 110, 253, 0.1)',
                        tension: 0.1,
                        fill: true
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {display: false}
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {stepSize: 1}
                        }
                    }
                }
            });
        }
    } else {
        const el = document.getElementById('calculationsChart');
        if (el) el.style.display = 'none';
    }
});