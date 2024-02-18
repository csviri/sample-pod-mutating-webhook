package io.csivir;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Path("/")
public class PodMutatingEndpoint {

    private final AsyncAdmissionController<Pod> asyncMutatingController;

    @Inject
    public PodMutatingEndpoint(AsyncAdmissionController<Pod> asyncMutatingController) {
        this.asyncMutatingController = asyncMutatingController;
    }

    @POST
    @Path("mutate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AdmissionReview> mustate(AdmissionReview admissionReview) {
        return Uni.createFrom()
                .completionStage(() -> this.asyncMutatingController.handle(admissionReview));
    }
}
