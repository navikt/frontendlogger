# FRONTENDLOGGER

Felles applikasjon som håndterer logging fra frontend og eksponerer ett script som 
alle applikasjoner kan bruke.

### Eksempel på bruk:

De to følgende script-tagene må legges til i index.html
Førstnevnte gir en fallback slik at applikasjonen din ikke feiler om frontendloggeren skulle være nede, og spesifiserer `appname` som skal brukes.
Funksjonene definert i `window.frontendlogger` blir overskrevet scriptet blir lastet inn.

```html
<script type="application/javascript">
    window.frontendlogger = { info: function(){}, warn: function(){}, error: function(){}, event: function(){}};
    window.frontendlogger.appname = 'testapp';
</script>
<script type="application/javascript" src="/frontendlogger/logger.js"></script>
```

Errors som propageres til `window` vil automatisk bli fanget opp og logger som `error`.
Hvis man ønsker å manuelt sende logger fra applikasjonen kan det gjøres ved å kalle `window.frontendlogger.info` fra applikasjonen.

```javascript
window.frontendlogger.info('Min melding');
window.frontendlogger.info({
    message: 'Min melding',
    extra_felt_til_kibana: 'Litt ekstra informasjon her'
});
```

Hvis man ønsker å logge events/metrikker til Grafana fra applikasjonen kan det gjøres ved å kalle `window.frontendlogger.event(name, fields, tags)` fra applikasjonen.

```javascript
window.frontendlogger.event(
    'Tiltakinfo-sidevisning', 
    {'underOppfolging':true, 'Feltnavn2':3}, 
    {'Tagnavn1':'TagVerdi1','Tagnavn2':'TagVerdi2'}
);
```


### Kontakt og spørsmål

Opprett en issue i GitHub for eventuelle spørsmål.
