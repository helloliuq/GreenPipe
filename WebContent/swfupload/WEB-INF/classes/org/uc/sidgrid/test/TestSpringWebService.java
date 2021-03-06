package org.uc.sidgrid.test;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import org.uc.sidgrid.ws.Weather;

public class TestSpringWebService {

    public static void main(String[] args1) throws AxisFault {

        RPCServiceClient serviceClient = new RPCServiceClient();

        Options options = serviceClient.getOptions();

        EndpointReference targetEPR 
                = new EndpointReference(
                "http://sidgrid.ci.uchicago.edu:8888/axis2/services/WeatherService"); 
        
        options.setTo(targetEPR);

        // Get the weather (no setting, the Spring Framework has already initialized it for us)
        QName opGetWeather = new QName("http://service.spring.sample/xsd", "getWeather");

        Object[] opGetWeatherArgs = new Object[] { };
        Class[] returnTypes = new Class[] { Weather.class };
        
        
        Object[] response = serviceClient.invokeBlocking(opGetWeather,
                opGetWeatherArgs, returnTypes);
        
        Weather result = (Weather) response[0];
        
        // display results
        if (result == null) {
            System.out.println("Weather didn't initialize!");
        }else{
            System.out.println("Temperature               : " +
                               result.getTemperature());
            System.out.println("Forecast                  : " +
                               result.getForecast());
            System.out.println("Rain                      : " +
                               result.getRain());
            System.out.println("How much rain (in inches) : " +
                               result.getHowMuchRain());
        }
    }
}
