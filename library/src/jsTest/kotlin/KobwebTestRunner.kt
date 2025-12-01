package xyz.malefic.doppelganger

import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.renderComposable

/**
 * Simple test runner for Kobweb examples
 * This renders the examples page without requiring a full Kobweb application setup
 * 
 * To run:
 * 1. Build the JS test: ./gradlew :library:jsTestClasses
 * 2. Serve the test output directory and open in browser
 * 
 * Quick test with Python:
 * python3 -m http.server 8080
 * Then open: http://localhost:8080/library/build/dist/jsTest/productionExecutable/
 */
fun main() {
    window.onload = {
        document.getElementById("root")?.let { root ->
            renderComposable(root) {
                // Use the simpler example for easier testing
                SimpleKobwebExamples()
            }
        }
    }
}
