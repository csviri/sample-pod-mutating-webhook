package io.csivir;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import jakarta.inject.Singleton;

import java.util.concurrent.CompletableFuture;

public class AdmissionControllerConfig {

    public static final String SAMPLE_KEY = "platform.app";
    public static final String SAMPLE_VALUE = "true";

    @Singleton
    public AsyncAdmissionController<Pod> defaultAnnotationController() {
        return new AsyncAdmissionController<>(
                (AsyncMutator<Pod>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
                    resource.getMetadata().getAnnotations().put(SAMPLE_KEY, SAMPLE_VALUE);
                    return resource;
                }));
    }

}
