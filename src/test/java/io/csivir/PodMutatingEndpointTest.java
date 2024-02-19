package io.csivir;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.webhook.admission.Operation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static io.csivir.AdmissionControllerConfig.SAMPLE_KEY;
import static io.csivir.AdmissionControllerConfig.SAMPLE_VALUE;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class PodMutatingEndpointTest {

    @Test
    void testHelloEndpoint() {
        var reviewObject = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(testAdmissionReview())
                .when()
                .post("/mutate")
                .then()
                .statusCode(200)
                .extract().as(AdmissionReview.class);

        assertThat(new String(Base64.getDecoder().decode(reviewObject.getResponse().getPatch())))
                .contains(SAMPLE_KEY).contains(SAMPLE_VALUE);
    }

    private AdmissionReview testAdmissionReview() {
        var res = new AdmissionReview();
        var request = new AdmissionRequest();
        request.setOperation(Operation.UPDATE.toString());
        res.setRequest(request);
        Pod pod = new Pod();
        pod.setMetadata(new ObjectMetaBuilder()
                .withName("test1")
                .build());
        request.setObject(pod);
        return res;
    }

}