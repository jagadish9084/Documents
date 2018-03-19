package com.scb.channels.research;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.HttpMethod;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1554435 on 2/18/2018.
 */
public class Test {
    private static OkHttpClient okHttpClient;
    private static ObjectMapper objectMapper = new ObjectMapper().
            configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false).
            setSerializationInclusion(JsonInclude.Include.NON_NULL).
            configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).
            registerModule(new JavaTimeModule());

    public static void main(String... args) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("10.65.128.43", 8080));

        okHttpClient = trustAllCerts(new OkHttpClient.Builder()).
                connectTimeout(1000, TimeUnit.SECONDS).
                readTimeout(1000, TimeUnit.SECONDS).
                writeTimeout(1000, TimeUnit.SECONDS).
                //proxy(proxy).
                build();

        String data = Files.toString(new ClassPathResource("test.xml").getFile(), Charsets.UTF_8);

        //Object str = doGet("https://10.23.210.60:9012/api-banking/upi/ReqBalEnq/1.0", String.class, data);
        // Object str = doGet("https://10.23.210.60:9012/api-banking/core/event/peek", String.class, data);

        Object object = doPost(data, "https://apitest.standardchartered.com/upi/ReqBalEnq/1.0", Object.class);

        //Object res = get();
        System.out.println();
    }

    private static Object get() {

        Request request = new Request.Builder()

                .url("https://cadm.global.standardchartered.com/cadmapi/service/user/GRP38573/SCBGRPGP")
                //.url("https://cadm.global.standardchartered.com/cadmapi/service/group/accounts/INGRP02")
                /*.header("Content-Type", "application/json")
                .header("GroupId", "B0001")
                .url("http://localhost:8095/core/event/peek")*/
                .build();
        return execute(request, Object.class);
    }

    private static <T> T doGet(String url, Class<T> type, String data) {
        Request request = new Request.Builder()
                .url(url)
                .header("GroupId", "INDGRP")
                /*.method(HttpMethod.POST,
                        RequestBody.create(
                                MediaType.parse("application/xml"),
                                data))*/
                .build();
        return execute(request, type);
    }

    private <T, R> T doPut(R detail, String url, Class<T> responseType) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .method(HttpMethod.PUT,
                            RequestBody.create(
                                    MediaType.parse("application/xml"),
                                    objectMapper.writeValueAsString(detail)))
                    .build();
            return execute(request, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T, R> T doPost(String detail, String url, Class<T> responseType) {
        try {
            Request request = new Request.Builder()
                    .header("JWTToken", "eyJhbGciOiJSUzI1NiJ9eyJpc3MiOiJTQ0IiLCJhdWQiOiJTQ0ItQVBJQmFua2luZyIsImlhdCI6MTUxNzkwNzA5NiwiZXhwIjoxNTE3OTA3MTI2LCJwYXlsb2FkIjp7ImVuYWJsZVdlYkhvb2siOiJ0cnVlIiwid2ViSG9va1VybCI6Imh0dHA6Ly8xMC4yMy4yMTAuNjA6ODQ0My93ZWJob29rIiwiYWN0aXZhdGlvbktleSI6eyJjb250ZW50IjoiNzU1VFVVZVpTV001amZsMG5BUGFCbmRTVysvMWVLYTFRUFZsd2xLd0FiTDg1blhGejcvRHA2dmlaUlhJd1crRUhlanFwU211amJtOGZEVVorYWp0TE1ScXpIdmZhVGFCaEJyTjU2dlN5T1ByWmVqUjhvamRXL2U3UVJ0VkRDam05RUo4UVg3K2FENTNPMGpEcEduRmtxOXRrcmNmVkErUFc4cTdzdSs2TmlDLzBBR3J6d25hY3pSSHpKZHkyZXVLb0RJRkNlRG9udG1pc3R1SG5LS1hsaWlMc1A3SDd4anNlU3M3Nk5udjR5aS9JYWJhZkg0NmMzRkNybDdhbUhKSG9pQkhvR3M4TkVwL3kyalRlcG1pSjgxQjczN1U0M01idGJHUFBBSmFSaTFoNmk3TGhmZ29Lbm5OQ2VpSWYweTlLdEs2T2FhVFBoU1g1dzNwR0lBcXI5RzA5SklyWStYN2Zxb0gxRmVvVnE1dU5iOE1rWStWNzh1ZnVwMkluM2JlQ0luYWE2L2g1cERRc01lOGhLOGpnNDhSRUtwWGlsdlRyNWJFT3VXdWpOUVBvL092RXpIUk92UlpEaXpBdVY5L0tOVjNBS0pnMVhDTUlXUlNlbmM0Q0RyVHRWQ01GTW9Ra2I1dnUwQjVsbUZlSkdmOGFMOUpySUtOaFltNVlRUzVzWGFDUStabEdVUHJzVVpZQTlQeUEydDhpUXpuM3FOL0Rqc1d0NGt0dmRzSm9MVkxMd3gyOEVWTThLNnd3ODZiQkZ2bDhhZ2hmYXRLaW9uTW1MVlZHeEFhQW55L2FRRGZTYzJZVVJ6b0FLMnNuWGF3UCtuemVLZE1uYkY2TjJyME12T1JlWVBrdWFOL0JBQlFsckl6TkR5S3I2VW0xMmZZd2xlUnMvNSs2YTNmRklENzg5dG1RaVBvR3FWbXU3RUMwc2k3MEJpRnFhSklNVzNSTU92ekxBcGVKakNYbXNLb2pldVAybXJ2ODMydFpIRE04TUdIRlhhTGgzam1rMys2cmJidWlKM2hkZnpGck4zcFNxZkx4K2VSVElQOFo3TTU3aisxd0FXYm92L1RncXVuNXZENnJYbWx1QysvdUY3eCIsImtleSI6Im56ZExFVXpGYURJVWhiaUJScEF4bitjNGVoQTI2cU1nR1ZrVG83RG9OdXZPOStxbmhvSXU4Q3VrMDlpemhlYUR5WTZaOTNKSGJaYjkxTlVQaGM0bkluV1ZKMUloVG81Z0hnSUVaMm5laDY4SkIzeUM4OHV5SE5PNHk5dkQwZ3h3eldxZTdaRTdrSlh4Sng2VDZJbTBIVUp2VnpyZnROWTM5b1M4K2dDSzdZVDNodGJQNitFWUVraEp6Ui85NjdYRW4xckkvL0VBcnA4S21LS2JKVkRoaU9SN3VjTURwRUI5TEFiWlc4cURySUExT3h2NG54ZzZlUWlnMGRmbC9EV3N4MWNKYUF0eitab0M4L1VrM0REM2o3TnI3MTd5OHg4UEVqTWRHM2thTXBXZTNBYmVsV1RtYTl4bHpEWVB0aWlhWElsSUgyc3poZnFRTm5BOGdvUWZTZ1x1MDAzZFx1MDAzZCJ9fX0")
                    .header("GroupId", "INDSEGSE")
                    .url(url)
                    .method(HttpMethod.POST,
                            RequestBody.create(
                                    MediaType.parse("application/xml"),
                                    detail))
                    .build();
            return execute(request, responseType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T execute(Request request, Class<T> type) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            int statusCode = response.code();
            ResponseBody body = response.body();
            if (statusCode == 200) {
                if (body != null && body.contentLength() != 0) {
                    String bodyString = body.string();
                    return (T) objectMapper.readValue(bodyString, type);
                }
            }
            if (statusCode > 405) {
                throw new RuntimeException("Failed to get details from CADM, Response code: [{}]" +
                        response.code() + " Response body: " + (body != null ? body.string() : null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private static OkHttpClient.Builder trustAllCerts(OkHttpClient.Builder clientBuilder) throws NoSuchAlgorithmException, KeyManagementException {

        final TrustManager[] trustAllCertsManager = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, trustAllCertsManager, new java.security.SecureRandom());
        clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertsManager[0]);

        HostnameVerifier hostnameVerifier = (hostname, session) -> {
            //logger.info("Trusting host " + hostname + ".. should match " + baseUrl);
            return true;
        };

        clientBuilder.hostnameVerifier(hostnameVerifier);
        return clientBuilder;
    }
}
