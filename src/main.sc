require: js/functions.js

theme: /

    state: Start
        event: noMatch
        a: Hello! I am a bot assistant. I can provide weather and currency exchange rate info.
        a: What can I help you with?

    state: HelloHandler
        intent!: /HelloIntent
        a: Hello! Glad to see you.
        go: /Start

    state: WeatherHandler
        intent!: /WeatherIntent
        a: Please tell me the name of your city.
        state: AwaitCity
            event: noMatch
            script:
                var city = $request.query;
                $session.city = city;
                getWeather(city);
            if: $session.weatherData
                script:
                    var data = $session.weatherData;
                    var description = getWeatherDescription(data.code);
                a: Weather in {{$session.city}}: {{data.temp}}°C, {{description}}.
            else:
                a: Sorry, could not get weather for {{$session.city}}.
            go: /Start

    state: CurrencyHandler
        intent!: /CurrencyIntent
        script:
            getCurrency();
        if: $session.currencyData
            script:
                var rates = $session.currencyData;
            a: Current exchange rates:
            a: USD to RUB: {{rates.USD}}
            a: EUR to RUB: {{rates.EUR}}
        else:
            a: Sorry, cannot get exchange rates right now.
        go: /Start

    state: Fallback
        event: noMatch
        a: I did not understand. Please use the menu or ask about weather or currency.