# FO-FRONTENDLOGGER

Felles applikasjon som håndterer logging fra frontend og eksponerer ett script som 
alle applikasjoner kan bruke.

### Eksempel på bruk:

De to følgende script-tagene må legges til i index.html
Førstnevnte gir en fallback slik at applikasjonen din ikke feiler om frontendloggeren skulle være nede, og spesifiserer `appname` som skal brukes.
Funksjonene definert i `window.frontendlogger` blir overskrevet scriptet blir lastet inn.

```html
<script type="application/javascript">
    window.frontendlogger = { info: function(){}, warn: function(){}, error: function(){}};
    window.frontendlogger.appname = 'testapp';
</script>
<script type="application/javascript" src="/frontendlogger/logger.js"></script>
```

### NB
Frontend-scriptet forventer at `fetch` finnes på det globale-scopet, hvis dette ikke finnes vil den loggen en feilmelding.
