package kp.company.controller.client.side.base;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * The base class for client-side tests.
 */
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ClientSideTestsBase {
    /**
     * {@link RestTestClient} for making REST calls in tests.
     */
    @Autowired
    protected RestTestClient restClient;
    /**
     * Flag for output printing.
     */
    protected static final boolean VERBOSE = false;
}