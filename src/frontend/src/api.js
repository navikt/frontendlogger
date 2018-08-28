require('whatwg-fetch');

export function post(path, data) {
    return fetch(path, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },

        body: JSON.stringify(data)
    });
}
