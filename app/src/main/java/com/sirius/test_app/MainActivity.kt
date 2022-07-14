package com.sirius.test_app

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.sirius.test_app.databinding.ActivityMainBinding
import com.sirius.test_app.databinding.ReviewLayoutBinding

class MainActivity : AppCompatActivity() {
    private val dataModel = DataModel()
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var reviewBinding: ReviewLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        // Set transparent Status Bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        mainBinding.btnBack.setOnClickListener { this@MainActivity.onBackPressed() }
        setImages()
        setTexts()
        setRatings()
        setChips()
        setReviews()
    }

    private fun setImages() {
        Glide.with(mainBinding.gamePreview.context)
            .load(dataModel.image)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.img_game_preview)
            .centerCrop()
            .into(mainBinding.gamePreview)
        Glide.with(mainBinding.gameLogo.context)
            .load(dataModel.logo)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.img_game_logo)
            .centerCrop()
            .into(mainBinding.gameLogo)
    }

    private fun setTexts() {
        mainBinding.run {
            gameName.text = dataModel.name
            rating.text = dataModel.rating.toString()
            grade.text = dataModel.gradeCnt
            description.text = dataModel.description
            btnInstall.text = dataModel.action.name
            reviewsCount.text = resources.getString(R.string.review_count, dataModel.gradeCnt)
        }
    }

    private fun setRatings() {
        mainBinding.run {
            gameRating.rating = RATING
            reviewRating.rating = RATING
        }
    }

    private fun setChips() {
        for (text in dataModel.tags) {
            val chip = layoutInflater
                .inflate(R.layout.chip_layout, mainBinding.chips, false) as Chip
            chip.text = text
            chip.setTextAppearance(R.style.FontChip)
            chip.setChipBackgroundColorResource(R.color.background_chip)
            chip.setTextColor(
                ContextCompat.getColor(this@MainActivity, R.color.chips_text)
            )
            mainBinding.chips.addView(chip)
        }
    }

    private fun setReviews() {
        for ((index, comment) in dataModel.reviews.withIndex()) {
            reviewBinding = ReviewLayoutBinding
                .inflate(layoutInflater, mainBinding.reviewAndRatings, false)

            reviewBinding.run {
                Glide.with(icon.context)
                    .load(comment.userImage)
                    .placeholder(R.drawable.progress_animation)
                    .error(USER_ICONS[index])
                    .centerCrop()
                    .into(reviewBinding.icon)
                userName.text = comment.userName
                date.text = comment.date
                message.text = comment.message
            }

            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            val marginLeftRight = (MARGIN_HORIZONTAL * resources.displayMetrics.density).toInt()
            val marginTop = (MARGIN_TOP * resources.displayMetrics.density).toInt()
            params.setMargins(marginLeftRight, marginTop, marginLeftRight, MARGIN_BOTTOM)
            reviewBinding.root.setLayoutParams(params)
            mainBinding.reviewAndRatings.addView(reviewBinding.root)
        }
    }

    companion object {
        const val MARGIN_HORIZONTAL = 24F
        const val MARGIN_TOP = 10F
        const val MARGIN_BOTTOM = 0
        const val RATING = 5F
        val USER_ICONS = mapOf(
            0 to R.drawable.img_review_user_icon_1,
            1 to R.drawable.img_review_user_icon_2
        )
    }
}