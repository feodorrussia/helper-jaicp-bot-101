// src/js/functions.js

// Функция для получения данных о погоде
function getWeatherData(city) {
    // Используем встроенный сервис $http для GET-запроса
    var response = $http.get("https://api.open-meteo.com/v1/forecast", {
        "latitude": 55.7558,  // Можно заменить на реальные координаты города через отдельный API
        "longitude": 37.6173, // Например, через сервис геокодинга
        "current_weather": true,
        "hourly": "temperature_2m,weathercode"
    });
    
    if (response && response.current_weather) {
        var data = response.current_weather;
        $session.weatherData = {
            "temp": data.temperature,
            "description": getWeatherDescription(data.weathercode) // Функция для описания погоды по коду
        };
    } else {
        $session.weatherData = null;
    }
}

// Функция для получения курсов валют
function getExchangeRates() {
    var response = $http.get("https://api.exchangerate-api.com/v4/latest/USD");
    
    if (response && response.rates) {
        $session.ratesData = {
            "USD": response.rates.RUB,
            "EUR": response.rates.RUB / response.rates.EUR // Более точный расчет
        };
    } else {
        $session.ratesData = null;
    }
}

// Вспомогательная функция для описания погоды по коду
function getWeatherDescription(code) {
    var weatherCodes = {
        0: "Ясно",
        1: "Преимущественно ясно",
        2: "Переменная облачность",
        3: "Пасмурно",
        45: "Туман",
        51: "Легкая морось",
        61: "Небольшой дождь",
        63: "Дождь",
        80: "Ливень"
    };
    return weatherCodes[code] || "Неизвестно";
}