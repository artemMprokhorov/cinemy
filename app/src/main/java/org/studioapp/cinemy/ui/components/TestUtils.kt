package org.studioapp.cinemy.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag as semanticsTestTag

/**
 * Test utilities for QA automation and UI testing.
 * Provides test tags, test IDs, and automation-friendly attributes for testing frameworks.
 */
object TestUtils {

    /**
     * Modifier extensions for adding test attributes to Compose components.
     * Enables test automation frameworks to identify and interact with UI elements.
     */
    object TestModifiers {

        /**
         * Adds multiple test attributes to a Compose modifier for testing automation.
         *
         * @param tag Optional test tag for element identification
         * @param id Optional test ID for element identification
         * @param data Map of additional test data attributes as key-value pairs
         * @return Modifier with applied test attributes for automation
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
