export function getApiUrl() {
    const apiUrlFromScriptSource = Array.from(document.querySelectorAll('script'))
        .map((script) => script.src)
        .find((src) => src.endsWith('/frontendlogger/logger.js'))
        ?.slice(0, -1 * 'logger.js'.length)
        ?.concat('api/');

    return apiUrlFromScriptSource ?? '/frontendlogger/api/';
}

export function post(path, data) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', path);
    xhr.setRequestHeader('Content-Type','application/json');
    xhr.send(JSON.stringify(data));
}
