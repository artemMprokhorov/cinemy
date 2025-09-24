package org.studioapp.cinemy.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag as semanticsTestTag

/**
 * Test utilities for QA automation and UI testing
 * Provides test tags, test IDs, and automation-friendly attributes
 */
object TestUtils {

    /**
     * Test tags for different UI components
     */
    object TestTags {
        // Navigation
        const val NAV_MOVIES = "nav_movies"
        const val NAV_DETAILS = "nav_details"
        const val NAV_BACK = "nav_back"
        
        // Movie List
        const val MOVIE_LIST = "movie_list"
        const val MOVIE_CARD = "movie_card"
        const val MOVIE_TITLE = "movie_title"
        const val MOVIE_RATING = "movie_rating"
        const val MOVIE_POSTER = "movie_poster"
        const val MOVIE_RELEASE_DATE = "movie_release_date"
        
        // Movie Details
        const val MOVIE_DETAILS_SCREEN = "movie_details_screen"
        const val MOVIE_DETAILS_TITLE = "movie_details_title"
        const val MOVIE_DETAILS_DESCRIPTION = "movie_details_description"
        const val MOVIE_DETAILS_RATING = "movie_details_rating"
        const val MOVIE_DETAILS_GENRES = "movie_details_genres"
        const val MOVIE_DETAILS_RUNTIME = "movie_details_runtime"
        const val MOVIE_DETAILS_BUDGET = "movie_details_budget"
        const val MOVIE_DETAILS_REVENUE = "movie_details_revenue"
        
        // Buttons
        const val BUTTON_RETRY = "button_retry"
        const val BUTTON_BACK = "button_back"
        const val BUTTON_PLAY = "button_play"
        const val BUTTON_LOAD_MORE = "button_load_more"
        const val BUTTON_PREVIOUS = "button_previous"
        const val BUTTON_NEXT = "button_next"
        
        // Loading States
        const val LOADING_INDICATOR = "loading_indicator"
        const val LOADING_TEXT = "loading_text"
        const val LOADING_MOVIES = "loading_movies"
        const val LOADING_DETAILS = "loading_details"
        
        // Error States
        const val ERROR_MESSAGE = "error_message"
        const val ERROR_RETRY = "error_retry"
        const val ERROR_GENERIC = "error_generic"
        const val ERROR_NETWORK = "error_network"
        
        // Pagination
        const val PAGINATION_INFO = "pagination_info"
        const val PAGINATION_PREVIOUS = "pagination_previous"
        const val PAGINATION_NEXT = "pagination_next"
        const val PAGINATION_LOADING = "pagination_loading"
        
        // Sentiment Analysis
        const val SENTIMENT_ANALYSIS = "sentiment_analysis"
        const val SENTIMENT_POSITIVE = "sentiment_positive"
        const val SENTIMENT_NEGATIVE = "sentiment_negative"
        const val SENTIMENT_LOADING = "sentiment_loading"
        
        // Foldable Device Support
        const val FOLDABLE_LAYOUT = "foldable_layout"
        const val DUAL_PANE_LEFT = "dual_pane_left"
        const val DUAL_PANE_RIGHT = "dual_pane_right"
        const val ADAPTIVE_LAYOUT = "adaptive_layout"
        
        // Accessibility
        const val ACCESSIBLE_TEXT = "accessible_text"
        const val ACCESSIBLE_BUTTON = "accessible_button"
        const val ACCESSIBLE_CARD = "accessible_card"
        const val ACCESSIBLE_IMAGE = "accessible_image"
    }

    /**
     * Test IDs for different UI elements
     */
    object TestIds {
        // Movie List
        const val MOVIE_LIST_ID = "movie_list_id"
        const val MOVIE_CARD_ID = "movie_card_id"
        const val MOVIE_TITLE_ID = "movie_title_id"
        const val MOVIE_RATING_ID = "movie_rating_id"
        const val MOVIE_POSTER_ID = "movie_poster_id"
        
        // Movie Details
        const val MOVIE_DETAILS_ID = "movie_details_id"
        const val MOVIE_DETAILS_TITLE_ID = "movie_details_title_id"
        const val MOVIE_DETAILS_DESCRIPTION_ID = "movie_details_description_id"
        const val MOVIE_DETAILS_RATING_ID = "movie_details_rating_id"
        
        // Buttons
        const val BUTTON_RETRY_ID = "button_retry_id"
        const val BUTTON_BACK_ID = "button_back_id"
        const val BUTTON_PLAY_ID = "button_play_id"
        
        // Loading
        const val LOADING_INDICATOR_ID = "loading_indicator_id"
        const val LOADING_TEXT_ID = "loading_text_id"
        
        // Error
        const val ERROR_MESSAGE_ID = "error_message_id"
        const val ERROR_RETRY_ID = "error_retry_id"
    }

    /**
     * Test data attributes for automation
     */
    object TestData {
        // Movie data
        const val MOVIE_ID = "data-movie-id"
        const val MOVIE_TITLE = "data-movie-title"
        const val MOVIE_RATING = "data-movie-rating"
        const val MOVIE_RELEASE_DATE = "data-movie-release-date"
        const val MOVIE_RUNTIME = "data-movie-runtime"
        const val MOVIE_GENRES = "data-movie-genres"
        const val MOVIE_BUDGET = "data-movie-budget"
        const val MOVIE_REVENUE = "data-movie-revenue"
        
        // UI state
        const val LOADING_STATE = "data-loading-state"
        const val ERROR_STATE = "data-error-state"
        const val SUCCESS_STATE = "data-success-state"
        const val EMPTY_STATE = "data-empty-state"
        
        // Pagination
        const val CURRENT_PAGE = "data-current-page"
        const val TOTAL_PAGES = "data-total-pages"
        const val HAS_NEXT = "data-has-next"
        const val HAS_PREVIOUS = "data-has-previous"
        
        // Device type
        const val DEVICE_TYPE = "data-device-type"
        const val SCREEN_SIZE = "data-screen-size"
        const val ORIENTATION = "data-orientation"
    }

    /**
     * Modifier extensions for adding test attributes
     */
    object TestModifiers {
        
        /**
         * Add test tag to modifier
         */
        fun Modifier.testTag(tag: String): Modifier {
            return this.testTag(tag)
        }
        
        /**
         * Add test ID to modifier
         */
        fun Modifier.testId(id: String): Modifier {
            return this.semantics {
                this.testTag = id
            }
        }
        
        /**
         * Add test data attribute to modifier
         */
        fun Modifier.testData(key: String, value: String): Modifier {
            return this.semantics {
                this.testTag = "$key:$value"
            }
        }
        
        /**
         * Add multiple test attributes to modifier
         */
        fun Modifier.testAttributes(
            tag: String? = null,
            id: String? = null,
            data: Map<String, String> = emptyMap()
        ): Modifier {
            var modifier = this
            
            tag?.let { modifier = modifier.testTag(it) }
            id?.let { modifier = modifier.testId(it) }
            
            data.forEach { (key, value) ->
                modifier = modifier.testData(key, value)
            }
            
            return modifier
        }
    }

    /**
     * Test content descriptions for automation
     */
    object TestDescriptions {
        const val MOVIE_CARD = "Movie card"
        const val MOVIE_TITLE = "Movie title"
        const val MOVIE_RATING = "Movie rating"
        const val MOVIE_POSTER = "Movie poster"
        const val MOVIE_DETAILS = "Movie details"
        const val LOADING_INDICATOR = "Loading indicator"
        const val ERROR_MESSAGE = "Error message"
        const val RETRY_BUTTON = "Retry button"
        const val BACK_BUTTON = "Back button"
        const val PLAY_BUTTON = "Play button"
        const val PAGINATION_INFO = "Pagination information"
        const val SENTIMENT_ANALYSIS = "Sentiment analysis"
        const val FOLDABLE_LAYOUT = "Foldable device layout"
        const val DUAL_PANE_LAYOUT = "Dual pane layout"
    }

    /**
     * Test roles for automation
     */
    object TestRoles {
        const val BUTTON = "button"
        const val CARD = "card"
        const val IMAGE = "image"
        const val TEXT = "text"
        const val LIST = "list"
        const val PROGRESS_INDICATOR = "progress_indicator"
        const val ERROR_MESSAGE = "error_message"
        const val NAVIGATION = "navigation"
        const val LAYOUT = "layout"
    }
}
