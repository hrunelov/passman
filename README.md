# SeedPass (Working Name)

### What it is ###
Account password generator and manager for Android (Java) and Chrome (JavaScript). Manages passwords without storing them by deterministically generating strong passwords from strings.<br>
### Branching strategy ###
Each milestone from 1 onwards will have its own branch.<br>
### Testing strategy ###
The project will be heavily tested during development, both with JUnit tests and manually, to make sure no bugs are present and that passwords generated in Java are exactly equal to those generated in JavaScript.<br>
### How to install ###
Android:
1. Download and install [Android Studio](https://developer.android.com/studio/index.html)
2. Start Android Studio. Select "Check out project from Version Control" and then GitHub.
3. Enter the path to this repository (from "Clone or download"), the parent directory to clone to and the name of the project directory.
4. Click Clone.
Chrome (Google Chrome browser required):
1. Download the directory "chrome".
2. Go to chrome://extensions and check "Developer mode".
3. Click on "Load unpacked extension" and browse to the downloaded "chrome" directory.
