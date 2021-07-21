package com.san.simpletaxiapp.ui

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.san.simpletaxiapp.R
import com.san.simpletaxiapp.databinding.ActivityBookNowBinding
import com.san.simpletaxiapp.utils.showToast
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import kotlinx.android.synthetic.main.activity_book_now.*
import kotlinx.android.synthetic.main.dialog_picker.*
import java.text.SimpleDateFormat
import java.util.*


class BookNowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookNowBinding

    lateinit var name: String
    lateinit var mobile: String
    lateinit var destination: String
    var paymentType: String = ""
    var tripDate: String = ""
    var tripTime: String = ""

    companion object {
        private const val TAG = "BookNowActivity-01"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookNowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val driver = intent.extras!!.getString("DRIVER")
        var distance = intent.extras!!.getString("DISTANCE")

        var formattedDistanceValue = String.format("%.2f", distance?.toDouble())

        binding.distanceTxt.text = "$formattedDistanceValue KM"
        binding.driverName.text = driver

        /*binding.chooseTripTime.setOnClickListener {
            var calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)
            var amPm = "PM"
            var hourr = 0

            var timePickerDialog = TimePickerDialog(this, { timePicker, getHour, getMinute ->
                if(getHour <= 12) {
                    amPm = "AM"
                }

                hourr = getHour

                if(hourr > 12) {
                    hourr -= 12
                }

                tripTime = "$hourr:$getMinute $amPm"
                binding.time.text = tripTime

            }, hour, minute, false)

            timePickerDialog.show()
        }*/

        binding.chooseTripTime.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_picker)
                .setPositiveButton(android.R.string.yes, null)
                .show()
            val picker = dialog.picker
            picker.setTypeface(Typeface.DEFAULT_BOLD)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                var amPm = "PM"

                if(picker.hours <= 12) {
                    amPm = "AM"
                }

                var hourr = picker.hours

                if(hourr > 12) {
                    hourr -= 12
                }

                tripTime = "${hourr}:${picker.minutes} $amPm"
                binding.time.text = tripTime

                dialog.dismiss()
            }
        }

        binding.choosePaymentMethod.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> {
                oldIndex, oldItem, newIndex, newItem ->
            if (newItem != null) {
                paymentType = newItem
            }
        })

        binding.chooseTripDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            var getMonth = 1

            val date = DatePickerDialog(this, { view, year, month, day ->

                getMonth += month

                tripDate = "$day/$getMonth/$year"
                binding.tripdate.text = tripDate

            }, year, month, day)

            date?.datePicker?.minDate = System.currentTimeMillis();
            date.show()
        }

        binding.chooseCreditCard.setOnClickListener {
            it.background = ContextCompat.getDrawable(this, R.drawable.payment_selected)
            binding.chooseCash.setBackgroundResource(0)
            paymentType = "CARD"
        }

        binding.chooseCash.setOnClickListener {
            it.background = ContextCompat.getDrawable(this, R.drawable.payment_selected)
            binding.chooseCreditCard.setBackgroundResource(0)
            paymentType = "CASH"
        }

        binding.submitButton.setOnClickListener {

            name = binding.nameEdt.text.toString()
            mobile = binding.mobileEdt.text.toString()
            destination = binding.destinationEdt.text.toString()

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")
            val currentDateTime = sdf.format(Date())

            when {
                name.isNullOrBlank() -> {
                    showToast(this, "Enter your name")
                }
                mobile.isNullOrBlank() -> {
                    showToast(this, "Enter your mobile")
                }
                mobile.count() < 10 -> {
                    showToast(this, "Enter valid mobile")
                }
                destination.isNullOrBlank() -> {
                    showToast(this, "Enter your destination")
                }
                tripDate.isNullOrBlank() -> {
                    showToast(this, "Choose Trip Date")
                }
                tripTime.isNullOrBlank() -> {
                    showToast(this, "Choose Trip Time")
                }
                paymentType.isNullOrBlank() -> {
                    showToast(this, "Choose payment method")
                }
                else -> {
                    val bottomSheetFragment = BookingDetailsFragment()
                    var bundle = Bundle()

                    bundle.putString("customer_name", name)
                    bundle.putString("customer_mobile", mobile)
                    bundle.putString("trip_destination", destination)
                    bundle.putString("booking_date_time", currentDateTime)
                    bundle.putString("trip_date_time", "$tripDate $tripTime")
                    bundle.putString("payment_method", paymentType)
                    bundle.putString("driver_name", driver)

                    bottomSheetFragment.arguments = bundle
                    bottomSheetFragment.show(this.supportFragmentManager, bottomSheetFragment.tag )
                }
            }

        }

    }
}