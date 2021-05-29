package com.kararnab.contacts.v2.ui

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kararnab.contacts.R
import com.kararnab.contacts.UiUtils
import com.kararnab.contacts.UiUtils.callPhoneNumber
import com.kararnab.contacts.UiUtils.checkCallPermission
import com.kararnab.contacts.UiUtils.requestCallPermission
import com.kararnab.contacts.callbacks.DebouncedOnClickListener
import com.kararnab.contacts.callbacks.PermissionsCallback
import com.kararnab.contacts.databinding.FragmentContactViewBinding
import dagger.hilt.android.AndroidEntryPoint


// Bottom sheet Tutorial: https://betterprogramming.pub/bottom-sheet-android-340703e114d2
@AndroidEntryPoint
class ContactViewFragment: BottomSheetDialogFragment() {

    //BottomSheet
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    val args: ContactViewFragmentArgs by navArgs()
    private var _binding: FragmentContactViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, R.layout.fragment_contact_view, null)
        _binding = DataBindingUtil.bind(view)
        bottomSheet.setContentView(view)
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO

        binding.extraSpace.minimumHeight = (Resources.getSystem().displayMetrics.heightPixels) / 2;

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(binding.appBarLayout, getActionBarSize())
                    hideAppBar(binding.profileLayout);

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    // hideAppBar(binding.appBarLayout);
                    showView(binding.profileLayout, getActionBarSize())
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

        binding.cancelBtn.setOnClickListener{
            dismiss()
        }

        binding.editBtn.setOnClickListener{
            val action = ContactViewFragmentDirections.actionContactViewFragmentToContactAddEditFragment(args.contact, true)
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.actionCall.setOnClickListener(object : DebouncedOnClickListener(500) {
            override fun onDebouncedClick(v: View?) {
                checkCallPermission(requireContext(), object : PermissionsCallback {
                    override fun onGranted() {
                        val phoneNo = binding.phoneNumber.text.toString()
                        callPhoneNumber(requireContext(), phoneNo, true)
                    }

                    override fun onRejected() {
                        requestCallPermission(requireActivity())
                    }
                })
            }
        })

        binding.actionMsg.setOnClickListener {
            val phoneNo = binding.phoneNumber.text.toString()
            if(TextUtils.isEmpty(phoneNo)) {
                Toast.makeText(context, "No Phone number", Toast.LENGTH_LONG).show()
            } else {
                val smsIntent = Intent(Intent.ACTION_SENDTO)
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT)
                smsIntent.type = "vnd.android-dir/mms-sms"
                smsIntent.data = Uri.parse("sms:${phoneNo}")
                startActivity(smsIntent)
            }
        }

        binding.actionMail.setOnClickListener{
            val emailId = binding.emailId.text.toString()
            if(TextUtils.isEmpty(emailId)) {
                Toast.makeText(context, "No Email found", Toast.LENGTH_LONG).show()
            } else {
                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(emailId))
                //email.putExtra(Intent.EXTRA_SUBJECT, subject)
                //email.putExtra(Intent.EXTRA_TEXT, id.message)
                email.type = "message/rfc822"

                startActivity(Intent.createChooser(email, "Choose an Email client :"))
            }
        }

        binding.lifecycleOwner = this
        binding.contact = args.contact;

        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun hideAppBar(view: View) {
        //UiUtils.collapse(view)
        val params = view.layoutParams
        params.height = 0
        view.layoutParams = params
    }

    private fun showView(view: View, size: Int) {
        //UiUtils.expand(view)
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val array =
            requireContext().theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        return array.getDimension(0, 0f).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}