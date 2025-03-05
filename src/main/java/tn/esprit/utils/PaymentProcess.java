package tn.esprit.utils;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.Map;

public class PaymentProcess {

    private static final String STRIPE_API_KEY = "sk_test_51QxtaB01XbQ3yAamSCRzLrqKN1Nxt3f6dnZhwyCj0QZOPD6qjuQ0ZTsEYoGv0YM2FzjVfC9JvSaNdHliFuaPPnle0074lLqKNN";//llowing your application to interact with the Stripe API.
    private static final String SUCCESS_URL = "http://localhost:8080/success"; // Replace with your success URL
    private static final String CANCEL_URL = "http://localhost:8080/cancel"; // Replace with your cancel URL

    public static String createCheckoutSession(float amount) throws StripeException {//to generate a Stripe-hosted payment page.(page info)
        Stripe.apiKey = STRIPE_API_KEY;

        // Convert amount to cents
        long amountInCents = (long) (amount * 100);

        // Create a Stripe Checkout session
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("University Application Fee")
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(SUCCESS_URL)//either
                .setCancelUrl(CANCEL_URL)
                .build();

        Session session = Session.create(params);
        return session.getUrl(); // Redirect users to this URL
    }
}