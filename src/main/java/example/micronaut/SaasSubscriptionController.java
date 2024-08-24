package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

@Controller("/subscriptions")
public class SaasSubscriptionController {

    @Get("/{id}")
    HttpResponse<SaasSubscription> findById(@PathVariable Long id) {
        if (id.equals(99L)) {
            var subscription = new SaasSubscription(99L, "Advanced", 2900);
            return HttpResponse.ok(subscription);
        }
        return HttpResponse.notFound();
    }

    /*
     *Simplified method bellow is equivalent to the above
     */

    @Get("/simplified/{id}")
    SaasSubscription findById2(@PathVariable Long id) {
        if (id.equals(99L)) {
            return new SaasSubscription(99L, "Advanced", 2900);
        }
        return null;
    }

}
