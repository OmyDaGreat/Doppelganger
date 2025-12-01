#!/bin/bash

# Script to run the JVM Desktop examples application
# This will open a window showing all SVG examples

echo "Building and running JVM Desktop SVG Examples..."
echo "================================================"
echo ""

cd "$(dirname "$0")"

# Check if user wants XWayland mode (for Wayland compositors like niri)
if [ "$1" = "--xwayland" ] || [ "$1" = "-x" ]; then
    echo "Running in XWayland mode (for Wayland compositor compatibility)..."
    export GDK_BACKEND=x11
fi

# Run the application
echo "Starting the application..."
./gradlew :library:runJvmExamples

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Application ran successfully!"
else
    echo ""
    echo "✗ Application failed to run."
    echo ""
    echo "If you're using a Wayland compositor (niri, Sway, Hyprland),"
    echo "try running with XWayland mode:"
    echo "  ./run-jvm-examples.sh --xwayland"
    echo ""
    echo "Alternative methods to run:"
    echo "1. Using Gradle: ./gradlew :library:runJvmExamples"
    echo "2. Using Gradle with XWayland: GDK_BACKEND=x11 ./gradlew :library:runJvmExamples"
    echo "3. Using IntelliJ IDEA: Open library/src/jvmTest/kotlin/SvgExamplesApp.kt"
    echo "   and click the play button next to fun main()"
fi
