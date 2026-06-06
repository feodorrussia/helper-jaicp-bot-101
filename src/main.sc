// src/main.sc

require: js/functions.js
require: dicts/stopWords.sc

patterns: 
    $weatherPattern = (погода|прогноз|weather)
    $currencyPattern = (курс|валюта|currency|доллар|евро|рубль)

init:
    bind("getWeatherData", getWeatherData);
    bind("getExchangeRates", getExchangeRates);
    
theme: /

    state: Start
        q!: $regex</start>
        q!: $helloPattern
        script:
            // Проверяем, есть ли имя пользователя в сессии
            var currentName = $session.userName;
            if (currentName) {
                $reactions.answer("Приветствую, " + currentName + "! Чем могу помочь?");
            } else {
                $reactions.answer("Приветствую! Это бот-помощник. Как я могу к вам обращаться?");
                // Переходим в состояние для сохранения имени
                $session.go("/SaveUserName");
            }
            // Если имя уже есть, просто показываем меню после ответа

    state: SaveUserName
        a: Пожалуйста, представьтесь.
        state: AwaitName
            event: noMatch
            script:
                var name = $request.query;
                // Простая очистка имени от лишних слов и знаков препинания
                name = name.trim().split(' ')[0];
                $session.userName = name;
                $reactions.answer("Отлично, " + name + "! Теперь я могу помочь вам с погодой или курсами валют.");
                $reactions.transition("/Menu");

    state: Hello
        q!: $helloPattern
        intent: /HelloIntent
        script:
            var currentName = $session.userName;
            if (currentName) {
                $reactions.answer("Здравствуйте, " + currentName + "!");
            } else {
                $reactions.answer("Здравствуйте!");
            }
        go!: /Menu

    state: Weather
        q!: $weatherPattern
        intent: /WeatherIntent
        go!: /WeatherFlow

    state: Currency
        q!: $currencyPattern
        intent: /CurrencyIntent
        go!: /CurrencyFlow

    state: NoMatch
        event: noMatch
        a: Извините, я не совсем понял ваш запрос. Я умею говорить о погоде и курсах валют. Давайте начнем заново.
        go!: /Menu

    state: Menu
        a: Выберите интересующий вас раздел:
        buttons:
            "🌤️ Узнать погоду" -> /WeatherFlow
            "💱 Узнать курс валют" -> /CurrencyFlow
            "❌ Выйти из меню"
        state: NoMatch
            event: noMatch
            a: Пожалуйста, используйте кнопки для навигации.

    state: WeatherFlow
        a: Пожалуйста, напишите название города.
        state: AwaitCity
            event: noMatch
            script:
                var city = $request.query;
                $session.weatherCity = city;
                // Вызываем функцию получения данных
                getWeatherData(city);
                if ($session.weatherData) {
                    var wData = $session.weatherData;
                    $reactions.answer("Погода в городе " + city + ":\n" + 
                                       "Температура: " + wData.temp + "°C\n" +
                                       "Описание: " + wData.description);
                } else {
                    $reactions.answer("Не удалось получить данные о погоде для города " + city + ". Попробуйте другой город.");
                }
                $reactions.transition("/Menu");
        state: NoMatch
            event: noMatch
            a: Пожалуйста, введите название города текстом.

    state: CurrencyFlow
        script:
            getExchangeRates();
            if ($session.ratesData) {
                var rates = $session.ratesData;
                $reactions.answer("Актуальные курсы валют:\n" +
                                   "USD/RUB: " + rates.USD + "\n" +
                                   "EUR/RUB: " + rates.EUR);
            } else {
                $reactions.answer("Не удалось получить актуальные курсы валют. Попробуйте позже.");
            }
        go!: /Menu