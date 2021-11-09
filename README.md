# Kotlin Payment Gateway using Midtrans, and Razorpay
Make kotlin payment gateway using Midtrans and Razorpay

# Feature
- Payment using Midtrans
- Payment using Razorpay

## Preview App
![iphone-11-pro-mockup-scene](https://user-images.githubusercontent.com/75843138/140934713-63896204-9b5d-466d-ac42-bef29c235b36.png)

<img src="https://user-images.githubusercontent.com/75843138/140933446-d8cfeaff-e977-404b-ba89-050c2e53bd0e.jpg" alt="alt text" width="300" height="600"> <img src="https://user-images.githubusercontent.com/75843138/140933469-72b4ae8c-e134-460c-b6ae-bb71501bbbe5.jpg" alt="alt text" width="300" height="600"> <img src="https://user-images.githubusercontent.com/75843138/140933482-0589b81d-4361-4678-857f-1615e134602d.jpg" alt="alt text" width="300" height="600">

## Please read this
Please change this if u want to use payment gateway, and please take a look in build.gradle, if u use wrong version, your app will error

    object Constant {

        // USE YOUR OWN KEY
        // if u use this one, your app will error for sure

        const val MERCHAT_ID_MIDTRANS = ""
        const val CLIENT_KEY_MIDTRANS = "SB-Mid-client-"
        const val SERVER_KEY_MIDTRANS = "SB-Mid-server-"
        const val BASE_URL_MIDTRANS = ""

        const val KEY_ID_RAZORAPP = "rzp_test_";
        const val KEY_SECRET_RAZORAPP = "";
    }
  
