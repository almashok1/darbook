package kz.adamant.bookstore.ui.bookdetail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentBookDetailBinding
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.loadBookImage
import kz.adamant.bookstore.viewmodels.BookDetailViewModel
import kz.adamant.domain.utils.RENTED_BY_ANOTHER_USER
import kz.adamant.domain.utils.RENTED_BY_CURRENT_USER
import kz.adamant.domain.utils.RENTED_BY_NONE
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class BookDetailFragment: BindingFragment<FragmentBookDetailBinding>(FragmentBookDetailBinding::inflate) {

    private val args by navArgs<BookDetailFragmentArgs>()

    private val viewModel by viewModel<BookDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            bookImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_books))
            bookTitle.text = args.book.title
            bookAuthor.text = args.book.author
            bookImage.loadBookImage(args.book)
            bookDate.text = parseDateToReadableFormat(args.book.publishedDate)

            getGenre()
            observeBookGenre()

            bindProgressButton(rentBookBtn)
            rentBookBtn.attachTextChangeAnimator {
                fadeInMills = 150L
                fadeOutMills = 150L
            }
            when(args.book.rentedMark) {
                RENTED_BY_NONE -> {
                    observeReadingBookResponse()
                    setRentButtonClick()
                }
                RENTED_BY_ANOTHER_USER -> {
                    disableRentButton(getString(R.string.rented_by_someone))
                }
                RENTED_BY_CURRENT_USER -> {
                    disableRentButton(getString(R.string.you_already_rented))
                }
            }
        }
    }

    private fun parseDateToReadableFormat(date: Date?): String {
        if (date == null) return "No date"
        return SimpleDateFormat("dd, MMM yyyy", Locale.getDefault()).format(date)
    }

    private fun setRentButtonClick() {
        binding.rentBookBtn.setOnClickListener {
            binding.rentBookBtn.showProgress {
                buttonText = getString(R.string.loading)
                progressColor = Color.WHITE
            }
            viewModel.rentBook(args.book.id, "123")
        }
    }

    private fun disableRentButton(text: String) {
        binding.rentBookBtn.text = text
        binding.rentBookBtn.isEnabled = false
    }

    private fun getGenre() {
        args.book.genreId?.let { viewModel.getGenreById(it) }
    }

    private fun observeReadingBookResponse() {
        viewModel.readingBookResponse.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.rentBookBtn.hideProgress(getString(R.string.failed))
                binding.rentBookBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))
            } else {
                binding.rentBookBtn.hideProgress(getString(R.string.you_already_rented))
                binding.rentBookBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.success))
            }
        }
    }
    private fun observeBookGenre() {
        viewModel.genre.observe(viewLifecycleOwner) {
            it.data?.let { genre ->
                binding.bookGenre.text = genre.title
            }
        }
    }
}