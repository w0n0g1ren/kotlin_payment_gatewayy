package com.example.kotlin_payment_gateway

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlin_payment_gateway.Constant.BASE_URL_MIDTRANS
import com.example.kotlin_payment_gateway.Constant.CLIENT_KEY_MIDTRANS
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultListener {
    private var nameProduct: String = "PaymentProduct"
    private var totalProduct: Int = 123456;
    private var quantityProduct: Int = 1;
    private var transactionResult = TransactionResult()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Razorpay
        Checkout.preload(applicationContext)

        val buttonMidtrans = findViewById<Button>(R.id.buttonMidtrans)
        val buttonRazorpay = findViewById<Button>(R.id.buttonRazorpay)

        //                          Midtrans Payment

        // USE THIS WHILE U WANT USE MIDTRANS
        SdkUIFlowBuilder.init().setClientKey(CLIENT_KEY_MIDTRANS).setContext(applicationContext)
            .setTransactionFinishedCallback(
                TransactionFinishedCallback {
                    if (TransactionResult.STATUS_SUCCESS == "success") {
                        Toast.makeText(this, "Success transaction", Toast.LENGTH_LONG).show()
                    } else if (TransactionResult.STATUS_PENDING == "pending") {
                        Toast.makeText(this, "Pending transaction", Toast.LENGTH_LONG).show()
                    } else if (TransactionResult.STATUS_FAILED == "failed") {
                        Toast.makeText(
                            this,
                            "Failed ${transactionResult.response.statusMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (transactionResult.status.equals(
                            TransactionResult.STATUS_INVALID,
                            true
                        )
                    ) {
                        Toast.makeText(this, "Invalid transaction", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failure transaction", Toast.LENGTH_LONG).show()
                    }
                }).setMerchantBaseUrl(BASE_URL_MIDTRANS).enableLog(true).setColorTheme(
                CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")
            ).setLanguage("id").buildSDK()

        // THIS IS MIDTRANS PAYMENT
        // Watch out, if u have different data type, it will return error
        buttonMidtrans.setOnClickListener {
            Toast.makeText(this, "Open transaction", Toast.LENGTH_LONG).show()

            val transactionRequest = TransactionRequest(
                "Payment-Midtrans" + System.currentTimeMillis().toString(),
                totalProduct.toDouble()
            )
            val detail = com.midtrans.sdk.corekit.models.ItemDetails(
                "NamaItemId",
                totalProduct.toDouble(),
                quantityProduct,
                "Kursus Kotlin (Nama)"
            )
            val itemDetails = ArrayList<com.midtrans.sdk.corekit.models.ItemDetails>()

            itemDetails.add(detail)
            uiKitDetails(transactionRequest)
            transactionRequest.itemDetails = itemDetails
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)

            TransactionResult.STATUS_SUCCESS
        }


        //                          Razorpay Payment
        buttonRazorpay.setOnClickListener {
            startPayment()
        }
    }

    // MIDTRANS DETAIL
    // Change this as u wish
    fun uiKitDetails(transactionRequest: TransactionRequest) {

        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = "Supriyanto"
        customerDetails.phone = "082345678999"
        customerDetails.firstName = "Supri"
        customerDetails.lastName = "Yanto"
        customerDetails.email = "supriyanto6543@gmail.com"
        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Baturan, Gantiwarno"
        shippingAddress.city = "Klaten"
        shippingAddress.postalCode = "51193"
        customerDetails.shippingAddress = shippingAddress
        val billingAddress = BillingAddress()
        billingAddress.address = "Baturan, Gantiwarno"
        billingAddress.city = "Klaten"
        billingAddress.postalCode = "51193"

        customerDetails.billingAddress = billingAddress
        transactionRequest.customerDetails = customerDetails
    }

    // RAZORPAY PAYMENT
    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Izzat")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");

            //order_id will come from backend
            //options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount", "50000")//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email", "himanshudhimanhr@gmail.com")
            prefill.put("contact", "7009029910")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error in payment: " + p1.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        Checkout.clearUserData(this)
    }
}