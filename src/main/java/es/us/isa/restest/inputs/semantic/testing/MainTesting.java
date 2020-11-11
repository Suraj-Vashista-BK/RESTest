package es.us.isa.restest.inputs.semantic.testing;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.specification.OpenAPISpecification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static es.us.isa.restest.configuration.TestConfigurationIO.loadConfiguration;
import static es.us.isa.restest.util.PropertyManager.readProperty;


public class MainTesting {

    // Parámetros a cambiar
    private static String propertiesPath = "/semantic/chickenCoop.properties";
    private static String operationPath = "/games";
    private static String semanticParameterName = "title";
    private static String baseUrl = "https://chicken-coop.p.rapidapi.com";
    private static Integer limit = Integer.MAX_VALUE;

    // Parámetros derivados
    private static OpenAPISpecification spec;
    private static String confPath;
    private static String OAISpecPath;
    private static Operation operation;
    private static String host;
    private static TestConfigurationObject conf;

    public static void main(String[] args) throws IOException, InterruptedException {
        setParameters(readProperty("evaluation.properties.dir") + propertiesPath);

        String csvPath = getCsvPath();
        List<String> semanticInputs = readCsv(csvPath);


        System.out.println("Number of inputs " + semanticInputs.size());

        Integer maxCut = (limit < 20) ? limit : 20;

        Collections.shuffle(semanticInputs);

        // Select 20 random values
        List<String> randomSubList = semanticInputs.subList(0, maxCut);

        // API Calls
        int i = 1;
        for(String semanticInput: randomSubList){
            try {

                System.out.println(semanticInput);

                String query = "?title="+ semanticInput;         // TODO: Modify
                String url = baseUrl + operationPath + query;



                // country                  US
                // currency                 USD
                // locale                   en-US
                // originplace              SFO-sky
                // destination place        JFK-sky
                // outboundpartialdate      2019-09-01
                //inboundpartialdate        2019-12-01


//                          /apiservices/referral/v1.0/{country}/{currency}/{locale}/{originplace}/{destinationplace}/{outboundpartialdate}/{inboundpartialdate}
//                          /apiservices/autosuggest/v1.0/{country}/{currency}/{locale}/
//                          /apiservices/browsequotes/v1.0/{country}/{currency}/{locale}/{origin}/{destination}/{outboundpartialdate}
//
//
//                          /apiservices/browseroutes/v1.0/{country}/{currency}/{locale}/{originplace}/{destinationplace}/{outboundpartialdate}
//                          /apiservices/browsedates/v1.0/{country}/{currency}/{locale}/{originplace}/{destinationplace}/{outboundpartialdate}
//                          /apiservices/browsedates/v1.0/{country}/{currency}/{locale}/{originplace}/{destinationplace}/{outboundpartialdate}/{inboundpartialdate}
//                          /apiservices/browsequotes/v1.0/{country}/{currency}/{locale}/{originplace}/{destinationplace}/{outboundpartialdate}/{inboundpartialdate}
//                          /apiservices/browseroutes/v1.0/{country}/{currency}/{locale}/{originplace}/{destinationplace}/{outboundpartialdate}/{inboundpartialdate}



                System.out.println(url);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
//                        .addHeader("x-rapidapi-host", host)
                        .addHeader("x-rapidapi-host", host)
                        .addHeader("x-rapidapi-key", "xxxx")  // TODO: Modify
                        .build();

                Response response = client.newCall(request).execute();

                System.out.println("Iteración número " + i + "/" + maxCut);

                System.out.println("RESPONSE CODE: " + response.code());
                System.out.println(response.body().string());
                System.out.println("--------------------------------------------------------------------------------------");


                i++;
            }catch (Exception e){
                System.out.println(e);
            }

            TimeUnit.SECONDS.sleep(3);

        }


    }

    private static void setParameters(String propertyPath){
        OAISpecPath = readProperty(propertyPath, "oaispecpath");
        confPath = readProperty(propertyPath, "confpath");
        spec = new OpenAPISpecification(OAISpecPath);

        conf = loadConfiguration(confPath, spec);

        operation = conf.getTestConfiguration().getOperations().stream().filter(x -> x.getTestPath().equals(operationPath)).findFirst().get();
        host = operation.getTestParameters().stream().filter(x-> x.getName().equals("X-RapidAPI-Host")).findFirst().get().getGenerator().getGenParameters().get(0).getValues().get(0);

    }

    private static String getCsvPath(){
        return operation.getTestParameters().stream()
                .filter(x-> x.getName().equals(semanticParameterName))
                .findFirst().get()
                .getGenerator()
                .getGenParameters().get(0).getValues().get(0);
    }

    public static List<String> readCsv(String csvFile) {

        List<String> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line = "";
            while((line = br.readLine()) != null) {
                res.add(line);
            }
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return res;
    }


}
