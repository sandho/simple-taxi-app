package com.san.simpletaxiapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.san.simpletaxiapp.R
import com.san.simpletaxiapp.utils.CARD
import com.san.simpletaxiapp.utils.CASH
import com.san.simpletaxiapp.utils.CASH_CAPS
import kotlinx.android.synthetic.main.fragment_booking_details.view.*

class BookingDetailsFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_booking_details, container, false)

        val bundle = this.arguments
        var paymentMethod = bundle?.getString("payment_method")

        view.name.text = "Customer Name - "+bundle?.getString("customer_name")
        view.mob_no.text = "Customer Mobile - "+bundle?.getString("customer_mobile")
        view.destination.text = "Destination - "+bundle?.getString("trip_destination")
        view.booked_on.text = "Booking Date Time - "+bundle?.getString("booking_date_time")
        view.journy_date.text = "Trip Date Time - "+bundle?.getString("trip_date_time")
        view.driver_name.text = "Driver Name - "+bundle?.getString("driver_name")

        if(paymentMethod == CASH_CAPS) {
            view.payment_on.text = CASH
            view.payment_image.setImageResource(R.drawable.ic_cash)
        } else {
            view.payment_on.text = CARD
            view.payment_image.setImageResource(R.drawable.ic_card)
        }

        view.close.setOnClickListener {
            dismiss()
        }

        return view
    }


}