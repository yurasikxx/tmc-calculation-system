document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('employeeSearch');
    const suggestionsContainer = document.getElementById('employeeSuggestions');
    const hiddenInput = document.getElementById('employeeId');
    const displayInput = document.getElementById('selectedEmployeeDisplay');

    const employeeData = window.employeeData || [];

    if (employeeData.length === 0) {
        return;
    }

    const currentEmployeeId = hiddenInput.value;
    if (currentEmployeeId) {
        const current = employeeData.find(e => e.id === currentEmployeeId);
        if (current) {
            displayInput.value = current.fullName + ' (' + current.profession + ')';
            searchInput.value = current.fullName;
        }
    }

    function renderSuggestions(suggestions) {
        suggestionsContainer.innerHTML = '';
        if (suggestions.length === 0) {
            suggestionsContainer.style.display = 'none';
            return;
        }

        suggestions.forEach(emp => {
            const item = document.createElement('button');
            item.type = 'button';
            item.className = 'list-group-item list-group-item-action';
            item.textContent = emp.fullName + ' (' + (emp.profession ? emp.profession.name : 'Без профессии') + ')';
            item.addEventListener('click', function () {
                selectEmployee(emp.id, emp.fullName, emp.profession);
            });
            suggestionsContainer.appendChild(item);
        });

        suggestionsContainer.style.display = 'block';
    }

    function getSuggestions(query) {
        const trimmed = query.toLowerCase().trim();
        if (trimmed === '') {
            return employeeData;
        }
        return employeeData.filter(e =>
            e.fullName.toLowerCase().includes(trimmed)
        );
    }

    function selectEmployee(id, fullName, profession) {
        hiddenInput.value = id;
        displayInput.value = fullName + ' (' + profession + ')';
        searchInput.value = fullName;
        suggestionsContainer.style.display = 'none';
    }

    searchInput.addEventListener('focus', function () {
        const suggestions = getSuggestions(this.value);
        renderSuggestions(suggestions);
    });

    searchInput.addEventListener('input', function () {
        const suggestions = getSuggestions(this.value);
        renderSuggestions(suggestions);
    });

    searchInput.addEventListener('blur', function () {
        setTimeout(() => {
            suggestionsContainer.style.display = 'none';
        }, 200);
    });

    searchInput.addEventListener('change', function () {
        const query = this.value.trim();
        if (query === '') {
            hiddenInput.value = '';
            displayInput.value = '';
            return;
        }
        const found = employeeData.find(e => e.fullName === query);
        if (!found) {
            hiddenInput.value = '';
            displayInput.value = '';
        }
    });

    document.addEventListener('click', function (e) {
        if (!e.target.closest('#employeeSearch') && !e.target.closest('#employeeSuggestions')) {
            suggestionsContainer.style.display = 'none';
        }
    });
});