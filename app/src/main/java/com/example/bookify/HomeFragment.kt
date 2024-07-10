package com.example.bookify

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookify.R.*

class HomeFragment : Fragment() {
    private lateinit var horizontalScrollView: LinearLayout
    private lateinit var horizontalScrollViewBooks: LinearLayout
    private lateinit var textViewGreeting: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_home, container, false)
        horizontalScrollView = view.findViewById(R.id.horizontalScrollViewContent)
        horizontalScrollViewBooks = view.findViewById(R.id.horizontalScrollViewBooksContent)
        textViewGreeting = view.findViewById(R.id.textViewGreeting)

        sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        textViewGreeting.text = "Hello $username !"

        populatePopularBooks()
        populateBooksByCategories()

        return view
    }

    private fun populatePopularBooks() {
        val books = ApiService.getPopularBooks()
        val inflater = LayoutInflater.from(context)

        for (book in books) {
            // Inflate book item layout
            val bookView = inflater.inflate(R.layout.book_item_layout, horizontalScrollView, false)

            // Find views in inflated layout
            val imageBookCover = bookView.findViewById<ImageView>(R.id.imageBookCover)
            val textBookTitle = bookView.findViewById<TextView>(R.id.textBookTitle)
            val textAuthorName = bookView.findViewById<TextView>(R.id.textAuthorName)
            val imageReadIcon = bookView.findViewById<ImageView>(R.id.imageReadIcon)
            val textRead = bookView.findViewById<TextView>(R.id.textRead)

            // Load image using Glide
            Glide.with(requireContext())
                .load(book.imageUrl)
                .into(imageBookCover)

            // Set text and icons
            textBookTitle.text = book.title
            textAuthorName.text = book.author
            imageReadIcon.setImageResource(R.drawable.book)
            textRead.text = "Read"

            // Set layout params with margins
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.book_item_width),
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            layoutParams.setMargins(20, 0, 20, 0)
            bookView.layoutParams = layoutParams

            // Add inflated view to horizontalScrollView
            horizontalScrollView.addView(bookView)
        }
    }

    private fun populateBooksByCategories() {
        val books = ApiService.getBooksByCategories()
        val inflater = LayoutInflater.from(context)

        for (book in books) {
            // Inflate book item layout
            val bookView = inflater.inflate(R.layout.book_item_category_layout, horizontalScrollViewBooks, false)

            // Find views in inflated layout
            val imageBookCover = bookView.findViewById<ImageView>(R.id.imageBookCover)
            val textBookTitle = bookView.findViewById<TextView>(R.id.textBookTitle)
            val textAuthorName = bookView.findViewById<TextView>(R.id.textAuthorName)

            // Load image using Glide
            Glide.with(requireContext())
                .load(book.imageUrl)
                .into(imageBookCover)

            // Set text and icons
            textBookTitle.text = book.title
            textAuthorName.text = book.author


            // Add inflated view to horizontalScrollView
            horizontalScrollViewBooks.addView(bookView)
        }
    }

}

