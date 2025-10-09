package org.studioapp.cinemy.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag as semanticsTestTag

/**
 * Test utilities for QA automation and UI testing
 * Provides test tags, test IDs, and automation-friendly attributes
 */
object TestUtils {




    /**
     * Modifier extensions for adding test attributes
     */
    object TestModifiers {


        /**
         * Add multiple test attributes to modifier
         */
        fun testAttributes(
            tag: String? = null,
            id: String? = null,
            data: Map<String, String> = emptyMap()
        ): Modifier {
            var modifier: Modifier = Modifier

            tag?.let { modifier = modifier.semantics { semanticsTestTag = it } }
            id?.let { modifier = modifier.semantics { semanticsTestTag = it } }

            data.forEach { (key, value) ->
                modifier = modifier.semantics { semanticsTestTag = "$key:$value" }
            }

            return modifier
        }
    }

}
