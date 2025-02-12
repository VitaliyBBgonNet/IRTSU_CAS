// 1. Проверка токена при загрузке страницы
if (!localStorage.getItem("token") && window.location.pathname !== "/login") {
    window.location.href = "/login";
}

// 2. Автоматически добавляем JWT в HTMX-запросы
document.addEventListener("htmx:configRequest", function(event) {
    let token = localStorage.getItem("token");
    if (token) {
        event.detail.headers['Authorization'] = 'Bearer ' + token;
    }
});

// 3. Если получаем 401, отправляем на страницу логина
document.addEventListener("htmx:responseError", function(event) {
    if (event.detail.xhr.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login";
    }
});
