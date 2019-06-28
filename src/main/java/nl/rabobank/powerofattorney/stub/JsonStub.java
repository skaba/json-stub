package nl.rabobank.powerofattorney.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class JsonStub
{

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "Content-Type";

    private static WireMockConfiguration configure()
    {
        return WireMockConfiguration.wireMockConfig()
                .fileSource(new ClasspathFileSource("testdata"))
                .extensions(new CustomResponseTemplateTransformer());

    }

    private static void setUp()
    {

        stubFor(get(urlMatching("/swagger/.*\\.(js|html|png|css|yaml)"))
                .willReturn(aResponse()
                        .withBodyFile("static/{{request.path}}")));

        // Get power of attorneys
        stubFor(get(urlMatching("/power-of-attorneys"))
                .willReturn(
                        aResponse()
                                .withBodyFile("poa/poa.json")
                                .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)));

        // Get power of attorney details
        stubFor(get(urlMatching("/power-of-attorneys/\\d+"))
                .willReturn(
                        aResponse()
                                .withBodyFile("poa/{{request.path.[1]}}.json")
                                .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)));

        // Get debit card details
        stubFor(get(urlMatching("/debit-cards/\\d+"))
                .willReturn(
                        aResponse()
                                .withBodyFile("debit-card/{{request.path.[1]}}.json")
                                .withFixedDelay(2000)
                                .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)));

        // Get debit card details
        stubFor(get(urlMatching("/credit-cards/\\d+"))
                .willReturn(aResponse()
                        .withBodyFile("credit-card/{{request.path.[1]}}.json")
                        .withFixedDelay(2000)
                        .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)));
    }

    public static void main(final String[] args)
    {
        new WireMockServer(configure()).start();
        setUp();
    }

}
