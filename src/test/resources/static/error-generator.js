function generateError() {
    var obj = undefined;

    return obj.a + obj.b;
}

setInterval(generateError, 10000);

generateError();