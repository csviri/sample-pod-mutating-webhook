package io.csivir;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import jakarta.inject.Singleton;

import java.util.concurrent.CompletableFuture;

public class AdmissionControllerConfig {

    @Singleton
    public AsyncAdmissionController<Pod> asyncMutatingController() {
        return new AsyncAdmissionController<>(
                (AsyncMutator<Pod>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
                    resource.getMetadata().getAnnotations().put("io.csviri.sample", "sample-value");
                    return resource;
                }));
    }

}
