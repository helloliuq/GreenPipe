package org.uc.sidgrid.ws;

public class WeatherService{
    Weather weather;
    
    public void setWeather(Weather w){
        weather = w;
    }

    public Weather getWeather(){
        return weather;
    }
}