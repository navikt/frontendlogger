# FO-FRONTENDLOGGER

Felles applikasjon som håndterer logging fra frontend og eksponerer ett script som 
alle applikasjoner kan bruke.

### Eksempel på bruk:

Det anbefales å bruke de to følgende script-taggene i index.html.
Førstnevnte gir en fallback slik at applikasjonen din ikke feiler om frontendloggeren skulle være nede.
Funksjonene definert i `window.frontendlogger` blir overskrevet scriptet blir lastet inn.

For å få riktig navn i kibana må `appname` sendes med som nedenfor.
```html
<script type="application/javascript">
    window.frontendlogger = { info: function(){}, warn: function(){}, error: function(){}};
</script>
<script type="application/javascript" src="/frontendlogger/api/logger.js?appname=testapp"></script>
```

### NB
Frontend-scriptet forventer at `fetch` finnes på det globale-scopet, hvis dette ikke finnes vil den loggen en feilmelding.