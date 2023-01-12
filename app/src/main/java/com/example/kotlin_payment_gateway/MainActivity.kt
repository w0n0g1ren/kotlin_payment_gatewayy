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

class MainActivity : AppCompatActivity() {
    private var nameProduct: String = "PaymentProduct"
    private var totalProduct: Int = 123456;
    private var quantityProduct: Int = 1;
    private var transactionResult = TransactionResult()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
}
