function getWeather(city) {
    var response = $http.get("https://api.open-meteo.com/v1/forecast", {
        "latitude": 55.7558,
        "longitude": 37.6173,
        "current_weather": true
    });
    if (response && response.current_weather) {
        var data = response.current_weather;
        $session.weatherData = {
            "temp": data.temperature,
            "code": data.weathercode
        };
    } else {
        $session.weatherData = null;
    }
}

function getCurrency() {
    var response = $http.get("https://api.exchangerate-api.com/v4/latest/USD");
    if (response && response.rates) {
        var rates = response.rates;
        $session.currencyData = {
            "USD": rates.RUB,
            "EUR": rates.RUB / rates.EUR
        };
    } else {
        $session.currencyData = null;
    }
}

function getWeatherDescription(code) {
    var map = {
        0: "Clear sky",
        1: "Mainly clear",
        2: "Partly cloudy",
        3: "Overcast",
        45: "Foggy",
        51: "Light drizzle",
        61: "Slight rain",
        63: "Moderate rain",
        80: "Showers"
    };
    return map[code] || "Unknown";
}