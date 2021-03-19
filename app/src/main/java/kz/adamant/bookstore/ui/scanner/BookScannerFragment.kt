package kz.adamant.bookstore.ui.scanner

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.material.snackbar.Snackbar
import kz.adamant.bookstore.NavGraphDirections
import kz.adamant.bookstore.databinding.FragmentBookScannerBinding
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.viewmodels.BookScannerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookScannerFragment: BindingFragment<FragmentBookScannerBinding>(FragmentBookScannerBinding::inflate) {

    companion object {
        private const val CAMERA_REQUEST_CODE = 101
    }

    private val viewModel by viewModel<BookScannerViewModel>()

    private lateinit var codeScanner: CodeScanner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermissions()
        setUpScannerView()
        observeScannedBook()
    }

    private fun setUpScannerView() {
        val scannerView = binding.scannerView

        codeScanner = CodeScanner(requireContext(), scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ONE_DIMENSIONAL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            viewModel.findBookByIsbn(it.text)
        }
        codeScanner.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    private fun observeScannedBook() {
        viewModel.scannedBook.observeSingle(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                showErrorSnackbar("Book with that isbn not found")
            }
            else {
                navController.navigate(NavGraphDirections.actionGlobalBookDetailFragment(it.first()))
            }
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeCameraRequest()
        }
    }

    private fun makeCameraRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(
                        binding.root,
                        "You should grant camera permission",
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}