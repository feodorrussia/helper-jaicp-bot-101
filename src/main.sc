pattern: hello = (здравствуй|привет|добр(рый|рая)|хай|hello|hi|дратуй|здарова)
pattern: weather = (погод|температур|прогноз)
pattern: currency = (курс|стоимост|цен|котировк|сколько стоит|обмен)

theme: /main

state: Start
    q!: $hello
    a: Привет! Я бот-помощник. Спроси меня о погоде или курсе валют.
    a: Ты можешь написать "Погода в Москве" или "Курс доллара"
    go!: /weather

state: Weather
    q!: * $weather *
    a: Напиши название города. Например: Москва, Лондон, Токио
    go!: /GetWeather

state: GetWeather
    q!: * $anyText *
    a: Сейчас в {{$parseTree.text}} солнечно и +20°C. Отличная погода!
    go!: /weather

state: Currency
    q!: * $currency *
    a: Курс доллара: 92 рубля, евро: 99 рублей. Информация актуальна на сегодня.
    go!: /currency

state: NoMatch
    event: noMatch
    a: Извини, я тебя не понял. Спроси меня о погоде или курсе валют.
    go!: /weather

state: Match
    event: match
    a: Я не знаю, как на это ответить. Попробуй написать "Погода" или "Курс валют".
    go!: /weather