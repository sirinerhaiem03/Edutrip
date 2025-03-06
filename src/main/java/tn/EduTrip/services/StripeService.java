package tn.EduTrip.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.List;

public class StripeService {

    private static final String STRIPE_SECRET_KEY = "sk_test_51QzRECGvb9VTCgwhLKtFvTwdHqleQifRyuiGsXQgWShP6i1ZUfvpjxySgmuKZ3b9l2EMhyvsVCQWCylyk0cbIM6Z00RaAyoAEh"; // Replace with your secret key

    public StripeService() {
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    public String createCheckoutSession(double amount, String currency, String successUrl, String cancelUrl) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount((long) (amount * 100)) // Convert to cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Réservation de Vol")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
        return session.getUrl(); // This URL will redirect the user to Stripe’s payment page
    }
}
