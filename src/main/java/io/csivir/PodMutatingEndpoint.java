package io.csivir;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class PodMutatingEndpoint {

    public static final String ADD_ANNOTATION_PATH = "mutate";
    private static final Logger log = LoggerFactory.getLogger(AdmissionControllerConfig.class);


    private final AsyncAdmissionController<Pod> defaultAnnotationController;

    @Inject
    public PodMutatingEndpoint(AsyncAdmissionController<Pod> defaultAnnotationController) {
        this.defaultAnnotationController = defaultAnnotationController;
    }

    @POST
    @Path(ADD_ANNOTATION_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AdmissionReview> mutate(AdmissionReview admissionReview) {
        log.info("Mutating request: {}", admissionReview.getRequest().getUid());
        return Uni.createFrom()
                .completionStage(() -> this.defaultAnnotationController.handle(admissionReview));
    }
}
